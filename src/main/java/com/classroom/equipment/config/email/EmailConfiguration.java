package com.classroom.equipment.config.email;

import com.classroom.equipment.config.email.properties.GmailConfigProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Configuration
public class EmailConfiguration {
    @Bean(name = "gmailSenderConfigProperties")
    @ConfigurationProperties(prefix = "gmail.sender")
    public GmailConfigProperties getGmailConfigProperties() {
        return new GmailConfigProperties();
    }

    @Bean(name = "gmailSender")
    public JavaMailSender getGmailSender(@Qualifier("gmailSenderConfigProperties") GmailConfigProperties gmailConfigProperties) {
        return getJavaMailSender(gmailConfigProperties);
    }

    @Bean(name = "emailTemplate")
    public TemplateEngine loadEmailTemplate(){
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");

        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
    }

    private JavaMailSender getJavaMailSender(GmailConfigProperties gmailConfigProperties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(gmailConfigProperties.getHost());
        mailSender.setPort(gmailConfigProperties.getPort());
        mailSender.setUsername(gmailConfigProperties.getUsername());
        mailSender.setPassword(gmailConfigProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", gmailConfigProperties.getPropertiesMailTransportProtocol());
        props.put("mail.smtp.starttls.enable", gmailConfigProperties.isPropertiesMailSmtpStarttlsEnable());
        props.put("mail.smtp.starttls.required", gmailConfigProperties.isPropertiesMailSmtpStarttlsRequired());
        props.put("mail.smtp.auth", gmailConfigProperties.isPropertiesMailSmtpAuth());
        props.put("mail.debug", gmailConfigProperties.isPropertiesDebug());
        props.put("mail.smtp.socketFactory.fallback", gmailConfigProperties.isPropertiesMailSmtpSocketFactoryFallback());
        props.put("mail.smtp.connectiontimeout", gmailConfigProperties.getPropertiesMailSmtpConnectionTimeout());
        props.put("mail.smtp.timeout", gmailConfigProperties.getPropertiesMailSmtpTimeout());
        props.put("mail.smtp.writetimeout", gmailConfigProperties.getPropertiesMailSmtpWriteTimeout());

        return mailSender;
    }
}
