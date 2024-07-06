package dev.coder.booker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter @Builder
public class AuthResponse {
    private String token;
}
