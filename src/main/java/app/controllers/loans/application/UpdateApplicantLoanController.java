package app.controllers.loans.application;

import app.alerts.UserAlerts;
import app.documents.ImageReadWriter;
import app.models.MainModel;
import app.models.loans.LoansModel;
import app.repositories.accounts.CustomersDataRepository;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javax.imageio.stream.ImageInputStream;
import javax.swing.text.html.HTMLDocument;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateApplicantLoanController extends LoansModel implements Initializable {
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

    public static String setLoanNumber;
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

        loadFields();
    }

    /*******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/


    /*******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    private void loadFields() {
        Map<String, Object> data = getLoanApplicantDataByLoanNumber(loanNumberLabel.getText());
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
        landmarkField.setText(data.getOrDefault("key_landmark", null).toString());
        qualificationSelector.setValue(data.getOrDefault("educational_background", null).toString());
        idNumberField.setText(data.getOrDefault("id_number", null).toString());
        idTypeSelector.setValue(data.getOrDefault("id_type", null).toString());
        maritalStatusSelector.setValue(data.getOrDefault("marital_status", null).toString());
        contactPersonNameField.setText(data.getOrDefault("contactPersonName", null).toString());
        companyNameField.setText(data.getOrDefault("company_name", null).toString());
        companyNumberField.setText(data.getOrDefault("company_number", null).toString());
        companyAddressField.setText(data.getOrDefault("company_address", null).toString());
        staffIdField.setText(data.getOrDefault("staff_id", null).toString());
        occupationField.setText(data.getOrDefault("occupation", null).toString());
        employmentDatePicker.setValue(LocalDate.parse(data.get("employment_date").toString()));
        applicantBasicSalaryField.setText(data.get("basic_salary").toString());
        applicantGrossSalaryField.setText(data.get("gross_salary").toString());
        applicantNetSalaryField.setText(data.get("net_salary").toString());
        applicantTotalDeductionField.setText(data.get("total_deduction").toString());

    }

    /*******************************************************************************************************************
        IMPLEMENTATION OF KEY EVENT METHODS.
     *******************************************************************************************************************/


    public void validateRequestAmountInput(KeyEvent keyEvent) {

    }

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

    final AtomicInteger AGENT_ID = new AtomicInteger(0);
    @FXML void agentSelected() {
        var var1 = agentSelector.getValue();
        getAllAgents().forEach(x -> {if(Objects.equals(var1, x.getAgentName())) {AGENT_ID.set(x.getAgentId());}});
    }




    @FXML
    void saveButtonOnAction() {

    }

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
