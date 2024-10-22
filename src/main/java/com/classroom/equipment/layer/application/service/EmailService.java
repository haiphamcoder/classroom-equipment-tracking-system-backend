package com.classroom.equipment.layer.application.service;

import com.classroom.equipment.layer.application.models.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender gmailSender;
    private final TemplateEngine templateEngine;
    private final BlockingDeque<MimeMessage> queue;

    public EmailService(@Qualifier("gmailSender") JavaMailSender gmailSender,
                        @Qualifier("emailTemplate") TemplateEngine templateEngine) {
        this.gmailSender = gmailSender;
        this.templateEngine = templateEngine;
        queue = new LinkedBlockingDeque<>();
        this.start();
    }

    private void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                MimeMessage message;
                try {
                    message = queue.take();
                    gmailSender.send(message);
                    log.info("sent mail: {}", message);
                } catch (InterruptedException e) {
                    log.error("Error sending email", e);
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setName("gmail-sender");
        thread.start();
    }

    public void sendEmail(String to, String subject, Map<String, Object> model, String template) {
        MimeMessage message = gmailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);

            String content = templateEngine.process(template, new Context(Locale.getDefault(), model));
            helper.setText(content, true);
            queue.add(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(EmailMessage mailInfo, String template) {
        MimeMessage message = gmailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(mailInfo.getTo().toArray(new String[0]));
            helper.setSubject(mailInfo.getSubject());

            String content = templateEngine.process(template, new Context(Locale.getDefault(), mailInfo.getModel()));
            helper.setText(content, true);
            queue.add(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
