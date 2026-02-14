package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.entities.Booking;
import com.rashad.TourPlanner.entities.Tour;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendBookingConfirmationEmail(String toEmail, Booking booking) throws MessagingException {

        Tour tour = booking.getTour();

        String subject = "🎉 Your Tour Booking is Confirmed – " + tour.getTourName() + "!";

        String htmlBody = "<h2>Dear " + booking.getCustomer().getName() + ",</h2>" +
                "<p>Thank you for booking with <strong>Tourify</strong>! Your tour has been confirmed.</p>" +
                "<h3>Booking Details:</h3>" +
                "<ul>" +
                "<li><strong>Booking ID:</strong> " + booking.getBookingId() + "</li>" +
                "<li><strong>Destination:</strong> " + tour.getLocation().getCountry() + "</li>" +
                "<li><strong>Travel Dates:</strong> " + tour.getStartDate() + " to " + tour.getEndDate() + "</li>" +
                "<li><strong>Number of Tickets:</strong> " + booking.getNumberOfTickets() + "</li>" +
                "<li><strong>Total Paid:</strong> $" + booking.getTotalPrice() + "</li>" +
                "</ul>" +
                "<p>We look forward to giving you an amazing experience!</p>" +
                "<p>Best regards,<br>Tourify Team 🌍</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom(fromEmail);  

        mailSender.send(message);
    }
}


