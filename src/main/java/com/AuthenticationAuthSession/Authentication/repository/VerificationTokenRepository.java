package com.AuthenticationAuthSession.Authentication.repository;

import com.AuthenticationAuthSession.Authentication.entity.User;
import com.AuthenticationAuthSession.Authentication.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    void deleteByUser(User user);
}
