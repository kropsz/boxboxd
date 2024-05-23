package com.kropsz.github.backendboxboxd.web.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto{
    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    @Size(min = 3)
    @NotEmpty
    String username;
    byte photo;
    @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @NotEmpty
    String email;
    @Size(min = 5)
    @NotEmpty
    String password;
}

