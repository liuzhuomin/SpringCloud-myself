SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zhjf_request_model
-- ----------------------------
CREATE TABLE  IF NOT EXISTS `zhjf_request_model` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50001', '50001', 'http://localhost:8100/policy/policy/list/notCreate') ON DUPLICATE KEY UPDATE id=id;;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50002', '50002', 'http://localhost:8100/policy/policy/template/add') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50003', '50003', 'http://localhost:8100/policy/policy/template/edit') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50004', '50004', 'http://localhost:8100/policy/policy/template/delete') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50005', '50005', 'http://localhost:8100/policy/policy/template/list') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50006', '50006', 'http://localhost:8100/policy/policy/activity/list') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50007', '50007', 'http://localhost:8100/policy/policy/activity/delete') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50008', '50008', 'http://localhost:8100/policy/problem/add/other') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50009', '50009', 'http://localhost:8100/policy/problem/json') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50010', '50010', 'http://localhost:8100/policy/problem/add/singleradio') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50011', '50011', 'http://localhost:8100/policy/problem/limit/basedata') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50012', '50012', 'http://localhost:8100/policy/problem/limit/add') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50013', '50013', 'http://localhost:8100/policy/problem/edit/singleradio/before') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50014', '50014', 'http://localhost:8100/policy/problem/edit/singleradio') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50015', '50015', 'http://localhost:8100/policy/problem/edit/other') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50016', '50016', 'http://localhost:8100/policy/problem/delete/all') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50017', '50017', 'http://localhost:8100/policy/problem/limit/delete') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50018', '50018', 'http://localhost:8100/policy/problem/limit/edit') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50019', '50019', 'http://localhost:8100/policy/problem/limit/json') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50020', '50020', 'http://localhost:8100/policy/problem/trigger/listData/before/add') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50021', '50021', 'http://localhost:8100/policy/problem/trigger/add') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50022', '50022', 'http://localhost:8100/policy/problem/trigger/delete') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50023', '50023', 'http://localhost:8100/policy/problem/trigger/edit') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50024', '50024', 'http://localhost:8100/policy/problem/trigger/json') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50025', '50025', 'http://localhost:8100/policy/problem/trigger/result/add') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50026', '50026', 'http://localhost:8100/policy/problem/trigger/result/delete') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50027', '50027', 'http://localhost:8100/policy/problem/trigger/result/edit') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50028', '50028', 'http://localhost:8100/policy/problem/trigger/result/json') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50029', '50029', 'http://localhost:8100/policy/policy/list/group/all') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50030', '50030', 'http://localhost:8100/policy/policy/page/byGroup') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50031', '50031', 'http://localhost:8100/policy/policy/list/group/created') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50032', '50032', 'http://localhost:8100/policy/policy/create/template') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50033', '50033', 'http://localhost:8100/policy/policy/match') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50051', '50051', 'http://localhost:8100/policy/policy/editGroup') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50052', '50052', 'http://localhost:8100/policy/policy/listGroup') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50053', '50053', 'http://localhost:8100/policy/policy/findCompanyInfo/byName') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO `zhjf_request_model` (`id`, `code`, `method`) VALUES ('50054', '50054', 'http://localhost:8100/policy/policy/addCompany') ON DUPLICATE KEY UPDATE id=id;
