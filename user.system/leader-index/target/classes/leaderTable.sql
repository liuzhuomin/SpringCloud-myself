SET FOREIGN_KEY_CHECKS=0;//
-- ----------------------------
-- Table structure for zhjf_request_model
-- ----------------------------
CREATE TABLE  IF NOT EXISTS `zhjf_request_model` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;//


-- 企业诉求插入记录触发
DROP TRIGGER IF EXISTS qybf_difficult_company_after_insert;//
CREATE TRIGGER qybf_difficult_company_after_insert
AFTER INSERT ON qybf_difficult_company
FOR EACH ROW
	begin
		declare maxId int default 0;
		declare companyName varchar(255) default "";
		declare leaderId int;
		declare look_anyway TINYINT(4) default 0;
		select max(id) from tu_company_message into maxId;
		select name from tu_company where id=NEW.company_id	into companyName;
		select leader_id from qy_base_corp_extension_leader where corp_id =NEW.company_id into leaderId;
		select NEW.leader_ship_remarks is not null or NEW.flow is not null and NEW.flow=1 into look_anyway;
		-- 查询挂点领导不为空的企业上报诉求才需要进行通知，因为是通知到挂点领导的
		if(leaderId is not null) then
			if(maxId!=0) then
				INSERT INTO tu_company_message values(maxId+1,CONCAT(companyName,'上报诉求需要办理'),NOW(),0,'A',NEW.id,0,leaderId,look_anyway,-1);
			else
				INSERT INTO tu_company_message values(1,CONCAT(companyName,'上报诉求需要办理'),NOW(),0,'A',NEW.id,0,leaderId,look_anyway,-1);
			end if;
		end if;
	end;
	//

DROP TRIGGER IF EXISTS qybf_difficult_history_after_update;//
-- 企业历史表格插入代表发生转办触发

CREATE TRIGGER qybf_difficult_history_after_update
AFTER INSERT ON qybf_difficult_history
FOR EACH ROW
	begin
		declare messageId int default 0;
		declare companyName varchar(255) default "";
		declare lookAnyway TINYINT default 0;
		-- 将当前诉求关联的企业名称获取到，并且赋值给companyName变量
		select name from tu_company where id = (select company_id from qybf_difficult_company where id=NEW.difficult_id) into companyName;
		-- 查询type为'A'的，代表这条消息是诉求类型的，并且诉求id要对应上，然后将其赋值给messageId
		-- 呈批领导、转相关职能部门办理	当呈批、转办等处理需要将其进行通知
		select id from tu_company_message where type='A' and refrence_id=NEW.difficult_id into messageId;
		select look_anyway from tu_company_message where type='A' and refrence_id=NEW.difficult_id into lookAnyway;
		-- 当挂点领导对当前诉求进行跟踪或者批示，才会检测诉求状态的改变
		if(messageId is not null and messageId!=0) then
			if(NEW.status="呈批领导" OR NEW.status="转相关职能部门办理")then
				-- 只有呈批领导和转办才需要更新当前消息通知，将提示语修改，并且状态更改为未曾阅读
				update tu_company_message set	message=CONCAT('您需要办理',companyName,'的诉求'),date=NOW(),tu_company_message.read_status=0 where id=messageId;
			elseif(NEW.status="已完结") then
				-- 当诉求件的状态为已完结，则对最后的提示语进行修改为已经完结
				update tu_company_message set	message=CONCAT(companyName,'的诉求已经办结'),date=NOW(),tu_company_message.read_status=0,tu_company_message.end_status=1 where id=messageId;
			elseif(lookAnyway<>0)then
				-- 除了以上的状态更改之外，一旦跟踪或者批示了当前诉求，代表要对当前诉求的状态进行消息通知
				update tu_company_message set	message=CONCAT(companyName,'的诉求进度有更新:',NEW.status),date=NOW(),tu_company_message.read_status=0 where id=messageId;
			end if;
		end if;
	end;
	//


-- 莫名修改了此诉求的id，需要及时动态更新;
DROP TRIGGER IF EXISTS qybf_difficult_company_after_update;//
CREATE TRIGGER qybf_difficult_company_after_update
AFTER UPDATE ON qybf_difficult_company
FOR EACH ROW
	begin
		if( NEW.id!=OLD.id) then
				update tu_company_message set refrence_id=NEW.id where refrence_id=OLD.id AND type="A";
		end if;
	end;
	//


-- 莫名修改了此诉求的id，需要及时动态更新;
DROP TRIGGER IF EXISTS qybf_difficult_company_after_update; //
CREATE TRIGGER qybf_difficult_company_after_update
AFTER UPDATE ON qybf_difficult_company
FOR EACH ROW
	begin
		if( NEW.id!=OLD.id) then
				update tu_company_message set refrence_id=NEW.id where refrence_id=OLD.id AND type="A";
		end if;
	end;
	//

-- 添加申请的时候，需要创建一条消息通知
DROP TRIGGER IF EXISTS zhjf_apply_after_insert;//
CREATE TRIGGER zhjf_apply_after_insert
AFTER INSERT ON zhjf_apply
FOR EACH ROW
	begin
		declare messageId int default 0;
		declare companyName varchar(255) default "";
		declare leaderId int default 0;
		select name from tu_company where id=NEW.company_id into companyName;
		select max(id) from tu_company_message into messageId;
		select leader_id from qy_base_corp_extension_leader where corp_id =NEW.company_id into leaderId;
		if(messageId is not null and messageId!=0) then
			INSERT INTO tu_company_message values(messageId+1,CONCAT(companyName,'申报了新的政策'),NOW(),0,'C',NEW.id,0,leaderId,0,-1);
		else
			INSERT INTO tu_company_message values(1,CONCAT(companyName,'申报了新的政策'),NOW(),0,'C',NEW.id,0,leaderId,0,-1);
		end if;
	end;
	//



-- 更改申请状态的时候，需要更改当前申请实例对应消息通知的消息
DROP TRIGGER IF EXISTS zhjf_apply_after_update; //
CREATE TRIGGER zhjf_apply_after_update
AFTER UPDATE ON zhjf_apply
FOR EACH ROW
	begin
		declare messageId int default 0;
		declare companyName varchar(255) default "";
		declare leaderId int default 0;
		declare currentStatus int default 0;
		if(NEW.current_status!=OLD.current_status)then
				select name from tu_company where id=NEW.company_id into companyName;
				select leader_id from qy_base_corp_extension_leader where corp_id =NEW.company_id into leaderId;
				select id from tu_company_message where type='C' and refrence_id=NEW.id into messageId;
				select apply_status from zhjf_apply_status where id=NEW.current_status into currentStatus;	-- 查询当前申请的当前状态
				if(messageId is not null and messageId!=0) then
							if(currentStatus=20)then
								update tu_company_message set date=NOW() where id=messageId;	-- 20的状态，相当于填写完成并且开始申请
							else
								update tu_company_message set	message=CONCAT(companyName,'的申报政策进度更新'),date=NOW(),tu_company_message.read_status=0 where id=messageId;
							end if;
				end if;
		end if;
	end;
	//

