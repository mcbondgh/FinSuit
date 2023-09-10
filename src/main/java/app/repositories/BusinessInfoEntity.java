package app.repositories;

public class BusinessInfoEntity {
    private String name;
    String mobileNumber;
    String otherNumber;
    String email;
    String accountPassword;
    String digital;
    String location;
    String logo;

    public BusinessInfoEntity(String name, String mobileNumber, String otherNumber, String email, String accountPassword, String digital, String location, String logo) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.otherNumber = otherNumber;
        this.email = email;
        this.accountPassword = accountPassword;
        this.digital = digital;
        this.location = location;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtherNumber() {
        return otherNumber;
    }

    public void setOtherNumber(String otherNumber) {
        this.otherNumber = otherNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}//of of class...
