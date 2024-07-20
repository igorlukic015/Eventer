package com.eventer.user.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendNewEventNotification(Set<String> recipients, Event event) {
        String[] to = recipients.toArray(String[]::new);
        String subject = "Nov dogadjaj koji te mozda interesuje!";

        Context context = new Context();
        context.setVariable("title", "Dodat je nov događaj u kategoriji koju pratite.");

        StringBuilder builder = new StringBuilder();
        builder.append(event.getTitle());
        builder.append(" se održava u ");
        builder.append(event.getLocation());
        builder.append(" na dan ");
        builder.append(event.getDate().toString());

        context.setVariable("message", builder.toString());

        try {
            this.send(to, subject, "SubscriptionNotificationEmail", context);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEventUpdateNotification(Set<String> recipientEmails, Event event) {
        String[] to = recipientEmails.toArray(String[]::new);
        String subject = "Detalji događaja koji pratiš su izmenjeni.";

        Context context = new Context();
        context.setVariable("title", "");

        StringBuilder builder = new StringBuilder();
        builder.append(event.getTitle());
        builder.append(" se održava u ");
        builder.append(event.getLocation());
        builder.append(" na dan ");
        builder.append(event.getDate().toString());

        context.setVariable("message", builder.toString());

        try {
            this.send(to, subject, "SubscriptionNotificationEmail", context);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String[] to, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String html = templateEngine.process(templateName, context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
