package by.sergey.carrentapp.domain.dto.category;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CategoryResponseDto {
    Long id;
    String name;
    BigDecimal price;
}
