package com.kropsz.github.backendboxboxd.web.controller;

import com.kropsz.github.backendboxboxd.service.SessionService;
import com.kropsz.github.backendboxboxd.web.dtos.LoginResponse;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;
import com.kropsz.github.backendboxboxd.web.dtos.UserRegisterDto;
import com.kropsz.github.backendboxboxd.web.dtos.UserResponse;
import com.kropsz.github.backendboxboxd.web.dtos.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boxboxd")
public class SessionController {

    private  final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRegisterDto register) {
        var user = sessionService.registerUser(UserMapper.toUser(register));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> makeLogin(@RequestBody @Valid UserLoginDto login) {
        return ResponseEntity.ok(sessionService.makeLogin(login));
    }
}
