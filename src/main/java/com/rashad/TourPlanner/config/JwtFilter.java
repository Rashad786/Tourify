package com.rashad.TourPlanner.config;

import com.rashad.TourPlanner.service.JwtService;
import com.rashad.TourPlanner.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailService userService;

    private static final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader =  request.getHeader("Authorization");
        String token = null;
        String userName = null;

        logger.info("Processing request to URI: {}", request.getRequestURI());

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract the token
            logger.debug("Token extracted from Authorization header: {}", token);

            if(isTokenBlacklisted(token)) { // Check if the token is blacklisted
                logger.warn("Blacklisted token detected: {}", token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been invalidated. Please log in again.");
                return; // Stop further processing
            }
            try{
                userName = jwtService.extractUserName(token);
                logger.debug("Extracted username from token: {}", userName);
            } catch (Exception e) {
                logger.error("Error while extracting username from token: {}", e.getMessage());
            }
        }

        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            logger.info("User '{}' is not authenticated, proceeding with token validation.", userName);

            try{
                UserDetails userDetails = userService.loadUserByUsername(userName);
                if(jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("User '{}' successfully authenticated.", userName);
                }else {
                    logger.warn("Token validation failed for user '{}'.", userName);
                }
            } catch (Exception e) {
                logger.error("Error during token validation or user authentication: {}", e.getMessage());
            }
        }
        logger.info("Continuing filter chain for request to URI: {}", request.getRequestURI());
        filterChain.doFilter(request, response);  // Continue the filter chain
    }


    public static void addToBlacklist(String token) {
        blacklistedTokens.add(token);
        logger.info("Token added to blacklist: {}", token);
    }

    private boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
