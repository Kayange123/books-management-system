package dev.coder.booker.service.implementation;

import dev.coder.booker.common.BookSpecification;
import dev.coder.booker.dao.BookRepository;
import dev.coder.booker.dao.BookTransactionHistoryRepository;
import dev.coder.booker.dto.BookRequest;
import dev.coder.booker.dto.BookResponse;
import dev.coder.booker.dto.BorrowedBooksResponse;
import dev.coder.booker.dto.PageResponse;
import dev.coder.booker.entity.book.Book;
import dev.coder.booker.entity.book.BookTransactionHistory;
import dev.coder.booker.entity.user.UserEntity;
import dev.coder.booker.exception.OperationNotPermittedException;
import dev.coder.booker.service.BookService;
import dev.coder.booker.service.FileStorageService;
import dev.coder.booker.utils.ObjectMappers;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final FileStorageService fileStorageService;
    private final BookTransactionHistoryRepository transactionHistoryRepository;

    @Override
    public Long saveBook(BookRequest bookRequest, Authentication connectedUser) {
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Book book = ObjectMappers.convertToBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    @Override
    public BookResponse findBookById(Long id) {

        return bookRepository.findById(id)
                .map(ObjectMappers::convertToBookResponse)
                .orElseThrow(()-> new EntityNotFoundException("Book not found"));
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByPage(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(ObjectMappers::convertToBookResponse).toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(ObjectMappers::convertToBookResponse).toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBooksResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> bookHistory = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBooksResponse> borrowedBooks = bookHistory.stream()
                .map(ObjectMappers::convertToBorrowedBooksResponse)
                .toList();
        return new PageResponse<>(
                borrowedBooks,
                bookHistory.getNumber(),
                bookHistory.getSize(),
                bookHistory.getTotalElements(),
                bookHistory.getTotalPages(),
                bookHistory.isFirst(),
                bookHistory.isLast()
        );
    }

    @Override
    public PageResponse<?> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> bookHistory = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBooksResponse> borrowedBooks = bookHistory.stream()
                .map(ObjectMappers::convertToBorrowedBooksResponse)
                .toList();
        return new PageResponse<>(
                borrowedBooks,
                bookHistory.getNumber(),
                bookHistory.getSize(),
                bookHistory.getTotalElements(),
                bookHistory.getTotalPages(),
                bookHistory.isFirst(),
                bookHistory.isLast()
        );
    }

    @Override
    public Long updateBookStatus(Long id, Authentication connectedUser) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Book found with id " + id)
        );

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if(!Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You cannot update this book's status");
        }
        book.setShareable(!book.isShareable());
        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }

    @Override
    public Long updateArchiveStatus(Long id, Authentication connectedUser) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Book found with id " + id)
        );

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if(!Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You cannot update this book's status");
        }
        book.setArchived(!book.isArchived());
        Book savedBook = bookRepository.save(book);
        return savedBook.getId();

    }

    @Override
    public Long borrowBook(Long id, Authentication connectedUser) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Book found with id " + id)
        );
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The book cannot be borrowed since it is not shareable");
        }

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if(Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You cannot borrow your book");
        }
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isBorrowedByUser(id, user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }
        var bookTransaction = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransaction).getId();
    }

    @Override
    public Long returnBook(Long bookId, Authentication connectedUser) {
        var book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("No Book found with id " + bookId)
        );
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The book cannot be returned since it is not shareable");
        }

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if(Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You cannot return your book that you could not borrow");
        }
        var bookTransaction = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(()-> new OperationNotPermittedException("You cannot return book that you did not borrow"));
            bookTransaction.setReturned(true);
        return transactionHistoryRepository.save(bookTransaction).getId();
    }

    @Override
    public Long approveReturnBook(Long id, Authentication connectedUser) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Book found with id " + id)
        );
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The book cannot be returned since it is not shareable");
        }

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if(Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You cannot approve a return of book that you could not borrow");
        }
        var bookTransaction = transactionHistoryRepository.findByBookIdAndOwnerId(id, user.getId())
                .orElseThrow(()-> new OperationNotPermittedException("The book is NOT returned yet"));
        bookTransaction.setReturnApproved(true);
        return transactionHistoryRepository.save(bookTransaction).getId();
    }

    @Override
    public void uploadBookCoverImage(MultipartFile file, Authentication connectedUser, Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("No Book found with id " + bookId)
        );
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        if(!Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermittedException("You cannot update this book's cover image");
        }
        String bookCoverImage = fileStorageService.storeFile(file, user.getId());
        book.setBookCover(bookCoverImage);
        bookRepository.save(book);
    }
}
