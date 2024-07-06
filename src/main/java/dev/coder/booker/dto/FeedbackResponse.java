package dev.coder.booker.dto;

import lombok.*;

@Getter
@Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class FeedbackResponse {
    private Double rating;
    private String comment;
    private boolean anonymous;
    private boolean ownFeedback;
}
