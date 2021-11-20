package io.github.patrykblajer.todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Configuration
public class MailConfig {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String adminMail;

    @Autowired
    public MailConfig(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }


    public void sendMail(String to,
                         String subject,
                         String content) throws MessagingException {

        String htmlTemplate = this.constructHtmlTemplate(content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(adminMail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(htmlTemplate, true);
        mimeMessageHelper.setSubject(subject);
        javaMailSender.send(mimeMessage);
    }

    private String constructHtmlTemplate(String content) {
        Context context = new Context();
        context.setVariable("content", content);
        return templateEngine.process("reset-password-mail", context);
    }
}