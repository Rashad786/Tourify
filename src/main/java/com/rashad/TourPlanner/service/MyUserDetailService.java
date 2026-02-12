package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.config.UserPrincipal;
import com.rashad.TourPlanner.entities.User;
import com.rashad.TourPlanner.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class MyUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        logger.info("User found with email: {}, Role: {}", email, user.getRole());
        return new UserPrincipal(user);
    }
}
