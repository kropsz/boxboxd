package com.kropsz.github.backendboxboxd.common;

import com.kropsz.github.backendboxboxd.entities.Role;
import com.kropsz.github.backendboxboxd.entities.User;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;
import com.kropsz.github.backendboxboxd.web.dtos.UserRegisterDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class UserConstants {

    public static String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
    

    public static final String password = "maclaren";
    public static final String passwordEncode = encryptPassword(password);

    public static final User VALID_USER = createValidUser();

    private static User createValidUser() {
        User user = new User();
        user.setId(2L);
        user.setFirstName("Pedro");
        user.setLastName("Esteves");
        user.setUsername("pedrinho");
        user.setPhoto((byte) 0);
        user.setEmail("pedro@example.com");
        user.setPassword(passwordEncode);

        Role role = new Role();
        role.setRoleId(1L);
        role.setName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        return user;
    }

    public static final UserLoginDto VALID_LOGIN = new UserLoginDto("pedrinho", password);

    public static final UserRegisterDto VALID_REGISTER = new UserRegisterDto(
            "Pedro",
            "Esteves",
            "pedrinho",
            (byte) 0,
            "pedro@example.com",
            password
    );

    public static final UserRegisterDto EXISTING_USER = new UserRegisterDto("Jhon", "Doe", "johndoe", (byte) 0, "johndoe@example.com", "password");

    public static final UserRegisterDto INVALID_REGISTER = new UserRegisterDto("Jhon", 
    "", 
    "johndoe", 
    (byte) 0, 
    "johndoeexample.com", 
    "password");


}
