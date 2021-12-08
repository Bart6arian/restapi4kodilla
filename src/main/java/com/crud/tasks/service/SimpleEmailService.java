package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.exception.EmailReceiverException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEmailService {

    private final JavaMailSender javaMailSender;

    public void send(final Mail mail) {
        log.info("Starting email...");
        try {
            SimpleMailMessage mailMessage = createMailMessage(mail);
            javaMailSender.send(mailMessage);
            log.info("Email has been sent. ");
        }catch (MailException e) {
            log.error("Failed to process email sending: " + e.getMessage(), e);
        } catch (EmailReceiverException e) {
            e.printStackTrace();
        }
    }

    private SimpleMailMessage createMailMessage(final Mail mail) throws EmailReceiverException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());

        if(mail.getToCc() != null) {
            mailMessage.setCc(mail.getToCc());
        } else
            throw new EmailReceiverException("Incorrect input of: " + mail.getToCc());

        return mailMessage;
    }

}
