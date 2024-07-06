package dev.coder.booker.entity.book;

import dev.coder.booker.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Feedback extends AuditableEntity {
    private Double rate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
