package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.accident.AccidentCreateRequestDto;
import by.sergey.carrentapp.domain.dto.accident.AccidentResponseDto;
import by.sergey.carrentapp.domain.dto.accident.AccidentUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Accident;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.mapper.accident.AccidentCreateMapper;
import by.sergey.carrentapp.mapper.accident.AccidentResponseMapper;
import by.sergey.carrentapp.mapper.accident.AccidentUpdateMapper;
import by.sergey.carrentapp.repository.AccidentRepository;
import by.sergey.carrentapp.repository.OrderRepository;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.utils.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final AccidentResponseMapper accidentResponseMapper;
    private final AccidentCreateMapper accidentCreateMapper;
    private final AccidentUpdateMapper accidentUpdateMapper;
    private final OrderRepository orderRepository;

    @Transactional
    public Optional<AccidentResponseDto> create(AccidentCreateRequestDto requestDto) {
        Order existingOrder = getOrderByIdOrElseThrow(requestDto.getOrderId());

        Accident accident = accidentCreateMapper.mapToEntity(requestDto);
        existingOrder.setAccident(accident);

        return Optional.of(accident)
                .map(accidentRepository::save)
                .map(accidentResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<AccidentResponseDto> update(Long id, AccidentUpdateRequestDto requestDto) {
        Accident existingAccident = getByIdOrElseThrow(id);

        return Optional.of(accidentUpdateMapper.mapToEntity(requestDto, existingAccident))
                .map(accidentRepository::save)
                .map(accidentResponseMapper::mapToDto);
    }

    public Optional<AccidentResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(accidentResponseMapper::mapToDto);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (accidentRepository.existsById(id)) {
            accidentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<AccidentResponseDto> getAll(Integer page, Integer pageSize) {
        Pageable requestPage = PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "damage");
        return accidentRepository.findAll(requestPage)
                .map(accidentResponseMapper::mapToDto);
    }

    public List<AccidentResponseDto> getAllByOrderId(Long orderId) {
        return accidentRepository.findAllByOrderId(orderId)
                .stream()
                .map(accidentResponseMapper::mapToDto)
                .toList();
    }

    public List<AccidentResponseDto> getAllByCarNumber(String number) {
        return accidentRepository.findByCarNumber(number)
                .stream()
                .map(accidentResponseMapper::mapToDto)
                .toList();
    }

    private Order getOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", orderId)));
    }

    private Accident getByIdOrElseThrow(Long id) {
        return accidentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Accident", "id", id)));
    }
}
