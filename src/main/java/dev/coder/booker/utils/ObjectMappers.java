package dev.coder.booker.utils;

import dev.coder.booker.dto.BookRequest;
import dev.coder.booker.dto.BookResponse;
import dev.coder.booker.dto.BorrowedBooksResponse;
import dev.coder.booker.entity.book.Book;
import dev.coder.booker.entity.book.BookTransactionHistory;

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
}
