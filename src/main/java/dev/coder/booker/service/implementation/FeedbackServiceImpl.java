package dev.coder.booker.service.implementation;

import dev.coder.booker.dao.BookRepository;
import dev.coder.booker.dao.FeedbackRepository;
import dev.coder.booker.dto.FeedbackRequest;
import dev.coder.booker.dto.FeedbackResponse;
import dev.coder.booker.dto.PageResponse;
import dev.coder.booker.entity.book.Feedback;
import dev.coder.booker.entity.user.UserEntity;
import dev.coder.booker.exception.OperationNotPermittedException;
import dev.coder.booker.service.FeedbackService;
import dev.coder.booker.utils.ObjectMappers;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public Long createFeedback(FeedbackRequest request, Authentication connectedUser) {
        var book = bookRepository.findById(request.bookId())
                .orElseThrow(()-> new EntityNotFoundException("Booking not found"));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You can't give feedback to Archived or non shareable books");
        }
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        if(Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You can't give feedback to your own books");
        }
        Feedback feedback = ObjectMappers.convertToFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackList = feedbacks.stream()
                .map(feedback -> ObjectMappers.convertToFeedbackResponse(feedback, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackList,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
