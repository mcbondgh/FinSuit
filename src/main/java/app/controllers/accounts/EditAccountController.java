package app.controllers.accounts;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.email.EmailAPI;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.enums.MessageStatus;
import app.errorLogger.ErrorLogger;
import app.models.accounts.CustomerAccountModel;
import app.models.message.MessagesModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.accounts.CustomersDocumentRepository;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.specialmethods.SpecialMethods;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditAccountController extends CustomerAccountModel implements Initializable {
    UserAlerts ALERTS;
    UserNotification NOTIFICATION = new UserNotification();
    CustomersDataRepository customersDataRepository = new CustomersDataRepository();
    CustomerAccountsDataRepository balanceDataModel = new CustomerAccountsDataRepository();
    CustomersDocumentRepository documentRepository = new CustomersDocumentRepository();
    SmsAPI SMS = new SmsAPI();
    MessageLogsEntity MESSAGE_LOGS = new MessageLogsEntity();
    MessagesModel MESSAGE_MODEL = new MessagesModel();
    NotificationEntity NOTIFY_ENTITY = new NotificationEntity();

    ErrorLogger errorLogger = new ErrorLogger();

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/


    @FXML
    private Label accountNumberLabel, absoluteFilePathLabel, accountStatusLabel;
    @FXML private MFXButton uploadButton, saveButton, cancelButton;
    @FXML private JFXButton previewItemButton;
    @FXML private MFXScrollPane scrollPane;
    @FXML private AnchorPane anchorPane;
    @FXML private Pane contactPersonPane, filePane, applicantPane;
    @FXML private TextField firstNameField, lastNameField, otherNameField;
    @FXML private TextField placeOfBirthField, customerMobileNumberField, customerOtherNumberField, customerEmailAddressField;
    @FXML private TextField customerDigitalAddressField, customerResidentialField, customerLandmarkField, nameOfSpouseField, customerIdNumberField;
    @FXML private ComboBox<String> genderSelector, maritalStatusSelector, customerIdSelector, customerEducationalBackground;
    @FXML private DatePicker customerDobSelector, c_DobSelector;
    @FXML private TextArea commentsField, reasonField;
    @FXML private TextField fullNameField, ageField, c_numberField, institutionNumberField,  c_landmarkField, c_digitalAddressField;
    @FXML private TextField c_idNumberField, placeOfWorkField, institutionAddressField;
    @FXML private ComboBox<String> c_genderSelector, accountTypeSelector, c_idSelector, relationshipSelector, c_educationalBackgroundSelector;
    public JFXCheckBox editCheckBox, attachFileButton;
    @FXML private JFXToggleButton statusToggleButton;
    public ImageView imageView;

    @FXML
    private  TextField fileNameField;

    private long selectedCustomerId = 0;

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
    boolean isEditButtonChecked() {return editCheckBox.isSelected();}
    boolean isFileNameFieldEmpty() {return fileNameField.getText().isEmpty();}
    boolean isReasonFieldEmpty() {return reasonField.getText().isEmpty();}

    boolean isAttachFileButtonSelected() {
        return attachFileButton.isSelected();
    }


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     *******************************************************************************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionEventMethodsImplementation();
        populateFields();
        inputFieldValidationMethods();
        populateTextFields();
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
        accountNumberLabel.setText(ViewAccountController.selectedCustomerAccountNumber);
    }
    void populateTextFields() {
        String accountNumber = accountNumberLabel.getText();
        for (CustomerAccountsDataRepository value : fetchCustomersAccountData()){
            if (Objects.equals(value.getAccount_number(), accountNumber)) {
                selectedCustomerId = value.getCustomer_id();
                accountTypeSelector.setValue(value.getAccount_type());
                accountStatusLabel.setText(value.getAccount_status().toUpperCase());
                if (accountStatusLabel.getText().equalsIgnoreCase("active")) {
                    accountStatusLabel.setStyle("-fx-text-fill: #009a4d;");
                    statusToggleButton.setSelected(true);
                    statusToggleButton.setText("Enable Account");
                } else {
                    accountStatusLabel.setStyle("-fx-text-fill:#ff0000");
                    statusToggleButton.setSelected(false);
                    statusToggleButton.setText("Close Account");
                }
            }
        }

        for (CustomersDataRepository item : fetchCustomersData()) {
            if (Objects.equals(item.getAccount_Id(), selectedCustomerId)) {
                firstNameField.setText(item.getFirstname());
                lastNameField.setText(item.getLastname());
                otherNameField.setText(item.getOthername());
                genderSelector.setValue(item.getGender());
                customerDobSelector.setValue(item.getDob().toLocalDate());
                ageField.setText(String.valueOf(item.getAge()));
                placeOfBirthField.setText(item.getPlace_of_birth());
                customerMobileNumberField.setText(item.getMobile_number());
                customerOtherNumberField.setText(item.getOther_number());
                customerEmailAddressField.setText(item.getEmail());
                customerDigitalAddressField.setText(item.getDigital_address());
                customerResidentialField.setText(item.getResidential_address());
                customerLandmarkField.setText(item.getKey_landmark());
                maritalStatusSelector.setValue(item.getMarital_status());
                nameOfSpouseField.setText(item.getName_of_spouse());
                customerIdSelector.setValue(item.getId_type());
                customerIdNumberField.setText(item.getId_number());
                customerEducationalBackground.setValue(item.getEducational_background());
                commentsField.setText(item.getAdditional_comment());
                fullNameField.setText(item.getContact_person_fullname());
                c_DobSelector.setValue(item.getContact_person_dob().toLocalDate());
                c_numberField.setText(item.getContact_person_number());
                c_genderSelector.setValue(item.getContact_person_gender());
                c_landmarkField.setText(item.getContact_person_landmark());
                c_educationalBackgroundSelector.setValue(item.getContact_person_education_level());
                c_digitalAddressField.setText(item.getContact_person_digital_address());
                c_idSelector.setValue(item.getContact_person_id_type());
                c_idNumberField.setText(item.getContact_person_id_number());
                placeOfWorkField.setText(item.getContact_person_place_of_work());
                institutionAddressField.setText(item.getInstitution_address());
                institutionNumberField.setText(item.getInstitution_number());
                relationshipSelector.setValue(item.getRelationship_to_applicant());

            }
        }

        long customer_id = getCustomerIdByAccountNumber(accountNumber);
        for (CustomersDocumentRepository item : getLatestDocumentUpload(customer_id)) {
                fileNameField.setText(item.getDocument_name());
                reasonField.setText(item.getReason_for_upload());
        }


    }//enf of method.
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
        fileNameField.clear();
    }

    private byte[] readFileContent (String filePath) {
        try(FileInputStream inputStream = new FileInputStream(filePath)) {
           byte[] fileInByte = new byte[(int)filePath.length()];
           inputStream.read(fileInByte);
           return fileInByte;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void notificationLogger(String accountNumber) {
        String statusText = statusToggleButton.isSelected() ? "ACTIVE"  : "CLOSED";
        NOTIFY_ENTITY.setMessage("Bio data of account number ".concat(accountNumber).
                concat( " has been updated with account status set to ".concat(statusText)+ " by user ").
                concat(AppController.activeUserPlaceHolder));
        NOTIFY_ENTITY.setLogged_by(currentUserId);
        NOTIFY_ENTITY.setTitle("CUSTOMER BIO DATA UPDATE");
        NOTIFY_ENTITY.setSender_method("SMS and EMAIL");
        logNotification(NOTIFY_ENTITY);
    }
    void statusToggleButtonImplementation() {
        String accountNo = accountNumberLabel.getText();
        if (statusToggleButton.isSelected()) {
            updateCustomerAccountData(accountNo, "active");
        } else {
            updateCustomerAccountData(accountNo, "closed");
        }
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
            if (!event.getCharacter().matches("[0-9]")) {
                c_numberField.deletePreviousChar();
            }
            if (c_numberField.getText().length() >= 11) {
                c_numberField.deletePreviousChar();
            }
        });

        institutionNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
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


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/

    void actionEventMethodsImplementation() {
        statusToggleButton.setOnAction(actionEvent -> {
            statusToggleButton.setText( statusToggleButton.isSelected() ? "Enable Account" : "Close Account");
    });

        editCheckBox.setOnAction(event -> {
            applicantPane.setDisable(!isEditButtonChecked());
            filePane.setDisable(!isEditButtonChecked());
            contactPersonPane.setDisable(!isEditButtonChecked());
        });

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
            cancelButton.getScene().getWindow().hide();
        });

        scrollPane.setOnMouseMoved(mouseEvent -> {
            String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
            saveButton.setDisable(isFirstNameEmpty() || isLastNameEmpty() || isGenderEmpty() || isDobEmpty()
                    || isPlaceOfBirthEmpty() || isMobileNumberEmpty() || isDigitalAddressEmpty() || isResidentialAddressEmpty()||
                    isLandMarkEmpty() || isMaritalStatusEmpty() || isIdTypeEmpty() || isIdNumberEmpty() || isEducationalStatusEmpty() ||
                    isFullNameEmpty() || isGurantorDobEmpty() || isGurantorNumberEmpty() || isGurantorGenderEmpty() ||
                    isGurantorLandmarkEmpty() || isGurantorDigitalAddressEmpty() || isGurantorIdTypeEmpty() || isGurantorIdNumberEmpty() ||
                    isGurantorRelationshipTypeEmpty() || customerEmailAddressField.getStyle().equals(invalid) ||
                    !isEditButtonChecked()
            );
            if (isAttachFileButtonSelected()) {
                saveButton.setDisable(isFileNameFieldEmpty() || isReasonFieldEmpty());
            }
//            sendNotificationButton.setDisable(isSaveButtonEnabled());
            previewItemButton.setDisable(isFileNameFieldEmpty());


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

            ALERTS = new UserAlerts("UPDATE ACCOUNT", "ARE YOU SURE YOU WANT TO UPDATE CUSTOMER ACCOUNT DETAILS?", "please confirm your action to create this account else CANCEL to abort process.");
            if (ALERTS.confirmationAlert()) {
                if (isAttachFileButtonSelected()) {
                    try {
                        documentRepository.setCustomer_id(selectedCustomerId);
                        documentRepository.setDocument_name(fileNameField.getText());
                        documentRepository.setFile_content(readFileContent(absoluteFilePathLabel.getText()));
                        documentRepository.setReason_for_upload(reasonField.getText());
                        documentRepository.setUploaded_by(currentUserId);
                        saveDocument(documentRepository);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                customersDataRepository.setFirstname(firstname);
                customersDataRepository.setLastname(lastname);
                customersDataRepository.setOthername(otherName);
                customersDataRepository.setDob(Date.valueOf(customerDob));
                customersDataRepository.setAge(age);
                customersDataRepository.setPlace_of_birth(placeOfBirth);
                customersDataRepository.setMobile_number(mobileNumber);
                customersDataRepository.setOther_number(otherNumber);
                customersDataRepository.setEmail(email);
                customersDataRepository.setGender(gender);
                customersDataRepository.setDigital_address(digitalAddress);
                customersDataRepository.setResidential_address(residentialAddress);
                customersDataRepository.setKey_landmark(landmark);
                customersDataRepository.setMarital_status(maritalStatus);
                customersDataRepository.setName_of_spouse(spouseName);
                customersDataRepository.setId_type(idType);
                customersDataRepository.setId_number(idNumber);
                customersDataRepository.setEducational_background(qualification);
                customersDataRepository.setAdditional_comment(comments);
                customersDataRepository.setContact_person_fullname(fullname);
                customersDataRepository.setContact_person_dob(Date.valueOf(c_dob));
                customersDataRepository.setContact_person_gender(c_gender);
                customersDataRepository.setContact_person_number(c_mobileNumber);
                customersDataRepository.setContact_person_landmark(c_landMark);
                customersDataRepository.setContact_person_education_level(c_qualification);
                customersDataRepository.setContact_person_digital_address(c_digitalAddress);
                customersDataRepository.setContact_person_id_type(c_idType);
                customersDataRepository.setContact_person_id_number(c_idNumber);
                customersDataRepository.setContact_person_place_of_work(placeOfWOrk);
                customersDataRepository.setInstitution_address(institutionAddress);
                customersDataRepository.setInstitution_number(institutionNumber);
                customersDataRepository.setModified_by(currentUserId);
                customersDataRepository.setRelationship_to_applicant(relationshipType);
                customersDataRepository.setAccount_Id(selectedCustomerId);
                int flag = updateCustomerData(customersDataRepository);
                statusToggleButtonImplementation();
                if(flag > 0) {
                    NOTIFICATION.successNotification("DATA UPDATE", "You have successfully updated customer's bio data.");
                    updateAccountType(accountType, accountNumber);
                    String[] placeHolders = {firstname.concat(" " + lastname), accountType, accountNumber };
                    String message = new MessageBuilders().updateCustomerData(List.of(placeHolders));
                    String responseStatus = "";
                    String result = "";
                    try {
                        responseStatus = SMS.sendSms(mobileNumber, message);
                        result = MessageStatus.getMessageStatusResult(responseStatus).toString();
                        MESSAGE_LOGS.setMessage(message);
                        MESSAGE_LOGS.setRecipient(mobileNumber);
                        MESSAGE_LOGS.setStatus(result);
                        MESSAGE_LOGS.setTitle("CUSTOMER BIO DATA UPDATE");
                        MESSAGE_LOGS.setSent_by(currentUserId);
                        MESSAGE_MODEL.logNotificationMessages(MESSAGE_LOGS);
                        new EmailAPI(email, "CUSTOMER BIO DATA UPDATE", message, "Please contact/call or walk by our office if you did not authorize this process.").sendEmail();
                        notificationLogger(accountNumber);
                    } catch (IOException | RuntimeException e) {
                        notificationLogger(accountNumber);
                        MESSAGE_LOGS.setMessage(message);
                        MESSAGE_LOGS.setRecipient(mobileNumber);
                        MESSAGE_LOGS.setStatus(result);
                        MESSAGE_LOGS.setTitle("CUSTOMER BIO DATA UPDATE");
                        MESSAGE_LOGS.setSent_by(currentUserId);
                        MESSAGE_MODEL.logNotificationMessages(MESSAGE_LOGS);

                        String className = this.getClass().getName();
                        String error = Arrays.toString(e.getStackTrace());
                        errorLogger.logMessage(className, "actionEventMethodsImplementation", error);
                    }
                } else {
                    NOTIFICATION.errorNotification("UPDATE FAILED", "Failed to update customer bio data");
                }
            }//end of confirmation checker...
        });

        uploadButton.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Your File");
            FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
            FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");

            fileChooser.getExtensionFilters().addAll(pdfFilter, jpgFilter, pngFilter);
            File selectedFile =  fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

            //check if the user SELECTED A FILE
            if (!(selectedFile == null)){
                fileNameField.setText(selectedFile.getName());
                absoluteFilePathLabel.setText(selectedFile.getPath());
            } else {
                NOTIFICATION.informationNotification("EMPTY SELECTION ", "You made no selection after clicking the upload button.");
            }

        });

        previewItemButton.setOnAction(event -> {
            if (!absoluteFilePathLabel.getText().isEmpty()) {
                String filePathString = absoluteFilePathLabel.getText();
                try {
                    Desktop.getDesktop().browse(new File(filePathString).toURI());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        attachFileButton.setOnAction(action -> {
           fileNameField.setDisable(!isAttachFileButtonSelected());
           reasonField.setDisable(!isAttachFileButtonSelected());
           uploadButton.setDisable(!isAttachFileButtonSelected());
        });
    }//end of action event methods implementation




}//end of class...
