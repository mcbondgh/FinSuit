package app.documents;

import app.models.MainModel;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.documents.ReceiptsEntity;
import app.repositories.loans.CollectionSheetEntity;
import app.repositories.loans.LoanScheduleEntity;
import app.repositories.loans.ScheduleTableValues;
import app.repositories.transactions.TransactionsEntity;
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
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DocumentGenerator {

    MainModel DAO = new MainModel();

    public DocumentGenerator() {
    }
    
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
                .setFontSize(20).setBold().setTextAlignment(TextAlignment.CENTER).setMarginBottom(-5);
        Paragraph mobileNumbersText = new Paragraph(mobileNumbers)
                .setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER).setMarginTop(-5);
        Paragraph addressText = new Paragraph(email.concat(" | ".concat(digitalAddress)))
                .setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER);
        container.add(businessNameText).add(addressText).add(mobileNumbersText)
                .setTextAlignment(TextAlignment.CENTER).setMargins(0, 0, 2, 0);
        return container;
    }

    StringBuilder excelDocumentHeader() {
        StringBuilder builder = new StringBuilder();
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
        String addressText = email.concat(" | ").concat(digitalAddress);
        builder.append(businessName).append("\n").append(mobileNumbers).append("\n").append(addressText);


        return builder;
    }

    /*******************************************************************************************************************
     *************************************** DEPOSIT RECEIPT CREATION API *************************************************
     ******************************************************************************************************************/


    public void generateTransactionReceipt(String documentName, ReceiptsEntity receiptsEntity) {
        try {
            // CREATE A pdf document template that serves as the document environment to hold content...
            // This is equivalent to having an empty document space or white space...
            File receiptFolder = new File(createDirectoryIfNotExist().getPath() + File.separator + "receipts\\");
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
            receiptContentContainer.add(new Paragraph("PAID AMOUNT: Ghc".concat(receiptsEntity.getAmount())));
            receiptContentContainer.add(new Paragraph("PAYMENT METHOD: ".concat(receiptsEntity.getPaymentMethod())));
            receiptContentContainer.add(new Paragraph("PAYER'S NAME: ".concat(receiptsEntity.getDepositorName())));
            receiptContentContainer .add(new Paragraph("PAYER'S ID NO: ".concat(receiptsEntity.getDepositorIdNumber())));
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
    public File createDirectoryIfNotExist() {
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
            File newAccountsPath = new File(createDirectoryIfNotExist().getPath() + File.separator + "new accounts\\");
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

            File directoryPath = new File(createDirectoryIfNotExist().getPath() + File.separator + "loan schedules" + File.separator);
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

    public int exportLoanRepaymentSchedule(String docName, String loanNumber, TableView<LoanScheduleEntity> tableView) {
        int statusCode = 200;
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(docName);

            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontName("roboto");

            CellStyle sheetHeaderStyle = workbook.createCellStyle();
            sheetHeaderStyle.setFont(font);
            sheetHeaderStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);


            //Lets create the header rows for the table cells with their various names.
            Row headerRow = sheet.createRow(0);
            headerRow.setRowStyle(sheetHeaderStyle);
            headerRow.createCell(0).setCellValue("CUSTOMER NAME");
            headerRow.createCell(1).setCellValue(docName);
            headerRow.createCell(2).setCellValue("LOAN NUMBER");
            headerRow.createCell(3).setCellValue(loanNumber);

            //CREATE ROWS TO HOLD CUSTOMER'S AND LOAN NUMBER.
            Row tableHeaderRows = sheet.createRow(1);
            tableHeaderRows.createCell(0).setCellValue("INDEX");
            tableHeaderRows.createCell(1).setCellValue("INSTALLMENT");
            tableHeaderRows.createCell(2).setCellValue("PRINCIPAL");
            tableHeaderRows.createCell(3).setCellValue("INTEREST");
            tableHeaderRows.createCell(4).setCellValue("DUE DATE");
            tableHeaderRows.createCell(5).setCellValue("PENALTY");
            tableHeaderRows.createCell(6).setCellValue("PAID AMOUNT");
            tableHeaderRows.createCell(7).setCellValue("STATUS");

            //Lets iterate through the table and get the size of the table for the excel sheet.
            int tableSize = tableView.getItems().size();
            for (int i = 0; i < tableSize; i++) {
                Row row = sheet.createRow(2+i);
                row.createCell(0).setCellValue(tableView.getItems().get(i).getSchedule_id());
                row.createCell(1).setCellValue(tableView.getItems().get(i).getMonthly_installment());
                row.createCell(2).setCellValue(tableView.getItems().get(i).getPrincipal_amount());
                row.createCell(3).setCellValue(tableView.getItems().get(i).getInterest_amount());
                row.createCell(4).setCellValue(tableView.getItems().get(i).getPayment_date());
                row.createCell(5).setCellValue(tableView.getItems().get(i).getPenalty_amount());
                row.createCell(6).setCellValue(tableView.getItems().get(i).getMonthly_payment());
                row.createCell(7).setCellValue(tableView.getItems().get(i).getStatusLabel().getText());
            }

            File directoryPath = new File(createDirectoryIfNotExist().getPath() + File.separator + "loan schedules\\excel sheets" + File.separator);
            if (!directoryPath.exists()) {
                directoryPath.mkdir();
            }
            File file = new File(directoryPath, docName.concat(".xlsx"));
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

            workbook.close();
            return statusCode;
        }catch (Exception e){
            statusCode = 404;
            e.printStackTrace();};
        return statusCode;
    }

    public void exportScheduleAsPdf(String documentName, TableView<ScheduleTableValues> tableView, String totalLoanAmount) {
        try {
            File newAccountsPath = new File(createDirectoryIfNotExist().getPath() + File.separator + "loan schedules" + File.separator);
            if (!newAccountsPath.exists()) {
                newAccountsPath.mkdir();
            }
            File file = new File(newAccountsPath, documentName.concat(".pdf"));
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument, PageSize.A4);

            Table table = new Table(6).useAllAvailableWidth();
            table.addCell(new Cell(0, 6).add(new Paragraph("YOUR LOAN PAYMENT SCHEDULE ").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell(0, 4).add(new Paragraph("LOAN AMOUNT (PRINCIPAL + INTEREST)").setBold().setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell(0, 2).add(new Paragraph("Ghc".concat(totalLoanAmount)).setTextAlignment(TextAlignment.CENTER)));
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

    public void generateLoanRepaymentReceipt(String documentName, ReceiptsEntity receiptsEntity) {
        try{
            File receiptFolder = new File(createDirectoryIfNotExist().getPath() + File.separator + "receipts\\");
            if (!receiptFolder.exists()) {
                receiptFolder.mkdir();
            }
            File pdfFile = new File(receiptFolder, documentName.concat(".pdf"));
            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            String formattedDate = LocalDateTime.parse(receiptsEntity.getTransactionDate()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));

            //CREATE A DOCUMENT FOR THE PDF FILE.
            Document document = new Document(pdfDocument, PageSize.A4);

            Div paragraphContainer = new Div();

            Paragraph transactionIdLabel;
            Paragraph receiptDate;

            paragraphContainer.setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setMarginRight(19);
            transactionIdLabel = new Paragraph("TRANSACTION ID: ".concat(receiptsEntity.getTransactionNumber()));
            receiptDate = new Paragraph("DATE: ".concat(formattedDate));
            paragraphContainer.add(transactionIdLabel).add(receiptDate).setBold().setTextAlignment(TextAlignment.RIGHT);

            Paragraph receiptTitle = new Paragraph("OFFICIAL RECEIPT").setUnderline().setFontSize(20).setBold();
            Div secondContainer = new Div();
            secondContainer.add(receiptTitle).add(paragraphContainer).setTextAlignment(TextAlignment.CENTER);

            Div contentContainer = new Div();
            contentContainer.setFontSize(12);
            Paragraph customerName = new Paragraph("Customer Name:          ".concat(receiptsEntity.getCustomerName().toUpperCase()));
            Paragraph loanNumber = new Paragraph("Loan Number:              ".concat(receiptsEntity.getAccountNumber()));
            Paragraph transType = new Paragraph("Transaction Type:         ".concat(receiptsEntity.getTransactionType().toUpperCase()));
            Paragraph transStatus = new Paragraph("Transaction Status:      ".concat(receiptsEntity.getTransactionStatus().toUpperCase()));
            Paragraph amount = new Paragraph("Paid Amount:                Ghc".concat(receiptsEntity.getAmount()));
            Paragraph payMethod = new Paragraph("Payment Method:         ".concat(receiptsEntity.getPaymentMethod().toUpperCase()));
            Paragraph cashierName = new Paragraph("Cashier:                        ".concat(receiptsEntity.getCashierName().toUpperCase()));
            Paragraph dash = new Paragraph("-------------------------------").setMarginTop(20).setTextAlignment(TextAlignment.RIGHT).setMarginRight(40);
            Paragraph dash2 = new Paragraph("---------------------------------------------------------------------------------------------------------------------");
            contentContainer.add(dash2).add(customerName).add(loanNumber).add(transType).add(amount).add(transStatus).add(payMethod).add(cashierName).add(dash);
            contentContainer.setTextAlignment(TextAlignment.JUSTIFIED).setMarginLeft(40);
            document.add(documentHeader()).add(secondContainer).add(contentContainer);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int exportCollectionSheetAsExcel(String docName,  String collectionDate, TableView<CollectionSheetEntity> entityTableView) {
        int status = 200;
        try{
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(docName);

            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontName("Times");

            CellStyle sheetHeaderStyle = workbook.createCellStyle();
            sheetHeaderStyle.setFont(font);
            sheetHeaderStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            sheetHeaderStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

            //create rows for the various parts where data and headers are going to be populated
            Row letterReadRow = sheet.createRow(0);
            Row nameAndDateRow = sheet.createRow(2);
            Row nameAndDateRowData = sheet.createRow(3);
            Row tableHeaders = sheet.createRow(4);

            //Create cell mergers
            letterReadRow.getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 5));
            nameAndDateRow.getSheet().addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
            nameAndDateRow.getSheet().addMergedRegion(new CellRangeAddress(2, 2, 3, 5));
            nameAndDateRowData.getSheet().addMergedRegion(new CellRangeAddress(3, 3, 0, 2));
            nameAndDateRowData.getSheet().addMergedRegion(new CellRangeAddress(3, 3, 3, 5));

            //SET CELL VALUES
            letterReadRow.createCell(0).setCellValue(excelDocumentHeader().toString());
            nameAndDateRow.createCell(0).setCellValue("OFFICER'S NAME");
            nameAndDateRowData.createCell(0).setCellValue(docName);
            nameAndDateRow.createCell(3).setCellValue("COLLECTION DATE");
            nameAndDateRowData.createCell(3).setCellValue(collectionDate);

           int tableLength = entityTableView.getColumns().size();
            for (int i = 0; i < tableLength; i++) {
                tableHeaders.createCell(i).setCellValue(entityTableView.getColumns().get(i).getText());
            }

            //get table data and populate the cells...
            int tableSize = entityTableView.getItems().size();
            for (int i = 0; i < tableSize ; i++) {
                Row tableData = sheet.createRow(5 + i);
                tableData.createCell(0).setCellValue(entityTableView.getItems().get(i).getLoanId());
                tableData.createCell(1).setCellValue(entityTableView.getItems().get(i).getLoanNo());
                tableData.createCell(2).setCellValue(entityTableView.getItems().get(i).getCustomerName());
                tableData.createCell(3).setCellValue(entityTableView.getItems().get(i).getInstallmentAmount());
                tableData.createCell(4).setCellValue(entityTableView.getItems().get(i).getDueDateSelector().getValue());
            }

            File directoryPath = new File(createDirectoryIfNotExist().getPath() + File.separator + "Collection Sheets" + File.separator);
            if (!directoryPath.exists()) {
                directoryPath.mkdir();
            }

            File file = new File(directoryPath, docName+"_".concat(collectionDate).concat(".xlsx"));
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return status;
        }catch (Exception e){
            e.printStackTrace();
            status = 404;
        }
        return status;
    }

    public int generateTransactionLogsReport(TableView<TransactionsEntity> tableView){
        int tableSize = tableView.getItems().size();
        int visibleColumnSize = tableView.getVisibleLeafColumns().size();
        int status = 200;
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Transaction Report");

            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontName("Times");

            CellStyle sheetHeaderStyle = workbook.createCellStyle();
            sheetHeaderStyle.setFont(font);

            Row letterHead = sheet.createRow(0);
            letterHead.getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, visibleColumnSize-1));
            letterHead.createCell(0).setCellValue(excelDocumentHeader().toString());
            letterHead.getCell(0).getCellStyle().setWrapText(true);

            Row tableHeader = sheet.createRow(2);
            for (int x = 0; x < visibleColumnSize; x++) {
                tableHeader.createCell(x).setCellValue(tableView.getVisibleLeafColumn(x).getText());
            }

            for (int x = 0; x < tableSize; x++) {
                Row tableData = sheet.createRow(3+x);
                tableData.createCell(0).setCellValue(tableView.getItems().get(x).getId());
                tableData.createCell(1).setCellValue(tableView.getItems().get(x).getTransaction_id());
                tableData.createCell(2).setCellValue(tableView.getItems().get(x).getTransaction_type());
                tableData.createCell(3).setCellValue(tableView.getItems().get(x).getAccount_number());
                tableData.createCell(4).setCellValue(tableView.getItems().get(x).getPayment_method());
                tableData.createCell(5).setCellValue(tableView.getItems().get(x).getPayment_gateway());
                tableData.createCell(6).setCellValue(tableView.getItems().get(x).getCash_amount());
                tableData.createCell(7).setCellValue(tableView.getItems().get(x).getEcash_amount());
                tableData.createCell(8).setCellValue(tableView.getItems().get(x).getEcash_id());
                tableData.createCell(9).setCellValue(tableView.getItems().get(x).getTransaction_date());
                tableData.createCell(10).setCellValue(tableView.getItems().get(x).getUsername());
                tableData.createCell(11).setCellValue(tableView.getItems().get(x).getTransaction_made_by());
            }

            String dateTime = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).concat(String.valueOf(LocalTime.now().getSecond()));

            File directoryPath = new File(createDirectoryIfNotExist().getPath() + File.separator + "Reports" + File.separator);
            if (!directoryPath.exists()) {
                directoryPath.mkdir();
            }

            File file = new File(directoryPath, "Transactions_Report_".concat(dateTime).concat(".xlsx"));
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return status;
        }catch (Exception e){
            status = 404;
        }
        return status;
    }



}// end of class...
