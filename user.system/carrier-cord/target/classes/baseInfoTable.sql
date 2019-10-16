SET FOREIGN_KEY_CHECKS=0;//
-- ----------------------------
-- Table structure for technology_loan_amount
-- ----------------------------

CREATE TABLE IF NOT EXISTS `technology_loan_amount`  (
  `id` bigint(20) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `suffix` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;//

CREATE TABLE IF NOT EXISTS `technology_loan_date` (
  `id` bigint(20) NOT NULL,
  `date` int(11) NOT NULL,
  `suffix` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;//

-- ----------------------------
-- Table structure for technology_loan_type
-- ----------------------------
CREATE TABLE  IF NOT EXISTS  `technology_loan_type` (
  `id` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  `technology_financial_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtjb8if3qixqxi3aobdlx5g12j` (`technology_financial_id`),
  CONSTRAINT `FKtjb8if3qixqxi3aobdlx5g12j` FOREIGN KEY (`technology_financial_id`) REFERENCES `technology_financial` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;//

CREATE TABLE  IF NOT EXISTS  `tu_code` (
  `id`  int(12) NOT NULL,
 `code`  int(10)  NOT NULL COMMENT '值' ,
 `codedesc`  varchar(50)  NOT NULL COMMENT '描述' ,
 `protype`  varchar(50)  NOT NULL COMMENT '类型' ,
 `imgsrc`  text  NULL ,
 `parent_id`  int(12) NULL DEFAULT '-1' ,
 `map_table_field`  text  NULL ,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;//


