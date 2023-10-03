-- 25th July 2023
ALTER TABLE business_info
ADD COLUMN logoPath VARCHAR(255) AFTER location;  

INSERT INTO sms_and_email_api
VALUES("T2lVanFDcUdoR0VqYm1Zd3pyVGY", "FINSUIT-GH", "princemcbond33@gmail.com", "1234", DEFAULT);

ALTER TABLE business_info 
ADD COLUMN account_password VARCHAR(255) NOT NULL AFTER email;

ALTER TABLE employees
CHANGE COLUMN contact_person_digiital_address contact_person_digital_address VARCHAR(50);

ALTER TABLE users DROP FOREIGN KEY users_ibfk_1;

TRUNCATE TABLE employees;
TRUNCATE TABLE employees_account_details;

-- DROP TABLE sms_api;
SELECT COUNT(*) FROM employees ORDER BY emp_id DESC LIMIT 1;

INSERT INTO employees(work_id, firstname, lastname, othername, email, mobile_number, other_number, gender, dbo, digital_address, residential_address, landmark, id_type, id_number, marital_status, qualification, designation, working_experience, employment_date, contact_person_name, contact_person_number, contact_person_digital_address, contact_person_address, contact_person_landmark, contact_person_place_of_work, contact_person_org_number, contact_person_org_address, additional_information, added_by, modified_by)
VALUES('1001', 'MCBND', 'AIDIA', 'FASDFA', 'ISFIFJ@GMGO.COM','1001', 'MCBND', 'AIDIA', '2020-10-10', 'DAFADFAF','1001', 'MCBND', 'AIDIA', 'FASDFA', 'DAFADFAF','1001', 'MCBND', 'AIDIA', '2020-10-10', 'DAFADFAF','1001', 'MCBND', 'AIDIA', 'FASDFA', 'DAFADFAF','1001', 'MCBND', 'AIDIA', 1, 1);


-- 3RD AUGUST 2023
SELECT * FROM employees FULL JOIN employees_account_details as em ON work_id = em.emp_id;
SELECT * FROM employees_account_details;

-- 05-08-2023
DROP TABLE employees_account_details;



-- 06-08-2023
SELECT * FROM finsuit.users;

SELECT emp_id, role_name, username,is_active FROM users AS u
JOIN roles AS r ON  u.role_id = r.role_id WHERE is_deleted = 0;

-- 10-08-2023
ALTER TABLE users
MODIFY COLUMN user_password VARCHAR(255) COMMENT 'default password is SUIT1234';

SELECT customer_id from customer_data order by customer_id desc limit 1;

alter table customer_data
MODIFY COLUMN name_of_spouse VARCHAR(50) DEFAULT 'null'; 

SELECT concat(firstname, " ", lastname) AS fullname FROM employees AS emp 
INNER JOIN users AS u ON emp.work_id = u.emp_id
WHERE u.emp_id = "1000003";


-- 25-08-2023
SELECT concat(firstname, " ", lastname, " ", othername) AS fullname, gender, age, mobile_number,
	id_type, account_type, account_number, date_created FROM customer_data AS cd 
    INNER JOIN customer_account_data AS cad ON cd.customer_id = cad.customer_id
    WHERE cd.is_active = 0;

SELECT loan_count FROM loans_data WHERE customer_id = 1;


    
-- 26/08/2023
ALTER TABLE customer_data ADD COLUMN is_active TINYINT DEFAULT 1 AFTER date_created;
ALTER TABLE customer_document ADD COLUMN file_content BLOB NOT NULL AFTER document_name;

SELECT customer_id FROM customer_data ORDER BY customer_data.customer_id DESC LIMIT 1;
SELECT MAX(loan_id) AS 'max_id' FROM loans;


-- 27 /08 / 2023
SELECT * FROM customer_document WHERE customer_id = 1;

-- 16 / 09 /23 
ALTER TABLE business_info
ADD COLUMN loan_percentage DECIMAL(3,2) DEFAULT 0.00 NOT NULL AFTER logoPath;

SELECT * FROM customer_data WHERE id_number = "GHA-849494934-3";

SELECT id_number, account_number FROM customer_data cd
INNER JOIN customer_account_data AS cad
ON cd.customer_id = cad.customer_id;

SELECT COUNT(ln.customer_id) AS loan_count FROM loans ln
INNER JOIN customer_data AS cd
ON cd.customer_id = ln.customer_id
INNER JOIN customer_account_data AS cad
ON cad.customer_id = ln.customer_id
WHERE cd.id_number = '' OR cad.account_number = '';

-- SET foreign_key_checks = 0;




