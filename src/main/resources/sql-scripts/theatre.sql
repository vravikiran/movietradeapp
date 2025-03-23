CREATE TABLE `theatre` (
  `theatre_id` int NOT NULL AUTO_INCREMENT,
  `city_id` int NOT NULL,
  `location` varchar(255) NOT NULL,
  `property_name` varchar(255) NOT NULL,
  `theatre_name` varchar(255) NOT NULL,
  `capacity` int NOT NULL,
  PRIMARY KEY (`theatre_id`),
  UNIQUE KEY `theatre_unique` (`theatre_name`),
  KEY `theatre_city_FK` (`city_id`),
  CONSTRAINT `theatre_city_FK` FOREIGN KEY (`city_id`) REFERENCES `city` (`city_id`)
)