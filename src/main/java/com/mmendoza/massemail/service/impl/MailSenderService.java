package com.mmendoza.massemail.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mmendoza.massemail.model.User;
import com.mmendoza.massemail.model.enums.State;
import com.mmendoza.massemail.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    private final UserService userService;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String MAIL_SENDER;

    @Value("${spring.mail.sender.quantity}")
    private int MAIL_QUANTITY;

    public MailSenderService(UserService userService, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(cron = "0 0/24 * * * ?")
    public void sendEmails() {
        List<User> pendings = userService.getRecordsByQuantity(MAIL_QUANTITY);
        if (pendings.isEmpty()) {
            logger.info("No users to send emails.");
            return;
        }

        List<User> processed = new ArrayList<>();

        for (User user : pendings) {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                logger.warn("User {} has no email, marking as FAILED", user.getName());
                user.setState(State.FAILED);
                processed.add(user);
                continue;
            }

            try {
                MimeMessage message = createMailMessage(user);
                javaMailSender.send(message);
                user.setState(State.SENT);
                logger.info("Email sent to {}", user.getEmail());
            } catch (MessagingException e) {
                logger.error("Failed to create email for {}: {}", user.getEmail(), e.getMessage());
                user.setState(State.FAILED);
            } catch (Exception e) {
                logger.error("Unexpected error sending email to {}: {}", user.getEmail(), e.getMessage(), e);
                user.setState(State.FAILED);
            }

            processed.add(user);
        }

        if (!processed.isEmpty()) {
            userService.saveManyUser(processed);
        }
    }

    private MimeMessage createMailMessage(User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject("RECORDATORIO");
        helper.setTo(user.getEmail());
        helper.setText(user.getMessage(), true);
        helper.setFrom(MAIL_SENDER);
        return message;
    }
}