package org.example.eventplanner.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.repositories.InvitationRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email to " + to, e);
        }
    }

    public void sendEventInvitation(String to, String eventName, Long eventId) {
        String subject = "You're invited to: " + eventName;

        String linkAccept = "http://localhost:8081/invitations/accept?eventId=" + eventId + "&email=" + to;
        String linkDecline = "http://localhost:8081/invitations/decline?eventId=" + eventId + "&email=" + to;

        String html = """
        <html>
        <body style="font-family: Arial, sans-serif;">
            <h2>You are invited to the event: %s</h2>
            <p>Please confirm your participation:</p>
            <a href="%s" style="background-color:green;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;">Accept</a>
            <a href="%s" style="background-color:red;color:white;padding:10px 20px;text-decoration:none;margin-left:10px;border-radius:5px;">Decline</a>
            <p>If the buttons don’t work, copy and paste these links:</p>
            <p>%s</p>
            <p>%s</p>
        </body>
        </html>
    """.formatted(eventName, linkAccept, linkDecline, linkAccept, linkDecline);

        sendHtmlEmail(to, subject, html);
    }


}
