package dev.coder.booker.service.implementation;

import dev.coder.booker.constants.Utils;
import dev.coder.booker.dao.RoleRepository;
import dev.coder.booker.dao.TokenRepository;
import dev.coder.booker.dao.UserEntityRepository;
import dev.coder.booker.dto.AuthResponse;
import dev.coder.booker.dto.LoginCredentials;
import dev.coder.booker.dto.RegistrationRequest;
import dev.coder.booker.entity.user.UserEntity;
import dev.coder.booker.entity.user.Token;
import dev.coder.booker.enumeration.EmailTemplate;
import dev.coder.booker.security.JwtService;
import dev.coder.booker.service.AuthService;
import dev.coder.booker.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MailService mailService;

    @Value("${application.mailing.front-end.url}")
    private String confirmationUrl;

    @Override
    public void register(RegistrationRequest registration) throws MessagingException {
        var role = roleRepository.findByName("USER").orElseThrow(
                ()-> new IllegalStateException("User role not found")
        );
        var exists = userRepository.findByEmail(registration.getEmail());
        if(exists.isPresent()){
            throw new IllegalStateException("User with this email already exists");
        }
        var user = UserEntity.builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .email(registration.getEmail())
                .password(passwordEncoder.encode(registration.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(role))
                .build();

        userRepository.save(user);
        // Send Email to the USER for verification
        String token = generateAndSaveToken(user);
        sendEmail(user, token);
        log.info("Saving token"+token);
    }

    private void sendEmail(UserEntity user, String token) throws MessagingException {
        mailService.sendMail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                confirmationUrl,
                token,
                "Account Activation"
        );
    }

    @Override
    public AuthResponse authenticate(LoginCredentials loginCredentials) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCredentials.getEmail(),
                        loginCredentials.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((UserEntity) auth.getPrincipal());
        claims.put("fullName", user.getFullName());
        var token = jwtService.generateToken(claims, user);
        return AuthResponse.builder().token(token).build();
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("Could not find token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            // Resend the token to the User
            sendEmail(savedToken.getUser(), savedToken.getToken());
            throw new RuntimeException("Token already expired new token was sent");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()->new UsernameNotFoundException("User NOT Found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateAndSaveToken(UserEntity user){
        var otp = Utils.generateActivationOtp(6);
        var token = Token.builder()
                .token(otp)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        log.info("Token generated and saved successfully");

        return otp;
    }
}
