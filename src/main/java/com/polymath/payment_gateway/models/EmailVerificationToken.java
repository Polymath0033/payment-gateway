package com.polymath.payment_gateway.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_verification_token",indexes = {
        @Index(name = "idx_token",columnList = "token"),
        @Index(name = "idx_user_id",columnList = "user_id"),
        @Index(name = "idx_expires_at",columnList = "expires_at")
})
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private Users user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime verifiedAt;
    public EmailVerificationToken(Users user,String token,int expiresHour) {
        this.user = user;
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusHours(expiresHour);
    }


}
