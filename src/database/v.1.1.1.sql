ALTER TABLE `task` CHANGE `quota` `quota` INT(11) NULL
ALTER TABLE `systemlog` CHANGE `id` `id` BIGINT(11) NOT NULL AUTO_INCREMENT;

CREATE TABLE IF NOT EXISTS `taskquota` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `taskId` bigint(11) NOT NULL,
  `q1` int(11) NOT NULL,
  `q2` int(11) NOT NULL,
  `q3` int(11) NOT NULL,
  `q4` int(11) NOT NULL,
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


CREATE TABLE IF NOT EXISTS `taskdefaultvalue` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `taskId` bigint(11) NOT NULL,
  `defaultValue` double NOT NULL,
  `quarter` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;

ALTER TABLE `qlkh`.`taskdefaultvalue`
  ADD CONSTRAINT `fk_taskdefaultvalue_task2`
  FOREIGN KEY (`taskId` )
  REFERENCES `qlkh`.`task` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_taskdefaultvalue_task2` (`taskId` ASC);