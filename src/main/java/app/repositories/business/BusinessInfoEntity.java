package app.repositories.business;

import java.sql.Timestamp;

public class BusinessInfoEntity {
    private String name;
    String mobileNumber;
    String otherNumber;
    String email;
    String accountPassword;
    String digital;
    double accountBalance, previousBalance;
    Timestamp account_modified_date;

    double loanPercentage, taxPercentage;
    String location;
    private byte[] logo;

    public BusinessInfoEntity() {}

    public BusinessInfoEntity(String name, String mobileNumber, String otherNumber, String email, String accountPassword, String digital, String location, byte[] logo, double loanPercentage, double texPercentage) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.otherNumber = otherNumber;
        this.email = email;
        this.accountPassword = accountPassword;
        this.digital = digital;
        this.location = location;
        this.logo = logo;
        this.loanPercentage = loanPercentage;
        this.taxPercentage = texPercentage;
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

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public Timestamp getAccount_modified_date() {
        return account_modified_date;
    }

    public void setAccount_modified_date(Timestamp account_modified_date) {
        this.account_modified_date = account_modified_date;
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

    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
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

    public double getLoanPercentage() {
        return loanPercentage;
    }

    public void setLoanPercentage(double loanPercentage) {
        this.loanPercentage = loanPercentage;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo.getBytes();
    }
}//end of class...
