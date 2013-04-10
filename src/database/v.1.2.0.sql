ALTER TABLE `material_price`
  DROP `q1`,
  DROP `q2`,
  DROP `q3`,
  DROP `q4`;

ALTER TABLE  `material_price` ADD  `price` DOUBLE NOT NULL AFTER  `materialId` ,
ADD  `quarter` INT NOT NULL AFTER  `price`;