package com.assessment.vending.service;

import com.assessment.vending.dto.request.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import static com.assessment.vending.util.AppConstants.PURCHASE_NOTIFICATION_QUEUE;

@Service
@RequiredArgsConstructor
public class JmsConsumer {

    private final EmailService emailService;

    @JmsListener(destination = PURCHASE_NOTIFICATION_QUEUE, containerFactory = "myJmsContainerFactory")
    public void receivePurchaseNotification(EmailDto emailDto) {
        emailService.sendEmail(emailDto.getEmail(), emailDto.getMessage());
    }
}
