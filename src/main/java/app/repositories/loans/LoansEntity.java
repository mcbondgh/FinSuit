package app.repositories.loans;

import java.sql.Timestamp;

public class LoansEntity {
    private int loan_id, customer_id;
    private String customerName;
    private String loan_no, loan_type;
    private double requested_amount, disbursed_amount, total_payment;
    private String application_status, loan_purpose, loan_status;
    private byte is_drafted;
    private Timestamp date_created, date_modified;
   private int created_by, updated_by, approved_by;
   public int getLoan_id() {
        return loan_id;
    }

    public LoansEntity(){}

    public LoansEntity(int loan_id, String customerName, String loan_no, String loan_type, double requested_amount, double disbursed_amount, double total_payment, String application_status, String loan_purpose, String loan_status, byte is_drafted, Timestamp date_created, Timestamp date_modified, int created_by, int updated_by, int approved_by) {
        this.loan_id = loan_id;
        this.customerName = customerName;
        this.loan_no = loan_no;
        this.loan_type = loan_type;
        this.requested_amount = requested_amount;
        this.disbursed_amount = disbursed_amount;
        this.total_payment = total_payment;
        this.application_status = application_status;
        this.loan_purpose = loan_purpose;
        this.loan_status = loan_status;
        this.is_drafted = is_drafted;
        this.date_created = date_created;
        this.date_modified = date_modified;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.approved_by = approved_by;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getLoan_no() {
        return loan_no;
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

    public double getDisbursed_amount() {
        return disbursed_amount;
    }

    public void setDisbursed_amount(double disbursed_amount) {
        this.disbursed_amount = disbursed_amount;
    }

    public double getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(double total_payment) {
        this.total_payment = total_payment;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    public String getLoan_purpose() {
        return loan_purpose;
    }

    public void setLoan_purpose(String loan_purpose) {
        this.loan_purpose = loan_purpose;
    }

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
}
