package com.demo.demo.service;

import com.demo.demo.dto.EmailDetail;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(EmailDetail emailDetail) {
        try {
            log.info("Chuẩn bị gửi email tới: {}", emailDetail.getRecipient());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(emailDetail.getRecipient());
            helper.setSubject(emailDetail.getSubject());
            helper.setText(emailDetail.getBody(), true);

            javaMailSender.send(mimeMessage);
            log.info("Đã gửi email thành công tới: {}", emailDetail.getRecipient());
        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới {}: {}", emailDetail.getRecipient(), e.getMessage(), e);
        }
    }

    @Async
    public void sendRegistrationConfirmation(String email, String name) {
        Context context = new Context();
        context.setVariable("name", name);
        String html = templateEngine.process("registration-confirmation", context);
        sendEmail(new EmailDetail(email, "Chào mừng bạn đến với hệ thống", html));
    }

    @Async
    public void sendMeetingLink(String email, String name, String link) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("meetingLink", link);
        String html = templateEngine.process("meeting-invite", context);
        sendEmail(new EmailDetail(email, "Link buổi họp của bạn", html));
    }


    @Async
    public void sendPasswordResetEmail(String email, String name, String otpCode) {
        Context context = new Context();
        context.setVariable("name", name);
        // Sử dụng đúng biến 'otpCode' đã được truyền vào
        context.setVariable("token", otpCode);

        // Render file "password-reset.html" với các biến trên
        String htmlBody = templateEngine.process("password-reset", context);

        // Gửi email
        sendEmail(new EmailDetail(email, "Yêu cầu Đặt lại Mật khẩu", htmlBody));
    }
}