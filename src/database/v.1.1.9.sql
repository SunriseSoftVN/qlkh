CREATE TABLE IF NOT EXISTS `material_price` (
  `id` bigint(20) NOT NULL,
  `materialId` bigint(20) NOT NULL,
  `q1` double NOT NULL,
  `q2` double NOT NULL,
  `q3` double NOT NULL,
  `q4` double NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  KEY `materialId` (`materialId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;