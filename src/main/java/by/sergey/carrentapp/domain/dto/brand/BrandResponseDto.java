package by.sergey.carrentapp.domain.dto.brand;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class BrandResponseDto {
    Long id;
    String name;

}
