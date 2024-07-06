package dev.coder.booker.service;

import dev.coder.booker.dto.AuthResponse;
import dev.coder.booker.dto.LoginCredentials;
import dev.coder.booker.dto.RegistrationRequest;
import jakarta.mail.MessagingException;


public interface AuthService {
    void register(RegistrationRequest registration) throws MessagingException;

    AuthResponse authenticate(LoginCredentials loginCredentials);

    void activateAccount(String token) throws MessagingException;
}
