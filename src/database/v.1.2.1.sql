CREATE VIEW `task_material` AS
  SELECT
    `task`.`id`                 AS `taskId`,
    `material`.`id`             AS `materialId`,
    `material_limit`.`quantity` AS `quantity`,
    `material_price`.`price`    AS `price`,
    `material_price`.`year`     AS `year`,
    `material_price`.`quarter`  AS `quarter`
  FROM `task`
    INNER JOIN `material_limit`
      ON `task`.`id` = `material_limit`.`taskId`
    INNER JOIN `material`
      ON `material_limit`.`materialId` = `material`.`id`
    INNER JOIN `material_price`
      ON `material_limit`.`materialId` = `material_price`.`materialId`