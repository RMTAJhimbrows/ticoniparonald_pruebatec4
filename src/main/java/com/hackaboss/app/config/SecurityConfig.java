package com.hackaboss.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/doc/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        // Endpoints Públicos (No requieren autenticación)
                        .requestMatchers(HttpMethod.GET, "/agency/hotels").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/rooms").permitAll()
                        .requestMatchers(HttpMethod.POST, "/agency/room-booking/new").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/flights").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/flights/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/agency/flight-booking/new").permitAll()

                        // Endpoints privados (Sí requieren autenticación)
                        .anyRequest().authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .httpBasic(httpBasic -> httpBasic.realmName("app"))
                .build();
    }
}
