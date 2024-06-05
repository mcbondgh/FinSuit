package app.config.email;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.SmsAPIEntity;
import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
public class EmailAPI extends MainModel {

    private String senderMail, senderPassword;
    private final String receiverMail;
    private final String messageBody;
    private final String messageHeader;
    private final String messageFooter;
    ErrorLogger logger = new ErrorLogger();


    public EmailAPI(String receiverMail,  String messageHeader, String messageBody, String messageFooter) {
        this.receiverMail = receiverMail;
        this.messageHeader = messageHeader;
        this.messageBody  = messageBody;
        this.messageFooter = messageFooter;
    }

    public String sendEmail() throws IOException {

        for (SmsAPIEntity items : getSmsApi()) {
            senderMail = items.getEmailAddress();
            senderPassword = items.getPassword();
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
        props.put("mail.smtp.port", "587"); // SMTP port

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderMail, senderPassword);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderMail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMail));
            message.setSubject(messageHeader);

            String fullMessage = generateEmailBody();

            message.setContent(fullMessage, "text/html");
//          message.setText(messageBody);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {

            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logger.logMessage(className, error);
            return e.getMessage();
        }
        return "OK";
    }

    private @NotNull String generateEmailBody() {
        String header = "<h1 style='color:#fff; background-color:#0677e0; text-align:center; paddin: 10px 5px;'>" + messageHeader + "</h1>";
        String body = "<p style='font-size:18px; padding:5%; border:1px solid #ddd; background:color:#fff; border-radius: 10px 10px 0 0;'>" + messageBody + "</p>";
        String footer = "<h5 style='background:color:#eee; border-radius: 10px 10px 0 0'>" + messageFooter + "</h5>";
        String hr = "<hr>";
        return header + body + hr + footer;
    }
    public static void main(String[] args) throws IOException {
//        EmailAPI email = new EmailAPI("realmcbond@hotmail.com", "FINSUIT - GHANA", "App passwords aren't recommended and are unnecessary in most cases. To help keep your account secure, use ign in with Google to connect apps to your Google Account.", "this is the message footer...");
//        String response = email.sendEmail();
//        System.out.println(response);
    }

}//end of class...
