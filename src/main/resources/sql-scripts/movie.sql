CREATE TABLE `movie` (
  `movieid` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `language` varchar(255) NOT NULL,
  `duration` time NOT NULL,
  `cbfc_rating` varchar(3) NOT NULL,
  `release_date` date NOT NULL,
  `imageurl` varchar(255) DEFAULT NULL,
  `format_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `genre` varchar(255) NOT NULL,
  `isactive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`movieid`),
  UNIQUE KEY `movie_name_lang_format_unique` (`name`,`language`,`format_type`)
)