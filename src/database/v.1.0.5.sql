ALTER TABLE `task` ADD `taskTypeCode` INT NOT NULL AFTER `quota`;
ALTER TABLE `task` ADD `childTasks` TEXT NULL AFTER `taskTypeCode`;