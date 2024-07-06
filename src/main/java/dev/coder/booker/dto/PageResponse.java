package dev.coder.booker.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class PageResponse<T> {
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
