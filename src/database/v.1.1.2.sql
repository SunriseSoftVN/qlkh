ALTER TABLE `task`  ADD `dynamicQuota` BOOL NOT NULL AFTER `quota`;

DROP TABLE `taskquota`;