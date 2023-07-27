CREATE TABLE IF NOT EXISTS business_info(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    business_name VARCHAR(100) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL, 
    other_number VARCHAR(20),
    email VARCHAR(100) NOT NULL,
    digital_address VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    date_modified DATETIME DEFAULT CURRENT_TIMESTAMP
);



