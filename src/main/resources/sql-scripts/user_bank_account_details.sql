CREATE TABLE `user_bank_account_details` (
  `bank_acc_details_id` int NOT NULL AUTO_INCREMENT,
  `account_holder_name` varchar(255) NOT NULL,
  `account_number` varchar(255) NOT NULL,
  `ifsc_code` varchar(255) NOT NULL,
  `created_date` date NOT NULL,
  `updated_date` date NOT NULL,
  PRIMARY KEY (`bank_acc_details_id`)
)