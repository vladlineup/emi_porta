package pvt.emi.ptl8.emailSender;

import java.security.Security;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * <p> Created by Rubezhin Evgenij on 7/1/2019. <br>
 * Copyright (c) 2019 LineUp. <br> Project: bm71term, pvt.talinor.ptl8.emailSender </p>
 *
 * @author Rubezhin Evgenij
 * @version 1.0
 */
public class GMailSender extends Authenticator {

  static {
    Security.addProvider(new JSSEProvider());
  }

  private String mailhost = "smtp.gmail.com";
  private String user;
  private String password;
  private Session session;

  public GMailSender(String user, String password) {
    this.user = user;
    this.password = password;

    Properties props = new Properties();
    props.setProperty("mail.transport.protocol", "smtp");
    props.setProperty("mail.host", mailhost);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.setProperty("mail.smtp.quitwait", "false");

    session = Session.getDefaultInstance(props, this);
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(user, password);
  }

  public synchronized void sendMail(String subject, String body,
      String sender, String recipients) throws Exception {
    MimeMessage message = new MimeMessage(session);
    DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
    message.setSender(new InternetAddress(sender));
    message.setSubject(subject);
    message.setDataHandler(handler);

    if (recipients.indexOf(',') > 0) {
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
    } else {
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
    }

    Transport.send(message);
  }
}
