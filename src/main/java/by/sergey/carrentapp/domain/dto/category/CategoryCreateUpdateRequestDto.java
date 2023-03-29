package by.sergey.carrentapp.domain.dto.category;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public class CategoryCreateUpdateRequestDto {
    @NotBlank(message = "Name is required")
    String name;
    @NotNull(message = "Price is required")
    BigDecimal price;
}
