package app.repositories.loans;

import java.sql.Timestamp;
import java.time.LocalDate;

public class LoanApplicationEntity {
    private long loan_id;
    private long customer_id;
    private String loan_no;
    private String loan_type;
    private double requested_amount;
    private byte application_status, is_drafted;
    Timestamp date_created, date_modified;
    private int created_by, updated_by, approved_by;
    private String company_name, company_mobile_number, company_address, staff_id, occupation;
    private LocalDate employment_date;
    private double basic_salary, gross_salary, total_deduction, net_salary;
    private  String gender;
    private String profile_picture;
    private String guranter_name, guranter_number, guranter_digital_address, guranter_residential_address;
    private String guranter_idType, guranter_idNumber, guranter_relationship, guranter_occupation;
    private String gurater_place_of_work, guranter_institution_address;
    private double guranter_income;

    private String loanPurpose;

    public LoanApplicationEntity(){}

    public LoanApplicationEntity(long loan_id, long customer_id, String loan_no, String loan_type, double requested_amount, byte application_status, String loanPurpose, byte is_drafted, Timestamp date_created, Timestamp date_modified, int created_by, int updated_by, int approved_by, String company_name, String company_mobile_number, String company_address, String staff_id, String occupation, LocalDate employment_date, double basic_salary, double gross_salary, double total_deduction, double net_salary, String guranter_name, String guranter_number, String gender, String guranter_digital_address, String guranter_residential_address, String guranter_idType, String guranter_idNumber, String guranter_relationship, String guranter_occupation, String gurater_place_of_work, String guranter_institution_address, double guranter_income) {
        this.loan_id = loan_id;
        this.customer_id = customer_id;
        this.loan_no = loan_no;
        this.loan_type = loan_type;
        this.requested_amount = requested_amount;
        this.application_status = application_status;
        this.loanPurpose = loanPurpose;
        this.is_drafted = is_drafted;
        this.date_created = date_created;
        this.date_modified = date_modified;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.approved_by = approved_by;
        this.company_name = company_name;
        this.company_mobile_number = company_mobile_number;
        this.company_address = company_address;
        this.staff_id = staff_id;
        this.occupation = occupation;
        this.employment_date = employment_date;
        this.basic_salary = basic_salary;
        this.gross_salary = gross_salary;
        this.total_deduction = total_deduction;
        this.net_salary = net_salary;
        this.guranter_name = guranter_name;
        this.guranter_number = guranter_number;
        this.gender = gender;
        this.guranter_digital_address = guranter_digital_address;
        this.guranter_residential_address = guranter_residential_address;
        this.guranter_idType = guranter_idType;
        this.guranter_idNumber = guranter_idNumber;
        this.guranter_relationship = guranter_relationship;
        this.guranter_occupation = guranter_occupation;
        this.gurater_place_of_work = gurater_place_of_work;
        this.guranter_institution_address = guranter_institution_address;
        this.guranter_income = guranter_income;
    }

    public long getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(long loan_id) {
        this.loan_id = loan_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getGuranter_residential_address() {
        return guranter_residential_address;
    }

    public void setGuranter_residential_address(String guranter_residential_address) {
        this.guranter_residential_address = guranter_residential_address;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public String getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(String loan_type) {
        this.loan_type = loan_type;
    }

    public double getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(double requested_amount) {
        this.requested_amount = requested_amount;
    }

    public byte getApplication_status() {
        return application_status;
    }

    public void setApplication_status(byte application_status) {
        this.application_status = application_status;
    }

    public byte getIs_drafted() {
        return is_drafted;
    }

    public void setIs_drafted(byte is_drafted) {
        this.is_drafted = is_drafted;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(int updated_by) {
        this.updated_by = updated_by;
    }

    public int getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(int approved_by) {
        this.approved_by = approved_by;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_mobile_number() {
        return company_mobile_number;
    }

    public void setCompany_mobile_number(String company_mobile_number) {
        this.company_mobile_number = company_mobile_number;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public LocalDate getEmployment_date() {
        return employment_date;
    }

    public void setEmployment_date(LocalDate employment_date) {
        this.employment_date = employment_date;
    }

    public double getBasic_salary() {
        return basic_salary;
    }

    public void setBasic_salary(double basic_salary) {
        this.basic_salary = basic_salary;
    }

    public double getGross_salary() {
        return gross_salary;
    }

    public void setGross_salary(double gross_salary) {
        this.gross_salary = gross_salary;
    }

    public double getTotal_deduction() {
        return total_deduction;
    }

    public void setTotal_deduction(double total_deduction) {
        this.total_deduction = total_deduction;
    }

    public double getNet_salary() {
        return net_salary;
    }

    public void setNet_salary(double net_salary) {
        this.net_salary = net_salary;
    }


    public String getGuranter_name() {
        return guranter_name;
    }

    public void setGuranter_name(String guranter_name) {
        this.guranter_name = guranter_name;
    }

    public String getGuranter_number() {
        return guranter_number;
    }

    public void setGuranter_number(String guranter_number) {
        this.guranter_number = guranter_number;
    }

    public String getGuranter_digital_address() {
        return guranter_digital_address;
    }

    public void setGuranter_digital_address(String guranter_digital_address) {
        this.guranter_digital_address = guranter_digital_address;
    }

    public String getGuarantor_landmark() {
        return guranter_residential_address;
    }

    public void setGuarantor_landmark(String guranter_residential_address) {
        this.guranter_residential_address = guranter_residential_address;
    }

    public String getGuranter_idType() {
        return guranter_idType;
    }

    public void setGuranter_idType(String guranter_idType) {
        this.guranter_idType = guranter_idType;
    }

    public String getGuranter_idNumber() {
        return guranter_idNumber;
    }

    public void setGuranter_idNumber(String guranter_idNumber) {
        this.guranter_idNumber = guranter_idNumber;
    }

    public String getGuranter_relationship() {
        return guranter_relationship;
    }

    public void setGuranter_relationship(String guranter_relationship) {
        this.guranter_relationship = guranter_relationship;
    }

    public String getGuranter_occupation() {
        return guranter_occupation;
    }

    public void setGuranter_occupation(String guranter_occupation) {
        this.guranter_occupation = guranter_occupation;
    }

    public String getGurater_place_of_work() {
        return gurater_place_of_work;
    }

    public void setGurater_place_of_work(String gurater_place_of_work) {
        this.gurater_place_of_work = gurater_place_of_work;
    }

    public String getGuranter_institution_address() {
        return guranter_institution_address;
    }

    public void setGuranter_institution_address(String guranter_institution_address) {
        this.guranter_institution_address = guranter_institution_address;
    }

    public double getGuranter_income() {
        return guranter_income;
    }

    public void setGuranter_income(double guranter_income) {
        this.guranter_income = guranter_income;
    }
}//end of class...
