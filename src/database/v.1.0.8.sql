UPDATE  `taskdetail` SET YEAR =2012;
ALTER TABLE  `systemlog` CHANGE  `content`  `content` TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;

CREATE
 ALGORITHM = UNDEFINED
 VIEW `subtaskdetail_view`
 AS SELECT
task.id as taskId,
taskdetail.id as taskDetailId,
taskdetail.stationId,
taskdetail.year,
taskdetail.annual,
subtaskdetail.id as subTaskId,
subtaskdetail.branchId as branchId,
subtaskdetail.q1,
subtaskdetail.q2,
subtaskdetail.q3,
subtaskdetail.q4
FROM task
INNER JOIN taskdetail ON task.id = taskdetail.taskId
INNER JOIN subtaskdetail ON taskdetail.id = subtaskdetail.taskDetailId;

CREATE
 ALGORITHM = UNDEFINED
 VIEW `taskdetail_view`
 AS SELECT
task.id as taskId,
taskdetail.id as taskDetailId,
taskdetail.stationId as stationId,
taskdetail.annual as annual,
taskdetail.year as year
FROM
task
INNER JOIN taskdetail ON task.id = taskdetail.taskId;

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
subtaskannualdetail.decreaseValue,
subtaskannualdetail.realValue
FROM task
INNER JOIN taskdetail ON task.id = taskdetail.taskId
INNER JOIN subtaskannualdetail ON taskdetail.id = subtaskannualdetail.taskDetailId;