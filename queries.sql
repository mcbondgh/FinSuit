SELECT schedule_id,
monthly_installment,
principal_amount,
interest_amount,
payment_date,
penalty_amount,
	(SELECT SUM(paid_amount) FROM loan_payment_logs
	WHERE( installment_month = payment_date)) AS monthly_payment
FROM loan_schedule AS ls
WHERE loan_no = ?;

SELECT (cash_amount + ecash_amount) AS monthly_payment FROM transaction_logs 
	INNER JOIN loan_schedule AS ls ON 
    account_number = ls.loan_no
	WHERE (account_number = '2000000000003' 
	AND transaction_type = 'REPAYMENT' 
	AND DATE(transaction_date) BETWEEN ls.payment_date AND now());
	
INSERT INTO loan_payment_logs(loan_no, installment_month, paid_amount, collected_by)
VALUES(?, ?, ?, ?);

INSERT INTO transaction_logs( 
	account_number,
	transaction_id, 
    transaction_type,
    payment_method,
	cash_amount, 
    ecash_amount, 
    user_id)
VALUES(?, ?, ?, ?, ?, ?, ?);

INSERT INTO message(
	sent_to,
    title, 
    message,
    status,
    sent_by
) 
VALUES(?, ?, ?, ?, ?);

INSERT INTO notifications(
	title,
    sender_method,
    message,
    logged_by
)
VALUES(?, ?, ?, ?);

UPDATE loans 
SET total_payment = ? WHERE(loan_no = ?);

UPDATE loans
SET loan_status = 'cleared' WHERE(total_payment >= approved_amount);
    
CREATE TABLE IF NOT EXISTS loan_payment_logs(
	log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    loan_no VARCHAR(50) NOT NULL,
    installment_month DATE NOT NULL,
    paid_amount DECIMAL(10,2),
    date_collected DATETIME DEFAULT CURRENT_TIMESTAMP, 
    collected_by INT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_logs(
	log_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    user_role VARCHAR(50) NOT NULL,
    log_date DATETIME DEFAULT CURRENT_TIMESTAMP
)


SELECT CONCAT(firstname, ' ', othername, ' ', lastname) AS fullname,
loan_id, loan_no, loan_type, requested_amount, approved_amount,
total_payment, application_status, loan_purpose, loan_status,
is_drafted, ln.date_created,
ln.date_modified, ln.created_by, ln.updated_by, ln.approved_by FROM loans AS ln
INNER JOIN customer_data AS cd 
ON ln.customer_id = cd.customer_id;

SELECT 
	DISTINCT(monthly_installment), ls.loan_id, CONCAT(e.lastname, ' ', e.firstname) AS officer, 
	ls.loan_no,  
	CONCAT(cd.lastname, ' ', cd.othername, ' ',cd.firstname) AS fullname FROM group_supervisors AS gs
INNER JOIN employees AS e
	ON gs.emp_id = work_id
INNER JOIN loans AS ls 
	ON gs.loan_id = ls.loan_no
INNER JOIN loan_schedule AS sch
	ON ls.loan_no = sch.loan_no
INNER JOIN customer_data AS cd 
	ON ls.customer_id = cd.customer_id
WHERE loan_status = 'active' AND work_id = '1000005'
;

SELECT id, account_number, 
transaction_id, 
transaction_type, 
payment_method, 
payment_gateway, 
cash_amount, 
ecash_amount,
ecash_id, 
 transaction_date,
 transaction_made_by,
 username FROM transaction_logs AS tl 
	INNER JOIN users AS u 
    ON u.user_id = tl.user_id
	WHERE (DATE(transaction_date) BETWEEN ? AND ? )
    LIMIT ?;
    

SELECT monthly_installment, paid_amount, ls.loan_no, payment_date FROM loan_schedule ls
INNER JOIN loan_payment_logs AS plogs
ON plogs.installment_month = ls.payment_date
WHERE ls.loan_no = '2000000000006';

SELECT * FROM loans
CROSS JOIN customer_data AS cd
USING(customer_id)
CROSS JOIN loan_applicant_details
USING(loan_no)
WHERE loan_no = '2000000000004';

--
UPDATE customer_account_data SET account_status = ? WHERE account_number = ?;
--
SELECT transferred_to AS `Name`, SUM(dtl.amount) AS start_amount, tca.amount AS current_balance FROM domestic_transfer_logs AS dtl
INNER JOIN temporal_cashier_account as tca
ON transferred_to = teller
WHERE transferred_to = 'allotey@example.com' AND DATE(dtl.entry_date) = CURRENT_DATE();

-- INSERT DATA INTO THE TABLE...
INSERT INTO closed_teller_transaction_logs(
	start_amount, closed_amount, physical_cash,
	 e_cash, overage_amount, shortage_amount, `comment`,
	 entered_by
 )
VALUES(?, ?, ?, ?, ?, ?, ?, ?);

SELECT account_password, account_balance, previous_balance, account_date_modified
FROM business_info;

-- ----------------------------------------------------------------------
-- today 13/05/2024
-- ----------------------------------------------------------------------
ALTER TABLE business_info ADD COLUMN 
account_balance DECIMAL(10,2) DEFAULT 0.00 AFTER account_password;

ALTER TABLE business_info ADD COLUMN
previous_balance DECIMAL(10,2) DEFAULT 0.00 AFTER account_balance;

ALTER TABLE business_info ADD COLUMN
account_date_modified DATETIME DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE IF NOT EXISTS business_transaction_logs(
	Id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_type VARCHAR(50) NOT NULL,
    bank_name VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) DEFAULT 0.00,
    transaction_id VARCHAR(100) NOT NULL,
    account_number VARCHAR(100),
    transaction_date DATE,
    notes VARCHAR(255),
    created_by INT,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP()
);

-- //    id, transfer_type, transferred_to, amount, entered_by, entry_date, time
CREATE TABLE IF NOT EXISTS domestic_transfer_logs(
	id INT PRIMARY KEY AUTO_INCREMENT,
    transfer_type VARCHAR(50) NOT NULL,
    transferred_to VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    entered_by INT,
    entry_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS temporal_cashier_account(
	id INT PRIMARY KEY AUTO_INCREMENT,
    teller VARCHAR(100) UNIQUE,
    amount DECIMAL(10,2), 
    entry_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE closed_teller_transaction_logs(
	id INT PRIMARY KEY AUTO_INCREMENT,
	start_amount DECIMAL(10, 2) NOT NULL,
    closed_amount DECIMAL(10,2) NOT NULL,
    overage_amount DECIMAL(10,2) DEFAULT 0.00,
    shortage_amount DECIMAL(10,2) DEFAULT 0.00,
    is_suspended BOOLEAN DEFAULT TRUE,
    is_closed BOOLEAN DEFAULT FALSE,
    `comment` VARCHAR(255),
    entered_by INT,
    closed_by INT,
    entry_date DATETIME DEFAULT CURRENT_TIMESTAMP(),
    closure_date DATETIME DEFAULT CURRENT_TIMESTAMP()    
);

CREATE TABLE IF NOT EXISTS group_supervisors(
	super_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id VARCHAR(50) NOT NULL, 
    loan_id VARCHAR(50) NOT NULL, 
    added_by INT,
    entry_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS revenue_account(
	id INT PRIMARY KEY DEFAULT 1,
    account_balance DECIMAL(10,2) DEFAULT 0.0,
    userId INT DEFAULT 1,
    date_updated DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO revenue_account
VALUES(1, 0.00, 1, DEFAULT);

CREATE TABLE IF NOT EXISTS revenue_account_logs(
	id INT PRIMARY KEY AUTO_INCREMENT,
    reference_number VARCHAR(50) NOT NULL, 
    entry_type VARCHAR(20) NOT NULL, 
    amount DECIMAL(10,2) NOT NULL, 
    entered_by INT,
    entry_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);


-- 18/05/2024
SELECT * FROM loans;
UPDATE loans SET loan_status = "closed", date_modified = DEFAULT
WHERE loan_no = ?;

ALTER TABLE loans CHANGE COLUMN disbursed_amount 
repayment_amount DECIMAL(10,2);


-- 24/05/2024
CREATE TABLE IF NOT EXISTS terminated_loans(
	id INT PRIMARY KEY AUTO_INCREMENT,
    loan_no VARCHAR(50) NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    write_off DECIMAL(10,2) DEFAULT 0.00,
    terminated_by INT NOT NULL,
    date_terminated DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 27/05/2024
CREATE TABLE IF NOT EXISTS agents(
	agent_id INT PRIMARY KEY AUTO_INCREMENT,
    agent_name VARCHAR(100) NOT NULL,
	mobile_number VARCHAR(20) NOT NULL,
    other_number VARCHAR(20),
    information VARCHAR(255),
    date_joined DATETIME DEFAULT CURRENT_TIMESTAMP(),
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP(),
    added_by INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS logins(
	id INT PRIMARY KEY,
    user_id INT,
    role_id TINYINT,
    entry_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- 02/06/2024 
CREATE TABLE IF NOT EXISTS modules(
	id INT PRIMARY KEY AUTO_INCREMENT,
    module_name VARCHAR(50),
    alias VARCHAR(50),
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS permissions(
	id INT PRIMARY KEY AUTO_INCREMENT,
    module_id INT COMMENT 'Holds the ID to a perticular MODULE or VIEW',
    operation_id INT COMMENT 'Holds the id to a specific operation in the system',
    role_id INT comment'Holds the ID to the specific role that can perform this opetaion',
    is_allowed BOOLEAN DEFAULT FALSE comment 'holds a YES OR NO BOOLEAN VALUE that determins in a perticular role is permitted to have access to a view or operation',
	FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (operation_id) REFERENCES operation(operation_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS operation(
	operation_id INT PRIMARY KEY AUTO_INCREMENT,
    module_status BOOLEAN,
    operation_name VARCHAR(50),
    alias VARCHAR(50),
    description VARCHAR(255),
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS module_control(
	id INT PRIMARY KEY AUTO_INCREMENT,
    module_id INT UNIQUE,
    role_id TINYINT,
    is_allowd BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL ON UPDATE CASCADE
);

ALTER TABLE access_control
CHANGE module_id module_status BOOLEAN DEFAULT 0;

ALTER TABLE customer_data ADD COLUMN agent_id INT;
ALTER TABLE customer_data ADD FOREIGN KEY fk_customer_agent 
(agent_id) REFERENCES agents(agent_id);

ALTER TABLE customer_data CHANGE COLUMN agent_id agent_id INT DEFAULT 1;

SELECT agent_name, mobile_number, other_number, information,   
(SELECT COUNT(agent_id) FROM customer_data AS cd WHERE(cd.agent_id = a.agent_id)) AS counts, 
date_modified, added_by
FROM agents AS a WHERE(is_deleted = FALSE);

UPDATE agents SET is_deleted = TRUE WHERE agent_id = ?;


-- 29/05/2024
UPDATE customre_data
	SET firstname = ?, lastname = ?, othername = ?, gender = ?, dob = ?, mobile_number = ?, other_number = ?, 
    email = ?, digital_address = ?, residential_address = ?, key_landmark =?, marital_status = ?, id_type =?, 
    id_number = ?, educational_background = ?, contact_person_fullname = ?, contact_person_number = ?, 
    contact_person_gender = ?, contact_person_landmark =?, contact_person_digital_address = ?,  contact_person_id_type = ?,
	contact_person_id_number = ?, contact_person_place_of_work = ?, institution_address = ?, relationship_to_applicant = ?,
    date_modified = DEFAULT, modified_by = ?, agent_id = ?
    WHERE customer_id = ?;
    
UPDATE loan_applicant_details 
SET image = ? , company_name = ?, company_mobile_number =?, company_address =?, staff_id =?, occupation =?, employment_date =?, 
basic_salary =?, gross_salary=?, total_deduction=?, net_salary=?, guarantor_name=?, guarantor_gender=?, guarantor_number=?, 
guarantor_digital_address=?, guarantor_landmark =?, guarantor_idType=?, guarantor_idNumber=?, guarantor_relationship=?,
guarantor_occupation =?, guarantor_place_of_work =?, guarantor_institution_address =?, guarantor_income =?
WHERE(loan_no = ?);

UPDATE loans 
	SET loan_type =?, requested_amount =?, 
	loan_purpose =?, updated_by = ?
WHERE(loan_no = ?);


-- ALTER TABLE terminated_loans
-- ADD COLUMN write_off DECIMAL(10,2) DEFAULT 0.00 AFTER purpose;

SELECT id, title, sender_method, message, logged_date, `read`, username FROM notifications AS n
                   INNER JOIN users AS u
                   ON u.user_id = n.logged_by ORDER BY id DESC  LIMIT 50;

SELECT id, title, sender_method, message, logged_date, `read`, username FROM notifications AS n
                   INNER JOIN users AS u
                   ON u.user_id = n.logged_by ORDER BY Id DESC LIMIT 5;
                   
SELECT * FROM notifications ORDER BY id DESC LIMIT 4;
UPDATE notifications SET `read` = true WHERE(id = 367);

SELECT * FROM message_logs;
UPDATE message_logs SET status = ?, sent_date = DEFAULT, sent_by = ?  WHERE(log_id = ?);

-- SET FOREIGN_KEY_CHECKS = FALSE;


-- 31/05/2024
SELECT username, user_password, role_name, is_active FROM users AS u
INNER JOIN roles AS r 
USING(role_id) WHERE(username = 'admin' AND is_deleted = 0 AND is_active = 1);

SELECT COUNT(loan_id) AS part_payments FROM loans
WHERE total_payment != repayment_amount;

SELECT COUNT(loan_id) cleared FROM loans AS total
WHERE loan_status = 'cleared';

SELECT COUNT(loan_no) as due FROM loan_schedule 
WHERE payment_date = CURRENT_DATE();

SELECT COUNT(loan_id) disbursed FROM loans 
WHERE application_status = 'disbursed';

 SELECT gs.emp_id , concat(firstname, ' ',lastname) AS `supervisor`,
        gs.loan_id AS `loan_number`, application_status, entry_date FROM employees AS emp
                    INNER JOIN group_supervisors AS gs
                    ON emp.work_id = gs.emp_id
                    INNER JOIN loans AS ln
                    ON gs.loan_id = ln.loan_no;
                    
INSERT INTO cusotmer_account_data(customer_id, account_type, account_status, account_number, modified_by)
VALUES(?, ?, ?, ?, ?);

INSERT INTO access_control(module_id, role_id, permission_id, is_allowed, modified_by)
VALUES(?, ?, ?, ?, ?)
ON DUPLICATE KEY 
UPDATE 
permission_id = VALUES(?),
is_allowed = VALUES(?),
modified_by = VALUES(?);


