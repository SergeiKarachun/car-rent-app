package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeCreateRequestDto;
import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeResponseDto;
import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.mapper.rentaltime.RentalTimeCreateMapper;
import by.sergey.carrentapp.mapper.rentaltime.RentalTimeResponseMapper;
import by.sergey.carrentapp.mapper.rentaltime.RentalTimeUpdateMapper;
import by.sergey.carrentapp.repository.OrderRepository;
import by.sergey.carrentapp.repository.RentalTimeRepository;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalTimeService {

    private final RentalTimeRepository rentalTimeRepository;
    private final OrderRepository orderRepository;
    private final RentalTimeCreateMapper rentalTimeCreateMapper;
    private final RentalTimeResponseMapper rentalTimeResponseMapper;
    private final RentalTimeUpdateMapper rentalTimeUpdateMapper;

    @Transactional
    public Optional<RentalTimeResponseDto> create(RentalTimeCreateRequestDto requestDto) {
        Order existingOrder = getOrderByIdOrElseThrow(requestDto.getOrderId());
        RentalTime rentalTime = rentalTimeCreateMapper.mapToEntity(requestDto);
        rentalTime.setOrder(existingOrder);

        return Optional.of(rentalTime)
                .map(rentalTimeRepository::save)
                .map(rentalTimeResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<RentalTimeResponseDto> update(Long id, RentalTimeUpdateRequestDto requestDto) {
        RentalTime existingRentalTime = getByIdOrElseThrow(id);

        return Optional.of(rentalTimeUpdateMapper.mapToEntity(requestDto, existingRentalTime))
                .map(rentalTimeRepository::save)
                .map(rentalTimeResponseMapper::mapToDto);
    }

    public Optional<RentalTimeResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(rentalTimeResponseMapper::mapToDto);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (rentalTimeRepository.existsById(id)) {
            return true;
        }
        return false;
    }

    public List<RentalTimeResponseDto> getAll() {
        return rentalTimeRepository.findAll()
                .stream()
                .map(rentalTimeResponseMapper::mapToDto)
                .toList();
    }

    private Order getOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", orderId)));
    }

    private RentalTime getByIdOrElseThrow(Long id) {
        return rentalTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Rental Time", "id", id)));
    }


}
