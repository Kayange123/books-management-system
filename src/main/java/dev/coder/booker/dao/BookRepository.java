package dev.coder.booker.dao;

import dev.coder.booker.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("""
        SELECT book FROM Book book WHERE book.archived = false 
        AND book.shareable = true AND book.owner.id != :id
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Long id);
}
