package com.rashad.TourPlanner.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "This is required!!")
    @Size(min = 2, max = 20, message = "Min 2 and Max 20 characters are allowed!!")
    private String name;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@+[a-zA-Z0-9.-]+$", message = "This is required !!!")
    private String email;

    @NotBlank(message = "This is required!!")
    @Size(min = 8, message = "Password must be at least 8 characters long!!")
    private String password;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be a valid 10-digit number!!")
    private String contactNumber;

    private String role;  // ROLE_ADMIN or ROLE_CUSTOMER
    private boolean enabled;

    // Add this to your User class
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Important to prevent infinite loops in JSON
    private List<Booking> bookings;
}
