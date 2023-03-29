package by.sergey.carrentapp.domain.dto.brand;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class BrandCreateUpdateRequestDto {

    @NotBlank(message = "Brand name is required")
    String name;
}
