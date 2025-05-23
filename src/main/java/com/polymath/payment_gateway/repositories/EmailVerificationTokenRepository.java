package com.polymath.payment_gateway.repositories;

import com.polymath.payment_gateway.models.EmailVerificationToken;
import com.polymath.payment_gateway.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByUser(Users user);

    @Query("SELECT evt FROM EmailVerificationToken evt WHERE evt.user = :user AND evt.verifiedAt IS NULL")
    Optional<EmailVerificationToken> findUnverifyUser(@Param("user")Users user);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken  evt WHERE evt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken  evt WHERE evt.user = :user")
    void deleteByUser(@Param("user") Users user);



}
