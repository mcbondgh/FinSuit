package app.repositories.accounts;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CustomersDataRepository {
    private long account_Id;
    private String firstname;
    private String lastname, othername, gender;
    private Date dob;
    private int age;
    private String place_of_birth;
    private String mobile_number, other_number, email;
    private String digital_address, residential_address;
    private String key_landmark, marital_status, name_of_spouse;
    private String id_type, id_number, educational_background;
    private String additional_comment, contact_person_fullname;
    private String contact_person_number;
    private Date contact_person_dob;
    private String contact_person_gender, contact_person_landmark;
    private String contact_person_education_level, contact_person_digital_address, contact_person_id_type;
    private String contact_person_id_number, contact_person_place_of_work, institution_address;
    private String institution_number, relationship_to_applicant;
    private Timestamp date_created;
    private int created_by;
    private Timestamp date_modified;
    private int modified_by;
    private String formatted_date_created;

    public CustomersDataRepository() {}

    public CustomersDataRepository(long account_Id, String firstname, String lastname, String othername, String gender, Date dob, int age, String place_of_birth, String mobile_number, String other_number, String email, String digital_address, String residential_address, String key_landmark, String marital_status, String name_of_spouse, String id_type, String id_number, String educational_background, String additional_comment, String contact_person_fullname, Date contact_person_dob, String contact_person_number, String contact_person_gender, String contact_person_landmark, String contact_person_education_level, String contact_person_digital_address, String contact_person_id_type, String contact_person_id_number, String contact_person_place_of_work, String institution_address, String institution_number, String relationship_to_applicant, Timestamp date_created, int created_by, Timestamp date_modified, int modified_by) {
        this.account_Id = account_Id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.othername = othername;
        this.gender = gender;
        this.dob = dob;
        this.age = age;
        this.place_of_birth = place_of_birth;
        this.mobile_number = mobile_number;
        this.other_number = other_number;
        this.email = email;
        this.digital_address = digital_address;
        this.residential_address = residential_address;
        this.key_landmark = key_landmark;
        this.marital_status = marital_status;
        this.name_of_spouse = name_of_spouse;
        this.id_type = id_type;
        this.id_number = id_number;
        this.educational_background = educational_background;
        this.additional_comment = additional_comment;
        this.contact_person_fullname = contact_person_fullname;
        this.contact_person_dob = contact_person_dob;
        this.contact_person_number = contact_person_number;
        this.contact_person_gender = contact_person_gender;
        this.contact_person_landmark = contact_person_landmark;
        this.contact_person_education_level = contact_person_education_level;
        this.contact_person_digital_address = contact_person_digital_address;
        this.contact_person_id_type = contact_person_id_type;
        this.contact_person_id_number = contact_person_id_number;
        this.contact_person_place_of_work = contact_person_place_of_work;
        this.institution_address = institution_address;
        this.institution_number = institution_number;
        this.relationship_to_applicant = relationship_to_applicant;
        this.date_created = date_created;
        this.created_by = created_by;
        this.date_modified = date_modified;
        this.modified_by = modified_by;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        this.formatted_date_created = date_created.toLocalDateTime().format(formatter);
    }

    public long getAccount_Id() {
        return account_Id;
    }

    public void setAccount_Id(long account_Id) {
        this.account_Id = account_Id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getKey_landmark() {
        return key_landmark;
    }

    public void setKey_landmark(String key_landmark) {
        this.key_landmark = key_landmark;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getName_of_spouse() {
        return name_of_spouse;
    }

    public void setName_of_spouse(String name_of_spouse) {
        this.name_of_spouse = name_of_spouse;
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

    public String getEducational_background() {
        return educational_background;
    }

    public void setEducational_background(String educational_background) {
        this.educational_background = educational_background;
    }

    public String getAdditional_comment() {
        return additional_comment;
    }

    public void setAdditional_comment(String additional_comment) {
        this.additional_comment = additional_comment;
    }

    public String getContact_person_fullname() {
        return contact_person_fullname;
    }

    public void setContact_person_fullname(String contact_person_fullname) {
        this.contact_person_fullname = contact_person_fullname;
    }

    public Date getContact_person_dob() {
        return contact_person_dob;
    }

    public void setContact_person_dob(Date contact_person_dob) {
        this.contact_person_dob = contact_person_dob;
    }

    public String getContact_person_number() {
        return contact_person_number;
    }

    public void setContact_person_number(String contact_person_number) {
        this.contact_person_number = contact_person_number;
    }

    public String getContact_person_gender() {
        return contact_person_gender;
    }

    public void setContact_person_gender(String contact_person_gender) {
        this.contact_person_gender = contact_person_gender;
    }

    public String getContact_person_landmark() {
        return contact_person_landmark;
    }

    public void setContact_person_landmark(String contact_person_landmark) {
        this.contact_person_landmark = contact_person_landmark;
    }

    public String getContact_person_education_level() {
        return contact_person_education_level;
    }

    public void setContact_person_education_level(String contact_person_education_level) {
        this.contact_person_education_level = contact_person_education_level;
    }

    public String getContact_person_digital_address() {
        return contact_person_digital_address;
    }

    public void setContact_person_digital_address(String contact_person_digital_address) {
        this.contact_person_digital_address = contact_person_digital_address;
    }

    public String getContact_person_id_type() {
        return contact_person_id_type;
    }

    public void setContact_person_id_type(String contact_person_id_type) {
        this.contact_person_id_type = contact_person_id_type;
    }

    public String getContact_person_id_number() {
        return contact_person_id_number;
    }

    public void setContact_person_id_number(String contact_person_id_number) {
        this.contact_person_id_number = contact_person_id_number;
    }

    public String getContact_person_place_of_work() {
        return contact_person_place_of_work;
    }

    public void setContact_person_place_of_work(String contact_person_place_of_work) {
        this.contact_person_place_of_work = contact_person_place_of_work;
    }

    public String getInstitution_address() {
        return institution_address;
    }

    public void setInstitution_address(String institution_address) {
        this.institution_address = institution_address;
    }

    public String getInstitution_number() {
        return institution_number;
    }

    public void setInstitution_number(String institution_number) {
        this.institution_number = institution_number;
    }

    public String getRelationship_to_applicant() {
        return relationship_to_applicant;
    }

    public void setRelationship_to_applicant(String relationship_to_applicant) {
        this.relationship_to_applicant = relationship_to_applicant;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public String getFormatted_date_created() {
        return formatted_date_created;
    }

    public void setFormatted_date_created(String formatted_date_created) {
        this.formatted_date_created = formatted_date_created;
    }
}
