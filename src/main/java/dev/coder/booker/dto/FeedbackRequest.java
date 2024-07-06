package dev.coder.booker.dto;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
        @NotNull(message = "203")
                @NotEmpty(message = "203")
                @NotBlank(message = "203")
        String comment,
        @Positive(message = "200")
                @Min(value = 0, message = "201")
                @Max(value = 5, message = "202")
        double note,
        @NotNull(message = "204")
                @Positive(message = "204")
        Long bookId
) {}
