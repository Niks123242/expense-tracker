package com.nikhil.expense_tracker.controller;

import com.nikhil.expense_tracker.dto.LoginRequest;
import com.nikhil.expense_tracker.dto.RegisterRequest;
import com.nikhil.expense_tracker.security.CustomUserDetails;
import com.nikhil.expense_tracker.security.CustomUserDetailsService;
import com.nikhil.expense_tracker.service.UserService;
import com.nikhil.expense_tracker.util.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(userDetails.getUserId(), request.getEmail());

        return ResponseEntity.ok(token);
    }

    @GetMapping("/secure-test")
    public String test() {
        return "Authenticated!";
    }
}
