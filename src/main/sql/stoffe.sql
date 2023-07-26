CREATE TABLE `stoffe` (
  `StoffId` int NOT NULL AUTO_INCREMENT,
  `StoffName` varchar(128) NOT NULL,
  PRIMARY KEY (`StoffId`),
  UNIQUE KEY `Stoffname_UNIQUE` (`StoffName`)
) ENGINE=InnoDB AUTO_INCREMENT=3985 DEFAULT CHARSET=utf8mb3;
