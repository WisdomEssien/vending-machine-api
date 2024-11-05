package com.assessment.vending.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String recipient, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setFrom("noreply@vending.com");
        simpleMailMessage.setSubject("Product Order Confirmation");
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);
    }
}
