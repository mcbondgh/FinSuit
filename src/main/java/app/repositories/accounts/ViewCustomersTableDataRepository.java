package app.repositories.accounts;


import io.github.palexdev.materialfx.controls.MFXButton;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ViewCustomersTableDataRepository {
    private String fullname;
    private String gender;
    private int age;
    private String mobile_number;
    private String id_type;
    private String account_type;
    private String account_number;
    private Timestamp date_created;
    private String formattedDate;
    private MFXButton viewButton;

    public ViewCustomersTableDataRepository(String fullname, String gender, int age, String mobile_number, String id_type, String account_type, String account_number, Timestamp date_created) {
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.mobile_number = mobile_number;
        this.id_type = id_type;
        this.account_type = account_type;
        this.account_number = account_number;
        this.date_created = date_created;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        this.formattedDate = date_created.toLocalDateTime().format(formatter);
        this.viewButton = new MFXButton("🔎");
        viewButton.setStyle("-fx-font-size: 14px; -fx-text-fill: #278c8f; -fx-cursor:hand; -fx-padding: 3px; -fx-background-radius: 100px; -fx-alignment:center");
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }

    public MFXButton getViewButton() {
        return viewButton;
    }

    public void setViewButton(MFXButton viewButton) {
        this.viewButton = viewButton;
    }



}//end of class
