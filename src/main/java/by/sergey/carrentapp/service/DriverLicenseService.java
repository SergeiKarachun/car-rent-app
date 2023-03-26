package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseCreateRequestDto;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.mapper.driverlicense.DriverLicenseCreateMapper;
import by.sergey.carrentapp.mapper.driverlicense.DriverLicenseResponseMapper;
import by.sergey.carrentapp.mapper.driverlicense.DriverLicenseUpdateMapper;
import by.sergey.carrentapp.repository.DriverLicenseRepository;
import by.sergey.carrentapp.repository.UserRepository;
import by.sergey.carrentapp.service.exception.DriverLicenseBadRequestException;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.utils.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriverLicenseService {

    private final DriverLicenseRepository driverLicenseRepository;
    private final DriverLicenseCreateMapper driverLicenseCreateMapper;
    private final DriverLicenseResponseMapper driverLicenseResponseMapper;
    private final DriverLicenseUpdateMapper driverLicenseUpdateMapper;
    private final UserRepository userRepository;

    @Transactional
    public Optional<DriverLicenseResponseDto> create(DriverLicenseCreateRequestDto driverLicenseCreateRequestDto) {
        Optional<User> optionalUser = userRepository.findById(driverLicenseCreateRequestDto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new DriverLicenseBadRequestException("Can't create new driver license for user." + ExceptionMessageUtil.getNotFoundMessage("User", "id", driverLicenseCreateRequestDto.getUserId()));
        } else {
            User user = optionalUser.get();
            checkNumberIsUnique(driverLicenseCreateRequestDto.getDriverLicenseNumber());
            DriverLicense driverLicense = driverLicenseCreateMapper.mapToEntity(driverLicenseCreateRequestDto);
            driverLicense.setUserDetails(user.getUserDetails());
            return Optional.of(driverLicense)
                    .map(driverLicenseRepository::save)
                    .map(driverLicenseResponseMapper::mapToDto);
        }
    }

    @Transactional
    public Optional<DriverLicenseResponseDto> update(Long id, DriverLicenseUpdateRequestDto driverLicenseUpdateRequestDto) {
        DriverLicense existingDriverLicense = getDriverLicenseByIdOrElseThrow(id);

        if (!existingDriverLicense.getNumber().equals(driverLicenseUpdateRequestDto.getDriverLicenseNumber())){
            checkNumberIsUnique(driverLicenseUpdateRequestDto.getDriverLicenseNumber());
        }

        return Optional.of(driverLicenseUpdateMapper.mapToEntity(driverLicenseUpdateRequestDto, existingDriverLicense))
                .map(driverLicenseRepository::save)
                .map(driverLicenseResponseMapper::mapToDto);
    }

    public Optional<DriverLicenseResponseDto> getById(Long id) {
        return Optional.of(getDriverLicenseByIdOrElseThrow(id))
                .map(driverLicenseResponseMapper::mapToDto);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (driverLicenseRepository.existsById(id)) {
            driverLicenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<DriverLicenseResponseDto> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageableUtils.getSortedPageable(page, size, Sort.Direction.ASC, "number");

        return driverLicenseRepository.findAll(pageRequest)
                .map(driverLicenseResponseMapper::mapToDto);
    }

    public Optional<DriverLicenseResponseDto> getByUserId(Long userId) {
        return driverLicenseRepository.findByUserId(userId)
                .map(driverLicenseResponseMapper::mapToDto);
    }

    public Optional<DriverLicenseResponseDto> getByNumber(String number) {
        return driverLicenseRepository.findByNumberIgnoreCase(number)
                .map(driverLicenseResponseMapper::mapToDto);
    }

    public List<DriverLicenseResponseDto> getAllExpiredDriverLicenses() {
        return driverLicenseRepository.findByExpirationDateLessThanEqual(LocalDate.now()).stream()
                .map(driverLicenseResponseMapper::mapToDto)
                .toList();
    }

    private DriverLicense getDriverLicenseByIdOrElseThrow(Long id) {
        return driverLicenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Driver license", "id", id)));
    }

    private void checkNumberIsUnique(String licenseNumber) {
        if (driverLicenseRepository.existsByNumber(licenseNumber)){
            throw new DriverLicenseBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("Driver license", "number", licenseNumber)));
        }
    }
}
