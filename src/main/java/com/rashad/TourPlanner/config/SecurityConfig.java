package com.rashad.TourPlanner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rashad.TourPlanner.repo.UserRepo;
import com.rashad.TourPlanner.service.CustomOAuth2UserService;
import com.rashad.TourPlanner.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtFilter jwtFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          JwtFilter jwtFilter,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.jwtFilter = jwtFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring HTTP Security filters.");
        return http
                .cors(cors -> {
                    logger.info("Configuring CORS with allowed origins and methods.");
                    cors.configurationSource(corsConfigurationSource());
                })
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/signup", "/auth/login", "/login/oauth2", "/error",
                                "/swagger-ui/**", "/v3/api-docs/**", "/auth/email").permitAll()
                        .requestMatchers("/auth/admin/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/auth/customer/**", "/customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .logout(logout -> logout
                        .logoutUrl("/auth/logout").logoutSuccessHandler((request, response, authentication) -> {
                            String token = request.getHeader("Authorization");
                            if(token != null && token.startsWith("Bearer ")) {
                                token = token.substring(7);
                                jwtFilter.addToBlacklist(token);
                                logger.info("Token has been blacklisted after logout.");
                            }
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"message\":\"Logout Successful\"}");
                            response.getWriter().flush();
                        }))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("Configuring CORS Configuration.");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://tourifys.netlify.app/")); // Replace it with your frontend URL
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("x-auth-token"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setExposedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        logger.info("CORS Configuration successfully set.");
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // optional: to serialize dates as ISO-8601 strings
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
