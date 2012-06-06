CREATE TABLE IF NOT EXISTS `systemlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `content` text NOT NULL,
  `className` varchar(250) DEFAULT NULL,
  `methodName` varchar(250) DEFAULT NULL,
  `exceptionClass` varchar(250) DEFAULT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `stationlock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stationId` bigint(20) NOT NULL,
  `code` int(2) NOT NULL,
  `updatedDate` date NOT NULL,
  `createdDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `stationlock_index` (`stationId`,`code`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

ALTER TABLE `branch` ENGINE = InnoDB;
ALTER TABLE `station` ENGINE = InnoDB;
ALTER TABLE `subtaskannualdetail` ENGINE = InnoDB;
ALTER TABLE `subtaskdetail` ENGINE = InnoDB;
ALTER TABLE `task` ENGINE = InnoDB;
ALTER TABLE `taskdetail` ENGINE = InnoDB;
ALTER TABLE `user` ENGINE = InnoDB;

ALTER TABLE  `station` ADD  `company` BOOL NOT NULL AFTER  `name`;
UPDATE  `qlvt`.`station` SET  `company` =  '1' WHERE  `station`.`id` =27;
