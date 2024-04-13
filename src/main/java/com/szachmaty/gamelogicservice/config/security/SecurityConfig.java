package com.szachmaty.gamelogicservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .cors(cors -> cors.configurationSource(corsConfig()))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers("/game-handshake").permitAll()
                            .anyRequest().hasRole("USER");
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // TO-DO
//    @Bean
//    static SecurityContextHolderStrategy securityContextHolderStrategy() {
//        SecurityContextHolder.setStrategyName(MODE_INHERITABLETHREADLOCAL);
//        return SecurityContextHolder.getContextHolderStrategy();
//    }
//
//    @Bean
//    static MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
//        return new DefaultMethodSecurityExpressionHandler();
//    }

    private CorsConfigurationSource corsConfig() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.OPTIONS.name()
        ));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
