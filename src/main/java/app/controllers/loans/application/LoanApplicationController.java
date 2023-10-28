package app.controllers.loans.application;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.documents.ImageReadWriter;
import app.enums.MessageStatus;
import app.errorLogger.ErrorLogger;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.LoanApplicationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoanApplicationController extends LoansModel implements Initializable {

    UserNotification NOTIFY = new UserNotification();
    UserAlerts ALERT;

    CustomersDataRepository customerRepository = new CustomersDataRepository();
    LoanApplicationEntity applicationEntity = new LoanApplicationEntity();
    CustomerAccountsDataRepository accountRepository = new CustomerAccountsDataRepository();
    MessagesModel MESSAGE_MODEL_OBJECT = new MessagesModel();
    MessageLogsEntity logsEntity = new MessageLogsEntity();

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML private Label loanNumberLabel;
    @FXML
    private Label filterCustomerLabel, customerTypeLabel, loanCountLabel, paidAmountLabel, pendingAmountLabel, draftCountLabel;
    @FXML
    private VBox vBox;
    @FXML
    Pane customerSelectorPane;
    @FXML private MFXButton resetButton;
    @FXML private ComboBox<String> customerTypeSelector, loanTypeSelector;
    @FXML private ImageView imageView;
    @FXML private MFXFilterComboBox<Object> filterIdOrAccountNo;
    @FXML private GridPane gridPane;
    @FXML private MFXButton saveButton, draftButton, uploadImageButton;
    @FXML private TextField loanRequestField, firstNameField, lastNameField, otherNameField, mobileNumberField;
    @FXML private TextField otherNumberField, emailField, digitalAddressField, residentialAddressField;
    @FXML private TextField landmarkField, idNumberField;
    @FXML private TextField companyNameField, companyNumberField, companyAddressField, occupationField;

    @FXML private ComboBox<String> contactPersonGenderSelector, guranterGenderSelector;
    @FXML private TextField contactPersonNameField, contactPersonNumberField, contactPersonDigitalAddField;
    @FXML private TextField contactPersonResidentialField, contactPersonIdNoField, placeOfWorkField, institutionAddressField;
    @FXML private TextField guranterNameField, guranterNumberField, guranterDigitalAddressField, guranterLandmarkField;
    @FXML private  TextField guranterIdNumberField, guranterOccupationField, guranterPlaceOfWorkField, guranterWorkAddressField, guranterNetSalaryField;
    @FXML private TextField applicantBasicSalaryField, applicantTotalDeductionField, applicantNetSalaryField, applicantGrossSalaryField;
    @FXML private TextField staffIdField;
    @FXML private ComboBox<String> maritalStatusSelector, qualificationSelector, idTypeSelector, genderSelector;
    @FXML private ComboBox<String> relationshipTypeSelector, contactPersonIdTypeSelector;
    @FXML private ComboBox<String> guranterIdTypeSelector, guranterRelationshipTypeSelector;
    @FXML private MFXScrollPane scrollPane;
    @FXML private DatePicker dobPicker, employmentDatePicker;
    @FXML private Pane personalInformationPane;
    @FXML private TextArea loanPurposeField;
    private File imageFile = null;
    final long STANDARD_IMAGE_SIZE = (300 * 1024);

    //Default constructor
    public LoanApplicationController() throws IOException {}


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    boolean checkImageSize(long imageSize) {
        return imageSize > STANDARD_IMAGE_SIZE;
    }
    boolean isFirstNameEmpty() {return firstNameField.getText().isBlank();}
    boolean isLastNameEmpty() {return lastNameField.getText().isEmpty();}

    boolean isLoanAmountFieldEmpty() {
        return loanRequestField.getText().isEmpty();
    }
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
     *********************************************** IMPLEMENTATION OF OTHER METHODS.***********************************
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] customerTypes = {"New Client", "Existing Client"};
        customerTypeSelector.setValue(customerTypes[0]);
        customerSelectorPane.setVisible(false);
        for (String customerType : customerTypes) {
            customerTypeSelector.getItems().add(customerType);
        }
        loanNumberLabel.setText(SpecialMethods.generateLoanNumber(getTotalLoanCount() + 1));
        SpecialMethods.setLoanType(loanTypeSelector);
        SpecialMethods.setMaritalStatus(maritalStatusSelector);
        SpecialMethods.setGenderParameters(genderSelector);
        SpecialMethods.setIdTypeParameters(idTypeSelector);
        SpecialMethods.setIdTypeParameters(guranterIdTypeSelector);
        SpecialMethods.setQualification(qualificationSelector);
        SpecialMethods.setRelationshipTypes(guranterRelationshipTypeSelector);
        SpecialMethods.setRelationshipTypes(relationshipTypeSelector);
        SpecialMethods.setIdTypeParameters(contactPersonIdTypeSelector);
        SpecialMethods.setGenderParameters(guranterGenderSelector);
        SpecialMethods.setGenderParameters(contactPersonGenderSelector);

        filterIdOrAccountNo.setItems(getCustomerIdAndAccountNumbers());

    }
     private void resetFields() {
         loanNumberLabel.setText(SpecialMethods.generateLoanNumber(getTotalLoanCount() + 1));
         loanTypeSelector.setValue(null);
         loanRequestField.clear();
         firstNameField.clear();
         lastNameField.clear();
         otherNumberField.clear();
         dobPicker.setValue(null);
         genderSelector.setValue(null);
         mobileNumberField.clear();
         otherNumberField.clear();
         emailField.clear();
         digitalAddressField.clear();
         residentialAddressField.clear();
         landmarkField.clear();
         qualificationSelector.setValue(null);
         maritalStatusSelector.setValue(null);
         idTypeSelector.setValue(null);
         idNumberField.clear();
         companyNumberField.clear();
         companyNumberField.clear();
         companyAddressField.clear();
         staffIdField.clear();
         occupationField.clear();
         employmentDatePicker.setValue(null);
         applicantBasicSalaryField.clear();
         applicantGrossSalaryField.clear();
         applicantNetSalaryField.clear();
         applicantTotalDeductionField.clear();
         contactPersonNameField.clear();
         contactPersonNumberField.clear();
         contactPersonDigitalAddField.clear();
         contactPersonResidentialField.clear();
         contactPersonGenderSelector.setValue(null);
         relationshipTypeSelector.setValue(null);
         contactPersonIdTypeSelector.setValue(null);
         contactPersonIdNoField.clear();
         placeOfWorkField.clear();
         institutionAddressField.clear();
         guranterNameField.clear();
         guranterNumberField.clear();
         guranterDigitalAddressField.clear();
         guranterLandmarkField.clear();
         guranterGenderSelector.setValue(null);
         guranterIdTypeSelector.setValue(null);
         guranterIdNumberField.clear();
         guranterRelationshipTypeSelector.setValue(null);
         guranterOccupationField.clear();
         guranterPlaceOfWorkField.clear();
         guranterWorkAddressField.clear();
         guranterNetSalaryField.clear();
         loanPurposeField.clear();
    }

    private InputStream getImageStream() {
        InputStream imageStream = null;
        try {
            imageStream = new FileInputStream(imageFile);
        }catch (FileNotFoundException ignore){}
       return imageStream;
    }

    private void saveUploadedImage() {
        try {
            String imageName = imageFile.getName();
            ImageReadWriter.saveImageToDestination(imageName, imageView);
        }catch (Exception e) {
            imageFile = ImageReadWriter.defaultImageName;
        }

    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/

    @FXML void validateRequestAmountInput() {
        loanRequestField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                loanRequestField.deletePreviousChar();
                loanRequestField.deleteNextChar();
            }
        });
        mobileNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                mobileNumberField.deletePreviousChar();
                mobileNumberField.deleteNextChar();
            }
        });
        otherNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                otherNumberField.deletePreviousChar();
                otherNumberField.deleteNextChar();
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
        });
        guranterNumberField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                guranterNumberField.deletePreviousChar();
                guranterNumberField.deleteNextChar();
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
                    || isContactPersonNameEmpty() || isContactPersonNumberEmpty() || isDigitalAddressEmpty() || isResidentialAddressEmpty() || isGuarantorRelationshipEmpty()
                    || isContactIdNumberEmpty() || isContactIdTypeEmpty() || isContactPlaceOfWorkEmpty() || isContactInstitutionAddressEmpty()
                    || isGuarantorNameEmpty() || isGuarantorNumberEmpty() || isGuarantorAddressEmpty() || isGuarantorDigitalAddressEmpty() || isGuarantorRelationshipEmpty()
                    || isGuarantorIdNumberEmpty() || isGuarantorIdTypeEmpty() || isGuarantorRelationshipEmpty() || isGuarantorRelationshipEmpty() || isGuarantorOccupationEmpty()
                    || isGuarantorPlaceOfWorkEmpty() || isGuarantorInstitutionAddressEmpty() || isNetSalaryEmpty() || isContactPersonAddressEmpty() || isContactPersonResidentialAddressEmpty() || isContactPersonRelationshipEmpty()
                    || isContactGenderEmpty() || isGuarantorGenderEmpty() || isPurposeFieldEmpty()
            );
        });
    }//end of input validation method...

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/

    @FXML
    void customerTypeSelected(ActionEvent event) {
        String value = customerTypeSelector.getValue();
        switch (value) {
            case "New Client" -> {
                customerSelectorPane.setVisible(false);
            }
            case "Existing Client" -> {
                customerSelectorPane.setVisible(true);
            }
        }
    }
    @FXML void uploadImageButtonClicked() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Preferred Image");
            FileChooser.ExtensionFilter filter = fileChooser.getSelectedExtensionFilter();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg",  "*.jpeg"));
            imageFile = fileChooser.showOpenDialog(uploadImageButton.getScene().getWindow());
            Image image = new Image(imageFile.getPath());
            long imageSize = imageFile.length();
            //CHECK IF THE IMAGE SIZE IS GREATER THAN THE ACTUAL PERMITTED SIZE ie 300kb.
            if (checkImageSize(imageSize)) {
                NOTIFY.informationNotification("FILE TOO LARGE", "Selected file size should be less or equal to 300kb");
            } else  {
                imageView.setImage(image);
            }
        }catch (Exception ignore) {

        }
    }
    @FXML void checkCustomerIdField(KeyEvent keyEvent) {
        String value = idNumberField.getText().replaceAll(" ", "");
        boolean found = false;
        for (CustomersDataRepository data : fetchCustomersData()) {
            if (Objects.equals(value, data.getId_number()) && customerTypeSelector.getValue().equals("New Client")) {
                String name = data.getLastname().toUpperCase() + " " + data.getFirstname().toUpperCase();
                ALERT = new UserAlerts("EXISTING CUSTOMER ID", "Sorry anther customer owns this ID Number bearing the name '" + name + "'","you cannot register a customer with the same Id Number, please provide a unique id number.");
                ALERT.warningAlert();
                idNumberField.clear();
                break;
            }
        }

    }
    @FXML void filterCustomerOnAction() {
        try {
            DecimalFormat currencyFormat = new DecimalFormat("#.##");
            String value = filterIdOrAccountNo.getValue().toString();
            int loanCount = (int) countTotalLoans(value).get(0);
            double disbursedAmount = (double) countTotalLoans(value).get(1);
            double totalPayment = disbursedAmount - (double)countTotalLoans(value).get(2);
            loanCountLabel.setText(String.valueOf(loanCount));
            paidAmountLabel.setText(String.valueOf(currencyFormat.format(disbursedAmount)));
            pendingAmountLabel.setText(String.valueOf(currencyFormat.format(totalPayment)));

            ArrayList<Object> registeredUser = getExistingCustomerForLoan(value);
            firstNameField.setText(registeredUser.get(0).toString());
            lastNameField.setText(registeredUser.get(1).toString());
            otherNameField.setText(registeredUser.get(2).toString());
            genderSelector.setValue(registeredUser.get(3).toString());
            dobPicker.setValue(LocalDate.parse(registeredUser.get(4).toString()));
            mobileNumberField.setText(registeredUser.get(5).toString());
            otherNumberField.setText(registeredUser.get(6).toString());
            emailField.setText(registeredUser.get(7).toString());
            digitalAddressField.setText(registeredUser.get(8).toString());
            residentialAddressField.setText(registeredUser.get(9).toString());
            landmarkField.setText(registeredUser.get(10).toString());
            maritalStatusSelector.setValue(registeredUser.get(11).toString());
            idTypeSelector.setValue(registeredUser.get(12).toString());
            idNumberField.setText(registeredUser.get(13).toString());
            qualificationSelector.setValue(registeredUser.get(14).toString());
            personalInformationPane.setDisable(true);
        }catch (Exception ignore){}
    }
    @FXML private void saveButtonClicked() {
        int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);
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

        ALERT = new UserAlerts("LOAN APPLICATION", "You have initiated a loan request for '" + firstName.toUpperCase() +" " + lastName.toUpperCase() +"', do you wish to apply now?", "please confirm your decision to apply else CANCEL to abort.");
        if (ALERT.confirmationAlert()) {
            saveUploadedImage();
            customerRepository.setFirstname(firstName);
            customerRepository.setLastname(lastName);
            customerRepository.setOthername(otherName);
            customerRepository.setGender(gender);
            customerRepository.setDob(Date.valueOf(dateOfBirth));
            customerRepository.setAge(age);
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
            customerRepository.setCreated_by(currentUserId);

            applicationEntity.setLoan_no(loanNumber);
            applicationEntity.setProfile_picture(imageFile.getName());
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
            applicationEntity.setNet_salary(guarantorNetSalary);

            int flag = applyForLoan(applicationEntity, customerRepository);
            flag += createLoan( totalCustomersCount(), loanNumber, loanType, loanAmount, loanPurpose, currentUserId);

            //CREATE ACCOUNT FOR LOAN CLIENT
            accountRepository.setCustomer_id(totalCustomersCount());
            accountRepository.setAccount_number(SpecialMethods.generateAccountNumber(totalCustomersCount() + 1));
            accountRepository.setAccount_type("Savings Account");
            accountRepository.setModified_by(currentUserId);
            flag += createAccountBalance(accountRepository);

            String message = new MessageBuilders().loanApplicationMessageBuilder(firstName.concat(" ").concat(lastName), loanNumber,loanType, loanAmount);
            try {
                String responseValue = new SmsAPI().sendSms(mobileNumber, message);
                String statusValue = MessageStatus.getMessageStatusResult(responseValue).toString();
                logsEntity.setRecipient(mobileNumber);
                logsEntity.setTitle("Loan Application");
                logsEntity.setMessage(message);
                logsEntity.setStatus(statusValue);
                logsEntity.setSent_by(currentUserId);
                new MessagesModel().logNotificationMessages(logsEntity);
            }catch (Exception e) {
                new ErrorLogger().log(e.getLocalizedMessage());
            }
            if (flag >= 2) {
                NOTIFY.successNotification("LOAN REQUEST SUCCESSFUL", "Perfect, loan request successfully placed, your loan request has been queued for review.");
                resetFields();
            }
        }


    }//end of save button...
    @FXML void resetButtonClicked() {
        ALERT = new UserAlerts("RESET FIELDS", "Do you wish to clear the form and reset all fields? ");
        if (ALERT.confirmationAlert()) {
            resetFields();
            if (filterIdOrAccountNo.getSearchText() != null) {
                filterIdOrAccountNo.clearSelection();
                personalInformationPane.setDisable(false);
                loanCountLabel.setText("0.00");
                paidAmountLabel.setText("0.00");
                pendingAmountLabel.setText("0.00");
            }
        }
    }




}//end of class.