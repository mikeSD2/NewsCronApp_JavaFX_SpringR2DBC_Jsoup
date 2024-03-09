CREATE TABLE IF NOT EXISTS `news` (
  `headline` VARCHAR(255) NOT NULL unique,
  `description` VARCHAR(10255) NULL DEFAULT NULL,
  `publication_time` TIMESTAMP,
  PRIMARY KEY (`headline`));