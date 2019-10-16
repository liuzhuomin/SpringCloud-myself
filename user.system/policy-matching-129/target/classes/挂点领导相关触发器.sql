show variables like "sql_mode";
set sql_mode='';
set sql_mode='NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES';	
set FOREIGN_KEY_CHECKS=0;
set global event_scheduler = 1;  -- 启动定时器
DROP TABLE IF EXISTS `tu_company_message`;
CREATE TABLE `tu_company_message` (
  `id` bigint(20) NOT NULL,
  `message` varchar(255) NOT NULL,
  `date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `read` tinyint(4) NOT NULL,
  `type` char(255) NOT NULL,
  `refrence_id` bigint(20) NOT NULL,
  `end` tinyint(4) NOT NULL,
  `leader_id` bigint(20),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 企业诉求插入记录触发
DROP TRIGGER IF EXISTS qybf_difficult_company_after_insert;
DELIMITER //
CREATE TRIGGER qybf_difficult_company_after_insert
AFTER INSERT ON qybf_difficult_company 
FOR EACH ROW
	begin
		declare maxId int default 0;
		declare companyName varchar(255) default "";
		declare leaderId int;
		select max(id) from tu_company_message into maxId;
		select name from tu_company where id=NEW.company_id	into companyName;
		select leader_id from qy_base_corp_extension_leader where corp_id =NEW.company_id into leaderId;
		-- 查询挂点领导不为空的企业上报诉求才需要进行通知，因为是通知到挂点领导的
		if(leaderId is not null) then
			if(maxId!=0) then
				INSERT INTO tu_company_message values(maxId+1,CONCAT(companyName,'上报诉求需要办理'),NOW(),0,'A',NEW.id,0,leaderId,NEW.leader_ship_remarks);
			else 
				INSERT INTO tu_company_message values(1,CONCAT(companyName,'上报诉求需要办理'),NOW(),0,'A',NEW.id,0,leaderId,NEW.leader_ship_remarks);
			end if;
		end if;
	end;
	//
DELIMITER;

DROP TRIGGER IF EXISTS qybf_difficult_history_after_update;
-- 企业历史表格插入代表发生转办触发
DELIMITER //
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
		if(messageId is not null and messageId !=0) then
			if(NEW.status="呈批领导" OR NEW.status="转相关职能部门办理")then
				-- 只有呈批领导和转办才需要更新当前消息通知，将提示语修改，并且状态更改为未曾阅读
				update tu_company_message set	message=CONCAT('您需要办理',companyName,'的诉求'),date=NOW(),tu_company_message.read=0 where id=messageId;
			else if(NEW.status="已完结") then
				-- 当诉求件的状态为已完结，则对最后的提示语进行修改为已经完结
				update tu_company_message set	message=CONCAT(companyName,'的诉求已经办结'),date=NOW(),tu_company_message.read=0 where id=messageId;
			else if(lookAnyway=true || lookAnyway=0)then
				-- 除了以上的状态更改之外，一旦跟踪或者批示了当前诉求，代表要对当前诉求的状态进行消息通知
				update tu_company_message set	message=CONCAT(companyName,'的诉求进度有更新',NEW.status),date=NOW(),tu_company_message.read=0 where id=messageId;
			end if;
		end if;
	end;
//
DELIMITER;

select * from tu_company where id=290468

select tu_company.id from tu_company left join tu_user on tu_company.id=unique_group_id where username="17620441597"

select * from tu_user where username="17620441597";

select * from tu_company where leader_id is not null;

262331

290468

select * from qy_base_corp_extension_leader where leader_id is not null


DROP EVENT IF EXISTS `event_at`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `event_at` ON SCHEDULE AT '2017-11-28 15:39:00' 
ON COMPLETION NOT PRESERVE #当这个事件不会再发生的时候会被Drop掉
ENABLE DO 
BEGIN
    INSERT INTO `user`(name, address,addtime) VALUES('AT','AT',now());
END
//
DELIMITER ;



INSERT INTO `zhjfgm`.`qybf_difficult_company` (`id`, `commit_date`, `commune_situation`, `diffcult_title`, `end_time`, `is_mass`, `is_proxy`, `main_problems`, `status`, `upload_file_name`, `upload_file_path`, `company_id`, `creator_id`, `current_status_id`, `diffcult_type_id`, `sponsor_id`, `source_to`, `order_index`, `user_ids`, `feedback_date`, `first_date`, `handling_situation`, `lead_company`, `leader_ship`, `leader_survey_count`, `remarks`, `suggestion`, `leader_ship_remarks`, `difficult_history_id`, `approval_date`, `difficult_source`, `is_stick`, `read_history`, `satisfy_comments`, `source_date`, `read_date`, `overdue`, `overdue_date`, `visit_date`, `visit_status`, `opinions`, `visitre_cord_status`, `xcjjqk`, `difficult_company_relation_id`, `visitre_cord_id`) VALUES ('15596148325131', '2019-06-04 10:20:33', 'asdasd', 'asdasdasdasd', NULL, '0', '0', 'asdasd', '0', NULL, NULL, '290468', '290414', NULL, '1', NULL, '0', '1', '265656,213054,', NULL, NULL, NULL, NULL, '', '0', NULL, NULL, NULL, NULL, NULL, '0', b'0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);

select * from tu_company_message

desc qybf_difficult_history;
desc qybf_difficult_company
desc tu_company_message;
desc qy_base_corp_extension_leader;
desc tu_company;
desc qy_base_corp_essence;