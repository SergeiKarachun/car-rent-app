package by.sergey.carrentapp.mapper.car;

import by.sergey.carrentapp.domain.dto.car.CarUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.mapper.UpdateMapper;
import by.sergey.carrentapp.repository.CategoryRepository;
import by.sergey.carrentapp.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.*;

@Component
@RequiredArgsConstructor
public class CarUpdateMapper implements UpdateMapper<CarUpdateRequestDto, Car> {

    private final ModelRepository modelRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Car mapToEntity(CarUpdateRequestDto dto, Car entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(CarUpdateRequestDto dto, Car entity) {
        entity.setColor(dto.getColor());
        entity.setYear(dto.getYear());
        entity.setRepaired(dto.getIsRepaired());
        entity.setCarNumber(dto.getCatNumber());
        entity.setCategory(getCategory(dto.getCategoryId()));

        if (!Objects.equals(dto.getCategoryId(), entity.getModel().getId())){
            var model = getModel(dto.getModelId());
            entity.setModel(model);
        }


        Optional.ofNullable(dto.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> entity.setImage(image.getOriginalFilename()));
    }


    private Model getModel(Long modelId) {
        return Optional.ofNullable(modelId)
                .flatMap(modelRepository::findById)
                .orElse(null);
    }

    private Category getCategory(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .flatMap(categoryRepository::findById)
                .orElse(null);
    }
}
