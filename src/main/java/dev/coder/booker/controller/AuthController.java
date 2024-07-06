package dev.coder.booker.controller;

import dev.coder.booker.dto.LoginCredentials;
import dev.coder.booker.dto.RegistrationRequest;
import dev.coder.booker.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegistrationRequest registration
    ) throws MessagingException {
        authService.register(registration);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginCredentials loginCredentials){
        return ResponseEntity.ok(authService.authenticate(loginCredentials));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token) throws MessagingException {
        authService.activateAccount(token);
        return ResponseEntity.ok().build();
    }
}
