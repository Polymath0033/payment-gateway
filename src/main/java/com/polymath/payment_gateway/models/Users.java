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
    private boolean isActive = true;
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
