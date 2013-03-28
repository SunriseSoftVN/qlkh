CREATE TABLE IF NOT EXISTS `material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  `name` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `unit` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `note` text COLLATE utf8_unicode_ci,
  `price` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_index` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `material_limit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `taskId` bigint(20) NOT NULL,
  `materialId` bigint(20) NOT NULL,
  `quantity` double NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `task_index` (`taskId`),
  KEY `material_index` (`materialId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

ALTER TABLE `user` CHANGE `userRole` `userRole` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL;