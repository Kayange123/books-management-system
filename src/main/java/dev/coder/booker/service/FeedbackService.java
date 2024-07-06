package dev.coder.booker.service;

import dev.coder.booker.dto.FeedbackRequest;
import dev.coder.booker.dto.FeedbackResponse;
import dev.coder.booker.dto.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Long createFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size, Authentication connectedUser);
}
