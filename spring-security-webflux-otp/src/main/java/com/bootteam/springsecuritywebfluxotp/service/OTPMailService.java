package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.configuration.EmailProperties;
import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.EmailSenderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service for sending emails.
 */
@Slf4j
@Service
public class OTPMailService {
    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private static final String DEFAULT_LANGUAGE = "en";
    private final EmailProperties emailProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public OTPMailService(
            EmailProperties emailProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.emailProperties = emailProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    private void sendEmail(EmailSenderDTO sender) {

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, sender.isMultipart(), StandardCharsets.UTF_8.name());
            message.setTo(sender.getTo());
            message.setFrom(emailProperties.getFrom());
            message.setSubject(sender.getSubject());
            message.setText(sender.getContent(), sender.isHtml());
            javaMailSender.send(mimeMessage);
            LOGGER.debug("Sent email to User '{}'", sender.getTo());
        } catch (MailException | MessagingException e) {
            LOGGER.warn("Email could not be sent to user '{}'", sender.getTo(), e);
        }
    }

    private void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            LOGGER.debug("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
        Locale locale = Locale.forLanguageTag(DEFAULT_LANGUAGE);
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, emailProperties.getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);

        sendEmail(EmailSenderDTO.builder()
                .content(content).subject(subject).to(user.getEmail())
                .isHtml(true).isMultipart(false)
                .build());
    }

    @Async
    public void sendLoginOTPEmail(User user) {
        LOGGER.debug("Sending login OTP email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/OTPCodeEmail", "email.otp.verification");
    }
}
