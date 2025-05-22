package com.polymath.payment_gateway.repositories;

import com.polymath.payment_gateway.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserEmail(String email);
    void deleteByUserId(UUID userId);
    boolean existsByUserId(UUID userId);

}
