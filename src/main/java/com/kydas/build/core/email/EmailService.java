package com.kydas.build.core.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailUsername, "Kydas");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
        };
        emailSender.send(mailMessage);
    }

    @Async
    public void sendEmailHtml(String to, String subject, String htmlText) {
        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailUsername, "Kydas");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(htmlText, true);
        };
        emailSender.send(mailMessage);
    }

}
