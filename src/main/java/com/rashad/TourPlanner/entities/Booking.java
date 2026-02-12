package com.rashad.TourPlanner.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false)
    private Tour tour;

    private int numberOfTickets;
    private Double totalPrice;

    private PaymentStatus paymentStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate bookingDate;
    private boolean isBookingConfirmed;
    private String paymentTransactionId;

    public enum PaymentStatus {
        SUCCESS,
        PENDING,
        FAILED
    }

    public boolean checkAvailability() {
        return tour.getTicketsAvailable()>0;
    }

    public void confirmBooking() {
        if(paymentStatus == PaymentStatus.SUCCESS && checkAvailability()) {
            tour.setTicketsAvailable(tour.getTicketsAvailable()-numberOfTickets);
            this.isBookingConfirmed=true;
        }else{
            this.isBookingConfirmed=false;
        }
    }

    public void handleSystemFailure() {
        this.paymentStatus = PaymentStatus.FAILED;
        this.isBookingConfirmed=false;
    }
}
