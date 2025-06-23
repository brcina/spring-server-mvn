package com.group.iso.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Paginated response wrapper")
public class PagedResponse<T> {

    @Schema(description = "List of content elements")
    private List<T> content;

    @Schema(description = "Current page number (0-based)")
    private int pageNumber;

    @Schema(description = "Size of the page")
    private int pageSize;

    @Schema(description = "Total number of elements available")
    private long totalElements;

    @Schema(description = "Total number of pages available")
    private int totalPages;

    @Schema(description = "Whether this is the last page")
    private boolean last;
}
