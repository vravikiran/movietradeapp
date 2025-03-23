CREATE TABLE `user_kyc_details` (
  `kyc_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `date_of_birth` varchar(255) NOT NULL,
  `mobile_no` varchar(255) NOT NULL,
  `aadhar_card_number` varchar(255) NOT NULL,
  `aadhar_image_front_url` varchar(255) DEFAULT NULL,
  `aadhar_image_back_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`kyc_id`)
)