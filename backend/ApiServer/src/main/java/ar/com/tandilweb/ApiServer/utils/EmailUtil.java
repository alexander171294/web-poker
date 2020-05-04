package ar.com.tandilweb.ApiServer.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.mail.smtp.SMTPTransport;

@Component
public class EmailUtil {
	
	@Value("${mail.smtpserver}")
	private String smtpserver;
	
	@Value("${mail.email}")
	private String email;
	
	@Value("${mail.password}")
	private String password;
	
	
	public void sendMail(String to, String subject , String message) {
		 Properties props = System.getProperties();
	        props.put("mail.smtps.host", smtpserver);
	        props.put("mail.smtps.auth","true");
	        Session session = Session.getInstance(props, null);
	        Message msg = new MimeMessage(session);
	        try {
				msg.setFrom(new InternetAddress(email));
				msg.setRecipients(Message.RecipientType.TO,
		        InternetAddress.parse(to, false));
		        msg.setSubject(subject);
		        msg.setText(message);
		        msg.setSentDate(new Date());
		        SMTPTransport t =
		            (SMTPTransport)session.getTransport("smtps");
		        t.connect(email, password);
		        t.sendMessage(msg, msg.getAllRecipients());
		        System.out.println("Response: " + t.getLastServerResponse());
		        t.close();
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			};
	}

}
