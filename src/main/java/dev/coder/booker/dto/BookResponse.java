package dev.coder.booker.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class BookResponse {
    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String description;
    private String owner;
    private byte[] coverImageUrl;
    private double rate;
    private boolean archived;
    private boolean shareable;
}
