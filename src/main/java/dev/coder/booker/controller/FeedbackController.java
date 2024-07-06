package dev.coder.booker.controller;

import dev.coder.booker.dto.FeedbackRequest;
import dev.coder.booker.dto.FeedbackResponse;
import dev.coder.booker.dto.PageResponse;
import dev.coder.booker.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Feedback", description = "Feedback API")
@RequestMapping("feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("")
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackRequest feedbackRequest, Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedbackRequest, connectedUser));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacks(
            @PathVariable("bookId") Long bookId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            Authentication connectedUser,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }
}
