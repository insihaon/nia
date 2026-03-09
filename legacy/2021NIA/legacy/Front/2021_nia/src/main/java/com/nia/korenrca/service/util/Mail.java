package com.nia.korenrca.service.util;


import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.properties.CommonProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class Mail {
    private static final Logger LOGGER = LogManager.getLogger();

	public static boolean sendMail(Data data, String EMAIL_KEY) {
        String[] receivers = data.getString(CommonProperties.RECEIVER).split(",");
		String subject = data.get(CommonProperties.SUBJECT);
		String body = data.get(CommonProperties.BODY);
		body = body.replace("#KEY#", EMAIL_KEY);

		String host = ConfigProperties.get().getEmailHost();
        String port = ConfigProperties.get().getEmailPort();
        String ssl = ConfigProperties.get().getEmailSsl();
        String auth = ConfigProperties.get().getEmailAuth();
		String user = ConfigProperties.get().getEmailUser();
		String password = ConfigProperties.get().getEmailPassword();
        String sender = ConfigProperties.get().getEmailSender();

		if (host != null && host.length() > 0 && user != null && user.length() > 0 && password != null && password.length() > 0) {
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.ssl.enable", ssl);
            props.put("mail.smtp.starttls.enable","false");
            props.put("java.net.preferIPv4Stack","true");

            Session session = null;
            if ("true".equals(auth)) {
                session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(user, password);
                    }
                });
            } else {
                session = Session.getDefaultInstance(props);
            }

			try {
				MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                for (int i = 0; i < receivers.length; i++) {
                    // 수신
                    message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(receivers[i]));
                }
				message.setSubject(subject); // 메일 제목
                message.setSentDate(new Date());

                // Create a multipart message
                MimeMultipart multipart = new MimeMultipart("related");

                // 본문 HTML 예제
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(body,"text/html; charset=euc-kr");
                multipart.addBodyPart(messageBodyPart);

                // 첨부파일 이미지 예제
//                messageBodyPart = new MimeBodyPart();
//                DataSource fds = new FileDataSource("D:\\이미지\\beverage-3157395_1920.jpg");
//                messageBodyPart .setDataHandler(new DataHandler(fds));
//                messageBodyPart .setHeader("Content-ID","<my-image>");
//                multipart.addBodyPart(messageBodyPart );

                // put everything together
                message.setContent(multipart);

                Transport transport = session.getTransport();
                transport.connect();
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                transport.close();

                LOGGER.debug("발송완료");
				return true;
			} catch (MessagingException e) {
                LOGGER.error("host="+ host + ", port="+ port);
                LOGGER.error("user="+ user + ", password="+ password);
                LOGGER.error(e);
			}
		}
		return false;
	}

    public static void main(String[] args) {
	    Data data = new Data();
//        data.set(CommonProperties.RECEIVER, "infobiz@koren.kr");
        data.set(CommonProperties.RECEIVER, "codej@koren.kr");
        data.set(CommonProperties.SUBJECT, "SUBJECT");
        data.set(CommonProperties.BODY, "BODY, 한글테스트 <h1>제목</h1>");
        sendMail(data, "");
    }
}
