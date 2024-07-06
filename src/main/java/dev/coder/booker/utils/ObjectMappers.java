package dev.coder.booker.utils;

import dev.coder.booker.dto.*;
import dev.coder.booker.entity.book.Book;
import dev.coder.booker.entity.book.BookTransactionHistory;
import dev.coder.booker.entity.book.Feedback;
import org.springframework.security.core.Authentication;

public abstract class ObjectMappers {
    public static Book convertToBook(BookRequest bookRequest){
       return Book.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .shareable(bookRequest.shareable())
                .build();
    }

    public static BookResponse convertToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .authorName(book.getAuthorName())
                .shareable(book.isShareable())
                .archived(book.isArchived())
                .isbn(book.getIsbn())
                .coverImageUrl(FileUtils.getBookCoverUrl(book.getBookCover()))
                .owner(book.getOwner().getFullName())
                .build();
    }

    public static BorrowedBooksResponse convertToBorrowedBooksResponse(BookTransactionHistory history) {
        return BorrowedBooksResponse.builder()
                .id(history.getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }

    public static Feedback convertToFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .description(request.comment())
                .rate(request.note())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();
    }

    public static FeedbackResponse convertToFeedbackResponse(Feedback feedback, Long userId) {
        return FeedbackResponse.builder()
                .rating(feedback.getRate())
                .comment(feedback.getDescription())
                .anonymous(false)
                .ownFeedback(feedback.getCreatedBy().equals(userId))
                .build();
    }
}
