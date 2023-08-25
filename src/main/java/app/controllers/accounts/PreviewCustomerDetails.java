package app.controllers.accounts;

import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.fetchedData.BusinessInfoObject;
import app.models.MainModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class PreviewCustomerDetails implements Initializable {

    MainModel MODEL_OBJ = new MainModel();
    UserNotification NOTIFICATION = new UserNotification();

    @FXML // fx:id="accountNumberLabel"
    private Label accountNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="accountTypeLabel"
    private Label accountTypeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ageLabel"
    private Label ageLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantEmailLabel"
    private Label applicantEmailLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantDigitalAddressLabel"
    private Label applicantDigitalAddressLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantDobLabel"
    private Label applicantDobLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantFullnameLabel"
    private Label applicantFullnameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantGenderLabel"
    private Label applicantGenderLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantIdNumberLabel"
    private Label applicantIdNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantIdTypeLabel"
    private Label applicantIdTypeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantLandmarkLabel"
    private Label applicantLandmarkLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantNumberLabel"
    private Label applicantNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantOtherNumberLabel"
    private Label applicantOtherNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantPlaceOfBirthLabel"
    private Label applicantPlaceOfBirthLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantQualificationLabel"
    private Label applicantQualificationLabel; // Value injected by FXMLLoader

    @FXML // fx:id="applicantResidentialAddressLabel"
    private Label applicantResidentialAddressLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_dateOfBirthLabel"
    private Label c_dateOfBirthLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_digitalAddressLabel"
    private Label c_digitalAddressLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_educationLevel"
    private Label c_educationLevel; // Value injected by FXMLLoader

    @FXML // fx:id="c_fullnameLabel"
    private Label c_fullnameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_genderLabel"
    private Label c_genderLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_idTypeLabel"
    private Label c_idTypeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_institutionAddress"
    private Label c_institutionAddress; // Value injected by FXMLLoader

    @FXML // fx:id="c_institutionNumber"
    private Label c_institutionNumber; // Value injected by FXMLLoader

    @FXML // fx:id="c_landmarkLabel"
    private Label c_landmarkLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_mobileNumberLabel"
    private Label c_mobileNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_placeOfWorkLabel"
    private Label c_placeOfWorkLabel; // Value injected by FXMLLoader

    @FXML // fx:id="c_relationshipLabel"
    private Label c_relationshipLabel; // Value injected by FXMLLoader

    @FXML // fx:id="exportButton"
    private MFXButton exportButton; // Value injected by FXMLLoader

    @FXML // fx:id="gripView"
    private GridPane gripView; // Value injected by FXMLLoader

    @FXML // fx:id="gripView1"
    private GridPane gridView2; // Value injected by FXMLLoader

    @FXML // fx:id="idNumberLabel"
    private Label idNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="initialDepositLabel"
    private Label initialDepositLabel; // Value injected by FXMLLoader

    @FXML // fx:id="maritalStatusLabel"
    private Label maritalStatusLabel; // Value injected by FXMLLoader

    @FXML // fx:id="spouseNameLabel"
    private Label spouseNameLabel; // Value injected by FXMLLoader

    @FXML
    private TextArea commentsField;


    //STATIC METHODS DECLARATION
    static String applicantAccountType, applicantAccountNumber, applicantFullName, applicantGender, applicantAge;
    static String applicantPlaceOfBirth, applicantNumber, applicantOtherNumber, applicantEmail, applicantDigitalAddress;
    static String applicantResidentialAddress, applicantLandmark, applicantMaritalStatus, applicantSpouseName;
    static String applicantIdType, applicantIdNumber, applicantEducationalBackground, applicantExtraInfo;
    static Double applicantDepositAmount;
    static String applicantDob;

    static String contactFullName, contactGender, contactMobileNumber, contactLandmark, contactEducationStatus, contactDigitalAddress;
    static String contactIdType, contactIdNumber, contactPlaceOfWork, contactInstitutionAddress, contactInstitutionNumber, relationshipType;
    static String contactDob;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountNumberLabel.setText(applicantAccountNumber);
        accountTypeLabel.setText(applicantAccountType);
        applicantFullnameLabel.setText(applicantFullName);
        applicantGenderLabel.setText(applicantGender);
        ageLabel.setText(applicantAge);
        applicantDobLabel.setText(applicantDob);
        applicantPlaceOfBirthLabel.setText(applicantPlaceOfBirth);
        applicantNumberLabel.setText(applicantNumber);
        applicantOtherNumberLabel.setText(applicantOtherNumber);
        applicantEmailLabel.setText(applicantEmail);
        applicantDigitalAddressLabel.setText(applicantDigitalAddress);
        applicantResidentialAddressLabel.setText(applicantResidentialAddress);
        applicantLandmarkLabel.setText(applicantLandmark);
        maritalStatusLabel.setText(applicantMaritalStatus);
        spouseNameLabel.setText(applicantSpouseName);
        applicantIdNumberLabel.setText(applicantIdNumber);
        applicantIdTypeLabel.setText(applicantIdType);
        applicantQualificationLabel.setText(applicantEducationalBackground);
        initialDepositLabel.setText(String.valueOf(applicantDepositAmount));
        commentsField.setText(applicantExtraInfo);
        c_fullnameLabel.setText(contactFullName);
        c_dateOfBirthLabel.setText(contactDob);
        c_mobileNumberLabel.setText(contactMobileNumber);
        c_genderLabel.setText(contactGender);
        c_landmarkLabel.setText(contactLandmark);
        c_educationLevel.setText(contactEducationStatus);
        c_digitalAddressLabel.setText(contactDigitalAddress);
        c_idTypeLabel.setText(contactIdType);
        idNumberLabel.setText(contactIdNumber);
        c_placeOfWorkLabel.setText(contactPlaceOfWork);
        c_institutionAddress.setText(contactInstitutionAddress);
        c_institutionNumber.setText(contactInstitutionNumber);
        c_relationshipLabel.setText(relationshipType);
    }

    @FXML
    void exportButtonClicked(ActionEvent event) {
        try {
            exportButton.setDisable(true);
            //CREATE FILE DIRECTORY ON DESKTOP TO SAVE THE DOCUMENT.
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
            String directoryPath = desktopPath + File.separator + "Finsuit Document";
            File directory = new File(directoryPath);

            //check if the directory exists, if true save file else create the folder and save the document within.
            if (!directory.exists()) {
                directory.mkdirs();
                return;
            }
            String fileName = applicantFullnameLabel.getText()+ ".pdf";
            String documentPath = directoryPath + File.separator + fileName;

            createMiniDocument().save(documentPath);
            createMiniDocument().close();
            Thread.sleep(3000);
            exportButton.setDisable(false);
            NOTIFICATION.successNotification("Export Successful", "Document Successfully Exported to desktop.");

        }catch (Exception e) {
            e.printStackTrace();
        }


    }


    String getFullName() {
        String username = AppController.activeUserPlaceHolder;
        String emp_id = MODEL_OBJ.getEmployeeIdByUsername(username);
        return MODEL_OBJ.getFullNameByUserId(emp_id);
    }

    // Create a Document object
     @NotNull
     private PDDocument createMiniDocument() throws IOException {
        // Create a new Word document
         PDDocument document = new PDDocument();
         PDPage documentPage = new PDPage();
         document.addPage(documentPage);



         //SETUP THE PAGE CONTENT
         PDPageContentStream pageHeader = new PDPageContentStream(document, documentPage);
         pageHeader.setLineWidth(18);

         pageHeader.close();


         String businessName = "";
         String mobileNumber = "";
         String otherNumber = "";
         String email = "";
         String digitalAddress = "";

         for (BusinessInfoObject info :MODEL_OBJ.getBusinessInfo()) {
             businessName = info.getName();
             mobileNumber = info.getMobileNumber();
             otherNumber = info.getOtherNumber();
             email = info.getEmail();
             digitalAddress = info.getDigital();
         }

         //parse the value and table data as key value pairs
         Map<String, String> tableContent = new HashMap<>();
         tableContent.put("FULL NAME", applicantFullnameLabel.getText());
         tableContent.put("ACCOUNT TYPE", applicantAccountType);
         tableContent.put("ACCOUNT NUMBER", applicantAccountNumber);
         tableContent.put("INITIAL DEPOSIT", applicantDepositAmount.toString());
         tableContent.put("EMAIL ADDRESS", applicantEmail);
         tableContent.put("MOBILE NUMBER", applicantFullName);
         tableContent.put("REGISTRATION OFFICER ID", getFullName());

         //populate table
         for (String mapKeys : tableContent.keySet()) {

         }
         return document;
    }





}//end of class..
