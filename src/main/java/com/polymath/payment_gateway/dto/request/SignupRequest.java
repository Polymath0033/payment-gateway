package com.polymath.payment_gateway.dto.request;

import com.polymath.payment_gateway.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SignupRequest(@NotBlank(message = "Username cannot be empty") String username, @NotBlank(message = "Password cannot be empty") @Length(min = 5, message = "Password length must be at least 5 characters") String password, @NotBlank(message = "Email cannot be blank")  @Email(message = "Invalid email format") String email, @NotNull
                            Role role) {
}
