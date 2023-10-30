package app.documents;

import app.models.MainModel;
import app.repositories.BusinessInfoEntity;
import app.repositories.documents.ReceiptsEntity;
import app.repositories.loans.ScheduleTableValues;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import javafx.scene.control.TableView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DocumentGenerator {

    MainModel DAO = new MainModel();
    
    /*
    THE PURPOSE OF THIS CLASS IS TO ENABLE US GENERATE ALL REQUIRED DOCUMENT FORMAT ie excel, word, ppt and pdf FOR
    OUR APPLICATION. WE ARE GOING TO DEFILE CUSTOM METHODS THAT WOULD REPRESENT EVERY DOCUMENT BASED ON ITS
    FUNCTIONALITY. ie receipts, reports, excel files AND OTHER RELATED DOCUMENTS.
     */

    Div documentHeader() {
        Div container = new Div();
        //GET BUSINESS CREDENTIALS FOR THE LETTER HEAD.
        String businessName = "";
        String mobileNumbers = "";
        String email = "";
        String digitalAddress = "";

        for (BusinessInfoEntity data : DAO.getBusinessInfo()) {
            businessName = data.getName();
            mobileNumbers = data.getMobileNumber() + " | ".concat(data.getOtherNumber());
            email = data.getEmail();
            digitalAddress = data.getDigital();
        }
        //CREATE A PARAGRAPH TO HOLD THE VARIOUS TEXTS IN THE LETTER HEAD...
        Paragraph businessNameText = new Paragraph(businessName)
                .setFontSize(14).setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph mobileNumbersText = new Paragraph(mobileNumbers)
                .setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph addressText = new Paragraph(email.concat(" | ".concat(digitalAddress)))
                .setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER);
        container.add(businessNameText).add(addressText).add(mobileNumbersText)
                .setTextAlignment(TextAlignment.CENTER).setMargins(0, 0, 2, 0);
        return container;
    }

    /*******************************************************************************************************************
     *************************************** DEPOSIT RECEIPT CREATION API *************************************************
     ******************************************************************************************************************/


    public void generateDepositReceipt(String documentName, ReceiptsEntity receiptsEntity) {
        try {
            // CREATE A pdf document template that serves as the document environment to hold content...
            // This is equivalent to having an empty document space or white space...
            File receiptFolder = new File(createAccountOpeningFolderIfNotExists().getPath() + File.separator + "receipts\\");
            if (!receiptFolder.exists()) {
                receiptFolder.mkdir();
            }
            File pdfFile = new File(receiptFolder, documentName);
            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            //CREATE A DOCUMENT FOR THE PDF FILE.
            Document document = new Document(pdfDocument, PageSize.A4);
            Table table = new Table(2).setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setFontSize(10).setPadding(6f)
                    .useAllAvailableWidth();

            Div receiptBodyContainer = new Div();

            Paragraph combinedParagraph = new Paragraph();
            Paragraph labelNames;
            Paragraph dataNames;
            DottedLine dottedLine = new DottedLine();
            dottedLine.setLineWidth(1);
            dottedLine.setGap(5);

            labelNames = new Paragraph("TRANSACTION NO: ").setBold().setFontSize(10);
            dataNames = new Paragraph(receiptsEntity.getTransactionNumber()).setFontSize(10);
            combinedParagraph.add(labelNames).add(dataNames);

            labelNames = new Paragraph("  DATE: ").setBold().setFontSize(10);
            dataNames = new Paragraph(receiptsEntity.getTransactionDate()).setFontSize(10);
            combinedParagraph.add(labelNames.setMarginLeft(22f)).add(dataNames.setMarginRight(40f));
            combinedParagraph.setTextAlignment(TextAlignment.CENTER);

            combinedParagraph.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
            Div receiptContentContainer = new Div();
            receiptContentContainer.add(new Paragraph("CUSTOMER NAME: ".concat(receiptsEntity.getCustomerName())));
            receiptContentContainer.add(new Paragraph("ACCOUNT NUMBER: ".concat(receiptsEntity.getAccountNumber())));
            receiptContentContainer.add(new Paragraph("TRANSACTION TYPE: ".concat(receiptsEntity.getTransactionType())));
            receiptContentContainer.add(new Paragraph("DEPOSIT AMOUNT: Ghc".concat(receiptsEntity.getAmount())));
            receiptContentContainer.add(new Paragraph("PAYMENT METHOD: ".concat(receiptsEntity.getPaymentMethod())));
            receiptContentContainer.add(new Paragraph("DEPOSITOR'S NAME: ".concat(receiptsEntity.getDepositorName())));
            receiptContentContainer .add(new Paragraph("DEPOSITOR'S ID NO: ".concat(receiptsEntity.getDepositorIdNumber())));
            receiptContentContainer.add(new Paragraph("CASHIER'S NAME: ".concat(receiptsEntity.getCashierName())));
            receiptContentContainer.add(new Paragraph("-----------------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER));
            receiptContentContainer.add(new Paragraph("THANK YOU DEAR CUSTOMER, WE APPRECIATE YOU!!!").setFontSize(6).setMarginTop(5).setTextAlignment(TextAlignment.CENTER));
            receiptContentContainer.setMarginLeft(32f);

            receiptBodyContainer.add(combinedParagraph).add(receiptContentContainer);
            document.add(documentHeader()).add(receiptBodyContainer);
            document.close();

        } catch (Exception ignore) {
        }
    }


    /*******************************************************************************************************************
        *************************************** ACCOUNT CREATION API *************************************************
     ******************************************************************************************************************/
    public File createAccountOpeningFolderIfNotExists() {
        File path = new File(System.getProperty("user.home") + File.separator + "Desktop");
        String createFolder = path.getPath() + File.separator + "Finsuit Document\\";
        File directory = new File(createFolder) ;
        if (!directory.exists()){
            directory.mkdir();
        }
        return directory;
    }
    public void generateNewAccountFile(String documentName, String fullName, String mobile, String accountType, String accountNumber, String initialDeposit, String email, String officerName, String digitalAddress ) {
        try {
            File newAccountsPath = new File(createAccountOpeningFolderIfNotExists().getPath() + File.separator + "new accounts\\");
            if (!newAccountsPath.exists()) {
                newAccountsPath.mkdir();
            }
            File file = new File(newAccountsPath, documentName);
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument, PageSize.A4);

            //CREATE TABLE OF TWO COLUMNS
            Table table = new Table(2).useAllAvailableWidth();
            table.setMarginTop(10f);
            Cell tableHeader = new Cell(0, 2).add(new Paragraph("CUSTOMER ACCOUNT SUMMARY SHEET").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER).setPadding(5f));
            Cell rowOneTile = new Cell().add(new Paragraph("CUSTOMER FULL NAME")); Cell rowOneData = new Cell().add(new Paragraph(fullName));
            Cell rowTowTitle = new Cell().add(new Paragraph("ACCOUNT NUMBER "));Cell rowTwoDats = new Cell().add(new Paragraph(accountNumber));
            Cell rowThreeTitle = new Cell().add(new Paragraph("ACCOUNT TYPE")); Cell rowThreeData = new Cell().add(new Paragraph(accountType));
            Cell rowFourTitle = new Cell().add(new Paragraph("INITIAL DEPOSIT")); Cell rowFourData = new Cell().add(new Paragraph(initialDeposit));
            Cell rowFiveTitle = new Cell().add(new Paragraph("MOBILE NUMBER")); Cell rowFiveData = new Cell().add(new Paragraph(mobile));
            Cell rowSixTitle = new Cell().add(new Paragraph("EMAIL ADDRESS")); Cell rowSixData = new Cell().add(new Paragraph(email));
            Cell rowSevenTitle = new Cell().add(new Paragraph("DIGITAL ADDRESS")); Cell rowSevenData = new Cell().add(new Paragraph(digitalAddress));
            Cell rowEightTitle = new Cell().add(new Paragraph("REGISTRATION OFFICER")); Cell rowEightData = new Cell().add(new Paragraph(officerName));
            Cell lastCell = new Cell(0, 2).add(new Paragraph("You are welcome to our family.").setFontSize(6)).setTextAlignment(TextAlignment.CENTER);

            table.setFontSize(12);
            table.addCell(tableHeader);
            table.addCell(rowOneTile).addCell(rowOneData);//row 1
            table.addCell(rowTowTitle).addCell(rowTwoDats);// row 2
            table.addCell(rowThreeTitle).addCell(rowThreeData); // row 3
            table.addCell(rowFourTitle).addCell(rowFourData); // row 4
            table.addCell(rowFiveTitle).addCell(rowFiveData); // row 5
            table.addCell(rowSixTitle).addCell(rowSixData); // row 6
            table.addCell(rowSevenTitle).addCell(rowSevenData); // row 7
            table.addCell(rowEightTitle).addCell(rowEightData); //row 8
            table.addCell(lastCell);

            document.add(documentHeader()).add(table);
            document.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void exportScheduleSheet(String documentName, TableView<ScheduleTableValues> tableView) {
        try {

            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(documentName);

            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontName("roboto");

            CellStyle sheetHeaderStyle = workbook.createCellStyle();
            sheetHeaderStyle.setFont(font);
            sheetHeaderStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            sheetHeaderStyle.setBorderBottom(BorderStyle.DASHED);

            //Lets create the header rows for the table cells with their various names.
            Row headerRow = sheet.createRow(0);
            headerRow.setRowStyle(sheetHeaderStyle);
            headerRow.createCell(0).setCellValue("NO.");
            headerRow.createCell(1).setCellValue("MONTHLY INSTALLMENT.");
            headerRow.createCell(2).setCellValue("PRINCIPAL AMOUNT.");
            headerRow.createCell(3).setCellValue("INTEREST AMOUNT.");
            headerRow.createCell(4).setCellValue("PAYMENT DATE.");
            headerRow.createCell(5).setCellValue("BALANCE");

            //Lets iterate through the table and get the size of the table for the excel sheet.
            int tableSize = tableView.getItems().size();
            for (int i = 0; i < tableSize; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(tableView.getItems().get(i).getIndex());
                row.createCell(1).setCellValue(tableView.getItems().get(i).getMonthlyInstallment());
                row.createCell(2).setCellValue(tableView.getItems().get(i).getPrincipal());
                row.createCell(3).setCellValue(tableView.getItems().get(i).getInterestAmount());
                row.createCell(4).setCellValue(tableView.getItems().get(i).getFormattedScheduleDate());
                row.createCell(5).setCellValue(tableView.getItems().get(i).getBalance());
            }

            File directoryPath = new File(createAccountOpeningFolderIfNotExists().getPath() + File.separator + "loan schedules" + File.separator);
            if (!directoryPath.exists()) {
                directoryPath.mkdir();
            }
            File file = new File(directoryPath, documentName.concat(".xlsx"));
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void exportScheduleAsPdf(String documentName, TableView<ScheduleTableValues> tableView) {
        try {
            File newAccountsPath = new File(createAccountOpeningFolderIfNotExists().getPath() + File.separator + "loan schedules" + File.separator);
            if (!newAccountsPath.exists()) {
                newAccountsPath.mkdir();
            }
            File file = new File(newAccountsPath, documentName.concat(".pdf"));
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument, PageSize.A4);

            Table table = new Table(6).useAllAvailableWidth();
            table.addCell(new Cell(0, 6).add(new Paragraph("YOUR LOAN PAYMENT SCHEDULE ").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().add(new Paragraph("NO").setFontSize(10).setBold()));
            table.addCell(new Cell().add(new Paragraph("MONTHLY INSTALLMENT").setBold().setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph("PRINCIPAL").setFontSize(10).setBold()));
            table.addCell(new Cell().add(new Paragraph("INTEREST AMOUNT").setBold().setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph("PAYMENT DATE").setFontSize(10).setBold()));
            table.addCell(new Cell().add(new Paragraph("BALANCE").setFontSize(10).setBold()));

            for (ScheduleTableValues items : tableView.getItems()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(items.getIndex()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(items.getMonthlyInstallment()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(items.getPrincipal()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(items.getInterestAmount()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(items.getScheduleDate()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(items.getBalance()))));
            }
            document.add(documentHeader()).add(table);
            document.close();
        }catch (Exception e) {e.printStackTrace();}


    }


}// end of class...
