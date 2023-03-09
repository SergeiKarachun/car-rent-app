package by.sergey.carrentapp.mapper.car;

import by.sergey.carrentapp.domain.dto.car.CarCreateRequestDto;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.repository.BrandRepository;
import by.sergey.carrentapp.repository.CategoryRepository;
import by.sergey.carrentapp.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class CarCreateMapper implements CreateMapper<CarCreateRequestDto, Car> {
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Car mapToEntity(CarCreateRequestDto requestDto) {
        var brand = getBrand(requestDto.brandId());
        var model = getModel(requestDto.modelId());
        var category = getCategory(requestDto.categoryId());

        var car = Car.builder()
                .color(requestDto.color())
                .year(requestDto.year())
                .carNumber(requestDto.carNumber())
                .vin(requestDto.vin())
                .repaired(requestDto.isRepaired())
                .build();
        car.setBrand(brand);
        car.setModel(model);
        car.setCategory(category);

        Optional.ofNullable(requestDto.image())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> car.setImage(image.getOriginalFilename()));

        return car;
    }

    private Brand getBrand(Long brandId) {
        return Optional.ofNullable(brandId)
                .flatMap(brandRepository::findById)
                .orElse(null);
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
