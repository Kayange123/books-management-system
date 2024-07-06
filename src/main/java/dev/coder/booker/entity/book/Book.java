package dev.coder.booker.entity.book;

import dev.coder.booker.entity.AuditableEntity;
import dev.coder.booker.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Book extends AuditableEntity {

    private String title;
    private String description;
    private String authorName;
    private String isbn;
    private String bookCover;
    private String synopsis;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if(feedbacks == null || feedbacks.isEmpty()) return 0.0;
        return this.feedbacks.stream().mapToDouble(Feedback::getRate)
                .average()
                .orElse(0.0);
    }
}
