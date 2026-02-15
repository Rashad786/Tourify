package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.entities.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // This pulls your Gmail address from the environment variable we set
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Sends a booking confirmation email asynchronously.
     * This prevents the user's browser from "spinning" while waiting for the mail server.
     */
    @Async
    public void sendBookingConfirmationEmail(String toEmail, Booking booking) {
        logger.info("Starting background email task for: {}", toEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            // 'true' indicates this message is multipart (supports HTML and attachments)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("🎉 Booking Confirmed: " + booking.getTour().getTourName());

            // Build a professional HTML receipt
            String htmlBody = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                    "  <div style='max-width: 600px; margin: auto; border: 1px solid #eee; padding: 20px; border-radius: 10px;'>" +
                    "    <h2 style='color: #2e7d32; text-align: center;'>Tour Confirmed!</h2>" +
                    "    <p>Hi <b>" + booking.getCustomer().getName() + "</b>,</p>" +
                    "    <p>Great news! Your payment was successful and your spot is reserved.</p>" +
                    "    <div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px;'>" +
                    "      <p><b>Tour:</b> " + booking.getTour().getTourName() + "</p>" +
                    "      <p><b>Booking ID:</b> #" + booking.getBookingId() + "</p>" +
                    "      <p><b>Tickets:</b> " + booking.getNumberOfTickets() + "</p>" +
                    "      <p><b>Total Paid:</b> $" + booking.getTotalPrice() + "</p>" +
                    "      <p><b>Travel Date:</b> " + booking.getBookingDate() + "</p>" +
                    "    </div>" +
                    "    <p style='margin-top: 20px;'>If you have any questions, simply reply to this email.</p>" +
                    "    <p>Safe travels,<br><b>The TourPlanner Team</b></p>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlBody, true); // true = send as HTML

            mailSender.send(message);
            logger.info("SUCCESS: Confirmation email sent to {}", toEmail);

        } catch (MessagingException e) {
            logger.error("ERROR: Failed to create email for {}. Details: {}", toEmail, e.getMessage());
        } catch (Exception e) {
            logger.error("FATAL SMTP ERROR: Connection failed. Check your App Password or Port 465 settings.");
            logger.error("Stacktrace: ", e);
        }
    }
}
