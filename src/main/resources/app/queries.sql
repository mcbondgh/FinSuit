CREATE DATABASE IF NOT EXISTS finsuit;

CREATE TABLE IF NOT EXISTS business_info(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    business_name VARCHAR(100) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL, 
    other_number VARCHAR(20),
    email VARCHAR(100) NOT NULL,
    account_password VARCHAR(255) NOT NULL,
    digital_address VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    logoPath VARCHAR(255),
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- THIS TABLE IS DROPPED AND REPLACED BY sms_and_email_api
-- CREATE TABLE IF NOT EXISTS sms_api(
	-- api_key VARCHAR(255) NOT NULL,
	-- sender_id VARCHAR(11) NOT NULL,
    -- date_added DATETIME DEFAULT CURRENT_TIMESTAMP
-- );

CREATE TABLE IF NOT EXISTS sms_and_email_api(
	api_key VARCHAR(255) NOT NULL,
	sender_id VARCHAR(11) NOT NULL,
    email_address VARCHAR(100) NOT NULL,
    email_password VARCHAR(255) NOT NULL,
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `employees` (
  `emp_id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `othername` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `other_number` varchar(20) DEFAULT NULL,
  `gender` varchar(10) NOT NULL,
  `dbo` date NOT NULL,
  `digital_address` varchar(50) NOT NULL,
  `residential_address` varchar(100) NOT NULL,
  `landmark` varchar(100) NOT NULL,
  `id_type` varchar(50) NOT NULL,
  `id_number` varchar(20) NOT NULL,
  `marital_status` varchar(20) NOT NULL,
  `qualification` varchar(100) NOT NULL,
  `designation` varchar(100) DEFAULT NULL,
  `working_experience` varchar(100) NOT NULL,
  `employment_date` date DEFAULT NULL,
  `contact_person_name` varchar(100) NOT NULL,
  `contact_person_number` varchar(20) NOT NULL,
  `contact_person_digital_address` varchar(50) DEFAULT NULL,
  `contact_person_address` varchar(100) NOT NULL,
  `contact_person_landmark` varchar(100) NOT NULL,
  `contact_person_place_of_work` varchar(100) NOT NULL,
  `contact_person_org_number` varchar(20) DEFAULT NULL,
  `contact_person_org_address` varchar(100) DEFAULT NULL,
  `additional_information` text,
  `is_active` tinyint DEFAULT '1',
  `is_deleted` tinyint DEFAULT '0',
  `date_added` datetime DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime DEFAULT CURRENT_TIMESTAMP,
  `added_by` int DEFAULT '1',
  `modified_by` int DEFAULT '1',
  `work_id` varchar(45) NOT NULL,
  PRIMARY KEY (`emp_id`),
  UNIQUE KEY `work_id_UNIQUE` (`work_id`)
);

CREATE TABLE IF NOT EXISTS employees_account_details(
	id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT,
    salary DECIMAL(5,2) NOT NULL,
    bank_name VARCHAR(100),
    account_name VARCHAR(100),
    account_number VARCHAR(50),
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    FOREIGN KEY (emp_id) REFERENCES employees(emp_id) ON DELETE CASCADE ON UPDATE SET NULL
);


CREATE TABLE IF NOT EXISTS users(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT ,
    role_id INT,
    usernane VARCHAR(100) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT 1,
    is_deleted BOOLEAN DEFAULT 0,
	date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    added_by INT,
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    FOREIGN KEY(emp_id) REFERENCES employees(emp_id) ON DELETE CASCADE ON UPDATE SET NULL,
    FOREIGN KEY(role_id) REFERENCES roles(role_id) ON DELETE CASCADE ON UPDATE SET NULL
);

CREATE TABLE IF NOT EXISTS roles(
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(20),
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP
);


