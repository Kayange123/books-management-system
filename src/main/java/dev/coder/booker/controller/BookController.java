package dev.coder.booker.controller;

import dev.coder.booker.dto.BookRequest;
import dev.coder.booker.dto.BookResponse;
import dev.coder.booker.dto.BorrowedBooksResponse;
import dev.coder.booker.dto.PageResponse;
import dev.coder.booker.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {
    private final BookService bookService;

    @PostMapping("")
    public ResponseEntity<Long> saveBook( @Valid @RequestBody BookRequest book, Authentication connectedUser){
        return ResponseEntity.ok(bookService.saveBook(book, connectedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookService.findBookById(id));
    }

    @GetMapping()
    public ResponseEntity<PageResponse<BookResponse>> findBooksByPage(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
            ){
        return ResponseEntity.ok(bookService.findAllBooksByPage(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<?> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{bookId}")
    public ResponseEntity<Long> updateBookStatus(@PathVariable("bookId") Long id, Authentication connectedUser){

       return ResponseEntity.ok(bookService.updateBookStatus(id, connectedUser));

    }

    @PatchMapping("/archived/{bookId}")
    public ResponseEntity<Long> updateArchiveStatus(@PathVariable("bookId") Long id, Authentication connectedUser){
        return ResponseEntity.ok(bookService.updateArchiveStatus(id, connectedUser));
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<Long> borrowBook(@PathVariable("bookId") Long id, Authentication connectedUser){
        return ResponseEntity.ok(bookService.borrowBook(id, connectedUser));
    }

    @PatchMapping("/borrow/return/{bookId}")
    public ResponseEntity<Long> returnBook(@PathVariable("bookId") Long id, Authentication connectedUser){
        return ResponseEntity.ok(bookService.returnBook(id, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{bookId}")
    public ResponseEntity<Long> approveReturnBook(@PathVariable("bookId") Long id, Authentication connectedUser){
        return ResponseEntity.ok(bookService.approveReturnBook(id, connectedUser));
    }

    @PostMapping(value = "/cover/{bookId}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverImage(@PathVariable("bookId") Long bookId, @Parameter() @RequestPart("file") MultipartFile file, Authentication connectedUser){
        bookService.uploadBookCoverImage( file, connectedUser, bookId );
        return ResponseEntity.accepted().build();
    }
}
