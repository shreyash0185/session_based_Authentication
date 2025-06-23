package com.AuthenticationAuthSession.Authentication.controller;

import com.AuthenticationAuthSession.Authentication.entity.User;
import com.AuthenticationAuthSession.Authentication.entity.UserDTO;
import com.AuthenticationAuthSession.Authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public User registerUser(@RequestBody UserDTO userDTO){
        User registeredUser = authenticationService.registerUser(userDTO);
        String generatedToken = UUID.randomUUID().toString();
        String applicationUrl = "http://localhost:8080/verify?token=" + generatedToken;
        System.out.println("Verification link: " + applicationUrl); //send the mail for this as wedont have SMTP server for nw -> we nned SMTp server to send the mail
        authenticationService.persistVerificationToken(registeredUser, generatedToken);
        return registeredUser;
    }

    @PostMapping("/verifyRegistration")
    public String verifyRegistration(@RequestBody String token){
        boolean isVerified = authenticationService.verifyRegistration(token);
        if (isVerified) {
            authenticationService.enableUser(token);
            return "User registration verified successfully.";
        } else {
            return "Invalid or expired verification token.";
        }
    }

    @GetMapping("/hellow")
    public String helloWorld() {
        return "Hello World";
    }


}
