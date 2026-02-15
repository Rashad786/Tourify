package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private UserRepo userRepo;

    // This is the only key you need. 
    // It pulls from Dashoard -> application.properties -> JVM
    @Value("${service.jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        logger.info("Generating token for username: {}", username);
        String role = userRepo.findRoleByUsername(username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        // .trim() is important because copy-pasting from dashboards 
        // often adds invisible spaces or newlines
        byte[] keyBytes = Decoders.BASE64.decode(secretKey.trim()); 
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUserName(String token) {
        logger.info("Extracting username from token.");
        return extractClaim(token, Claims::getSubject); // Retrieve the subject claim (username)
    }

    public String extractUserRole(String token) {
        logger.info("Extracting user role from token.");
        return extractClaim(token, claims -> claims.get("role", String.class)); // Retrieve the "role" claim from the token
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token); // Extract all claims
        return claimResolver.apply(claims); // Resolve specific claim
    }

    private Claims extractAllClaims(String token) {
        logger.info("Extracting all claims from token.");
        return Jwts.parser()
                .setSigningKey(getKey()) // Use signing key to parse the token
                .build()
                .parseClaimsJws(token)
                .getBody(); // Retrieve the claims
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        logger.info("Validating token for user: {}", userDetails.getUsername());
        final String userName = extractUserName(token);
        boolean isValid = userName.equals(userDetails.getUsername()) && !isTokenExpired(token); // Check username match and expiration
        if (isValid) {
            logger.info("Token is valid for user: {}", userDetails.getUsername());
        } else {
            logger.warn("Token validation failed for user: {}", userDetails.getUsername());
        }
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        logger.info("Checking if token is expired.");
        return extractExpiration(token).before(new Date()); // Compare token expiration with current time
    }

    private Date extractExpiration(String token) {
        logger.info("Extracting expiration date from token.");
        return extractClaim(token, Claims::getExpiration); // Retrieve expiration date from claims
    }
}
