package by.sergey.carrentapp.mapper.model;

import by.sergey.carrentapp.domain.dto.model.ModelCreateRequestDto;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModelCreateMapper implements CreateMapper<ModelCreateRequestDto, Model> {

    private final BrandRepository brandRepository;

    @Override
    public Model mapToEntity(ModelCreateRequestDto requestDto) {
        var brand = getBrand(requestDto.getBrandId());
        var model = Model.builder()
                .name(requestDto.getName())
                .engineType(requestDto.getEngineType())
                .transmission(requestDto.getTransmission())
                .build();

        brand.ifPresent(br -> br.setModel(model));
        return model;
    }

    private Optional<Brand> getBrand(Long brandId) {
        return Optional.ofNullable(brandId)
                .flatMap(brandRepository::findById);
    }
}
