package com.example.shikakurush.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/images/**",
                                "/user/start/go",
                                "/user/login",
                                "/user/logout",
                                "/user/registered-email",
                                "/user/send-registration-email",
                                "/user/registration",
                                "/user/top",
                                "/user/my-page",
                                "/user/my-page/username",
                                "/user/my-page/title",
                                "/user/my-page/delete",
                                "/user/password-reset-email",
                                "/user/password-reset",
                                "/user/password-reset-complete",
                                "/user/genre",
                                "/user/category",
                                "/user/difficulty",
                                "/user/loading",
                                "/user/game",
                                "/user/game/change",
                                "/user/game/answer",
                                "/user/game/giveup",
                                "/user/result",
                                "/user/inquiry",
                                "/user/notification",
                                "/user/notice",
                                "/user/ranking",
                                "/user/help",
                                "/api/**",
                                "/admin/login",
                                "/admin/top",
                                "/admin/logout",
                                "/admin/**",
                                "/user/report",
                                "/user/report/complete"
                        ).permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().authenticated()
                        
                )
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/security/login")
                        .defaultSuccessUrl("/user/start")
                        .failureUrl("/user/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/user/login")
                        .invalidateHttpSession(true)
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}