package by.sergey.carrentapp.domain.dto.category;

import java.math.BigDecimal;

public record CategoryCreateUpdateRequestDto(String name,
                                             BigDecimal price) {
}
