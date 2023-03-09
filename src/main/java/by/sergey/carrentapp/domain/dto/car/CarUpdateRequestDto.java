package by.sergey.carrentapp.domain.dto.car;

import by.sergey.carrentapp.domain.model.Color;
import org.springframework.web.multipart.MultipartFile;

public record CarUpdateRequestDto(Long modelId,
                                  Long categoryId,
                                  Color color,
                                  Integer year,
                                  String catNumber,
                                  Boolean isRepaired,
                                  MultipartFile image) {
}
