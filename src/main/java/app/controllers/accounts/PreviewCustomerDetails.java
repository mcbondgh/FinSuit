package app.controllers.accounts;

import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.repositories.BusinessInfoEntity;
import app.models.MainModel;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.TextAlignment;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
            }
            String fileName = applicantFullnameLabel.getText() + LocalDate.now() + ".pdf";
            String documentPath = directory + File.separator + fileName;

            createMiniDocument(documentPath);
            NOTIFICATION.successNotification("Export Successful", "Document Successfully Exported to desktop.");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    String getFullName() {
        String username = AppController.activeUserPlaceHolder;
        String emp_id = MODEL_OBJ.getEmployeeIdByUsername(username);
        return MODEL_OBJ.getEmployeeFullNameByWorkId(emp_id);
    }

    // Create a Document object
     @NotNull
     private void createMiniDocument(String fileName) throws IOException, SQLException {
        // Create a new Word document
         PdfWriter pdfWriter = new PdfWriter(fileName);
         PdfDocument pdfDocument = new PdfDocument(pdfWriter);

         String businessName = "";
         String mobileNumber = "";
         String otherNumber = "";
         String email = "";
         String digitalAddress = "";

         for (BusinessInfoEntity info :MODEL_OBJ.getBusinessInfo()) {
             businessName = info.getName();
             mobileNumber = info.getMobileNumber();
             otherNumber = info.getOtherNumber();
             email = info.getEmail();
             digitalAddress = info.getDigital();
         }

         //SET UP THE PAGE CONTENT AND CREATE THE TABLE WITH TWO COLUMNS.
         Document page = new Document(pdfDocument, PageSize.A4);
         Table documentTable = new Table(2).useAllAvailableWidth();
         Div headerContainer = new Div();


         //CREATE HEADER WITH LETTER HEAD
         Paragraph letterHead = new Paragraph(businessName)
            .setFontColor(ColorConstants.BLACK)
            .setFontSize(18)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER).setPaddingTop(4);

         Paragraph numberHead = new Paragraph(mobileNumber + " | " + otherNumber)
                 .setFontColor(ColorConstants.BLACK).setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER)
                 .setMarginBottom(20);

         Paragraph addressHead = new Paragraph(email + " | " + digitalAddress)
                 .setFontSize(12).setBold().setFontColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER);
         headerContainer.add(letterHead);
         headerContainer.add(addressHead);
         headerContainer.add(numberHead);
         headerContainer.setBackgroundColor(new DeviceRgb( 254, 245, 345), 10);
         headerContainer.setBorderRadius(new BorderRadius(2)).setMarginBottom(5);
         page.add(headerContainer);

         //parse the value and table data as key value pairs
         Map<String, String> tableContent = new HashMap<>();
         tableContent.put("MOBILE NUMBER", contactMobileNumber);
         tableContent.put("ACCOUNT NUMBER", applicantAccountNumber);
         tableContent.put("REGISTRATION OFFICER", getFullName());
         tableContent.put("ACCOUNT TYPE", applicantAccountType);
         tableContent.put("FULL NAME", applicantFullName);
         tableContent.put("INITIAL DEPOSIT", applicantDepositAmount.toString());
         tableContent.put("EMAIL ADDRESS", applicantEmail);

         //populate table
         for(Map.Entry<String, String> items : tableContent.entrySet()) {
            Cell titleCell = new Cell().add(new Paragraph(items.getKey())).setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
            Cell contentCell = new Cell().add(new Paragraph(items.getValue()))
                    .setTextAlignment(TextAlignment.CENTER).setPadding(5);
            documentTable.addCell(titleCell);
            documentTable.addCell(contentCell);
         }
         page.add(documentTable);
        page.close();
     }







}//end of class..
