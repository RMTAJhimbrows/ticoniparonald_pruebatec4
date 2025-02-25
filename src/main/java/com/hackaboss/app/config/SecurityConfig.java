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
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/doc/**", "/swagger-ui.html", "/swagger-ui/**, /agency/room-booking/new").permitAll()
                        .requestMatchers(HttpMethod.POST, " /agency/room-booking/new").permitAll()
                        .requestMatchers("/agency/hotels/new", "/agency/hotels/delete").authenticated()
                        .requestMatchers("/agency/flights/new", "/agency/flights/delete").authenticated()
                        .anyRequest().authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .httpBasic(httpBasic -> httpBasic.realmName("app"))
                .build();
    }
}
