package app.config.email;

import app.fetchedData.SmsAPIObject;
import app.models.MainModel;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailAPI extends MainModel {

    private String senderMail, senderPassword;
    private final String receiverMail;
    private final String messageBody;
    private final String messageHeader;
    private final String messageFooter;


    public EmailAPI(String receiverMail,  String messageHeader, String messageBody, String messageFooter) {
        this.receiverMail = receiverMail;
        this.messageHeader = messageHeader;
        this.messageBody  = messageBody;
        this.messageFooter = messageFooter;
    }

    public void sendEmail() throws IOException {

        for (SmsAPIObject items : getSmsApi()) {
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

            String header = "<h1 style='color:#fff; background-color:#0677e0; text-align:center;'>" + messageHeader + "</h1>";
            String body = "<p style='font-size:18px; padding:5%; background:color:#fff; border-radius: 10px 10px 0 0;'>" + messageBody + "</p>";
            String footer = "<h5 style='background:color:#fff; border-radius: 10px 10px 0 0'>" + messageFooter + "</h5>";
            String hr = "<hr>";
            String fullMessage = header + body + hr + footer;

            message.setContent(fullMessage, "text/html");
//            message.setText(messageBody);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
    public static void main(String[] args) throws IOException {
        EmailAPI email = new EmailAPI("realmcbond@hotmail.com", "FINSUIT - GHANA", "App passwords aren't recommended and are unnecessary in most cases. To help keep your account secure, use ign in with Google to connect apps to your Google Account.", "this is the message footer...");
        email.sendEmail();
    }

}//end of class...
