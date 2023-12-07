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

SELECT monthly_installment, paid_amount, ls.loan_no, payment_date FROM loan_schedule ls
INNER JOIN loan_payment_logs AS plogs
ON plogs.installment_month = ls.payment_date
WHERE ls.loan_no = '2000000000006';
