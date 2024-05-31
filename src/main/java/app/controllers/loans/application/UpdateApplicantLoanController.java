package app.controllers.loans.application;

import app.alerts.UserAlerts;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.documents.ImageReadWriter;
import app.enums.MessageStatus;
import app.errorLogger.ErrorLogger;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.LoanApplicationEntity;
import app.repositories.loans.LoansEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class UpdateApplicantLoanController extends LoansModel implements Initializable {

    public UpdateApplicantLoanController() {}

    @FXML
    private Label loanNumberLabel;
    @FXML
    private ComboBox<String> loanTypeSelector, genderSelector, agentSelector;
    @FXML private TextField loanRequestField;
    @FXML private DatePicker dobPicker, employmentDatePicker;
    @FXML private TextField firstNameField, lastNameField, otherNameField, mobileNumberField;
    @FXML private TextField otherNumberField, emailField, digitalAddressField, residentialAddressField;
    @FXML private  TextField landmarkField, idNumberField, contactPersonIdNoField, placeOfWorkField, institutionAddressField;
    @FXML private ComboBox<String> qualificationSelector, maritalStatusSelector, idTypeSelector;
    @FXML private TextField companyNameField, companyAddressField, companyNumberField, staffIdField, occupationField;
    @FXML private TextField applicantBasicSalaryField, applicantGrossSalaryField, applicantTotalDeductionField, applicantNetSalaryField;
    @FXML private TextField contactPersonNameField, contactPersonNumberField, contactPersonDigitalAddField, contactPersonResidentialField;
    @FXML private ComboBox<String> contactPersonGenderSelector, relationshipTypeSelector, contactPersonIdTypeSelector;
    @FXML private TextField guranterNameField, guranterNumberField, guranterDigitalAddressField, guranterLandmarkField;
    @FXML private ComboBox<String> guranterGenderSelector, guranterIdTypeSelector, guranterRelationshipTypeSelector;
    @FXML private TextField guranterIdNumberField, guranterOccupationField, guranterPlaceOfWorkField, guranterWorkAddressField, guranterNetSalaryField;
    @FXML private TextArea loanPurposeField;
    @FXML private ImageView imageView;
    @FXML private MFXButton uploadImageButton, saveButton;
    @FXML private ScrollPane scrollPane;

    public static String setLoanNumber;
    private final AtomicReference<Long> CUSTOMER_ID = new AtomicReference<>();
    private static final AtomicInteger AGENT_ID = new AtomicInteger(0);
    UserAlerts ALERT;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanNumberLabel.setText(setLoanNumber);
        SpecialMethods.setLoanType(loanTypeSelector);
        SpecialMethods.setQualification(qualificationSelector);
        SpecialMethods.setGenderParameters(genderSelector);
        SpecialMethods.setMaritalStatus(maritalStatusSelector);
        SpecialMethods.setIdTypeParameters(idTypeSelector);
        SpecialMethods.setRelationshipTypes(relationshipTypeSelector);
        SpecialMethods.setGenderParameters(guranterGenderSelector);
        SpecialMethods.setIdTypeParameters(guranterIdTypeSelector);
        SpecialMethods.setRelationshipTypes(guranterRelationshipTypeSelector);
        SpecialMethods.setGenderParameters(contactPersonGenderSelector);
        SpecialMethods.setIdTypeParameters(contactPersonIdTypeSelector);
        getAllAgents().forEach(item -> {
            agentSelector.getItems().add(item.getAgentName());
        });
        validateInputFields();
        loadFields();
    }

    /*******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    boolean isFirstNameEmpty() {return firstNameField.getText().isBlank();}
    boolean isLastNameEmpty() {return lastNameField.getText().isEmpty();}

    boolean isLoanAmountFieldEmpty() {return loanRequestField.getText().isEmpty();}
    boolean isLoanTypeEmpty() {
        return loanTypeSelector.getValue() == null;
    }
    boolean isDobEmpty() {return dobPicker.getValue() == null;}
    boolean isGenderEmpty() {return genderSelector.getValue() == null;}
    boolean isMobileNumberEmpty() {return mobileNumberField.getText().isEmpty();}
    boolean isDigitalAddressEmpty() {return digitalAddressField.getText().isEmpty();}
    boolean isResidentialAddressEmpty() {return residentialAddressField.getText().isEmpty();}
    boolean isLandmarkEmpty() {return landmarkField.getText().isEmpty();}
    boolean isMaritalStatusEmpty() {return maritalStatusSelector.getValue() == null;}
    boolean isIdTypeEmpty() {return idTypeSelector.getValue() == null;}
    boolean isIdNumberEmpty() {return idNumberField.getText().isEmpty();}
    boolean isCompanyNameEmpty() {return companyNameField.getText().isEmpty();}
    boolean isContactGenderEmpty() {
        return contactPersonGenderSelector.getValue() == null;
    }
    boolean isCompanyNumberEmpty() {return companyNumberField.getText().isEmpty();}
    boolean isCompanyAddressEmpty() {return companyAddressField.getText().isEmpty();}
    boolean isStaffIdEmpty() {return staffIdField.getText().isEmpty();}
    boolean isOccupationEmpty() {return occupationField.getText().isEmpty();}
    boolean isEmploymentDateEmpty() {return employmentDatePicker.getValue() == null;}
    boolean isGuarantorGenderEmpty() {return guranterGenderSelector.getValue() == null;}
    boolean isContactPersonNameEmpty() {return contactPersonNameField.getText().isEmpty();}
    boolean isContactPersonNumberEmpty() {return contactPersonNumberField.getText().isEmpty();}
    boolean isContactPersonAddressEmpty() {return contactPersonDigitalAddField.getText().isEmpty();}
    boolean isContactPersonResidentialAddressEmpty() {return contactPersonResidentialField.getText().isEmpty();}
    boolean isContactPersonRelationshipEmpty() {return relationshipTypeSelector.getValue() == null;}
    boolean isContactIdTypeEmpty() {return contactPersonIdTypeSelector.getValue() == null;}
    boolean isContactIdNumberEmpty() {return contactPersonIdNoField.getText().isEmpty();}
    boolean isContactPlaceOfWorkEmpty() {return placeOfWorkField.getText().isEmpty();}
    boolean isContactInstitutionAddressEmpty() {return institutionAddressField.getText().isEmpty();}
    boolean isGuarantorNameEmpty() {return guranterNameField.getText().isEmpty();}
    boolean isGuarantorNumberEmpty() {return guranterNumberField.getText().isEmpty();}
    boolean isGuarantorDigitalAddressEmpty() {return guranterDigitalAddressField.getText().isEmpty();};
    boolean isGuarantorAddressEmpty() {return guranterLandmarkField.getText().isEmpty();}
    boolean isGuarantorIdTypeEmpty() {return guranterIdTypeSelector.getValue() == null;}
    boolean isGuarantorIdNumberEmpty() {return guranterIdNumberField.getText().isEmpty();}
    boolean isGuarantorPlaceOfWorkEmpty() {return guranterPlaceOfWorkField.getText().isEmpty();}
    boolean isGuarantorRelationshipEmpty() {return guranterRelationshipTypeSelector.getValue() == null;}
    boolean isGuarantorOccupationEmpty() {return guranterOccupationField.getText().isEmpty();}
    boolean isGuarantorInstitutionAddressEmpty() {return guranterWorkAddressField.getText().isEmpty();}
    boolean isGuarantorSalaryEmpty() {return guranterNetSalaryField.getText().isEmpty();}
    boolean isBasicSalaryEmpty() {return applicantBasicSalaryField.getText().isEmpty();}
    boolean isGrossSalaryEmpty() {return applicantGrossSalaryField.getText().isEmpty();}
    boolean isNetSalaryEmpty() {return applicantNetSalaryField.getText().isEmpty();}
    boolean isTotalDeductionEmpty() {return applicantTotalDeductionField.getText().isEmpty();}
    boolean isPurposeFieldEmpty() {return loanPurposeField.getText().isEmpty();}

    /*******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    private void loadFields() {
    Map<String, Object> data = getLoanApplicantDataByLoanNumber(loanNumberLabel.getText());
        CUSTOMER_ID.set(Long.parseLong(data.get("customer_id").toString()));
        loanTypeSelector.setValue(data.get("loanType").toString());
        loanRequestField.setText(data.get("amount").toString());
        loanPurposeField.setText(data.get("loanPurpose").toString());
        firstNameField.setText(data.get("firstname").toString());
        lastNameField.setText(data.get("lastname").toString());
        otherNameField.setText(data.get("otherName").toString());
        byte[] imageByte = (byte[]) data.get("image");
        imageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        agentSelector.setValue(data.get("agentName").toString());
        genderSelector.setValue(data.get("gender").toString());
        dobPicker.setValue(LocalDate.parse(data.get("dob").toString()));
        mobileNumberField.setText(data.getOrDefault("mobile_number", "null").toString());
        otherNumberField.setText(data.getOrDefault("other_number", "null").toString());
        emailField.setText(data.getOrDefault("email", "null").toString());
        digitalAddressField.setText(data.getOrDefault("digital_address", "null").toString());
        residentialAddressField.setText(data.getOrDefault("residential_address", "null").toString());
        landmarkField.setText(data.getOrDefault("key_landmark", "null").toString());
        qualificationSelector.setValue(data.getOrDefault("educational_background", "null").toString());
        idNumberField.setText(data.getOrDefault("id_number", "null").toString());
        idTypeSelector.setValue(data.getOrDefault("id_type", "null").toString());
        maritalStatusSelector.setValue(data.getOrDefault("marital_status", "null").toString());
        contactPersonNameField.setText(data.getOrDefault("contactPersonName", "null").toString());
        companyNameField.setText(data.getOrDefault("company_name", "null").toString());
        companyNumberField.setText(data.getOrDefault("company_number", "null").toString());
        companyAddressField.setText(data.getOrDefault("company_address", "null").toString());
        staffIdField.setText(data.getOrDefault("staff_id", "null").toString());
        occupationField.setText(data.getOrDefault("occupation", "null").toString());
        employmentDatePicker.setValue(LocalDate.parse(data.get("employment_date").toString()));
        applicantBasicSalaryField.setText(data.get("basic_salary").toString());
        applicantGrossSalaryField.setText(data.get("gross_salary").toString());
        applicantNetSalaryField.setText(data.get("net_salary").toString());
        applicantTotalDeductionField.setText(data.get("total_deduction").toString());
        guranterNameField.setText(data.getOrDefault("guarantor_name", "Null").toString());
        guranterNumberField.setText(data.getOrDefault("guarantor_number", "Null").toString());
        guranterGenderSelector.setValue(data.getOrDefault("guarantor_gender", "Null").toString());
        guranterDigitalAddressField.setText(data.getOrDefault("guarantor_address", "Null").toString());
        guranterLandmarkField.setText(data.getOrDefault("guarantor_landmark", "Null").toString());
        guranterIdTypeSelector.setValue(data.getOrDefault("guarantor_idType", "Null").toString());
        guranterIdNumberField.setText(data.getOrDefault("guarantor_idNumber", "Null").toString());
        guranterRelationshipTypeSelector.setValue(data.getOrDefault("relationship", "Null").toString());
        guranterOccupationField.setText(data.getOrDefault("occupation", "Null").toString());
        guranterNetSalaryField.setText(data.getOrDefault("income", "Null").toString());
        guranterWorkAddressField.setText(data.getOrDefault("institution_address", "Null").toString());
        guranterPlaceOfWorkField.setText(data.getOrDefault("place_of_work", "Null").toString());
        contactPersonNumberField.setText(data.getOrDefault("contactNumber", "Null").toString());
        contactPersonDigitalAddField.setText(data.getOrDefault("contact_digital_address", "Null").toString());
        contactPersonResidentialField.setText(data.getOrDefault("contactLandmark", "Null").toString());
        contactPersonGenderSelector.setValue(data.getOrDefault("contactGender", "Null").toString());
        relationshipTypeSelector.setValue(data.getOrDefault("relationship_to_applicant", "Null").toString());
        contactPersonIdTypeSelector.setValue(data.getOrDefault("contactIdType", "Null").toString());
        contactPersonIdNoField.setText(data.getOrDefault("contactIdNumber", "Null").toString());
        placeOfWorkField.setText(data.getOrDefault("contactWorkplace", "Null").toString());
        institutionAddressField.setText(data.getOrDefault("contactWorkplace", "Null").toString());
        retrieveAgentId();
    }

    void retrieveAgentId() {
        var var1 = agentSelector.getValue();
        getAllAgents().forEach(x -> {if(Objects.equals(var1, x.getAgentName())) {AGENT_ID.set(x.getAgentId());}});
    }

    //load and set applicant image if and only if applicant a new image was not selected
    byte[] loadApplicantPhoto() {
        Map<String, Object> data = getLoanApplicantDataByLoanNumber(loanNumberLabel.getText());
        return (byte[]) data.get("image");
    }

    /*******************************************************************************************************************
        IMPLEMENTATION OF KEY EVENT METHODS.
     *******************************************************************************************************************/

    void validateInputFields() {
        loanRequestField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                loanRequestField.deletePreviousChar();
            }
        });
        mobileNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                mobileNumberField.deletePreviousChar();
            }
        });
        otherNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                otherNumberField.deletePreviousChar();
            }
        });
        applicantBasicSalaryField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                applicantBasicSalaryField.deletePreviousChar();
            }
        });
        applicantGrossSalaryField.setOnKeyTyped(keyEvent -> {
            if(!keyEvent.getCharacter().matches("[0-9.]")) {
                applicantGrossSalaryField.deletePreviousChar();
            }
        });
        applicantNetSalaryField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                applicantNetSalaryField.deletePreviousChar();
            }
        });
        applicantTotalDeductionField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                applicantTotalDeductionField.deletePreviousChar();
            }
        });
        contactPersonNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                contactPersonNumberField.deletePreviousChar();
            }
            if (contactPersonNumberField.getText().length() >11) {
                contactPersonNumberField.deletePreviousChar();
            }
        });
        guranterNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                guranterNumberField.deletePreviousChar();
            }
        });
        guranterNetSalaryField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                guranterNetSalaryField.deletePreviousChar();
            }
        });
        companyNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                companyNumberField.deletePreviousChar();
            }
        });

        scrollPane.setOnMouseMoved(mouseEvent -> {
            saveButton.setDisable(
                    isLoanTypeEmpty() || isLoanAmountFieldEmpty() || isFirstNameEmpty() || isLastNameEmpty() || isDobEmpty() || isGenderEmpty()
                            || isMobileNumberEmpty() || isDigitalAddressEmpty() || isResidentialAddressEmpty() || isLandmarkEmpty() || isMaritalStatusEmpty()
                            || isIdNumberEmpty() || isIdTypeEmpty() || isCompanyNumberEmpty() || isCompanyNameEmpty() || isCompanyAddressEmpty() || isStaffIdEmpty()
                            || isOccupationEmpty() || isEmploymentDateEmpty() || isBasicSalaryEmpty() || isGrossSalaryEmpty() || isNetSalaryEmpty() || isTotalDeductionEmpty()
                            || isContactPersonNameEmpty() || isContactPersonNumberEmpty() || isContactIdNumberEmpty() || isContactIdTypeEmpty() || isContactPlaceOfWorkEmpty()
                            || isContactInstitutionAddressEmpty()|| isGuarantorNameEmpty() || isGuarantorNumberEmpty() || isGuarantorAddressEmpty() || isGuarantorDigitalAddressEmpty()
                            || isGuarantorRelationshipEmpty() || isGuarantorIdNumberEmpty() || isGuarantorIdTypeEmpty() ||  isGuarantorOccupationEmpty()
                            || isGuarantorPlaceOfWorkEmpty() || isGuarantorInstitutionAddressEmpty() || isNetSalaryEmpty() || isContactPersonAddressEmpty()
                            || isContactPersonResidentialAddressEmpty() || isContactPersonRelationshipEmpty()
                            || isContactGenderEmpty() || isGuarantorGenderEmpty() || isPurposeFieldEmpty()
            );
        });
    }//end of input validation method...

    @FXML void checkCustomerIdField(KeyEvent keyEvent) {
        String value = idNumberField.getText().replaceAll(" ", "");
        boolean found = false;
        for (CustomersDataRepository data : fetchCustomersData()) {
            if (Objects.equals(value, data.getId_number())) {
                String name = data.getLastname().toUpperCase() + " " + data.getFirstname().toUpperCase();
                ALERT = new UserAlerts("EXISTING CUSTOMER ID", "Sorry anther customer owns this ID Number bearing the name '" + name + "'","you cannot register a customer with the same Id Number, please provide a unique id number.");
                ALERT.warningAlert();
                idNumberField.clear();
                break;
            }
        }
    }


    /*******************************************************************************************************************
        IMPLEMENTATION OF ACTION EVENT METHODS
     *******************************************************************************************************************/

    @FXML
    void uploadImageButtonClicked() {
        ImageReadWriter.uploadImageFile(uploadImageButton, imageView);
    }

    //Retrieve and set agent_id based on the selected agent name
    @FXML void agentSelected() {
        retrieveAgentId();
    }

    @FXML private void saveButtonOnAction() throws IOException {
        CustomersDataRepository customerRepository = new CustomersDataRepository();
        LoanApplicationEntity applicationEntity = new LoanApplicationEntity();
        LoansEntity loansEntity = new LoansEntity();
        MessageLogsEntity messageLogsEntity = new MessageLogsEntity();

        int USER_ID = getUserIdByName(AppController.activeUserPlaceHolder);
        String loanNumber = loanNumberLabel.getText();
        String loanType = loanTypeSelector.getValue();
        double loanAmount = Double.parseDouble(loanRequestField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String otherName = otherNameField.getText();
        String gender = genderSelector.getValue();
        LocalDate dateOfBirth = dobPicker.getValue();
        String mobileNumber = mobileNumberField.getText();
        String otherNumber = otherNumberField.getText();
        String email = emailField.getText();
        String digitalAddress = digitalAddressField.getText();
        String residentialAddress = residentialAddressField.getText();
        String landMark = landmarkField.getText();
        String education = qualificationSelector.getValue();
        String idType = idTypeSelector.getValue();
        String idNumber = idNumberField.getText();
        String maritalStatus = maritalStatusSelector.getValue();
        String companyName = companyNameField.getText();
        String companyContact = companyNumberField.getText();
        String staffId = staffIdField.getText();
        String companyAddress = companyAddressField.getText();
        String occupation = occupationField.getText();
        LocalDate employmentDate = employmentDatePicker.getValue();
        double basicSalary = Double.parseDouble(applicantBasicSalaryField.getText());
        double grossSalary = Double.parseDouble(applicantGrossSalaryField.getText());
        double totalDeduction = Double.parseDouble(applicantTotalDeductionField.getText());
        double netSalary = Double.parseDouble(applicantNetSalaryField.getText());
        String contactFullName = contactPersonNameField.getText();
        String contactMobileNumber = contactPersonNumberField.getText();
        String contactGender = contactPersonGenderSelector.getValue();
        String contactDigitalAdd = contactPersonDigitalAddField.getText();
        String contactLandmark = contactPersonResidentialField.getText();
        String contactRelationshipType = relationshipTypeSelector.getValue();
        String contactIdType = contactPersonIdTypeSelector.getValue();
        String contactIdNumber = contactPersonIdNoField.getText();
        String placeOfWork = placeOfWorkField.getText();
        String institutionAddress = institutionAddressField.getText();

        String guarantorName = guranterNameField.getText();
        String guarantorNumber = guranterNumberField.getText();
        String guarantorDigitalAdd = guranterDigitalAddressField.getText();
        String guarantorLandmark = guranterLandmarkField.getText();
        String guarantorIdType = guranterIdTypeSelector.getValue();
        String guarantorIdNumber = guranterIdNumberField.getText();
        String guarantorRelationship = guranterRelationshipTypeSelector.getValue();
        String guarantorOccupation = guranterOccupationField.getText();
        String guarantorPlaceOfWork = guranterPlaceOfWorkField.getText();
        String guarantorInstitutionAdd = guranterWorkAddressField.getText();
        double guarantorNetSalary = Double.parseDouble(guranterNetSalaryField.getText());
        int age = LocalDate.now().getYear() - dobPicker.getValue().getYear();
        String loanPurpose = loanPurposeField.getText();

        ALERT = new UserAlerts("UPDATE APPLICANT DATA", "Do you want to save all changes made to applicant's information?", "please confirm your decision to update information else CANCEL to abort.");
        if (ALERT.confirmationAlert()) {
//            saveUploadedImage();
            customerRepository.setAccount_Id(CUSTOMER_ID.get());
            customerRepository.setFirstname(firstName);
            customerRepository.setLastname(lastName);
            customerRepository.setOthername(otherName);
            customerRepository.setGender(gender);
            customerRepository.setDob(Date.valueOf(dateOfBirth));
            customerRepository.setMobile_number(mobileNumber);
            customerRepository.setOther_number(otherNumber);
            customerRepository.setEmail(email);
            customerRepository.setDigital_address(digitalAddress);
            customerRepository.setResidential_address(residentialAddress);
            customerRepository.setKey_landmark(landMark);
            customerRepository.setMarital_status(maritalStatus);
            customerRepository.setId_type(idType);
            customerRepository.setId_number(idNumber);
            customerRepository.setEducational_background(education);
            customerRepository.setContact_person_fullname(contactFullName);
            customerRepository.setContact_person_number(contactMobileNumber);
            customerRepository.setContact_person_gender(contactGender);
            customerRepository.setContact_person_landmark(contactLandmark);
            customerRepository.setContact_person_digital_address(contactDigitalAdd);
            customerRepository.setContact_person_id_type(contactIdType);
            customerRepository.setContact_person_id_number(contactIdNumber);
            customerRepository.setContact_person_place_of_work(placeOfWork);
            customerRepository.setInstitution_address(institutionAddress);
            customerRepository.setRelationship_to_applicant(contactRelationshipType);
            customerRepository.setCreated_by(USER_ID);
            customerRepository.setAgentId(AGENT_ID.get());

            applicationEntity.setLoan_no(loanNumber);
            applicationEntity.setImage(ImageReadWriter.readImageStream(imageView.getImage(), loadApplicantPhoto()));
            applicationEntity.setCompany_name(companyName);
            applicationEntity.setCompany_mobile_number(companyContact);
            applicationEntity.setCompany_address(companyAddress);
            applicationEntity.setStaff_id(staffId);
            applicationEntity.setOccupation(occupation);
            applicationEntity.setEmployment_date(employmentDate);
            applicationEntity.setBasic_salary(basicSalary);
            applicationEntity.setGross_salary(grossSalary);
            applicationEntity.setTotal_deduction(totalDeduction);
            applicationEntity.setNet_salary(netSalary);
            applicationEntity.setGender(gender);
            applicationEntity.setGuranter_name(guarantorName);
            applicationEntity.setGuranter_number(guarantorNumber);
            applicationEntity.setGuranter_digital_address(guarantorDigitalAdd);
            applicationEntity.setGuarantor_landmark(guarantorLandmark);
            applicationEntity.setGuranter_idType(guarantorIdType);
            applicationEntity.setGuranter_idNumber(guarantorIdNumber);
            applicationEntity.setGuranter_relationship(guarantorRelationship);
            applicationEntity.setGuranter_occupation(guarantorOccupation);
            applicationEntity.setGurater_place_of_work(guarantorPlaceOfWork);
            applicationEntity.setGuranter_institution_address(guarantorInstitutionAdd);
            applicationEntity.setGuranter_income(guarantorNetSalary);

            loansEntity.setLoan_no(loanNumber);
            loansEntity.setLoan_purpose(loanPurpose);
            loansEntity.setUpdated_by(USER_ID);
            loansEntity.setLoan_type(loanType);
            loansEntity.setRequested_amount(loanAmount);

            String title = "Loan Application Update";
            String notificationBody = "Loan application number " + loanNumber + " application information has been updated";
            NotificationEntity notification = new NotificationEntity(title, "SMS", notificationBody, USER_ID);
            logNotification(notification);

            int flag = updateLoanApplicantData(applicationEntity, customerRepository, loansEntity);

            Object[] loanData = {firstName.concat(" ").concat(lastName), loanNumber, loanType, loanAmount, loanNumber};
            String message = new MessageBuilders().loanApplicationUpdate(List.of(loanData));
            try {
                String responseValue = new SmsAPI().sendSms(mobileNumber, message);
                String statusValue = MessageStatus.getMessageStatusResult(responseValue).toString();
                messageLogsEntity.setRecipient(mobileNumber);
                messageLogsEntity.setTitle("Applicant Data Update");
                messageLogsEntity.setMessage(message);
                messageLogsEntity.setStatus(statusValue);
                messageLogsEntity.setSent_by(USER_ID);
                new MessagesModel().logNotificationMessages(messageLogsEntity);
            }catch (Exception e) {
                new MessagesModel().logNotificationMessages(messageLogsEntity);
                String className = this.getClass().getName();
                String error = Arrays.toString(e.getStackTrace());
                new ErrorLogger().logMessage(className, error);
            }
            if (flag >= 2) {
                saveButton.getScene().getWindow().hide();
            }
        }
    }//end of save button...

//    @FXML void resetButtonClicked() {
//        ALERT = new UserAlerts("RESET FIELDS", "Do you wish to clear the form and reset all fields? ");
//        if (ALERT.confirmationAlert()) {
//            resetFields();
//            if (filterIdOrAccountNo.getSearchText() != null) {
//                filterIdOrAccountNo.clearSelection();
//                personalInformationPane.setDisable(false);
//                loanCountLabel.setText("0.00");
//                paidAmountLabel.setText("0.00");
//                pendingAmountLabel.setText("0.00");
//            }
//        }
//    }

}
