package app.controllers.loans.application;

import app.models.MainModel;
import app.models.loans.LoansModel;
import app.specialmethods.SpecialMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import javax.swing.text.html.HTMLDocument;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateApplicantLoanController extends LoansModel implements Initializable {
    @FXML
    private Label loanNumberLabel;
    @FXML
    private ComboBox<String> loanTypeSelector, genderSelector;
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

    public static String setLoanNumber;
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
    }


    /*******************************************************************************************************************
        IMPLEMENTATION OF ACTION EVENT METHODS
     *******************************************************************************************************************/

    public void validateRequestAmountInput(KeyEvent keyEvent) {

    }

    public void checkCustomerIdField(KeyEvent keyEvent) {
    }
}
