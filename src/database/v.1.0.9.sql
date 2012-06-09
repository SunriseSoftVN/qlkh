DROP VIEW `subannualtaskdetail_view`;

CREATE
 ALGORITHM = UNDEFINED
 VIEW `subannualtaskdetail_view`
 AS SELECT
task.id as taskId,
taskdetail.id as taskDetailId,
taskdetail.stationId,
taskdetail.year,
taskdetail.annual,
subtaskannualdetail.id as subTaskId,
subtaskannualdetail.branchId as branchId,
subtaskannualdetail.lastYearValue,
subtaskannualdetail.increaseValue,
subtaskannualdetail.decreaseValue
FROM task
INNER JOIN taskdetail ON task.id = taskdetail.taskId
INNER JOIN subtaskannualdetail ON taskdetail.id = subtaskannualdetail.taskDetailId;

ALTER TABLE `subtaskannualdetail` DROP `realValue`;