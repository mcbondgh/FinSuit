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

-- QUERY DATE 04/10/23
ALTER TABLE loans
ADD COLUMN disbursed_amount DECIMAL(10,2) DEFAULT 0.00 AFTER requested_amount;
ALTER TABLE loans ADD COLUMN total_payment DECIMAL(10,2) DEFAULT 0.00 AFTER disbursed_amount;
ALTER TABLE loans MODIFY COLUMN application_status VARCHAR(20) DEFAULT 'processing'; 
ALTER TABLE loans ADD COLUMN loan_status VARCHAR(20) DEFAULT 'active' AFTER application_status;

TRUNCATE TABLE employees;
TRUNCATE TABLE employees_account_details;

-- DROP TABLE sms_api;
SELECT COUNT(*) FROM employees ORDER BY emp_id DESC LIMIT 1;

INSERT INTO employees(work_id, firstname, lastname, othername, email, mobile_number, other_number, gender, dbo, digital_address, residential_address, landmark, id_type, id_number, marital_status, qualification, designation, working_experience, employment_date, contact_person_name, contact_person_number, contact_person_digital_address, contact_person_address, contact_person_landmark, contact_person_place_of_work, contact_person_org_number, contact_person_org_address, additional_information, added_by, modified_by)
VALUES('1001', 'MCBND', 'AIDIA', 'FASDFA', 'ISFIFJ@GMGO.COM','1001', 'MCBND', 'AIDIA', '2020-10-10', 'DAFADFAF','1001', 'MCBND', 'AIDIA', 'FASDFA', 'DAFADFAF','1001', 'MCBND', 'AIDIA', '2020-10-10', 'DAFADFAF','1001', 'MCBND', 'AIDIA', 'FASDFA', 'DAFADFAF','1001', 'MCBND', 'AIDIA', 1, 1);

INSERT INTO message_operations(operation) 
VALUES("1.Account Opening"),
("Account Update"),
("Cash Deposit"),
(".Cash Withdrawal"),
("Loan Application"),
("Loan Payment"),
("Loan Reminder"),
("Loan Approval");

ALTER TABLE message_operations ADD PRIMARY KEY(id);

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

SELECT COUNT(ln.customer_id) AS loan_count, sum(ln.disbursed_amount) AS 'disbursed_amount' , SUM(total_payment) total_payment FROM loans ln
INNER JOIN customer_data AS cd
ON cd.customer_id = ln.customer_id
INNER JOIN customer_account_data AS cad
ON cad.customer_id = ln.customer_id 
WHERE cd.id_number = '' OR cad.account_number = '';

SELECT COUNT(loan_id) AS drafts FROM loans WHERE is_drafted = 1;

SELECT account_number FROM customer_account_data AS cad
INNER JOIN loans as ln ON 
cad.customer_id = ln.customer_id;

SELECT account_type, id_number FROM customer_account_data AS cad
JOIN customer_data AS cd ON 
cd.customer_id = cad.customer_id;

SELECT account_Number FROM customer_account_data ORDER BY account_number DESC LIMIT 1;
SELECT MAX(account_number) AS result FROM customer_account_data;

SELECT loan_id, CONCAT(lastname, ' ', firstname) AS fullname, loan_no, loan_type, DATE(ln.date_created) AS application_date, 
application_status FROM loans AS ln
JOIN customer_data AS cd ON 
ln.customer_id = cd.customer_id
JOIN users AS u On ln.created_by = u.user_id
WHERE(application_status = 'application');

SELECT firstname, lastname, othername, gender, dob, mobile_number, other_number, email, 
		digital_address, residential_address, key_landmark, marital_status, id_type, id_number,
        educational_background
FROM customer_data AS cd 
JOIN customer_account_data AS cad ON cd.customer_id = cad.customer_id
WHERE cd.id_number = ? OR cad.account_number = ?;

-- 14/10/2023
ALTER TABLE transaction_logs
ADD COLUMN national_id_number VARCHAR(20) AFTER transaction_made_by;

SELECT account_number ,mobile_number FROM customer_account_data, customer_data;

SELECT concat(firstname, ' ', lastname, ' ', othername) AS fullname, account_balance, account_number, cd.customer_id 
AS accountNo FROM customer_data AS cd
	JOIN customer_account_data AS cad ON
	cd.customer_id = cad.customer_id
	WHERE(cad.account_number = '1000000000003' OR mobile_number = '0202020202' );
    

-- 17 / 10 / 2023
SELECT * FROM transaction_logs;
SELECT tl.id, transaction_id, cad.account_number, concat(firstname, ' ', lastname, ' ', othername) AS fullname,
transaction_type, (cash_amount + ecash_amount) 
as amount, ecash_id, transaction_tax, payment_method, 
transaction_made_by AS 'made by', transaction_date, username AS amount  
FROM customer_data AS cd
INNER JOIN customer_account_data AS cad 
ON  cd.customer_id = cad.customer_id
INNER JOIN transaction_logs AS tl ON 
cad.account_number = tl.account_number 
INNER JOIN USERS AS u ON 
tl.user_id = u.user_id
ORDER BY transaction_id DESC LIMIT 100;

SELECT COUNT(*) count FROM transaction_logs WHERE DATE(transaction_date) = CURRENT_DATE();
select DATE(transaction_date) as date FROM transaction_logs WHERE DATE(transaction_date) = current_date();

SELECT transaction_id, transaction_type, (cash_amount + ecash_amount) AS amount, 
TIME(transaction_date) AS `time` FROM transaction_logs WHERE DATE(transaction_date) = current_date();

ALTER TABLE customer_account_data
DROP FOREIGN KEY customer_account_data_ibfk_1;


-- 22 / 10 / 2023
ALTER TABLE loans CHANGE COLUMN assigned_officer_id employee_id VARCHAR(20);
-- UPDATE loans SET application_status = 'processing', date_modified = DEFAULT, updated_by = ?, employee_id = ? WHERE(loan_no = ?); 

SELECT * FROM message_templates AS mt
JOIN message_operations AS mo 
ON mt.message_id = mo.template_id;




-- SET foreign_key_checks = 0;




