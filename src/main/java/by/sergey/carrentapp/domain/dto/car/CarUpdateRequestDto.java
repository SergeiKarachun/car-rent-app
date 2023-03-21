package by.sergey.carrentapp.domain.dto.car;

import by.sergey.carrentapp.domain.model.Color;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class CarUpdateRequestDto {
    Long modelId;
    Long categoryId;
    Color color;
    Integer year;
    String carNumber;
    Boolean isRepaired;
    MultipartFile image;
}
