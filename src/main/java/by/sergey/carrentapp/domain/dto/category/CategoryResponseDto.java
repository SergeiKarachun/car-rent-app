package by.sergey.carrentapp.domain.dto.category;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Builder
public class CategoryResponseDto {
    @NotNull
    Long id;
    String name;
    BigDecimal price;
}
