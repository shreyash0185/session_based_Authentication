package com.AuthenticationAuthSession.Authentication.repository;

import com.AuthenticationAuthSession.Authentication.entity.User;
import com.AuthenticationAuthSession.Authentication.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    void saveVerificationToken(VerificationToken verificationToken);
}
