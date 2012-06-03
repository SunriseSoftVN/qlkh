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