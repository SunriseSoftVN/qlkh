ALTER TABLE  `material_in` ADD  `exportDate` DATE NOT NULL AFTER  `stationId`;

INSERT INTO `user` (`id`, `userName`, `passWord`, `userRole`, `stationId`, `createdDate`, `updatedDate`, `createBy`, `updateBy`) VALUES
(50, 'thongke', '88f86247e8dc34a32ce1707877b5ee4f', 'WAREHOUSE_MANAGER', 27, '2013-05-03', '2013-05-03', 1, 1);