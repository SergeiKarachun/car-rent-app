package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.car.CarCreateRequestDto;
import by.sergey.carrentapp.domain.dto.car.CarResponseDto;
import by.sergey.carrentapp.domain.dto.car.CarUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.filterdto.CarFilter;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.mapper.car.CarCreateMapper;
import by.sergey.carrentapp.mapper.car.CarResponseMapper;
import by.sergey.carrentapp.mapper.car.CarUpdateMapper;
import by.sergey.carrentapp.repository.CarRepository;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.utils.PageableUtils;
import by.sergey.carrentapp.utils.predicate.CarPredicateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarResponseMapper carResponseMapper;
    private final CarCreateMapper carCreateMapper;
    private final CarUpdateMapper carUpdateMapper;
    //private final ImageService imageService;
    private final CarPredicateBuilder carPredicateBuilder;


    @Transactional
    public Optional<CarResponseDto> create(CarCreateRequestDto carCreateRequestDto) {
        /*return Optional.of(carCreateRequestDto)
                .map(dto -> {
                    if (dto.image() != null) {
                        downloadImage(dto.image());
                    }
                    return carCreateMapper.mapToEntity(dto);
                })
                .map(carRepository::save)
                .map(carResponseMapper::mapToDto);*/

        return Optional.of(carCreateMapper.mapToEntity(carCreateRequestDto))
                .map(carRepository::save)
                .map(carResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<CarResponseDto> update(Long id, CarUpdateRequestDto carUpdateRequestDto) {
        var existingCar = getByIdOrElseThrow(id);

        /*return Optional.of(carUpdateRequestDto)
                .map(dto -> {
                    if (dto.image() != null) {
                        downloadImage(dto.image());
                        imageService.delete(existingCar.getImage());
                    }
                    return carUpdateMapper.mapToEntity(dto, existingCar);
                })
                .map(carRepository::save)
                .map(carResponseMapper::mapToDto);*/

        return Optional.of(carUpdateMapper.mapToEntity(carUpdateRequestDto, existingCar))
                .map(carRepository::save)
                .map(carResponseMapper::mapToDto);
    }

    @Transactional
    public Boolean deleteById(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<CarResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(carResponseMapper::mapToDto);
    }

    public List<CarResponseDto> getAll() {
        return carRepository.findAll().stream()
                .map(carResponseMapper::mapToDto)
                .toList();
    }

    public Page<CarResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "number");

        return carRepository.findAll(pageRequest)
                .map(carResponseMapper::mapToDto);
    }

    public Optional<CarResponseDto> getByCarNumber(String number) {
        return carRepository.findCarByCarNumberIgnoreCase(number)
                .map(carResponseMapper::mapToDto);
    }

    public Page<CarResponseDto> getAllWithAccidents(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "id");
        return carRepository.findAllWithAccidents(pageRequest)
                .map(carResponseMapper::mapToDto);
    }

    public Page<CarResponseDto> getAllWithoutAccidents(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "id");

        return carRepository.findAllWithoutAccidents(pageRequest)
                .map(carResponseMapper::mapToDto);
    }

    public List<CarResponseDto> getAllRepaired() {
        return carRepository.findAllRepaired().stream()
                .map(carResponseMapper::mapToDto)
                .toList();
    }

    public List<CarResponseDto> getAllBroken() {
        return carRepository.findAllBroken().stream()
                .map(carResponseMapper::mapToDto)
                .toList();
    }

    public Page<CarResponseDto> getAll(CarFilter carFilter, Integer page, Integer pageSize) {
        return carRepository.findAll(carPredicateBuilder.build(carFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "brand_name"))
                .map(carResponseMapper::mapToDto);
    }

   /* public Optional<byte[]> findCarImage(Long id) {
        return carRepository.findById(id)
                .map(Car::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }*/

    private Car getByIdOrElseThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Car", "id", id)));
    }

   /* @SneakyThrows
    private void downloadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }*/


}
