package dev.coder.booker.controller;

import dev.coder.booker.dto.ApiResponse;
import dev.coder.booker.dto.AuthResponse;
import dev.coder.booker.dto.LoginCredentials;
import dev.coder.booker.dto.RegistrationRequest;
import dev.coder.booker.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(
            @Valid @RequestBody RegistrationRequest registration
    ) throws MessagingException {
        authService.register(registration);
        return ResponseEntity.ok(ApiResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                .message("Registered successful")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginCredentials loginCredentials){
        AuthResponse response = authService.authenticate(loginCredentials);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token) throws MessagingException {
        authService.activateAccount(token);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Account activated successfully")
                        .build()
        );
    }
}
