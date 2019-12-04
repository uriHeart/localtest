package com.argo.common.domain.common.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.MessageFormat;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class EmailService {

    public JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            log.error(MessageFormat.format("Failed to send mail to {0} with subject : {1}", to, subject));
        }
    }

    public void sendMessageWithAttachment(String to, String subject, String text, Map<String, File> attachmentMap) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            attachmentMap.forEach((attachmentName, attachment) -> {
                try {
                    helper.addAttachment(attachmentName, attachment);
                } catch (MessagingException e) {
                    log.error( MessageFormat.format("Failed to add attachment {1} with name {2}", attachment.getName(), attachmentName));
                    return;
                }
            });
        } catch (MessagingException e) {
            log.error( MessageFormat.format("Failed to add attachment to mail when sending to {0} with subject : {1}", to, subject));
            return;
        }
        try {
            emailSender.send(message);
        } catch (MailException e) {
            log.error( MessageFormat.format("Failed to send mail to {0} with subject : {1}", to, subject));
        }
    }
}