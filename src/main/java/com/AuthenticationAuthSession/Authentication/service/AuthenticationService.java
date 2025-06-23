package com.AuthenticationAuthSession.Authentication.service;

import com.AuthenticationAuthSession.Authentication.entity.User;
import com.AuthenticationAuthSession.Authentication.entity.UserDTO;
import com.AuthenticationAuthSession.Authentication.entity.VerificationToken;
import com.AuthenticationAuthSession.Authentication.repository.UserRepository;
import com.AuthenticationAuthSession.Authentication.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // This is used to encode the password before saving it to the database
    //we use bycrypet liyberary

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public User registerUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUserId());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(false);//after email verification, we will set it to true
        user.setRole("USER"); // Default role for new users
        return userRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!user.isEnabled()) // Set the account as disabled if isEnabled is false
                .build();

    }

    public void persistVerificationToken(User registeredUser, String generatedToken) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(registeredUser);
        verificationToken.setToken(generatedToken);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // Token valid for 24 hours
        verificationTokenRepository.save(verificationToken);

    }

    public boolean verifyRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) {
            return false; // Token not found
        }

        long registeredExpiryDate = verificationToken.getExpiryDate().getTime();
        if(registeredExpiryDate < System.currentTimeMillis()) {
            return false; // Token has expired
        }

        return true;
    }

    public void enableUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken != null) {
            User user = verificationToken.getUser();
            user.setEnabled(true); // Enable the user account
            userRepository.save(user); // Save the updated user
            verificationTokenRepository.delete(verificationToken); // Optionally delete the token after use
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
