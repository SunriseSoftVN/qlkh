ALTER TABLE `task` CHANGE `name` `name` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL;
ALTER TABLE `qlkh`.`taskdetail_dk` DROP INDEX `sub_task_detail_annual_index`, ADD UNIQUE `sub_task_detail_annual_index` (`taskId`, `branchId`, `year`);
ALTER TABLE `qlkh`.`taskdetail_nam` DROP INDEX `taskdetail_nam_index`, ADD UNIQUE `taskdetail_nam_index` (`taskId`, `branchId`, `year`);
ALTER TABLE `qlkh`.`taskdetail_kdk` DROP INDEX `sub_task_detail_index`, ADD UNIQUE `sub_task_detail_index` (`taskId`, `branchId`, `year`);