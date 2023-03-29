package by.sergey.carrentapp.domain.dto.brand;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Value
@Builder
public class BrandResponseDto {
    @NotEmpty
    Long id;
    String name;

}
