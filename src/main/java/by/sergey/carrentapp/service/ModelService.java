package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.filterdto.ModelFilter;
import by.sergey.carrentapp.domain.dto.model.ModelCreateRequestDto;
import by.sergey.carrentapp.domain.dto.model.ModelResponseDto;
import by.sergey.carrentapp.domain.dto.model.ModelUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.mapper.model.ModelCreateMapper;
import by.sergey.carrentapp.mapper.model.ModelResponseMapper;
import by.sergey.carrentapp.mapper.model.ModelUpdateMapper;
import by.sergey.carrentapp.repository.ModelRepository;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.utils.PageableUtils;
import by.sergey.carrentapp.utils.predicate.ModelPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelCreateMapper modelCreateMapper;
    private final ModelResponseMapper modelResponseMapper;
    private final ModelUpdateMapper modelUpdateMapper;
    private final ModelPredicateBuilder modelPredicateBuilder;

    @Transactional
    public Optional<ModelResponseDto> create(ModelCreateRequestDto modelCreateRequestDto) {
        return Optional.of(modelCreateMapper.mapToEntity(modelCreateRequestDto))
                .map(modelRepository::save)
                .map(modelResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<ModelResponseDto> update(Long id, ModelUpdateRequestDto modelUpdateRequestDto) {
        var existingModel = getByIdOrElseThrow(id);

        return Optional.of(modelUpdateMapper.mapToEntity(modelUpdateRequestDto, existingModel))
                .map(modelRepository::save)
                .map(modelResponseMapper::mapToDto);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (modelRepository.existsById(id)) {
            modelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<ModelResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(modelResponseMapper::mapToDto);
    }

    public List<ModelResponseDto> getAll() {
        return modelRepository.findAll().stream()
                .map(modelResponseMapper::mapToDto)
                .toList();
    }

    public List<ModelResponseDto> getAllByBrandId(Long id) {
        return modelRepository.findModelsByBrandId(id).stream()
                .map(modelResponseMapper::mapToDto)
                .toList();
    }

    public List<ModelResponseDto> getAllByFilter(ModelFilter modelFilter){
        return modelRepository.findAllByFilter(modelFilter)
                .stream().map(modelResponseMapper::mapToDto)
                .toList();
    }

    public Page<ModelResponseDto> getAll(ModelFilter modelFilter, Integer page, Integer pageSize) {
        return modelRepository
                .findAll(modelPredicateBuilder.build(modelFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "brand_name"))
                .map(modelResponseMapper::mapToDto);
    }

    private Model getByIdOrElseThrow(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Model", "id", id)));
    }
}
