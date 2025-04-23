package com.example.EmailMicroservice.Service.Impl;

import com.example.EmailMicroservice.EmailRequest;
import com.example.EmailMicroservice.ServerErrorException;
import com.example.EmailMicroservice.Service.Interface.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class SmtpEmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromMail;

    public void sendEmail(EmailRequest emailRequest) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(emailRequest.getReceiver());
            helper.setFrom(fromMail);
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getContent(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ServerErrorException("An error occurred while sending the E-Mail: " + e);
        }
    }
}