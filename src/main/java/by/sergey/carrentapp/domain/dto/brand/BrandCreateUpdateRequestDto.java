package by.sergey.carrentapp.domain.dto.brand;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;

@Value
@FieldNameConstants
public class BrandCreateUpdateRequestDto {

    @NotBlank(message = "Brand name is required")
    String name;
}
