/*
 * Copyright (C) 2023 Mandelkow
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/**
 * Author:  Mandelkow
 * Created: 26.07.2023
 */

CREATE TABLE `herstellungsprotokolle` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `herstellungsdatum` date NOT NULL,
  `plausibilitätsnummer` varchar(45) NOT NULL,
  `menge` int DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datei` longblob,
  `unterschrift` blob COMMENT 'Eine .sig Datei hat eine Größe von 438 Bytes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
