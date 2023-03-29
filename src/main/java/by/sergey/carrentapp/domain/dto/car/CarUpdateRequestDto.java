package by.sergey.carrentapp.domain.dto.car;

import by.sergey.carrentapp.annotation.EnumNamePattern;
import by.sergey.carrentapp.domain.model.Color;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class CarUpdateRequestDto {
    @NotNull
    Long modelId;
    @NotNull
    Long categoryId;
    @EnumNamePattern(regexp = "BLACK|GREEN|YELLOW|RED|GREY|SILVER|WHITE")
    Color color;
    @NotNull
    Integer year;
    @NotBlank
    String carNumber;
    Boolean isRepaired;
    MultipartFile image;
}
