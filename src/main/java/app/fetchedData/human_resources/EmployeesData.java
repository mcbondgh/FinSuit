package app.fetchedData.human_resources;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EmployeesData {

    private int emp_id;
    private String work_id;
    private String full_name;
    private String firstname;
    private String lastname;
    private String othername, email, mobile_number, other_number, gender;
    private LocalDate dbo;
    private String digital_address;
    private String residential_address, landmark, id_type, id_number, marital_status, qualification, designation, working_experience;
    LocalDate employment_date;
    private String contact_person_name, contact_person_number, contact_person_digital_address, contact_person_address, contact_person_landmark;
    private String contact_person_place_of_work, contact_person_org_number, contact_person_org_address, additional_information;
    private LocalDateTime date_added, date_modified;
    int added_by, modified_by, is_active, is_deleted;
    double salary;
    private String bank_name, account_name, account_number;
    private Label statusLabel = new Label();
    private JFXCheckBox actionCheckBox = new JFXCheckBox();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    private String formattedBirthdate;
    private String formattedEmploymentDate;

    public EmployeesData() {}
    public EmployeesData(int emp_id, String work_id, String firstname, String lastname, String othername, String email, String mobile_number, String other_number, String gender, LocalDate dbo, String digital_address, String residential_address, String landmark, String id_type, String id_number, String marital_status, String qualification, String designation, String working_experience,
                         LocalDate employment_date, String contact_person_name, String contact_person_number, String contact_person_digital_address, String contact_person_address, String contact_person_landmark, String contact_person_place_of_work, String contact_person_org_number, String contact_person_org_address, String additional_information, LocalDateTime date_added,
                         LocalDateTime date_modified, byte is_active, byte is_deleted, int added_by, int modified_by, double salary, String bank_name, String account_name, String account_number) {
        this.emp_id = emp_id;
        this.work_id = work_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.othername = othername;
        this.email = email;
        this.mobile_number = mobile_number;
        this.other_number = other_number;
        this.gender = gender;
        this.dbo = dbo;
        this.digital_address = digital_address;
        this.residential_address = residential_address;
        this.landmark = landmark;
        this.id_type = id_type;
        this.id_number = id_number;
        this.marital_status = marital_status;
        this.qualification = qualification;
        this.designation = designation;
        this.working_experience = working_experience;
        this.employment_date = employment_date;
        this.contact_person_name = contact_person_name;
        this.contact_person_number = contact_person_number;
        this.contact_person_digital_address = contact_person_digital_address;
        this.contact_person_address = contact_person_address;
        this.contact_person_landmark = contact_person_landmark;
        this.contact_person_place_of_work = contact_person_place_of_work;
        this.contact_person_org_number = contact_person_org_number;
        this.contact_person_org_address = contact_person_org_address;
        this.additional_information = additional_information;
        this.is_active = is_active;
        this.is_deleted = is_deleted;
        this.date_added = date_added;
        this.date_modified = date_modified;
        this.added_by = added_by;
        this.modified_by = modified_by;
        this.salary = salary;
        this.bank_name = bank_name;
        this.account_name = account_name;
        this.account_number = account_number;
        this.formattedBirthdate = dateTimeFormatter.format(date_added);
        this.formattedEmploymentDate = formatDate.format(employment_date);
    }

    public EmployeesData(String work_id, String full_name, String gender, String mobile_number, LocalDate employment_date, String designation, double salary, int is_active) {
        this.work_id = work_id;
        this.full_name = full_name;
        this.gender = gender;
        this.mobile_number = mobile_number;
        this.designation = designation;
        this.salary = salary;
        switch (is_active) {
            case 1 -> {
                actionCheckBox.setSelected(true);
                statusLabel.setText("active");
                statusLabel.setStyle("-fx-background-color:#36d800; -fx-font-family:poppins; -fx-padding: 3px 14px; -fx-text-fill: #fff; -fx-background-radius: 30px");
            }
            case  0 -> {
                actionCheckBox.setSelected(false);
                statusLabel.setText("inactive");
                statusLabel.setStyle("-fx-background-color: #ffdbdb; -fx-font-family:poppins; -fx-padding: 3px 14px; -fx-text-fill: #ff0000; -fx-background-radius: 30px");
            }
        }
        this.formattedEmploymentDate = formatDate.format(employment_date);
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public JFXCheckBox getActionCheckBox() {
        return actionCheckBox;
    }

    public void setActionCheckBox(JFXCheckBox actionCheckBox) {
        this.actionCheckBox = actionCheckBox;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getEmail() {
        return email;
    }

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getOther_number() {
        return other_number;
    }

    public void setOther_number(String other_number) {
        this.other_number = other_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDbo() {
        return dbo;
    }

    public void setDbo(LocalDate dbo) {
        this.dbo = dbo;
    }

    public String getDigital_address() {
        return digital_address;
    }

    public void setDigital_address(String digital_address) {
        this.digital_address = digital_address;
    }

    public String getResidential_address() {
        return residential_address;
    }

    public void setResidential_address(String residential_address) {
        this.residential_address = residential_address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getId_type() {
        return id_type;
    }

    public String getFormattedBirthdate() {
        return formattedBirthdate;
    }

    public void setFormattedBirthdate(String formattedBirthdate) {
        this.formattedBirthdate = formattedBirthdate;
    }

    public String getFormattedEmploymentDate() {
        return formattedEmploymentDate;
    }

    public void setFormattedEmploymentDate(String formattedEmploymentDate) {
        this.formattedEmploymentDate = formattedEmploymentDate;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorking_experience() {
        return working_experience;
    }

    public void setWorking_experience(String working_experience) {
        this.working_experience = working_experience;
    }

    public LocalDate getEmployment_date() {
        return employment_date;
    }

    public void setEmployment_date(LocalDate employment_date) {
        this.employment_date = employment_date;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getContact_person_number() {
        return contact_person_number;
    }

    public void setContact_person_number(String contact_person_number) {
        this.contact_person_number = contact_person_number;
    }

    public String getContact_person_digital_address() {
        return contact_person_digital_address;
    }

    public void setContact_person_digital_address(String contact_person_digital_address) {
        this.contact_person_digital_address = contact_person_digital_address;
    }

    public String getContact_person_address() {
        return contact_person_address;
    }

    public void setContact_person_address(String contact_person_address) {
        this.contact_person_address = contact_person_address;
    }

    public String getContact_person_landmark() {
        return contact_person_landmark;
    }

    public void setContact_person_landmark(String contact_person_landmark) {
        this.contact_person_landmark = contact_person_landmark;
    }

    public String getContact_person_place_of_work() {
        return contact_person_place_of_work;
    }

    public void setContact_person_place_of_work(String contact_person_place_of_work) {
        this.contact_person_place_of_work = contact_person_place_of_work;
    }

    public String getContact_person_org_number() {
        return contact_person_org_number;
    }

    public void setContact_person_org_number(String contact_person_org_number) {
        this.contact_person_org_number = contact_person_org_number;
    }

    public String getContact_person_org_address() {
        return contact_person_org_address;
    }

    public void setContact_person_org_address(String contact_person_org_address) {
        this.contact_person_org_address = contact_person_org_address;
    }

    public String getAdditional_information() {
        return additional_information;
    }

    public void setAdditional_information(String additional_information) {
        this.additional_information = additional_information;
    }

    public LocalDateTime getDate_added() {
        return date_added;
    }
    public void setDate_added(LocalDateTime date_added) {
        this.date_added = date_added;
    }
    public LocalDateTime getDate_modified() {
        return date_modified;
    }
    public void setDate_modified(LocalDateTime date_modified) {
        this.date_modified = date_modified;
    }
    public int getAdded_by() {
        return added_by;
    }
    public void setAdded_by(int added_by) {
        this.added_by = added_by;
    }
    public int getModified_by() {
        return modified_by;
    }
    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public String getBank_name() {
        return bank_name;
    }
    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
    public String getAccount_name() {
        return account_name;
    }
    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
    public String getAccount_number() {
        return account_number;
    }
    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

}// end of class...
