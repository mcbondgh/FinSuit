package app.documents;

import app.models.MainModel;
import app.repositories.BusinessInfoEntity;
import app.repositories.documents.ReceiptsEntity;
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

import java.io.File;
import java.io.FileNotFoundException;

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
        container.add(businessNameText).add(mobileNumbersText).add(addressText)
                .setTextAlignment(TextAlignment.CENTER).setMargins(2, 0, 2, 0);
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


}// end of class...
