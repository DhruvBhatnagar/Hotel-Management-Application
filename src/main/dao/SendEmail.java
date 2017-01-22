package comp9321.dao;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.*;

public class SendEmail
{
final String fromEmailID = "comp9321assignment2unsw";
final String fromPassword = "comp9321";
final String smtpServer = "smtp.gmail.com";
final String portNumber = "465";

String toEmailID = null;
String mailSubj = null;
String mailContent = null;

public SendEmail(String toEmailID, String mailSubj, String mailContent)
{
this.toEmailID=toEmailID;
this.mailSubj=mailSubj;
this.mailContent=mailContent;


Properties cond = new Properties();
cond.put("mail.smtp.user",fromEmailID);
cond.put("mail.smtp.host", smtpServer);
cond.put("mail.smtp.port", portNumber);
cond.put("mail.smtp.starttls.enable", "true");
cond.put("mail.smtp.auth", "true");
cond.put("mail.smtp.socketFactory.port", portNumber);
cond.put("mail.smtp.socketFactory.class",
"javax.net.ssl.SSLSocketFactory");
cond.put("mail.smtp.socketFactory.fallback", "false");

SecurityManager security = System.getSecurityManager();

try
{
Authenticator auth = new SMTPAuthenticator();
Session session = Session.getInstance(cond, auth);


MimeMessage msg = new MimeMessage(session);
msg.setText(mailContent);
msg.setSubject(mailSubj);
msg.setFrom(new InternetAddress(fromEmailID));
msg.addRecipient(Message.RecipientType.TO,
new InternetAddress(toEmailID));
Transport.send(msg);
}
catch (Exception mex)
{
mex.printStackTrace();
}


}
public class SMTPAuthenticator extends javax.mail.Authenticator
{
public PasswordAuthentication getPasswordAuthentication()
{
return new PasswordAuthentication(fromEmailID, fromPassword);
}
}

public static void main(String[] args)
{

SendEmail mailSender=new SendEmail("duskxii@gmail.com","Email Subjectl","Email Body");
}



}