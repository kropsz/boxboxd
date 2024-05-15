package com.kropsz.github.backendboxboxd.web.controller;

import com.kropsz.github.backendboxboxd.service.SessionService;
import com.kropsz.github.backendboxboxd.web.dtos.LoginResponse;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;
import com.kropsz.github.backendboxboxd.web.dtos.UserRegisterDto;
import com.kropsz.github.backendboxboxd.web.dtos.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boxboxd")
public class SessionController {

    private  final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegisterDto register) {
        var user = sessionService.registerUser(UserMapper.toUser(register));
        String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("profile", user.getUsername())
                .build()
                .toUriString();
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> makeLogin(@RequestBody @Valid UserLoginDto login) {
        return ResponseEntity.ok(sessionService.makeLogin(login));
    }
}
