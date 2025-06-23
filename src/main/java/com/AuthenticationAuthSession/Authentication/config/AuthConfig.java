package com.AuthenticationAuthSession.Authentication.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configurable
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/register","/verifyRegistration").permitAll() // Allow access to all learners endpoints
                                .anyRequest().authenticated() // Require authentication for any other request
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/hellow") // Custom login page
                        .permitAll() // Allow all users to access the login page
                );

        return httpSecurity.build();
    }
}
