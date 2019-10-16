-- 	show variables like "sql_mode";
-- 	set sql_mode='';
-- 	set sql_mode='NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES';	

-- 将两张企业表格添加verison字段-------------------
-- 	ALTER TABLE tu_company ADD COLUMN version int not null default 0;
-- 	ALTER TABLE qy_base_corp_essence ADD COLUMN version int not null default 0;
	-- update之前需要将version字段+1
-- 	DELIMITER //
-- 	CREATE TRIGGER qy_base_corp_essence_before_update
-- 	BEFORE UPDATE ON qy_base_corp_essence 
-- 	FOR EACH ROW
-- 		begin
-- 		set version=version+1;
-- 		end;
-- 	//
-- 	DELIMITER;
-- 
	-- update之前需要将version字段+1
-- 	DELIMITER //
-- 	CREATE TRIGGER tu_company_before_update
-- 	BEFORE UPDATE ON tu_company
-- 	FOR EACH ROW
-- 		begin
-- 		set version=version+1;
-- 		end;
-- 	//
-- 	DELIMITER;
-- 
-- --------------------------------------

	-- tu_company插入后同步插入qy_base_corp_essence
	DROP TRIGGER IF EXISTS qy_base_corp_essence_after_insert_trigger;
	DELIMITER //
	CREATE TRIGGER qy_base_corp_essence_after_insert_trigger
	AFTER INSERT ON qy_base_corp_essence 
	FOR EACH ROW
		begin
		if (SELECT count(id) from tu_company where id=NEW.id)=0 THEN
			INSERT INTO tu_company(id,name,fax,logo,order_num,telephone,apply_total,clk_total,evaluate) 
			SELECT id,name,fax,logo,order_num,telephone,0,0,0 from 
			(SELECT q.id as id ,q.corp_name as name ,t.fax,q.logo,q.id as order_num,t.corp_tel as telephone  
			FROM qy_base_corp_essence q LEFT JOIN qy_base_corp_other t ON t.corp_id=q.id where t.corp_id=NEW.id) as qy_xn;
		end if;
		end;
	//
	DELIMITER;

-- tu_company插入后同步插入qy_base_corp_essence
	DROP TRIGGER IF EXISTS tu_company_before_after_trigger;
	DELIMITER //
	CREATE TRIGGER tu_company_before_after_trigger
	AFTER INSERT ON tu_company 
	FOR EACH ROW 
		begin
		if (SELECT count(id) FROM qy_base_corp_essence where id=NEW.id)=0 THEN
				INSERT INTO qy_base_corp_essence(id,corp_name,logo) values(NEW.id,NEW.name,NEW.logo);
				INSERT INTO qy_base_corp_other(fax,corp_tel,corp_id) values(NEW.fax,NEW.telephone,NEW.id);
		end if;
		end;
	//
	DELIMITER;

-- ---------------------------------------

-- tu_company更新后，同步更新qy_base_corp_essence
	DROP TRIGGER IF EXISTS qy_base_corp_essence_after_update_trigger;
	DELIMITER //
	CREATE TRIGGER qy_base_corp_essence_after_update_trigger
	AFTER UPDATE ON qy_base_corp_essence 
	FOR EACH ROW
		begin
	-- 通过union all比较两张表的列是否有不同的数据，有的话id有返回
		DECLARE result int default 0;
		SELECT id
		FROM (
				SELECT id,name,fax,logo,order_num,telephone FROM tu_company where id=NEW.id
				UNION ALL SELECT id,name,fax,logo,order_num,telephone FROM 	(SELECT q.id as id ,q.corp_name as name ,t.fax,q.logo,q.id as order_num,t.corp_tel as telephone  
			FROM qy_base_corp_essence q LEFT JOIN qy_base_corp_other t ON t.corp_id=q.id where t.corp_id=NEW.id) as qy_xn
		) tbl
		GROUP BY id
		HAVING count(*) = 1
		ORDER BY id 
		-- 当查询到不同的列，则返回有值
		INTO result;
		if result!=0 THEN
			update tu_company set name=NEW.corp_name,logo=NEW.logo,order_num=NEW.id,telephone=(select corp_tel qy_base_corp_other where corp_id=NEW.id),
			fax=(select fax qy_base_corp_other where corp_id=NEW.id);
		end if;
		end;
	//
	DELIMITER;



-- tu_company更新后,触发更新qy_base_corp_essence，qy_base_corp_other
	DROP TRIGGER IF EXISTS `tu_company_after_update_trigger`;
	DELIMITER //
	CREATE TRIGGER tu_company_after_update_trigger
	AFTER UPDATE ON tu_company 
	FOR EACH ROW 
		begin
		DECLARE result int default 0;
		SELECT id
		FROM (
				SELECT id,name,fax,logo,order_num,telephone FROM tu_company where id=NEW.id
				UNION ALL SELECT id, name,fax,logo,order_num,telephone FROM 	(SELECT q.id as id ,q.corp_name as name ,t.fax,q.logo,q.id as order_num,t.corp_tel as telephone  
			FROM qy_base_corp_essence q LEFT JOIN qy_base_corp_other t ON t.corp_id=q.id where t.corp_id=NEW.id) as qy_xn
		) tbl
		GROUP BY id
		HAVING count(*) = 1
		ORDER BY id 
		-- 当查询到不同的列，则返回有值
		INTO result;
		if result!=0 THEN 
				UPDATE qy_base_corp_essence SET id=NEW.id,corp_name=NEW.name,logo=NEW.logo;
				UPDATE qy_base_corp_other SET fax=NEW.fax,corp_tel=NEW.telephone,corp_id=NEW.id;
		end if;
		end;
	//
	DELIMITER;

-- --------------删除-------------------------

-- qy_base_corp_essence删除后，触发删除tu_company 
	DROP TRIGGER IF EXISTS qy_base_corp_essence_after_delete_trigger;
	DELIMITER //
	CREATE TRIGGER qy_base_corp_essence_after_delete_trigger
	AFTER delete ON qy_base_corp_essence 
	FOR EACH ROW
		begin
		if (SELECT count(id) from tu_company where id=OLD.id)=0 THEN		
			DELETE FROM tu_company where id =OLD.id;
		end if;
		end;
	//
	DELIMITER;

-- tu_company删除后触发删除qy_base_corp_essence
	DROP TRIGGER IF EXISTS tu_company_after_delete_trigger;
	DELIMITER //
	CREATE TRIGGER tu_company_after_delete_trigger
	AFTER delete ON tu_company 
	FOR EACH ROW 
		begin
		if (SELECT count(id) FROM qy_base_corp_essence where id=OLD.id)!=0 THEN
			DELETE FROM qy_base_corp_essence where id=ODL.id;
		end if;
		end;
	//
	DELIMITER;


-- ------------------------同步两张企业表格的sql--------------------------------------
-- 从qy_base_corp_essence插入到tu-coampany
-- INSERT INTO tu_company(id,name,fax,logo,order_num,telephone,apply_total,clk_total,evaluate ) 
-- 	SELECT id,name,fax,logo,order_num,telephone,0,0,0 from 
-- 	(SELECT q.id as id ,q.corp_name as name ,t.fax,q.logo,q.id as order_num,t.corp_tel as telephone  
-- 	FROM qy_base_corp_essence q LEFT JOIN qy_base_corp_other t ON t.corp_id=q.id where q.id not in (select id from tu_company)) as qy_xn;
-- 	
-- 
-- -- 从tucompany 插入到qy_base_corp_essence
-- INSERT INTO qy_base_corp_essence(id,corp_name,logo)  select id,name as corp_name,logo from tu_company t where t.id not in (select id from qy_base_corp_essence ); 
-- INSERT INTO qy_base_corp_other(fax,corp_tel,corp_id) select fax,telephone as corp_tel,id as corp_id from  tu_company t;
-- 
-- 
