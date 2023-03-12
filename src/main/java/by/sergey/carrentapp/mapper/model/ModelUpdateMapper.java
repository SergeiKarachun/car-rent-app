package by.sergey.carrentapp.mapper.model;

import by.sergey.carrentapp.domain.dto.model.ModelUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelUpdateMapper implements UpdateMapper<ModelUpdateRequestDto, Model> {

    @Override
    public Model mapToEntity(ModelUpdateRequestDto dto, Model entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(ModelUpdateRequestDto dto, Model entity) {
        entity.setName(dto.getName());
        entity.setTransmission(dto.getTransmission());
        entity.setEngineType(dto.getEngineType());
    }
}
