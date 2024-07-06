package dev.coder.booker.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Setter @Getter @Builder @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
    private Integer code;
    private String description;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;
}
