package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.brand.BrandResponseDto;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.projection.BrandFullView;
import by.sergey.carrentapp.mapper.brand.BrandCreateMapper;
import by.sergey.carrentapp.mapper.brand.BrandResponseMapper;
import by.sergey.carrentapp.mapper.brand.BrandUpdateMapper;
import by.sergey.carrentapp.repository.BrandRepository;
import by.sergey.carrentapp.service.exception.BrandBadRequestException;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandCreateMapper brandCreateMapper;
    private final BrandResponseMapper brandResponseMapper;
    private final BrandUpdateMapper brandUpdateMapper;

    @Transactional
    public Optional<BrandResponseDto> create(BrandCreateUpdateRequestDto brandCreateUpdateRequestDto) {
        checkUniqueBrandName(brandCreateUpdateRequestDto.name());

        return Optional.of(brandCreateMapper.mapToEntity(brandCreateUpdateRequestDto))
                .map(brandRepository::save)
                .map(brandResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<BrandResponseDto> update(Long id, BrandCreateUpdateRequestDto brandCreateUpdateRequestDto) {
        var existingbrand = getByIdOrElseThrow(id);

        if (!existingbrand.getName().equals(brandCreateUpdateRequestDto.name())) {
            checkUniqueBrandName(brandCreateUpdateRequestDto.name());
        }

        return Optional.of(brandUpdateMapper.mapToEntity(brandCreateUpdateRequestDto, getByIdOrElseThrow(id)))
                .map(brandRepository::save)
                .map(brandResponseMapper::mapToDto);
    }

    public Optional<BrandResponseDto> getById(long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(brandResponseMapper::mapToDto);
    }

    public Page<BrandResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "name");
        return brandRepository.findAll(pageRequest)
                .map(brandResponseMapper::mapToDto);
    }

    public List<BrandResponseDto> getAll() {
        return brandRepository.findAll()
                .stream().map(brandResponseMapper::mapToDto)
                .toList();
    }

    public List<BrandFullView> getAllFullView() {
        return brandRepository.findAllByFullView();
    }

    public Optional<BrandFullView> getByIdFullView(Long id) {
        return brandRepository.findFullViewById(id);
    }

    public List<BrandFullView> getByNameFullView(String name) {
        return brandRepository.findAllFullViewByName(name);
    }

    public Optional<BrandResponseDto> getByName(String name) {
        return brandRepository.findByName(name)
                .map(brandResponseMapper::mapToDto);
    }

    public List<BrandResponseDto> getByNames (List<String> names) {
        return brandRepository.findByNameInIgnoreCase(names).stream()
                .map(brandResponseMapper::mapToDto)
                .toList();
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Brand getByIdOrElseThrow(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Brand", "id", id)));
    }

    private void checkUniqueBrandName(String brandName) {
        if (brandRepository.existsByNameIgnoreCase(brandName)) {
            throw new BrandBadRequestException(ExceptionMessageUtil.getAlreadyExistsMessage("Brand", "name", brandName));
        }
    }
}
