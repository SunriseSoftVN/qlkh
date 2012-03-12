ALTER TABLE `task` ADD `taskTypeCode` INT NOT NULL AFTER `quota`;
ALTER TABLE `task` ADD `childTasks` TEXT NULL AFTER `taskTypeCode`;
UPDATE task SET taskTypeCode = 3 WHERE (not (code like '%.5%')) and (taskTypeCode = 0);
TRUNCATE TABLE `subtaskannualdetail`;
TRUNCATE TABLE `subtaskdetail`;
TRUNCATE TABLE `taskdetail`;