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
		declare look_anyway TINYINT(4) default 0;
		select max(id) from tu_company_message into maxId;
		select name from tu_company where id=NEW.company_id	into companyName;
		select leader_id from qy_base_corp_extension_leader where corp_id =NEW.company_id into leaderId;
		select NEW.leader_ship_remarks is not null or NEW.flow is not null and NEW.flow=1 into look_anyway;
		-- 查询挂点领导不为空的企业上报诉求才需要进行通知，因为是通知到挂点领导的
		if(leaderId is not null) then
			if(maxId!=0) then
				INSERT INTO tu_company_message values(maxId+1,CONCAT(companyName,'上报诉求需要办理'),NOW(),0,'A',NEW.id,0,leaderId,look_anyway);
			else
				INSERT INTO tu_company_message values(1,CONCAT(companyName,'上报诉求需要办理'),NOW(),0,'A',NEW.id,0,leaderId,look_anyway);
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
DELIMITER;


-- 莫名修改了此诉求的id，需要及时动态更新;
DROP TRIGGER IF EXISTS qybf_difficult_company_after_update;
DELIMITER //
CREATE TRIGGER qybf_difficult_company_after_update
AFTER UPDATE ON qybf_difficult_company
FOR EACH ROW
	begin
		if( NEW.id!=OLD.id) then
				update tu_company_message set refrence_id=NEW.id where refrence_id=OLD.id AND type="A";
		end if;
	end;
	//
DELIMITER;

-- 莫名修改了此诉求的id，需要及时动态更新;
DROP TRIGGER IF EXISTS qybf_difficult_company_after_update;
DELIMITER //
CREATE TRIGGER qybf_difficult_company_after_update
AFTER UPDATE ON qybf_difficult_company
FOR EACH ROW
	begin
		if( NEW.id!=OLD.id) then
				update tu_company_message set refrence_id=NEW.id where refrence_id=OLD.id AND type="A";
		end if;
	end;
	//
DELIMITER;

-- 添加申请的时候，需要创建一条消息通知
DROP TRIGGER IF EXISTS zhjf_apply_after_insert;
DELIMITER //
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
			INSERT INTO tu_company_message values(messageId+1,CONCAT(companyName,'申报了新的政策'),NOW(),0,'C',NEW.id,0,leaderId,0);
		else
			INSERT INTO tu_company_message values(1,CONCAT(companyName,'申报了新的政策'),NOW(),0,'C',NEW.id,0,leaderId,0);
		end if;
	end;
	//
DELIMITER;


-- 更改申请状态的时候，需要更改当前申请实例对应消息通知的消息
DROP TRIGGER IF EXISTS zhjf_apply_after_update;
DELIMITER //
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
DELIMITER;


-- 添加挂点领导
insert into qy_base_corp_extension_leader (corp_id,leader_id,creator_id,create_time,index_c)  select c.id as corp_id,u.id as leader_id,u.id as creator_id,NOW() as create_time,100 as index_c from (select id from qy_base_corp_essence where corp_name in ("深圳市华星光电技术有限公司"
		,"深圳日东光学有限公司","深圳欣旺达智能科技有限公司","深圳市艾维普思科技有限公司","丽晶维珍妮内衣(深圳)有限公司","杜邦太阳能(深圳)有限公司"
		,"展辰新材料集团股份有限公司","深圳市飞亚达科技发展有限公司","深圳迈瑞科技有限公司","深圳迈瑞科技有限公司"))as c ,
(select id  from tu_user where realname like "王宏彬" )as u

-- 通过企业名称，领导名称，单位名称，做到将领导和挂点企业关联，领导和牵头单位关联
DELIMITER //
DROP PROCEDURE IF EXISTS insertleader;
CREATE PROCEDURE insertLeader(
IN n1 varchar(255),
IN n2 varchar(255),
IN n3 varchar(255),
IN n4 varchar(255),
IN n5 varchar(255),
IN n6 varchar(255),
IN n7 varchar(255),
IN n8 varchar(255),
IN n9 varchar(255),
IN n10 varchar(255),
IN realName2 varchar(255),
IN orgName varchar(255))
BEGIN
		declare leaderId bigint default 0;
		declare orgId bigint default 0;
		-- 按照用户名称，查找用户id
		select id  from tu_user where realname = realName2 into leaderId;
		if(leaderId is not null and leaderId!=0) then
			-- 将企业和leader关联到qy_base_corp_extension_leader表格，相当于为领导挂点了这些企业
			insert into qy_base_corp_extension_leader (corp_id,leader_id,creator_id,create_time,index_c)
			select c.id as corp_id,leaderId as leader_id,leaderId as creator_id,NOW() as create_time,100 as index_c from
			(select id from qy_base_corp_essence where corp_name in (n1,n2,n3,n4,n5,n6,n7,n8,n9,n10))as c ;
			-- 首先按照部门名称将部门id查询到，并且赋值给orgId,如果此部门名称的部门记录不存在，则按照最大id数插入一条记录
			select id from tu_organization where name like orgName or alias_name like orgName limit 1 into orgId;
			if(orgId is null or orgId=0)then
				select max(id)+1 from tu_organization into orgId;
				INSERT INTO tu_organization (`id`, `description`, `name`, `condition_`, `create_date`, `delete_type`, `fax`, `group_type`, `is_org`, `logo`, `order_num`, `group_status`, `telephone`, `parent_id`, `creator_id`, `customindustry_id`, `deparment_id`, `org_id`, `industry_id`, `region_id`, `code`, `audit_`, `receiver_no`, `apply_total`, `clk_total`, `evaluate`, `favorite_total`, `other_type`, `admin_type`, `autonym_date`, `autonym_type`, `manger_type`, `product_count`, `is_recommend`, `tdc_addr`, `street_id`, `allow_chat`, `should_managed`, `association_type`, `pdf_url`, `perfect_degree`, `geo_hash`, `lat`, `lng`, `is_administration`, `alias_name`) VALUES (orgId, '', orgName, NULL, NULL, NULL, NULL, 'organization', '1', '/resources/gec-ui-2.0/images/default/1.jpg', '100020', 'A', NULL, '2', '213875', NULL, NULL, NULL, NULL, '33', NULL, '1', NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, orgName);
			end if;
			insert into qy_base_corp_extension_manage_org (corp_id,manage_org,creator_id,create_time)
			select c.id as corp_id,orgId as manage_org,leaderId as creator_id,NOW() as create_time from
			(select id from qy_base_corp_essence where corp_name in (n1,n2,n3,n4,n5,n6,n7,n8,n9,n10))as c;
		end if;
END
//
DELIMITER

delete from qy_base_corp_extension_manage_org;
delete from qy_base_corp_extension_leader;

call insertLeader("深圳市华星光电技术有限公司","深圳日东光学有限公司","深圳欣旺达智能科技有限公司","深圳市艾维普思科技有限公司","丽晶维珍妮内衣(深圳)有限公司"
,"丽晶维珍妮内衣(深圳)有限公司","展辰新材料集团股份有限公司","深圳市飞亚达科技发展有限公司","深圳迈瑞科技有限公司","深圳市科陆电子科技股份有限公司","王宏彬","区发展和财政局");

call insertLeader("普联技术有限公司","圳科士达科技股份有限公司","旭硝子显示玻璃(深圳)有限公司","深圳市福瑞祥电器有限公司","深圳市贝特瑞纳米科技有限公司"
,"深圳市贝特瑞纳米科技有限公司","深圳琦富瑞电子有限公司","深圳纽迪瑞科技开发有限公司","深圳纽迪瑞科技开发有限公司","深圳市飞荣达科技股份有限公司","刘胜","区经济服务局");


call insertLeader("深圳市喜德盛自行车股份有限公司","兆赫电子(深圳)有限公司","兆赫电子(深圳)有限公司","深圳怡化金融设备制造有限公司","东江精创注塑(深圳)有限公司"
,"深圳天邦达科技有限公司","深圳天邦达科技有限公司","深圳康泰生物制品股份有限公司","吉田拉链(深圳)有限公司","深圳市捷永星皇钟表有限公司","王浩","区发展和财政局");


call insertLeader("研祥智能科技股份有限公司","深圳市华力特电气有限公司","深圳市金新农科技股份有限公司","深圳市明合发纸品有限公司","深圳市明辉达塑胶电子有限公司"
,"深圳市明辉达塑胶电子有限公司","深圳市柏英特电子科技有限公司","深圳科创新源新材料股份有限公司 ","深圳市巴科光电科技股份有限公司","联积电子(深圳)有限公司","何奕飞","区政法办");

call insertLeader("深圳市诚威新材料有限公司","正威科技(深圳)有限公司","深圳和而泰智能控制股份有限公司","深圳市瑞丰光电子股份有限公司","深圳市证通电子股份有限公司"
,"深圳深日钢材有限公司","凯茂科技(深圳)有限公司","深圳市镭煜科技有限公司","深圳中弘装备科技有限公司","深圳市特发信息光网科技股份有限公司","刘德峰","区发展和财政局");

call insertLeader("天王电子(深圳)有限公司","","","","","","","","","","刘德峰","区发展和财政局");

-- 5
call insertLeader("欧菲科技股份有限公司","宝利时计表业(深圳)有限公司","信泰光学(深圳)有限公司","信泰光学(深圳)有限公司","毅丰显示科技(深圳)有限公司"
,"深圳市艾普达科技有限公司","深圳市创益通技术股份有限公司","高顺昌钢板(深圳)有限公司","深圳万和制药有限公司","深圳市灵星雨科技开发有限公司","姚文胜","区纪委监委");

call insertLeader("深圳农牧美益肉业有限公司","深圳市瑞辉钟表有限公司","永德利硅橡胶科技(深圳)有限公司","深圳市钰创合成光电技术有限公司","深圳市百康光电有限公司"
,"深圳市耀嵘科技有限公司","深圳市创显光电有限公司","深圳德诚达光电材料有限公司","深圳市南德谱光电有限公司","深圳优力可科技股份有限公司","刘大岭","区城市管理局");

call insertLeader("新兴纺织(深圳)有限公司","深圳市津田电子有限公司","元大金属实业(深圳)有限公司","中泰制衣(深圳)有限公司","深圳好博窗控技术有限公司"
,"深圳兴先达五金塑胶制品有限公司","海林电脑科技（深圳）有限公司","深圳市优宝新材料科技有限公司","深圳市综合能源有限公司","深圳市贤达信息技术有限公司","覃敬腾","区统战和社会建设局");

call insertLeader("深圳市三利谱光电科技股份有限公司","深圳市海派特光伏科技有限公司","宝威亚太电子(深圳)有限公司","深圳市福瑞康电子有限公司","瑞安复合材料(深圳)有限公司"
,"深圳爱湾医学检验实验室","深圳巡天空间技术有限公司","深圳上泰生物工程有限公司","深圳市绚图新材科技有限公司","深圳市新阳唯康科技有限公司","绳万青","区组织人事局");

call insertLeader("深圳市官田电子包装材料有限公司","深圳市泰塑塑化材料科技有限公司","福华根记制衣(深圳)有限公司","深圳市柳鑫实业股份有限公司","深圳早川电子有限公司"
,"深圳天地宽视信息科技有限公司","捷鹏塑胶(深圳)有限公司","深圳市兴为通科技股份有限公司","深圳市康泰电气设备有限公司","深圳市科力尔电机有限公司","刘桂林","区综合办");

-- 10组

call insertLeader("丰宾电子(深圳)有限公司","深圳市晨光乳业有限公司","深圳市视安通电子有限公司","景烁皮具(深圳)有限公司","深圳市普耐光电科技有限公司"
,"深圳易方数码科技股份有限公司","深圳市瑞德丰精密制造有限公司","凯士林电子(深圳)有限公司","深圳恩佳升科技有限公司","深圳市九洲光电科技有限公司","周金堂","光明公安分局");

call insertLeader("美盈森集团股份有限公司","深圳市远望谷信息技术股份有限公司","深圳市泰和安科技有限公司","广东环威电线电缆股份有限公司","深圳市雪仙丽制衣有限公司"
,"深圳典邦科技有限公司","深圳市英威腾电气股份有限公司","深圳开立生物医疗科技股份有限公司","深圳一电科技有限公司","深圳市中瑞钟表科技开发有限公司","周辉","区住房和建设局");

call insertLeader("深圳市得润电子股份有限公司","深圳市新纶科技股份有限公司","深圳市崇达电路技术股份有限公司","深圳市大富科技股份有限公司","深圳市日联科技有限公司"
,"深圳市延纳科技有限公司","深圳奥特迅电力设备股份有限公司","深圳万润科技股份有限公司","深圳市景旺电子股份有限公司","深圳市邦凯新能源股份有限公司","姚高科","区城市发展促进中心");

call insertLeader("深圳市成天泰电缆实业发展有限公司","深圳友邦塑料印刷包装有限公司","深圳江浩电子有限公司","深圳市拓日新能源科技股份有限公司","深圳市华盛源机电有限公司"
,"海洋王照明科技股份有限公司","深圳市大疆灵眸科技有限公司","深圳市百事达卓越科技股份有限公司","深圳雷杜生命科学股份有限公司","康佳集团股份有限公司","张宗平","区经济服务局");

call insertLeader("深圳市振邦智能科技股份有限公司","荣轮科技(深圳)有限公司","深圳市渴望通信有限公司","深圳亿和模具制造有限公司","深圳市帝显电子有限公司"
,"空气化工产品(深圳)有限公司","深圳市能佳自动化设备有限公司","深圳华强集团有限公司","深圳市电达实业股份有限公司","华之欧(深圳)科技有限公司","陈佩群","区统战和社会建设局");

call insertLeader("深圳市振惠建混凝土有限公司","深圳市宇顺电子股份有限公司","深圳市华圣达拉链有限公司","平易电子技术(深圳)有限公司","长园长通新材料股份有限公司"
,"斌福电子(深圳)有限公司","深圳乘方电机有限公司","天伟装配时计(深圳)有限公司","深圳市嘉之宏电子有限公司","深圳海西高科有限公司","周荣生","区综合办");

call insertLeader("深圳品泰电子有限公司","深圳市蓝希望电子有限公司","深圳蓝普视讯科技有限公司","新德服装(深圳)有限公司","深圳市富鑫达电子有限公司"
,"深圳市弘新五金制品有限公司","深圳市兴力鑫科技有限公司","深圳市万鸿盛塑胶模具有限公司","深圳科利电器有限公司","深圳市燕麦科技股份有限公司(光明分公司)","宋杰","区土地整备局");

call insertLeader( "深圳市锦瑞新材料股份有限公司"          ,
		    "聚银塑料包装制品(深圳)有限公司"       ,
		    "康瑞电子(深圳)有限公司"               ,
		    "深圳市嘉纪印刷包装有限公司"           ,
		    "普力斯科精密材料(深圳)有限公司"       ,
		    "佳达制衣(深圳)有限公司"               ,
		    "深圳市冠科科技有限公司"               ,
		    "奥特宝家饰(深圳)有限公司"             ,
		    "深圳市仕兴鸿精密机械设备有限公司"     ,
		    "深圳市康尼塑胶有限公司"               ,"别社宏","区安监局");

-- 周戎生


call insertLeader("深圳市英泰格瑞科技有限公司"          ,
			"唐德科技(深圳)有限公司"              ,
			"深圳市汇思科电子科技有限公司"        ,
			"深圳市博为光电股份有限公司"          ,
			"森阳电子科技(深圳)有限公司"          ,
			"深圳市图腾电气技术有限公司"          ,
			"深圳盛凌电子股份有限公司"            ,
			"深圳市正泽祥金属供应链服务有限公司"  ,
			"深圳森丰真空镀膜有限公司"            ,
			"深圳市吉斯迪科技有限公司"            ,"张清明","区规划土地监察局");

-- 29
call insertLeader("雷松科技(深圳)有限公司","锦湖光电(深圳)有限公司","深圳市创仁科技有限公司","深圳市志腾永盛科技有限公司","深圳市德辰光电科技有限公司"
,"深圳市特深电气有限公司","彩浩内衣(深圳)有限公司","深圳市台技光电有限公司","爱米士电子（深圳）有限公司","深圳市山达士电子有限公司","赵杰","区政协办公室");

-- 28
call insertLeader("深圳市万至达电机制造有限公司","深圳市佳润纸业有限公司","深圳大乘电子制造有限公司","深圳市沈氏彤创航天模型有限公司","深圳市冠利得商标印刷有限公司",
"深圳市格耐电器有限公司","海洋纸品印刷(深圳)有限公司","深圳市祥森光电科技有限公司","樱之田复材科技(深圳)有限公司","深圳市欣普斯科技有限公司","胡育青","区人大办公室");

-- 27
call insertLeader("深圳市正顺康饲料有限公司","深圳市柏斯泰电脑配件有限公司","深圳市中远通电源技术开发有限公司","深圳市爱的声音响科技有限公司","深圳市爱维泰克科技有限公司",
"深圳市东升源电气设备有限公司","深圳市乐歌数码科技有限公司","深圳市华艺佳彩色印刷有限公司","深圳市荣茂电子材料有限公司","深圳市鑫航盛科技有限公司","乔海燕","区人大办公室");

-- 26
call insertLeader("合丰纸品(深圳)有限公司",
"深圳禾晟电子科技有限公司",
"佳乐电子(深圳)有限公司",
"深圳市德镒盟电子有限公司",
"骏日科技（深圳）有限公司",
"深圳市吉瑞达电路科技有限公司",
"深圳鑫华溢印务有限公司",
"深圳市三瑞电源有限公司",
"新月光电(深圳)股份有限公司",
"深圳市佩雅时装有限公司","赵华","区经济服务局");

-- 25
call insertLeader("理想工业(深圳)有限公司",
"深圳晶石电器制造有限公司",
"联明家用制品(深圳)有限公司",
"深圳市中银科技有限公司",
"深圳市注成科技股份有限公司",
"深圳市鼎高光电产品有限公司",
"深圳市罗博威视科技有限公司",
"深圳朋凯印刷有限公司",
"深圳市龙电科技实业有限公司",
"深圳威特姆光电科技有限公司"
,"刘晓鹏","市市场监督管理局光明分局");

-- 24
call insertLeader("深圳市麦士德福科技股份有限公司",
"深圳市合利来科技有限公司",
"深圳市闽辉五金电器有限公司",
"杰比电器(深圳)有限公司",
"深圳市汇业电子有限公司",
"深圳市伟铂瑞信科技有限公司",
"深圳市帝源新材料科技有限公司",
"深圳市井泰精密五金有限公司",
"深圳华药南方制药有限公司",
"深圳市尊泰自动化设备有限公司"
,"杨明","市交通运输委光明交通运输局");

-- 23
call insertLeader("深圳市诚捷智能装备股份有限公司",
"深圳市川祺盛实业有限公司",
"贸杰家庭用品(深圳)有限公司",
"盛兴隆塑胶电子(深圳)有限公司",
"保荣利塑胶原料(深圳)有限公司",
"深圳市中原塑胶有限公司",
"深圳市展业电机有限公司",
"深圳市深大极光科技有限公司",
"深圳勤本电子有限公司",
"深圳市凯达兴塑胶模具有限公司"
,"王中伟","市规划和自然资源局光明管理局");

-- 22
call insertLeader("深圳市诚捷智能装备股份有限公司",
"深圳市川祺盛实业有限公司",
"贸杰家庭用品(深圳)有限公司",
"盛兴隆塑胶电子(深圳)有限公司",
"保荣利塑胶原料(深圳)有限公司",
"深圳市中原塑胶有限公司",
"深圳市展业电机有限公司",
"深圳市深大极光科技有限公司",
"深圳勤本电子有限公司",
"深圳市凯达兴塑胶模具有限公司"
,"郭峰","区文体教育局");


-- 21
call insertLeader("电连技术股份有限公司",
"安费诺东亚电子科技(深圳)有限公司",
"深圳市卫光生物制品股份有限公司",
"朝阳电子(深圳)有限公司",
"深圳铭锋达精密技术有限公司",
"深圳市库贝尔生物科技股份有限公司",
"依波精品(深圳)有限公司",
"深圳市港利通科技有限公司",
"深圳莱宝高科技股份有限公司(分公司)",
"兰度生物材料有限公司"
,"李世清","区政协办公室");


-- 20
call insertLeader("深圳市德彩光电有限公司",
"英光电器(深圳)有限公司",
"深圳市福瑞达显示技术有限公司",
"深圳市亿能科技有限公司",
"深圳市平进股份有限公司",
"深圳市攀极科技有限公司",
"深圳市华德安科技有限公司",
"深圳市道尔顿电子材料有限公司",
"深圳市众恒世讯科技股份有限公司",
"深圳粤网节能技术服务有限公司"
,"王丽","区建筑工务局");


