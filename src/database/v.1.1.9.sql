CREATE TABLE IF NOT EXISTS `material_price` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `materialId` bigint(20) NOT NULL,
  `q1` double NOT NULL,
  `q2` double NOT NULL,
  `q3` double NOT NULL,
  `q4` double NOT NULL,
  `year` int(11) NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `price_year` (`materialId`,`year`),
  KEY `materialId` (`materialId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=0 ;

ALTER TABLE `material_price`
ADD CONSTRAINT `material_price_ibfk_1` FOREIGN KEY (`materialId`) REFERENCES `material` (`id`);

ALTER TABLE  `qlkh`.`material_limit` ADD UNIQUE  `task_material` (  `taskId` ,  `materialId` );

ALTER TABLE `material_limit`
ADD CONSTRAINT `material_limit_ibfk_1` FOREIGN KEY (`taskId`) REFERENCES `task` (`id`),
ADD CONSTRAINT `material_limit_ibfk_2` FOREIGN KEY (`materialId`) REFERENCES `material` (`id`);