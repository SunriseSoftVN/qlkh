ALTER TABLE  `subtaskannualdetail` DROP  `lastYear`;
ALTER TABLE  `subtaskannualdetail` DROP  `currentYear`;
ALTER TABLE  `qlvt`.`subtaskannualdetail` ADD UNIQUE  `sub_task_detail_annual_index` (  `taskDetailId` ,  `branchId` );
-- Fix bug set time on client when time on client is not correct.
update `taskdetail` set `year` = 2012 WHERE `year` = 2013;