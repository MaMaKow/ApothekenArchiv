CREATE TABLE `plausibilitätsprotokolle` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `datum` date NOT NULL,
  `plausibilitätsnummer` varchar(45) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datei` longblob,
  `unterschrift` blob COMMENT 'Eine .sig Datei hat eine Größe von 438 Bytes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
