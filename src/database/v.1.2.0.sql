ALTER TABLE `material_price`
  DROP `q1`,
  DROP `q2`,
  DROP `q3`,
  DROP `q4`;

ALTER TABLE  `material_price` ADD  `price` DOUBLE NOT NULL AFTER  `materialId` ,
ADD  `quarter` INT NOT NULL AFTER  `price`;

ALTER TABLE `material_price` DROP INDEX `price_year`;

ALTER TABLE  `qlkh`.`material_price` ADD UNIQUE  `price_year_quarter` (  `id` ,  `quarter` ,  `year` );