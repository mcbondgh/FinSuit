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