-- CREATE DATABASE IF NOT EXISTS finsuit;


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

CREATE TABLE IF NOT EXISTS employees(
	emp_id INT AUTO_INCREMENT,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    othername VARCHAR(50),
    email VARCHAR(100),
    mobile_number VARCHAR(20) NOT NULL,
    other_number VARCHAR(20),
    gender VARCHAR(10) NOT NULL,
    dbo DATE NOT NULL,
    digital_address VARCHAR(50) NOT NULL,
    residential_address VARCHAR(100) NOT NULL,
    landmark VARCHAR(100) NOT NULL,
    id_type VARCHAR(50) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    marital_status VARCHAR(20) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    designation VARCHAR(100),
    working_experience VARCHAR(100) NOT NULL,
    employment_date DATE,
    contact_person_name VARCHAR(100) NOT NULL,
    contact_person_number VARCHAR(20) NOT NULL,
    contact_person_digiital_address VARCHAR(50) NOT NULL,
    contact_person_address VARCHAR(100) NOT NULL,
    contact_person_landmark VARCHAR(100) NOT NULL,
    contact_person_place_of_work VARCHAR(100) NOT NULL,
    contact_person_org_number VARCHAR(20),
    contact_person_org_address VARCHAR(100),
    additional_information TEXT,
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    added_by INT,
    modified_by INT,
    PRIMARY KEY (emp_id)
);

/* CREATE TABLE IF NOT EXISTS employees_account_details(
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
); */
 
CREATE TABLE IF NOT EXISTS customer_accounts(
	account_Id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_type VARCHAR(50) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    othername VARCHAR(50) NOT NULL,
    gender VARCHAR(50),
    dob DATE NOT NULL,
    age TINYINT NOT NULL,
	place_of_birth VARCHAR(50),
    mobile_number VARCHAR(50) NOT NULL,
    other_number VARCHAR(50),
	email VARCHAR(50),
	digital_address VARCHAR(50) NOT NULL,
    residential_address VARCHAR(50) NOT NULL,
    key_landmark VARCHAR(50) NOT NULL,
    marital_status VARCHAR(50) NOT NULL,
    name_of_spouse VARCHAR(50),
    id_type VARCHAR(50) NOT NULL, 
    id_number VARCHAR(50) NOT NULL,
    educational_background VARCHAR(50),
    additional_comment TEXT,
    contact_person_fullname VARCHAR(50) NOT NULL,
    contact_person_dob DATE NOT NULL,
    contact_person_number VARCHAR(50) NOT NULL,
    contact_person_gender VARCHAR(50) NOT NULL,
    contact_person_landmark VARCHAR(50) NOT NULL,
    contact_person_education_level VARCHAR(50),
    contact_person_digital_address VARCHAR(50),
    contact_person_id_type VARCHAR(50) NOT NULL,
    contact_person_id_number VARCHAR(50) NOT NULL,
    contact_person_place_of_work VARCHAR(50),
    institution_address VARCHAR(50),
    institution_number VARCHAR(50),
    relationship_to_applicant VARCHAR(50),
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by INT, 
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by INT
);

CREATE TABLE IF NOT EXISTS account_balance(
	balance_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    current_balance DECIMAL(10,2) COMMENT 'Current balance displays the actual amount of money left in the customers account after all dedactions',
    previous_balance DECIMAL(10,2) COMMENT 'This balance shows the previous amount in the customers account before total dedaction.',
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by INT NOT NULL,
    FOREIGN KEY(account_id) REFERENCES customer_accounts(account_Id)
);
