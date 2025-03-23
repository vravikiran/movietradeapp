CREATE TABLE `transaction_details` (
  `transaction_id` varchar(255) NOT NULL,
  `status` varchar(20) NOT NULL,
  `bank_name` varchar(30) DEFAULT NULL,
  `transaction_date` date NOT NULL,
  `investment_id` varchar(255) NOT NULL,
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`transaction_id`)
)