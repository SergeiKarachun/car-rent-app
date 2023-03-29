package by.sergey.carrentapp.domain.dto.car;

import by.sergey.carrentapp.annotation.EnumNamePattern;
import by.sergey.carrentapp.domain.model.Color;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class CarCreateRequestDto {
    @NotNull
    Long brandId;
    @NotNull
    Long modelId;
    @NotNull
    Long categoryId;
    @EnumNamePattern(regexp = "BLACK|GREEN|YELLOW|RED|GREY|SILVER|WHITE")
    Color color;
    @NotNull
    Integer year;
    @NotBlank(message = "Car number is required")
    String carNumber;
    @NotBlank(message = "VIN is required")
    @Size(min = 10)
    String vin;
    Boolean isRepaired;
    MultipartFile image;
}
