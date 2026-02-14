package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.entities.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendBookingConfirmationEmail(String toEmail, Booking booking) {
        logger.info("Email process started for: {}", toEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("🎉 Your Tour Booking is Confirmed!");
            helper.setFrom(fromEmail);

            // Simplified for debugging
            helper.setText("Hi, your booking " + booking.getBookingId() + " is confirmed!", false);

            logger.info("Attempting to connect to Gmail SMTP for user: {}", toEmail);

            mailSender.send(message);

            logger.info("SUCCESS: Email sent successfully to {}", toEmail);

        } catch (Exception e) {
            logger.error("FATAL EMAIL ERROR: {}", e.getMessage(), e);
        }
    }
}


