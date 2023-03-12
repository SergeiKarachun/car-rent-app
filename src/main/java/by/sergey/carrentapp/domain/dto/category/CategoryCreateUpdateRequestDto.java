package by.sergey.carrentapp.domain.dto.category;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class CategoryCreateUpdateRequestDto {
    String name;
    BigDecimal price;
}
