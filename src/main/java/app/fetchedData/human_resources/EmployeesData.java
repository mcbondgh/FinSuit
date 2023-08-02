package app.fetchedData.human_resources;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Label;

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
    private LocalDate date_added, date_modified;
    int added_by, modified_by;
    double salary;
    private String bank_name, account_name, account_number;
    private Label statusLabel;
    private JFXCheckBox actionCheckBox;

    private DateTimeFormatter formattedBirthdate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    private DateTimeFormatter formattedEmploymentDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    public EmployeesData() {}
    public EmployeesData(int emp_id, String work_id, String firstname, String lastname, String othername, String email, String mobile_number, String other_number, String gender, LocalDate dbo, String digital_address, String residential_address, String landmark, String id_type, String id_number, String marital_status, String qualification, String designation, String working_experience,
                         LocalDate employment_date, String contact_person_name, String contact_person_number, String contact_person_digital_address, String contact_person_address, String contact_person_landmark, String contact_person_place_of_work, String contact_person_org_number, String contact_person_org_address, String additional_information, LocalDate date_added,
                         LocalDate date_modified, int added_by, int modified_by, double salary, String bank_name, String account_name, String account_number) {
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
        this.date_added = date_added;
        this.date_modified = date_modified;
        this.added_by = added_by;
        this.modified_by = modified_by;
        this.salary = salary;
        this.bank_name = bank_name;
        this.account_name = account_name;
        this.account_number = account_number;
        this.formattedBirthdate.format(date_added);
        this.formattedEmploymentDate.format(employment_date);
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
                statusLabel.setStyle("-fx-background-color: ");
            }
            case  0 -> {
                actionCheckBox.setSelected(false);
                statusLabel.setText("inactive");
            }
        }


        this.formattedEmploymentDate.format(employment_date);
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

    public DateTimeFormatter getFormattedBirthdate() {
        return formattedBirthdate;
    }

    public void setFormattedBirthdate(DateTimeFormatter formattedBirthdate) {
        this.formattedBirthdate = formattedBirthdate;
    }

    public DateTimeFormatter getFormattedEmploymentDate() {
        return formattedEmploymentDate;
    }

    public void setFormattedEmploymentDate(DateTimeFormatter formattedEmploymentDate) {
        this.formattedEmploymentDate = formattedEmploymentDate;
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

    public LocalDate getDate_added() {
        return date_added;
    }
    public void setDate_added(LocalDate date_added) {
        this.date_added = date_added;
    }
    public LocalDate getDate_modified() {
        return date_modified;
    }
    public void setDate_modified(LocalDate date_modified) {
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
