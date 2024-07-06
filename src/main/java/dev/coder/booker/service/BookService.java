package dev.coder.booker.service;


import dev.coder.booker.dto.BookRequest;
import dev.coder.booker.dto.BookResponse;
import dev.coder.booker.dto.BorrowedBooksResponse;
import dev.coder.booker.dto.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    Long saveBook(BookRequest book, Authentication connectedUser);

    BookResponse findBookById(Long id);

    PageResponse<BookResponse> findAllBooksByPage(int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBooksResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<?> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    Long updateBookStatus(Long id, Authentication connectedUser);

    Long updateArchiveStatus(Long id, Authentication connectedUser);

    Long borrowBook(Long id, Authentication connectedUser);

    Long returnBook(Long bookId, Authentication connectedUser);

    Long approveReturnBook(Long id, Authentication connectedUser);

    void uploadBookCoverImage(MultipartFile file, Authentication connectedUser, Long bookId);
}
