package com.example.TaskMngr.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInviteEmail(String toEmail, String inviteLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        String link="http://localhost:8080/register?token="+inviteLink;
        message.setTo(toEmail);
        message.setSubject("You're invited to join TaskMngr");
        message.setText(
            "You have been invited to join TaskMngr.\n\n" +
            "Click the link below to register:\n" +
            link + "\n\n" +
            "This link will expire."
        );

        mailSender.send(message);
    }
}
