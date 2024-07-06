package dev.coder.booker.dao;

import dev.coder.booker.entity.book.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.book.id = :bookId
            ORDER BY feedback.createdAt DESC
            
    """)
    Page<Feedback> findAllByBookId(Long bookId, Pageable pageable);
}
