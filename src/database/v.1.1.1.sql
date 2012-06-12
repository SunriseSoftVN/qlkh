ALTER TABLE `task`  ADD `dynamicDefaultValue` BOOL NOT NULL AFTER `defaultValue`;
ALTER TABLE `task` CHANGE `defaultValue` `defaultValue` DOUBLE NULL;
ALTER TABLE `systemlog` CHANGE `id` `id` BIGINT(11) NOT NULL AUTO_INCREMENT;

CREATE TABLE IF NOT EXISTS `taskdefaultvalue` (
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