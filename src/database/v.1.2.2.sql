CREATE TABLE IF NOT EXISTS `material_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stationId` bigint(20) NOT NULL,
  `personName` text COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `stationId` (`stationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=0 ;

ALTER TABLE `material_person`
ADD CONSTRAINT `material_person_ibfk_1` FOREIGN KEY (`stationId`) REFERENCES `station` (`id`);

CREATE TABLE IF NOT EXISTS `material_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codeDisplay` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  `name` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `regex` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=0 ;

--
-- Dumping data for table `material_group`
--

INSERT INTO `material_group` (`id`, `codeDisplay`, `createdDate`, `updatedDate`, `createBy`, `updateBy`, `name`, `code`, `regex`) VALUES
(11, 'A', '2013-04-29', '2013-04-29', 1, 1, 'TỔNG KẾ HOẠCH GIAO', 'A', 'A.*'),
(12, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'A.I', 'SF*.I'),
(13, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'A.II', 'SF*.II'),
(14, 'SF1', '2013-04-29', '2013-04-29', 1, 1, 'HT ĐƯỜNG TRUYỀN TẢI', 'SF1', 'SF1.*'),
(15, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF1.I', '1.3*,1.4*'),
(16, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF1.II', '1.5*,1.6*'),
(17, 'SF2', '2013-04-29', '2013-04-29', 1, 1, 'TRẠM-TỔNG ĐÀI', 'SF2', 'SF2.*'),
(18, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF2.I', '2.3*,2.4*'),
(19, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF2.II', '2.5*,2.6*'),
(20, 'SF3', '2013-04-29', '2013-04-29', 1, 1, 'TÍN HIỆU RA, VÀO GA', 'SF3', 'SF3.*'),
(21, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF3.I', '3.3*,3.4*'),
(22, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF3.II', '3.5*,3.6*'),
(23, 'SF4', '2013-04-29', '2013-04-29', 1, 1, 'THIẾT BỊ KHỐNG CHẾ', 'SF4', 'SF4.*'),
(24, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF4.I', '4.3*,4.4*'),
(25, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF4.II', '4.5*,4.6*'),
(26, 'SF5', '2013-04-29', '2013-04-29', 1, 1, 'THIẾT BỊ ĐIỀU KHIỂN', 'SF5', 'SF5.*'),
(27, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF5.I', '5.3*,5.4*'),
(28, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF5.II', '5.5*,5.6*'),
(29, 'SF6', '2013-04-29', '2013-04-29', 1, 1, 'HỆ THỐNG CÁP TÍN HIỆU', 'SF6', 'SF6.*'),
(30, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF6.I', '6.3*,6.4*'),
(31, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF6.II', '6.5*,6.6*'),
(32, 'SF7', '2013-04-29', '2013-04-29', 1, 1, 'HỆ THỐNG NGUỒN ĐIỆN', 'SF7', 'SF7.*'),
(33, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF7.I', '7.3*,7.4*'),
(34, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF7.II', '7.5*,7.6*');


CREATE TABLE IF NOT EXISTS `material_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  `materialId` bigint(20) NOT NULL,
  `total` double DEFAULT NULL,
  `materialGroupId` bigint(20) DEFAULT NULL,
  `materialPersonId` bigint(20) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `quarter` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `stationId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `materialId` (`materialId`,`materialGroupId`,`materialPersonId`),
  KEY `stationId` (`stationId`),
  KEY `materialPersonId` (`materialPersonId`),
  KEY `materialGroupId` (`materialGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=0 ;

--
-- Constraints for table `material_in`
--
ALTER TABLE `material_in`
ADD CONSTRAINT `material_in_ibfk_1` FOREIGN KEY (`materialGroupId`) REFERENCES `material_group` (`id`),
ADD CONSTRAINT `material_in_ibfk_2` FOREIGN KEY (`materialPersonId`) REFERENCES `material_person` (`id`),
ADD CONSTRAINT `material_in_ibfk_3` FOREIGN KEY (`stationId`) REFERENCES `station` (`id`),
ADD CONSTRAINT `material_in_ibfk_4` FOREIGN KEY (`materialId`) REFERENCES `material` (`id`);