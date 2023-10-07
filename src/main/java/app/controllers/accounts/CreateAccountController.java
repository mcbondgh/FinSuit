package app.controllers.accounts;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.accounts.CustomerAccountModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.accounts.CustomersDocumentRepository;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import com.jfoenix.controls.JFXToggleButton;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateAccountController extends CustomerAccountModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFICATION = new UserNotification();
    CustomersDataRepository customersDataRepository = new CustomersDataRepository();

    CustomersDocumentRepository documentRepository  = new CustomersDocumentRepository();
    CustomerAccountsDataRepository balanceDataModel = new CustomerAccountsDataRepository();

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label accountNumberLabel,absoluteFilePathLabel;
    @FXML private MFXButton previewButton, saveButton, cancelButton;
    @FXML private MFXScrollPane scrollPane;
    @FXML private AnchorPane anchorPane;
    @FXML private Pane contactPersonPane, politicallyExposedPane,applicantPane, filePane;
    @FXML private TextField firstNameField, lastNameField, otherNameField;
    @FXML private TextField placeOfBirthField, customerMobileNumberField, customerOtherNumberField, customerEmailAddressField;
    @FXML private TextField customerDigitalAddressField, customerResidentialField, customerLandmarkField, nameOfSpouseField, customerIdNumberField;
    @FXML private ComboBox<String> genderSelector, maritalStatusSelector, customerIdSelector, customerEducationalBackground;
    @FXML private DatePicker customerDobSelector, c_DobSelector;
    @FXML private TextArea commentsField;
    @FXML private TextField fullNameField, ageField, c_numberField, institutionNumberField,  c_landmarkField, c_digitalAddressField;
    @FXML private TextField c_idNumberField, placeOfWorkField, institutionAddressField;
    @FXML private ComboBox<String> c_genderSelector, accountTypeSelector, c_idSelector, relationshipSelector, c_educationalBackgroundSelector;

    @FXML private MFXButton uploadButton, previewFileButton;
    @FXML private TextField fileNameField;
    @FXML private TextArea reasonField;

    @FXML private CheckBox attachFileButton;

    @FXML private JFXToggleButton sendNotificationButton;
    @FXML private ComboBox<Double> initialDepositSelector;
    int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isFirstNameEmpty(){return firstNameField.getText().isBlank();}
    boolean isLastNameEmpty(){return lastNameField.getText().isEmpty();}
    boolean isGenderEmpty(){return genderSelector.getValue() == null;}
    boolean isDobEmpty(){return customerDobSelector.getValue() == null;}
    boolean isPlaceOfBirthEmpty() {return placeOfBirthField.getText().isEmpty();}
    boolean isMobileNumberEmpty(){return customerMobileNumberField.getText().isEmpty();}
    boolean isEmailEmpty() {return customerEmailAddressField.getText().isEmpty();}
    boolean isDigitalAddressEmpty(){return customerDigitalAddressField.getText().isEmpty();}
    boolean isResidentialAddressEmpty() {return customerResidentialField.getText().isEmpty();}
    boolean isLandMarkEmpty(){return customerLandmarkField.getText().isEmpty();}
    boolean isMaritalStatusEmpty() {return maritalStatusSelector.getValue() == null;}
    boolean isNameOfSpouseEmpty() {return nameOfSpouseField.getText().isEmpty();}
    boolean isIdTypeEmpty(){return customerIdSelector.getValue() == null;}
    boolean isIdNumberEmpty() {return customerIdNumberField.getText().isEmpty();}
    boolean isEducationalStatusEmpty() {return customerEducationalBackground.getValue() == null;}
    boolean isDepositEmpty() {return initialDepositSelector.getValue() == null;}
    boolean isFullNameEmpty() {return fullNameField.getText().isEmpty();}
    boolean isGurantorDobEmpty(){return c_DobSelector.getValue() == null;}
    boolean isGurantorNumberEmpty() {return c_numberField.getText().isEmpty();}
    boolean isGurantorGenderEmpty() {return c_genderSelector.getValue() == null;}
    boolean isGurantorLandmarkEmpty() {return c_landmarkField.getText().isEmpty();}
    boolean isGurantorDigitalAddressEmpty() {return c_digitalAddressField.getText().isEmpty();}
    boolean isGurantorIdTypeEmpty() {return c_idSelector.getValue() == null;}
    boolean isGurantorIdNumberEmpty() {return c_idNumberField.getText().isEmpty();}
    boolean isGurantorRelationshipTypeEmpty() {return relationshipSelector.getValue() == null;}

    boolean isSaveButtonEnabled() {return saveButton.isDisabled();}
    boolean isAttachFileButtonChecked(){return attachFileButton.isSelected();}
    boolean isFileNameFiledEmpty(){return fileNameField.getText().isEmpty();}
    boolean isPreviewButtonClicked() {return previewFileButton.isPressed();}



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            actionEventMethodsImplementation();
            populateFields();
            inputFieldValidationMethods();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void populateFields() {
        SpecialMethods.setGenderParameters(genderSelector);
        SpecialMethods.setIdTypeParameters(customerIdSelector);
        SpecialMethods.setIdTypeParameters(c_idSelector);
        SpecialMethods.setRelationshipTypes(relationshipSelector);
        SpecialMethods.setMaritalStatus(maritalStatusSelector);
        SpecialMethods.setAccountType(accountTypeSelector);
        SpecialMethods.setGenderParameters(c_genderSelector);
        SpecialMethods.setQualification(customerEducationalBackground);
        SpecialMethods.setQualification(c_educationalBackgroundSelector);
        SpecialMethods.setInitialDepositAmount(initialDepositSelector);
        setAccountNumber();
    }

    void setAccountNumber() {
        String totalCount = SpecialMethods.generateAccountNumber(getTotalCustomerIds() + 1);
        accountNumberLabel.setText(totalCount);
    }

    void resetFields() {
        accountTypeSelector.setValue(null);
        firstNameField.clear();
        lastNameField.clear();
        otherNameField.clear();
        genderSelector.setValue(null);
        customerDobSelector.setValue(null);
        ageField.clear();
        customerMobileNumberField.clear();
        customerOtherNumberField.clear();
        customerEmailAddressField.clear();
        customerDigitalAddressField.clear();
        customerResidentialField.clear();
        customerLandmarkField.clear();
        placeOfBirthField.clear();
        maritalStatusSelector.setValue(null);
        nameOfSpouseField.clear();
        customerIdSelector.setValue(null);
        customerIdNumberField.clear();
        customerEducationalBackground.setValue(null);
        initialDepositSelector.setValue(null);
        commentsField.clear();
        fullNameField.clear();
        c_numberField.clear();
        c_DobSelector.setValue(null);
        c_genderSelector.setValue(null);
        c_landmarkField.clear();
        c_educationalBackgroundSelector.setValue(null);
        c_digitalAddressField.clear();
        c_idSelector.setValue(null);
        c_idNumberField.clear();
        placeOfWorkField.clear();
        institutionAddressField.clear();
        institutionNumberField.clear();
        relationshipSelector.setValue(null);
    }

    void sendMailAndSMSNotification() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return null;
            }
        };
    }

    private byte[] getFileStream(String filePath) {
        byte[] bytes = new byte[0];
        try(FileInputStream stream = new FileInputStream(filePath)) {
           bytes = new byte[(byte) filePath.length()];
           stream.read(bytes);
        }catch (Exception e){e.printStackTrace();}
        return bytes;
    }
    String getUploadedDocument() {
        String fileName = "";
        String filePath = "";
        // Create a file Chooser to allow you to choose a file.
        FileChooser fileChooser = new FileChooser();

        //Create File Extensions
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(pdfFilter, jpgFilter, pngFilter);

        //Create a file to allow you store the selected into a file
        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

        //Check if a file was selected...
        if (!(selectedFile == null)) {
            fileName = selectedFile.getName();
            filePath = selectedFile.getPath();

            fileNameField.setText(fileName);
        } else {
            NOTIFICATION.informationNotification("EMPTY SELECTION ", "You made no selection after clicking the upload button.");
        }
        return filePath;
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/

    void inputFieldValidationMethods() {
        customerMobileNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                customerMobileNumberField.deletePreviousChar();
            }
            if (customerMobileNumberField.getText().length() >= 11) {
                customerMobileNumberField.deletePreviousChar();
            }
        });
        c_numberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9.]")) {
                c_numberField.deletePreviousChar();
            }
            if (c_numberField.getText().length() >= 11) {
                c_numberField.deletePreviousChar();
            }
        });

        institutionNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9.]")) {
                institutionNumberField.deletePreviousChar();
            }
            if (institutionNumberField.getText().length() >= 11) {
                institutionNumberField.deletePreviousChar();
            }
        });
        customerOtherNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                customerOtherNumberField.deletePreviousChar();
            }
            if (customerOtherNumberField.getText().length() >= 11) {
                customerOtherNumberField.deletePreviousChar();
            }
        });

        customerEmailAddressField.setOnKeyTyped(keyEvent -> {
            String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
            String valid = "-fx-border-color:#ddd; -fx-border-radius: 5px; ";
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$";

            if (!customerEmailAddressField.getText().matches(emailRegex)) {
                customerEmailAddressField.setStyle(invalid);
            } else customerEmailAddressField.setStyle(valid);
        });
    }//end of input fields validation

    @FXML void checkIfAccountExists() {
        List<String> accountTypes = new ArrayList<>();
        List<String> idNumbers = new ArrayList<>();
        checkAccountNumberExists(accountTypes, idNumbers);

        String accountValue = accountTypeSelector.getValue();
        String idValue = customerIdNumberField.getText();
        try {
            for (int var = 0; var <= accountTypes.size(); var ++) {
                if (accountTypes.get(var).equals(accountValue) && idNumbers.get(var).equals(idValue)) {
                    ALERTS = new UserAlerts("EXISTING CUSTOMER ID", "Sorry anther customer is registered with this ID Number associated to a '" + accountValue.toUpperCase() + "' at the moment" ,"Duplicate registration not allowed. Either choose a different account type or provide a unique Id Number for selected account type.");

                    ALERTS.warningAlert();
                    customerIdNumberField.clear();
                    return;
                }
            }
        }catch (IndexOutOfBoundsException ignore) {}

    }





    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/



    void actionEventMethodsImplementation() throws IOException {

        previewButton.setOnAction(event -> {
            try {
                PreviewCustomerDetails.applicantAccountType = accountTypeSelector.getValue();
                PreviewCustomerDetails.applicantDepositAmount = initialDepositSelector.getValue();
                PreviewCustomerDetails.applicantFullName = firstNameField.getText() + " " + otherNameField.getText() + " " + lastNameField.getText();
                PreviewCustomerDetails.applicantAccountNumber = accountNumberLabel.getText();
                PreviewCustomerDetails.applicantGender = genderSelector.getValue();
                PreviewCustomerDetails.applicantAge = ageField.getText();
                PreviewCustomerDetails.applicantDob = String.valueOf(customerDobSelector.getValue());
                PreviewCustomerDetails.applicantPlaceOfBirth = placeOfBirthField.getText();
                PreviewCustomerDetails.applicantNumber = customerMobileNumberField.getText();
                PreviewCustomerDetails.applicantOtherNumber = customerOtherNumberField.getText();
                PreviewCustomerDetails.applicantEmail = customerEmailAddressField.getText();
                PreviewCustomerDetails.applicantDigitalAddress = customerDigitalAddressField.getText();
                PreviewCustomerDetails.applicantResidentialAddress = customerResidentialField.getText();
                PreviewCustomerDetails.applicantLandmark = customerLandmarkField.getText();
                PreviewCustomerDetails.applicantMaritalStatus = maritalStatusSelector.getValue();
                PreviewCustomerDetails.applicantSpouseName = nameOfSpouseField.getText();
                PreviewCustomerDetails.applicantIdType = customerIdSelector.getValue();
                PreviewCustomerDetails.applicantIdNumber = customerIdNumberField.getText();
                PreviewCustomerDetails.applicantEducationalBackground = customerEducationalBackground.getValue();
                PreviewCustomerDetails.applicantDepositAmount = initialDepositSelector.getValue();
                PreviewCustomerDetails.applicantExtraInfo = commentsField.getText();
                PreviewCustomerDetails.contactFullName = fullNameField.getText();
                PreviewCustomerDetails.contactDob = String.valueOf(c_DobSelector.getValue());
                PreviewCustomerDetails.contactMobileNumber = c_numberField.getText();
                PreviewCustomerDetails.contactGender = c_genderSelector.getValue();
                PreviewCustomerDetails.contactLandmark = c_landmarkField.getText();
                PreviewCustomerDetails.contactEducationStatus = c_educationalBackgroundSelector.getValue();
                PreviewCustomerDetails.contactDigitalAddress = c_digitalAddressField.getText();
                PreviewCustomerDetails.contactIdType = c_idSelector.getValue();
                PreviewCustomerDetails.contactIdNumber = c_idNumberField.getText();
                PreviewCustomerDetails.contactPlaceOfWork = placeOfWorkField.getText();
                PreviewCustomerDetails.contactInstitutionAddress = institutionAddressField.getText();
                PreviewCustomerDetails.contactInstitutionNumber = institutionNumberField.getText();
                PreviewCustomerDetails.relationshipType = relationshipSelector.getValue();
                AppStages.previewApplicantStage().show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

//        politicallyExposedButton.setOnAction(event -> {
//            politicallyExposedPane.setDisable(!politicallyExposedButton.isSelected());
//        });

        customerDobSelector.setOnAction(action -> {
            LocalDate currentDate = LocalDate.now();
            LocalDate dob = customerDobSelector.getValue();
            int age = currentDate.getYear() - dob.getYear();
            if (dob.isAfter(currentDate)) {
                ageField.setText("NV");
                ageField.setStyle("-fx-text-fill:red; -fx-background-color:#ffdbdb");
            } else {
                ageField.setText(String.valueOf(age)); ageField.setStyle("-fx-text-fill:#000; fx-background-color:#ffdbdb");
            }
        });

        cancelButton.setOnAction(event -> {
            ALERTS = new UserAlerts("RESET FIELDS", "DO YOU WISH TO CLEAR ALL FIELDS TO RESTART THE ACCOUNT CREATING PROCESS?", "please confirm your decision to reset all fields else CANCEL to abort");
            if (ALERTS.confirmationAlert()) {
                resetFields();
            }
        });

        scrollPane.setOnMouseMoved(mouseEvent -> {
            String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
            saveButton.setDisable(isFirstNameEmpty() || isLastNameEmpty() || isGenderEmpty() || isDobEmpty()
            || isPlaceOfBirthEmpty() || isMobileNumberEmpty() || isDigitalAddressEmpty() || isResidentialAddressEmpty()||
                    isLandMarkEmpty() || isMaritalStatusEmpty() || isIdTypeEmpty() || isIdNumberEmpty() || isEducationalStatusEmpty() ||
                    isDepositEmpty() || isFullNameEmpty() || isGurantorDobEmpty() || isGurantorNumberEmpty() || isGurantorGenderEmpty() ||
                    isGurantorLandmarkEmpty() || isGurantorDigitalAddressEmpty() || isGurantorIdTypeEmpty() || isGurantorIdNumberEmpty() ||
                    isGurantorRelationshipTypeEmpty() || customerEmailAddressField.getStyle().equals(invalid) ||
                    (isFileNameFiledEmpty() && isAttachFileButtonChecked())
            );

            previewButton.setDisable(isFirstNameEmpty() || isLastNameEmpty() || isGenderEmpty() || isDobEmpty()
                    || isPlaceOfBirthEmpty() || isMobileNumberEmpty() || isDigitalAddressEmpty() || isResidentialAddressEmpty()||
                    isLandMarkEmpty() || isMaritalStatusEmpty() || isIdTypeEmpty() || isIdNumberEmpty() || isEducationalStatusEmpty() ||
                    isDepositEmpty() || isFullNameEmpty() || isGurantorDobEmpty() || isGurantorNumberEmpty() || isGurantorGenderEmpty() ||
                    isGurantorLandmarkEmpty() || isGurantorDigitalAddressEmpty() || isGurantorIdTypeEmpty() || isGurantorIdNumberEmpty() ||
                    isGurantorRelationshipTypeEmpty() || customerEmailAddressField.getStyle().equals(invalid)
            );
            sendNotificationButton.setDisable(isSaveButtonEnabled());
        });

        saveButton.setOnAction(event -> {
            int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);

            String accountType = accountTypeSelector.getValue();
            String accountNumber = accountNumberLabel.getText();
            String firstname = firstNameField.getText();
            String lastname = lastNameField.getText();
            String otherName = otherNameField.getText();
            String mobileNumber = customerMobileNumberField.getText();
            String otherNumber = customerOtherNumberField.getText();
            String gender = genderSelector.getValue();
            int age = Integer.parseInt(ageField.getText());
            LocalDate customerDob = customerDobSelector.getValue();
            String placeOfBirth = placeOfBirthField.getText();
            String email = customerEmailAddressField.getText();
            String digitalAddress = customerDigitalAddressField.getText();
            String residentialAddress = customerResidentialField.getText();
            String landmark = customerLandmarkField.getText();
            String maritalStatus = maritalStatusSelector.getValue();
            String spouseName = nameOfSpouseField.getText();
            String idType = customerIdSelector.getValue();
            String idNumber = customerIdNumberField.getText();
            String qualification = customerEducationalBackground.getValue();
            Double depositAmount = initialDepositSelector.getValue();
            String comments = commentsField.getText();

            String fullname = fullNameField.getText();
            LocalDate c_dob = c_DobSelector.getValue();
            String c_mobileNumber = c_numberField.getText();
            String c_gender = c_genderSelector.getValue();
            String c_landMark = c_landmarkField.getText();
            String c_qualification = c_educationalBackgroundSelector.getValue();
            String c_digitalAddress = c_digitalAddressField.getText();
            String c_idType = c_idSelector.getValue();
            String c_idNumber = c_idNumberField.getText();
            String placeOfWOrk = placeOfWorkField.getText();
            String institutionAddress= institutionAddressField.getText();
            String institutionNumber = institutionNumberField.getText();
            String relationshipType = relationshipSelector.getValue();

            ALERTS = new UserAlerts("CREATE ACCOUNT", "ARE YOU SURE YOU WANT TO CREATE NEW ACCOUNT WITH ACCOUNT TYPE AS '" +  accountType + "'", "please confirm your action to create this account else CANCEL to abort process.");
            if (ALERTS.confirmationAlert()) {
                customersDataRepository.setFirstname(firstname);
                customersDataRepository.setLastname(lastname);
                customersDataRepository.setOthername(otherName);
                customersDataRepository.setGender(gender);
                customersDataRepository.setDob(Date.valueOf(customerDob));//7
                customersDataRepository.setAge(age);//8
                customersDataRepository.setPlace_of_birth(placeOfBirth);//9
                customersDataRepository.setMobile_number(mobileNumber);//10
                customersDataRepository.setOther_number(otherNumber);
                customersDataRepository.setEmail(email);
                customersDataRepository.setDigital_address(digitalAddress);
                customersDataRepository.setResidential_address(residentialAddress);
                customersDataRepository.setKey_landmark(landmark);//15
                customersDataRepository.setMarital_status(maritalStatus);
                customersDataRepository.setName_of_spouse(spouseName);//17
                customersDataRepository.setId_type(idType);
                customersDataRepository.setId_number(idNumber);
                customersDataRepository.setEducational_background(qualification);
                customersDataRepository.setAdditional_comment(comments);
                customersDataRepository.setContact_person_fullname(fullname);
                customersDataRepository.setContact_person_dob(Date.valueOf(c_dob));//23
                customersDataRepository.setContact_person_number(c_mobileNumber);//24
                customersDataRepository.setContact_person_gender(c_gender);//25
                customersDataRepository.setContact_person_landmark(c_landMark);//26
                customersDataRepository.setContact_person_education_level(c_qualification);
                customersDataRepository.setContact_person_digital_address(c_digitalAddress);//28
                customersDataRepository.setContact_person_id_type(c_idType);
                customersDataRepository.setContact_person_id_number(c_idNumber);//30
                customersDataRepository.setContact_person_place_of_work(placeOfWOrk);
                customersDataRepository.setInstitution_address(institutionAddress);
                customersDataRepository.setInstitution_number(institutionNumber);//33
                customersDataRepository.setRelationship_to_applicant(relationshipType);//34
                customersDataRepository.setCreated_by(currentUserId);//35

                int flag = createNewAccount(customersDataRepository);

                balanceDataModel.setCustomer_id(getTotalCustomerIds());
                balanceDataModel.setAccount_type(accountType);
                balanceDataModel.setAccount_number(accountNumber);
                balanceDataModel.setAccount_balance(depositAmount);
                balanceDataModel.setPrevious_balance(depositAmount);
                balanceDataModel.setModified_by(currentUserId);

                flag += createAccountBalance(balanceDataModel);


                //Check if the file attachment button is checked. this button enables you to upload a file to the system
                //if true we, collect data and save to database.
                if (isAttachFileButtonChecked()) {
                   int customer_id = getCustomerIdByAccountNumber(accountNumber);
                   String filePath = absoluteFilePathLabel.getText();
                   documentRepository.setCustomer_id(customer_id);
                   documentRepository.setDocument_name(firstNameField.getText());
                   documentRepository.setFile_content(getFileStream(filePath));
                   documentRepository.setUploaded_by(currentUserId);
                   documentRepository.setReason_for_upload(reasonField.getText());

                   saveDocument(documentRepository);
                }
                System.out.println(flag);
                if (flag == 2) {
                    NOTIFICATION.successNotification("ACCOUNT CREATE", "Customer Account has successfully been created.");
                    resetFields();
                    setAccountNumber();
                }else {
                    NOTIFICATION.errorNotification("ACCOUNT CREATION FAILED", "Filed to create this account, please contact your system admin.");
                    rollBack();
                }
            }
        });

        attachFileButton.setOnAction(checkEvent-> {
            uploadButton.setDisable(!isAttachFileButtonChecked());
            reasonField.setDisable(!isAttachFileButtonChecked());
            if (!isAttachFileButtonChecked()) {
                fileNameField.clear();
            }
        });


        uploadButton.setOnAction(uploadAction -> {
            previewFileButton.setDisable(!isFileNameFiledEmpty());
            absoluteFilePathLabel.setText(getUploadedDocument());

        });

        previewFileButton.setOnAction(action -> {
            try {
                String file = absoluteFilePathLabel.getText();
                if (file.isEmpty()) {
                    return;
                }
                Desktop.getDesktop().browse(new File(file).toURI());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }//end of action event methods implementation

}//end of class
