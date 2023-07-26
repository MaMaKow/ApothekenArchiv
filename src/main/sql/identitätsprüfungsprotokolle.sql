CREATE TABLE `identitätsprüfungsprotokolle` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stoffName` varchar(128) DEFAULT NULL,
  `chargenNummer` varchar(45) DEFAULT NULL,
  `dateiName` varchar(256) DEFAULT NULL,
  `dateiNameUnterschrift` varchar(256) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;
