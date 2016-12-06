package com.joey.Fujikom.modules.spi.utils;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

import com.joey.Fujikom.common.config.Global;

@Service
public  class SendEmailService {
	
	public static void sendEmailByMemberEmail(String emailTo, String emailFrom,
			String title, String content) {

		String mailSmtpHost = Global.getMailSmtpHost();
		final String mailSmtpUser = Global.getMailSmtpUser();
		final String mailSmtpPassword = Global.getMailSmtpPassword();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", mailSmtpHost);
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSmtpUser,
								mailSmtpPassword);
					}
				});
		try {
			Message message = new MimeMessage(session);
			if (emailFrom != null && emailFrom.length() > 0) {
				message.setFrom(new InternetAddress(emailFrom));
			} else {
				message.setFrom(new InternetAddress(Global.getMailFrom()));
			}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailTo));
			message.setSubject(title);
			// message.setText(content);
			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
			BodyPart bodyPart = new MimeBodyPart();// 正文
			bodyPart.setContent(content, "text/html;charset=utf-8");// 网页格式
			mp.addBodyPart(bodyPart);
			message.setContent(mp);// 设置邮件内容对象
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}



}
