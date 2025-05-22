package com.polymath.payment_gateway.services.impl;

import com.polymath.payment_gateway.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}