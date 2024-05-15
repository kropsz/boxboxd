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
    @Email
    @NotEmpty
    String email;
    @Size(min = 5)
    @NotEmpty
    String password;
}

