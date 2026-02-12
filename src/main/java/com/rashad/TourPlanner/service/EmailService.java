package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.entities.Booking;
import com.rashad.TourPlanner.entities.Tour;
import io.jsonwebtoken.security.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmationEmail(String toEmail, Booking booking) throws MessagingException {

        Tour tour = booking.getTour();

        String subject = "🎉 Your Tour Booking is Confirmed – " + tour.getTourName() + "!";

        // Plain text version
//        String body = "Dear " + booking.getCustomer().getName() + ",\n\n" +
//                "Thank you for booking with Tourify! Your tour has been confirmed.\n\n" +
//                "Booking Details:\n" +
//                "Booking ID: " + booking.getBookingId() + "\n" +
//                "Destination: " + tour.getLocation() + "\n" +
//                "Travel Dates: " + tour.getStartDate() + " to " + tour.getEndDate() + "\n" +
//                "Number of Tickets: " + booking.getNumberOfTickets() + "\n" +
//                "Total Paid: $" + booking.getTotalPrice() + "\n\n" +
//                "We look forward to giving you an amazing experience!\n\n" +
//                "Best regards,\n" +
//                "Tourify Team 🌍";

        // HTML version (optional, looks nicer in inbox)
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
        helper.setText(htmlBody, true);  // true = send as HTML
        helper.setFrom("tourify@demomailtrap.com");

        mailSender.send(message);

    }

}
