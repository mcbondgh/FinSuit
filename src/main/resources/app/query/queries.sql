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
    loan_percentage DECIMAL(3, 2) NOT NULL,
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
 
CREATE TABLE IF NOT EXISTS customer_data(
	customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
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
    is_active TINYINT(1) COMMENT '0 means account closed | 1 means account open',
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by INT, 
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by INT
);

CREATE TABLE IF NOT EXISTS customer_account_data(
	account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
	account_type VARCHAR(50) DEFAULT 'Savings Account' NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    account_balance DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Current balance displays the actual amount of money left in the customers account after all dedactions',
    previous_balance DECIMAL(10,2) DEFAULT 0.00 COMMENT 'This balance shows the previous amount in the customers account before total dedaction.',
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by INT NOT NULL,
    FOREIGN KEY(customer_id) REFERENCES customer_data(customer_id)
);

CREATE TABLE customer_document(
	doc_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    document_type VARCHAR(10),
    document_name VARCHAR(100) not null,
    file_content BLOB NOT NULL,
    reason_for_upload TEXT NOT NULL,
    date_uploaded DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modified DATETIME DEFAULT NOW(),
    uploaded_by INT NOT NULL,
    modified_by INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer_data(customer_id)
);

CREATE TABLE IF NOT EXISTS group_supervisors(
	group_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id VARCHAR(50) NOT NULL,
    loan_id VARCHAR(50),
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    added_by INT, 
    modified_by INT,
    FOREIGN KEY(added_by) REFERENCES employees(emp_id)
);

CREATE TABLE IF NOT EXISTS loans(
	loan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    loan_no VARCHAR(50) NOT NULL,
    loan_type VARCHAR(50) NOT NULL,
    requested_amount DOUBLE(10,2) DEFAULT 0.00 NOT NULL,
    application_status TINYINT DEFAULT 1 COMMENT '1 means pending approval | 2 means pending payment | 3 means paid | 4 means rejected',
    is_drafted BOOLEAN DEFAULT 0 ,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    approved_by INT
);

CREATE TABLE IF NOT EXISTS loan_applicant_details(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    loan_no BIGINT,
    company_name VARCHAR(50),
    company_mobile_number VARCHAR(20),
    company_address VARCHAR(50),
    staff_id VARCHAR(50),
    occupation VARCHAR(50),
    employment_date DATE,
    basic_salary DECIMAL(10,2) DEFAULT '0.00',
    gross_salary DECIMAL(10,2) DEFAULT '0.00',
    total_deduction DECIMAL(10,2) DEFAULT '0.00',
    net_salary DECIMAL(10,2) DEFAULT '0.00',
    guranter_name VARCHAR(100),
    guranter_number VARCHAR(20),
    guranter_digital_address VARCHAR(50),
    guranter_residential_address VARCHAR(100),
    guranter_idType VARCHAR(20),
    guranter_idNumber VARCHAR(20),
    guranter_relationship VARCHAR(50),
    guranter_occupation VARCHAR(50),
    gurater_place_of_work VARCHAR(50),
    guranter_institution_address VARCHAR(100),
    guranter_income DECIMAL(10,2)
);



