package by.sergey.carrentapp.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(title = "Base wrapper for api response with pagination information")
@Value
public class PageResponse<T> {
    @Schema(description = "Actual return value of type <T>")
    List<T> content;
    @Schema(description = "Return metadata")
    Metadata metadata;


    public static <T> PageResponse<T> of(Page<T> page) {
        Metadata metadata = new Metadata(page.getNumber(), page.getSize(), page.isFirst(), page.isLast(), page.getNumberOfElements(), page.getTotalElements(), page.getTotalPages());
        return new PageResponse<>(page.getContent(), metadata);
    }


    @Value
    public static class Metadata {
        @Schema(description = "Pagination: number of page")
        int page;
        @Schema(description = "Pagination: number of items returned")
        int size;
        @Schema(description = "Pagination: true if this is the first page of results")
        boolean first;
        @Schema(description = "Pagination: true if this is the last page of results")
        boolean last;
        @Schema(description = "Pagination: number of results in given page")
        int numberOfElements;
        @Schema(description = "Pagination: total number of elements on page")
        long totalElements;
        @Schema(description = "Pagination: total number of pages containing results")
        int totalPages;
    }

}
