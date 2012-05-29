ALTER TABLE  `subtaskannualdetail` DROP  `lastYear`;
ALTER TABLE  `subtaskannualdetail` DROP  `currentYear`;
ALTER TABLE  `qlvt`.`subtaskannualdetail` ADD UNIQUE  `sub_task_detail_annual_index` (  `taskDetailId` ,  `branchId` );