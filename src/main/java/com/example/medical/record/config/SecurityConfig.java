package com.example.medical.record.config;

import com.example.medical.record.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authz -> authz
                    .requestMatchers("/patients/*").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")

                    .requestMatchers("/medicalVisits/history/patient/*").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")

                    .requestMatchers("/medicalVisits/history/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/doctors/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/medicalVisits/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/insurance/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/sickLeaves/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/specialties/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/diagnoses/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/treatments/**").hasAnyAuthority("ADMIN", "DOCTOR")

                    .requestMatchers("/**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
        ;
        return http.build();
    }
}
