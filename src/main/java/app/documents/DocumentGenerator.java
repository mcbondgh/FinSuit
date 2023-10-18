package app.documents;

import app.models.MainModel;
import app.repositories.BusinessInfoEntity;
import app.repositories.documents.ReceiptsEntity;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class DocumentGenerator {

    MainModel DAO = new MainModel();
    
    /*
    THE PURPOSE OF THIS CLASS IS TO ENABLE US GENERATE ALL REQUIRED DOCUMENT FORMAT ie excel, word, ppt and pdf FOR
    OUR APPLICATION. WE ARE GOING TO DEFILE CUSTOM METHODS THAT WOULD REPRESENT EVERY DOCUMENT BASED ON ITS
    FUNCTIONALITY. ie receipts, reports, excel files AND OTHER RELATED DOCUMENTS.
     */

    //CREATE A FILE PATH IF NOT EXISTS TO SAVE DOCUMENT...
    public String generateFolder(String documentName) {
        // CREATE FILE DIRECTORY IF NOT EXISTS..
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String directoryPath = desktopPath + File.separator + "Finsuit Document";
        File generatedFile = new File(directoryPath);

        //check if directory exists: if true then save file else create directory.
        if (!generatedFile.exists()) {
            generatedFile.mkdirs();
        }
        return generatedFile + File.separator + documentName;
    }

    public void generateDepositReceipt(String documentName, ReceiptsEntity receiptsEntity) {

        try {

            // CREATE A pdf document template that serves as the document environment to hold content...
            // This is equivalent to having an empty document space or white space...
            PdfWriter pdfWriter = new PdfWriter(documentName);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            //GET BUSINESS CREDENTIALS FOR THE LETTER HEAD.
            String businessName = "";
            String mobileNumbers = "";
            String email = "";
            String digitalAddress = "";

            for (BusinessInfoEntity data : DAO.getBusinessInfo()) {
                businessName = data.getName();
                mobileNumbers = data.getMobileNumber() + "|".concat(data.getOtherNumber());
                email = data.getEmail();
                digitalAddress = data.getDigital();
            }

            Document document = new Document(pdfDocument, PageSize.A4);
            Table table = new Table(2).setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setFontSize(12).setPadding(6f)
                    .useAllAvailableWidth();
            Div container = new Div();

            Paragraph businessNameText = new Paragraph(businessName)
                    .setFontSize(14).setBold().setTextAlignment(TextAlignment.CENTER);

            Paragraph mobileNumbersText = new Paragraph(mobileNumbers)
                    .setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER);

            Paragraph addressText = new Paragraph(email.concat(" | ".concat(digitalAddress)))
                    .setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER);
            container.add(businessNameText).add(mobileNumbersText).add(addressText)
                    .setTextAlignment(TextAlignment.CENTER).setMargins(5, 0, 5, 0);

            Div receiptBodyContainer = new Div();

            Paragraph combinedParagraph = new Paragraph();
            Paragraph labelNames;
            Paragraph dataNames;
            DottedLine dottedLine = new DottedLine();
            dottedLine.setLineWidth(1);
            dottedLine.setGap(5);

            labelNames = new Paragraph("TRANSACTION NO: ").setBold().setFontSize(10);
            dataNames = new Paragraph(receiptsEntity.getTransactionNumber()).setFontSize(12);
            combinedParagraph.add(labelNames).add(dataNames);

            labelNames = new Paragraph("TRANSACTION DATE: ").setBold().setFontSize(10);
            dataNames = new Paragraph(receiptsEntity.getTransactionDate()).setFontSize(12);
            combinedParagraph.add(labelNames.setMarginLeft(22f)).add(dataNames);
            combinedParagraph.setTextAlignment(TextAlignment.CENTER);

            combinedParagraph.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
            Div receiptContentContainer = new Div();
            receiptContentContainer.add(new Paragraph("CUSTOMER NAME: ".concat(receiptsEntity.getCustomerName())));
            receiptContentContainer.add(new Paragraph("ACCOUNT NUMBER: ".concat(receiptsEntity.getAccountNumber())));
            receiptContentContainer.add(new Paragraph("TRANSACTION TYPE: ".concat(receiptsEntity.getTransactionType())));
            receiptContentContainer.add(new Paragraph("DEPOSIT AMOUNT Ghc".concat(receiptsEntity.getAmount())));
            receiptContentContainer.add(new Paragraph("PAYMENT METHOD: ".concat(receiptsEntity.getPaymentMethod())));
            receiptContentContainer.add(new Paragraph("DEPOSITOR'S NAME: ".concat(receiptsEntity.getDepositorName())));
            receiptContentContainer .add(new Paragraph("DEPOSITOR'S ID NO: ".concat(receiptsEntity.getDepositorIdNumber())));
            receiptContentContainer.add(new Paragraph("CASHIER'S NAME: ".concat(receiptsEntity.getCashierName())));
            receiptContentContainer.add(new Paragraph("-----------------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER));
            receiptContentContainer.add(new Paragraph("THANK YOU DEAR CUSTOMER, WE APPRECIATE YOU!!!").setFontSize(9).setMarginTop(5).setTextAlignment(TextAlignment.CENTER));
            receiptContentContainer.setMarginLeft(32f);

            receiptBodyContainer.add(combinedParagraph).add(receiptContentContainer);
            document.add(container).add(receiptBodyContainer);
            document.close();
        } catch (Exception ignore) {
        }
    }

}
