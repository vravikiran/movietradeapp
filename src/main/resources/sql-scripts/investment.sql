CREATE TABLE `investment` (
  `investment_id` varchar(255) NOT NULL,
  `movie_release_date` date NOT NULL,
  `movie_name` varchar(255) NOT NULL,
  `theatre_name` varchar(255) NOT NULL,
  `showdate` date NOT NULL,
  `showtime` time NOT NULL,
  `status` varchar(255) NOT NULL,
  `investedamt` double NOT NULL,
  `earnedamt` double NOT NULL DEFAULT '0',
  `theatre_id` int NOT NULL,
  `movieid` int NOT NULL,
  `dealid` int NOT NULL,
  `house_capacity` int NOT NULL,
  `tickets_sold` int NOT NULL DEFAULT '0',
  `created_date` date NOT NULL,
  `updated_date` date NOT NULL,
  `mobileno` bigint DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `trans_digits` int NOT NULL,
  PRIMARY KEY (`investment_id`),
  UNIQUE KEY `investment_unique` (`investment_id`),
  KEY `investment_deal_FK` (`dealid`),
  KEY `investment_movie_FK` (`movieid`),
  KEY `investment_theatre_FK` (`theatre_id`),
  KEY `investment_user_profile_FK` (`mobileno`),
  KEY `investment_transaction_details_FK` (`transaction_id`),
  CONSTRAINT `investment_deal_FK` FOREIGN KEY (`dealid`) REFERENCES `deal` (`dealid`),
  CONSTRAINT `investment_movie_FK` FOREIGN KEY (`movieid`) REFERENCES `movie` (`movieid`),
  CONSTRAINT `investment_theatre_FK` FOREIGN KEY (`theatre_id`) REFERENCES `theatre` (`theatre_id`),
  CONSTRAINT `investment_transaction_details_FK` FOREIGN KEY (`transaction_id`) REFERENCES `transaction_details` (`transaction_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `investment_user_profile_FK` FOREIGN KEY (`mobileno`) REFERENCES `user_profile` (`mobileno`)
) 