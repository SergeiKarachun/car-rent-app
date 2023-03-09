package by.sergey.carrentapp.domain.dto.car;

import by.sergey.carrentapp.domain.model.Color;
import org.springframework.web.multipart.MultipartFile;

public record CarCreateRequestDto(Long brandId,
                                  Long modelId,
                                  Long categoryId,
                                  Color color,
                                  Integer year,
                                  String carNumber,
                                  String vin,
                                  Boolean isRepaired,
                                  MultipartFile image) {
}
