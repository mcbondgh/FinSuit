package app.controllers.loans.application;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.loans.LoansModel;
import app.repositories.accounts.CustomersDataRepository;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoanApplicationController extends LoansModel implements Initializable {

    UserNotification NOTIFY = new UserNotification();
    UserAlerts ALERT;

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
    @FXML private ComboBox<String> customerTypeSelector, loanTypeSelector, draftSelector;
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
    @FXML private TextField guranterNameField, guranterNumberField, guranterDigitalAddressField, guranterAddressField;
    @FXML private  TextField guranterIdNumberField, guranterOccupationField, guranterPlaceOfWorkField, guranterWorkAddressField, guranterNetSalaryField;
    @FXML private TextField applicantBasicSalaryField, applicantTotalDeductionField, applicantNetSalaryField, applicantGrossSalaryField;
    @FXML private TextField staffIdField;
    @FXML private ComboBox<String> maritalStatusSelector, qualificationSelector, idTypeSelector, genderSelector;
    @FXML private ComboBox<String> relationshipTypeSelector, contactPersonIdTypeSelector;
    @FXML private ComboBox<String> guranterIdTypeSelector, guranterRelationshipTypeSelector;
    @FXML private MFXScrollPane scrollPane;
    @FXML private DatePicker dobPicker, employmentDatePicker;
    @FXML private Pane personalInformationPane;
    private File imageFile = null;
    final long standardImageSize = (300 * 1024);

    //Default constructor
    public LoanApplicationController() throws IOException {}


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    boolean checkImageSize(long imageSize) {
        return imageSize > standardImageSize;
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
    boolean isGuarantorAddressEmpty() {return guranterAddressField.getText().isEmpty();}
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


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.***********************************
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] customerTypes = {"New Client", "Existing Client"};
        customerTypeSelector.setValue(customerTypes[0]);
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
        populateFields();

    }

     private String getCustomerIdOrIdType() {

        return "";
    }

    private InputStream getImageStream() {
        InputStream imageStream = null;
        try {
            imageStream = new FileInputStream(imageFile);
        }catch (FileNotFoundException ignore){}
       return imageStream;
    }

    private void populateFields() {
        getDrafts(loanCountLabel, draftSelector);
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
        lastNameField.setOnKeyTyped(keyEvent -> {
            draftButton.setDisable(isFirstNameEmpty() || isLastNameEmpty() || isLoanTypeEmpty() || isLoanAmountFieldEmpty());
        });
        firstNameField.setOnKeyTyped(keyEvent -> {
            draftButton.setDisable(isFirstNameEmpty() || isLastNameEmpty() || isLoanTypeEmpty() || isLoanAmountFieldEmpty());
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
                    || isContactGenderEmpty() || isGuarantorGenderEmpty()
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
        for (CustomersDataRepository data : fetchCustomersData()) {
            if (Objects.equals(value, data.getId_number()) && customerTypeSelector.getValue().equals("New Client")) {
                String name = data.getLastname().toUpperCase() + " " + data.getFirstname().toUpperCase();
                ALERT = new UserAlerts("EXISTING CUSTOMER ID", "Sorry anther customer owns this ID Number bearing the name '" + name + "'","you cannot register a customer with the same Id Number, please provide a unique id number.");
                ALERT.warningAlert();
                idNumberField.clear();
            }
        }

    }
    @FXML void filterCustomerOnAction() {
        String value = filterIdOrAccountNo.getValue().toString();
        int loanCount = countTotalLoans(value);
        loanCountLabel.setText(String.valueOf(loanCount));
    }
    @FXML void loadDraftVariablesOnAction() {

    }

    @FXML private void draftButtonClicked() {

    }
    @FXML private void saveButtonClicked() {
        int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);

        String loanNumber = loanNumberLabel.getText();
        String loanType = loanTypeSelector.getValue();
        double loanAmount = Double.parseDouble(loanRequestField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String otherName = otherNameField.getText();
        LocalDate dateOfBirth = dobPicker.getValue();
        String mobileNumber = mobileNumberField.getText();
        String otherNumber = otherNumberField.getText();
        String address = digitalAddressField.getText();
        String email = emailField.getText();
        String residentialAddress = residentialAddressField.getText();
        String landMark = landmarkField.getText();
        String education = qualificationSelector.getValue();
        String idType = idTypeSelector.getValue();
        String idNumber = idNumberField.getText();
        String maritalStatus = maritalStatusSelector.getValue();

        String companyName = companyNameField.getText();
        String companyContact = companyNumberField.getText();
        String digitalAddress = digitalAddressField.getText();
        String staffId = staffIdField.getText();
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
        String contactResidential = contactPersonResidentialField.getText();
        String contactRelationshipType = relationshipTypeSelector.getValue();
        String contactIdType = contactPersonIdTypeSelector.getValue();
        String contactIdNumber = contactPersonIdNoField.getText();
        String placeOfWork = placeOfWorkField.getText();
        String institutionAddress = institutionAddressField.getText();

        String guarantorName = guranterNameField.getText();
        String guarantorNumber = guranterNumberField.getText();
        String guarantorDigitalAdd = guranterDigitalAddressField.getText();
        String guarantorResidentialAdd = guranterAddressField.getText();
        String guarantorIdType = guranterIdTypeSelector.getValue();
        String guarantorIdNumber = guranterIdNumberField.getText();
        String guarantorRelationship = guranterRelationshipTypeSelector.getValue();
        String guarantorOccupation = guranterOccupationField.getText();
        String guarantorPlaceOfWork = guranterPlaceOfWorkField.getText();
        String guarantorInstitutionAdd = guranterWorkAddressField.getText();
        String guarantorNetSalary = guranterNetSalaryField.getText();
        int age = LocalDate.now().getYear() - dobPicker.getValue().getYear();

        ALERT = new UserAlerts("LOAN APPLICATION", "You have initiated a loan request for '" + firstName.toUpperCase() +" " + lastName.toUpperCase() +"', do you wish to apply now?", "please confirm your decision to apply else CANCEL to abort.");
        if (ALERT.confirmationAlert()) {

        }



    }






}//end of class...
