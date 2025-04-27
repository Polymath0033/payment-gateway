package com.polymath.payment_gateway;

import com.polymath.payment_gateway.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    public Users findByEmail(String email);
}
