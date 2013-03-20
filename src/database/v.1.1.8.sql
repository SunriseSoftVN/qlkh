CREATE TABLE IF NOT EXISTS `materiallimit` (
  `id` bigint(20) NOT NULL,
  `createdDate` date NOT NULL,
  `updatedDate` date NOT NULL,
  `createBy` bigint(20) NOT NULL,
  `updateBy` bigint(20) NOT NULL,
  `name` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `unit` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `quantity` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;