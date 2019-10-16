INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('1', '100', '万以下') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('2', '200', '万') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('3', '400', '万') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('4', '600', '万') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('5', '800', '万') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('6', '1000', '万') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('7', '2000', '万') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_amount` (`id`, `amount`, `suffix`) VALUES ('8', NULL , '其他') ON DUPLICATE KEY UPDATE id=id;//

INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('1', '3', '个月') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('2', '6', '个月') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('3', '12', '个月') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('4', '2', '年') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('5', '3', '年') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('6', '5', '年') ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_date` (`id`, `date`, `suffix`) VALUES ('7', '10', '年') ON DUPLICATE KEY UPDATE id=id;//


INSERT INTO `zhjf`.`technology_loan_type` (`id`, `type`, `technology_financial_id`) VALUES ('1', '有价证券', NULL) ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_type` (`id`, `type`, `technology_financial_id`) VALUES ('2', '票据', NULL) ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_type` (`id`, `type`, `technology_financial_id`) VALUES ('3', '股票', NULL) ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_type` (`id`, `type`, `technology_financial_id`) VALUES ('4', '房地产', NULL) ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_type` (`id`, `type`, `technology_financial_id`) VALUES ('5', '其他', NULL) ON DUPLICATE KEY UPDATE id=id;//
INSERT INTO `zhjf`.`technology_loan_type` (`id`, `type`, `technology_financial_id`) VALUES ('6', '无', NULL) ON DUPLICATE KEY UPDATE id=id;//



INSERT INTO `zhjf`.`tu_code` (`id`, `code`, `codedesc`, `protype`) VALUES ('1', '1', '纯通知', 'noticeType')ON DUPLICATE KEY UPDATE id=id;//