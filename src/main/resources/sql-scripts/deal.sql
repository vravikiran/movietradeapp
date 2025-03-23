CREATE TABLE `deal` (
  `dealid` int NOT NULL AUTO_INCREMENT,
  `theatre_id` int NOT NULL,
  `movieid` int NOT NULL,
  `showdate` date NOT NULL,
  `showtime` time NOT NULL,
  `total_dealprice` double NOT NULL DEFAULT '0',
  `total_actualprice` double NOT NULL DEFAULT '0',
  `maxprofit` double NOT NULL DEFAULT '0',
  `city_id` int NOT NULL,
  `is_invested` tinyint(1) DEFAULT '0',
  `created_date` date DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  `actual_ticket_price` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dealid`),
  UNIQUE KEY `deal_unique` (`theatre_id`,`showdate`,`showtime`),
  KEY `deal_city_FK` (`city_id`),
  CONSTRAINT `deal_city_FK` FOREIGN KEY (`city_id`) REFERENCES `city` (`city_id`)
)