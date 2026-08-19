package com.tourism.app.controller;

import com.tourism.app.dao.UserDao;
import com.tourism.app.dto.ApiResponse;
import com.tourism.app.dto.LoginRequest;
import com.tourism.app.dto.RegisterRequest;
import com.tourism.app.dto.UserResponse;
import com.tourism.app.exception.DuplicateResourceException;
import com.tourism.app.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Simple email/password authentication for the tourism site.
 * Passwords are always hashed with BCrypt before hitting the database and
 * are never returned in any response.
 *
 * Note: for a production deployment you would layer a token (JWT) or session
 * on top of this so that subsequent requests are authenticated; this keeps
 * the scope to what the project needs to demonstrate — secure registration
 * and credential verification against MySQL via JDBC.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userDao.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        User saved = userDao.save(user);
        return ApiResponse.success("Registration successful", UserResponse.fromEntity(saved));
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userDao.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return ApiResponse.success("Login successful", UserResponse.fromEntity(user));
    }
}
