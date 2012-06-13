ALTER TABLE `task`  ADD `dynamicQuota` BOOL NOT NULL AFTER `quota`;
ALTER TABLE `task` CHANGE `defaultValue` `defaultValue` DOUBLE NULL;
ALTER TABLE `systemlog` CHANGE `id` `id` BIGINT(11) NOT NULL AUTO_INCREMENT;

CREATE TABLE IF NOT EXISTS `taskquota` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `taskId` bigint(11) NOT NULL,
  `q1` double NOT NULL,
  `q2` double NOT NULL,
  `q3` double NOT NULL,
  `q4` double NOT NULL,
  `year` int(11) NOT NULL,
  `updateBy` bigint(11) NOT NULL,
  `createBy` bigint(11) NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;

ALTER TABLE `qlkh`.`taskquota`
  ADD CONSTRAINT `fk_taskdefaultvalue_task1`
  FOREIGN KEY (`taskId` )
  REFERENCES `qlkh`.`task` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_taskdefaultvalue_task1` (`taskId` ASC);

ALTER TABLE `qlkh`.`taskquota` ADD UNIQUE `task_index` (`taskId`, `year`);