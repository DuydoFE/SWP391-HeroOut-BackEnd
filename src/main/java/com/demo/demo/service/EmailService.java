package com.demo.demo.service;

import com.demo.demo.dto.EmailDetail;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(EmailDetail emailDetail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("admin@gmail.com");
            helper.setTo(emailDetail.getRecipient());
            helper.setSubject(emailDetail.getSubject());
            helper.setText(emailDetail.getBody(), true); // true => HTML

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRegistrationConfirmation(String email, String name) {
        Context context = new Context();
        context.setVariable("name", name);
        String html = templateEngine.process("registration-confirmation", context);

        sendEmail(new EmailDetail(email, "Welcome to Our System", html));
    }

    public void sendMeetingLink(String email, String name, String link) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("meetingLink", link);
        String html = templateEngine.process("meeting-invite", context);

        sendEmail(new EmailDetail(email, "Your Jitsi Meeting Link", html));
    }
}
