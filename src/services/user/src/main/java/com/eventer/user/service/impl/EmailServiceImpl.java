package com.eventer.user.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.data.model.User;
import com.eventer.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
        context.setVariable("header", "Dodat je nov događaj u kategoriji koju pratite.");
        context.setVariable("title", event.getTitle());
        context.setVariable("location", event.getLocation());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDateTime dateTime = LocalDateTime.ofInstant(event.getDate(), ZoneId.systemDefault());
        String formattedDate = dateTime.format(formatter);
        context.setVariable("date", formattedDate);

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
        context.setVariable("header", "Izmenjeni detalji događaja");
        context.setVariable("title", event.getTitle());
        context.setVariable("location", event.getLocation());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDateTime dateTime = LocalDateTime.ofInstant(event.getDate(), ZoneId.systemDefault());
        String formattedDate = dateTime.format(formatter);
        context.setVariable("date", formattedDate);

        try {
            this.send(to, subject, "SubscriptionNotificationEmail", context);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordResetEmail(Optional<User> foundUser, String code) {
        String[] to = new String[] {foundUser.get().getUsername()};
        String subject = "Restartovanje lozinke";

        Context context = new Context();
        context.setVariable("header", "Ovo je vaš kod za restartovanje lozinke.");
        context.setVariable("code", code);

        try {
            this.send(to, subject, "PasswordResetEmail", context);
        } catch (MessagingException e) {
            throw  new RuntimeException(e);
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
