package by.sergey.carrentapp.domain.dto.category;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryResponseDto(Long id,
                                  String name,
                                  BigDecimal price) {
}
