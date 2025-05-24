package com.polymath.payment_gateway.models;

import com.polymath.payment_gateway.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",indexes = {
        @Index(name="idx_users_email_active_verified",columnList = "email, active, email_verified"),
        @Index(name="idx_users_role_active", columnList = "role, active"),
        @Index(name = "idx_users_email_verified_role",columnList = "email_verified, role"),
        @Index(name = "idx_users_active_created_at",columnList = "active, created_at"),
        @Index(name = "idx_users_created_role",columnList = "created_at,role")

})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "Username cannot be null")
    @NotBlank(message="Username must not be empty")
    @Column(nullable = false, unique = true)
    private String username;
    @NotNull
    @NotBlank(message = "Password cannot be empty")
    @Length(min = 5, message = "Password must be at least 5 characters")
    @Column(nullable = false)
    private String password;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT true")
    private boolean active = true;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT false")
    private boolean emailVerified = false;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
