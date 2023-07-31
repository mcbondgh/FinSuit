-- 25th July 2023
ALTER TABLE business_info
ADD COLUMN logoPath VARCHAR(255) AFTER location;  

INSERT INTO sms_and_email_api
VALUES("T2lVanFDcUdoR0VqYm1Zd3pyVGY", "FINSUIT-GH", "princemcbond33@gmail.com", "1234", DEFAULT);

ALTER TABLE business_info 
ADD COLUMN account_password VARCHAR(255) NOT NULL AFTER email;

ALTER TABLE employees
CHANGE COLUMN contact_person_digiital_address contact_person_digital_address VARCHAR(50);




-- DROP TABLE sms_api;
