package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.filterdto.OrderFilter;
import by.sergey.carrentapp.domain.dto.order.OrderCreateRequestDto;
import by.sergey.carrentapp.domain.dto.order.OrderResponseDto;
import by.sergey.carrentapp.domain.dto.order.OrderUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.order.OrderUserReportDto;
import by.sergey.carrentapp.domain.entity.*;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.mapper.order.OrderCreateMapper;
import by.sergey.carrentapp.mapper.order.OrderReportMapper;
import by.sergey.carrentapp.mapper.order.OrderResponseMapper;
import by.sergey.carrentapp.mapper.order.OrderUpdateMapper;
import by.sergey.carrentapp.repository.CarRepository;
import by.sergey.carrentapp.repository.OrderRepository;
import by.sergey.carrentapp.repository.RentalTimeRepository;
import by.sergey.carrentapp.repository.UserRepository;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.service.exception.OrderBadRequestException;
import by.sergey.carrentapp.utils.PageableUtils;
import by.sergey.carrentapp.utils.predicate.OrderPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalTimeRepository rentalTimeRepository;
    private final OrderCreateMapper orderCreateMapper;
    private final OrderUpdateMapper orderUpdateMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderReportMapper orderReportMapper;
    private final OrderPredicateBuilder orderPredicateBuilder;

    @Transactional
    public Optional<OrderResponseDto> create(OrderCreateRequestDto orderCreateRequestDto) {
        checkIsUserExist(orderCreateRequestDto.getUserId());
        checkIsCarExist(orderCreateRequestDto.getCarId());

        if (!ensureCarAvailable(orderCreateRequestDto.getCarId(), orderCreateRequestDto.getStartRentalDate(), orderCreateRequestDto.getEndRentalDate())) {
            return Optional.empty();
        }

        User existUser = getUserByIdOrElseThrow(orderCreateRequestDto.getUserId());
        Optional<DriverLicense> driverLicenseByUser = getDriverLicenseByUser(existUser);
        if (driverLicenseByUser.isEmpty() || driverLicenseByUser.get().getExpirationDate().isBefore(orderCreateRequestDto.getStartRentalDate().toLocalDate())) {
            return Optional.empty();
        }

        BigDecimal price = getPriceByCarId(orderCreateRequestDto.getCarId()).get();
        long amountOfDays = calculateAmountOfDays(orderCreateRequestDto.getStartRentalDate(), orderCreateRequestDto.getEndRentalDate());

        BigDecimal orderSum = calculateOrderSum(amountOfDays, price);

        Order order = orderCreateMapper.mapToEntity(orderCreateRequestDto);
        order.setSum(orderSum);

        return Optional.of(order)
                .map(orderRepository::save)
                .map(orderResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<OrderResponseDto> update(Long id, OrderUpdateRequestDto dto) {
        getCarByIdOrElseThrow(dto.getCarId());

        Order existingOrder = getOrderByIdOrElseThrow(id);
        RentalTime existingRentalTime = getRentalTimeByByOrderIdOrElseThrow(id);

        if (!ensureCarAvailableByOrderId(dto.getCarId(), id, dto.getStartRentalDate(), dto.getEndRentalDate())) {
            return Optional.empty();
        }

        if (isRentalTimeChanged(dto, existingRentalTime)) {
            long amountOfDays = calculateAmountOfDays(dto.getStartRentalDate(), dto.getEndRentalDate());
            BigDecimal price = getPriceByOrder(existingOrder).get();

            existingOrder.setSum(calculateOrderSum(amountOfDays, price));
        }

        return Optional.of(orderUpdateMapper.mapToEntity(dto, existingOrder))
                .map(orderRepository::save)
                .map(orderResponseMapper::mapToDto);
    }

    public Optional<OrderResponseDto> getById(Long id) {
        return Optional.of(getOrderByIdOrElseThrow(id))
                .map(orderResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<OrderResponseDto> changeOrderStatus(Long id, OrderStatus orderStatus) {
        Order existingOrder = getOrderByIdOrElseThrow(id);
        existingOrder.setOrderStatus(orderStatus);

        return Optional.of(existingOrder)
                .map(orderRepository::save)
                .map(orderResponseMapper::mapToDto);
    }

    public Page<OrderResponseDto> getAll(OrderFilter orderFilter, Integer page, Integer pageSize) {
        return orderRepository.findAll(
                        orderPredicateBuilder.build(orderFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "date"))
                .map(orderResponseMapper::mapToDto);
    }

    public List<OrderResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderResponseMapper::mapToDto)
                .toList();
    }

    public List<OrderResponseDto> getAllLimitByDate() {
        return orderRepository.findAllLimitByDate(LocalDate.now().minusDays(30))
                .stream().map(orderResponseMapper::mapToDto)
                .toList();
    }

    public List<OrderResponseDto> getAllByStatus(OrderStatus orderStatus) {
        return orderRepository.findAllByOrderStatus(orderStatus)
                .stream()
                .map(orderResponseMapper::mapToDto)
                .toList();
    }

    public List<OrderUserReportDto> getAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderReportMapper::mapToDto)
                .toList();
    }

    public List<OrderUserReportDto> getAllByUserId(Long userId, OrderStatus orderStatus, BigDecimal sum) {
        return StreamSupport.stream(orderRepository.findAll(orderPredicateBuilder.usersBuild(userId, orderStatus, sum))
                .spliterator(), false)
                .map(orderReportMapper::mapToDto)
                .toList();
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void checkIsCarExist(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new OrderBadRequestException(String.format(ExceptionMessageUtil.getNotFoundMessage("Car", "id", carId)));
        }
    }

    private void checkIsUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new OrderBadRequestException(String.format(ExceptionMessageUtil.getNotFoundMessage("User", "id", userId)));
        }
    }

    private Car getCarByIdOrElseThrow(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Car", "id", carId)));
    }

    private Order getOrderByIdOrElseThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", id)));
    }

    private User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new OrderBadRequestException(ExceptionMessageUtil.getNotFoundMessage("User", "id", id)));
    }

    private RentalTime getRentalTimeByByOrderIdOrElseThrow(long orderId) {
        return rentalTimeRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderBadRequestException(ExceptionMessageUtil.getNotFoundMessage("Rental time", "orderId", orderId)));
    }

    private boolean ensureCarAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        return carRepository.isCarAvailable(carId, startDate, endDate);
    }

    private boolean ensureCarAvailableByOrderId(Long carId, Long orderId, LocalDateTime startDate, LocalDateTime endDate) {
        return carRepository.isCarAvailableByOrderId(orderId, carId, startDate, endDate);
    }

    private Optional<DriverLicense> getDriverLicenseByUser(User user) {
        return Optional.ofNullable(user.getUserDetails().getDriverLicense());
    }

    private Optional<BigDecimal> getPriceByCarId(Long carId) {
        return carRepository.findById(carId)
                .map(Car::getCategory)
                .map(Category::getPrice);
    }

    private Optional<BigDecimal> getPriceByOrder(Order order) {
        return carRepository.findById(order.getCar().getId())
                .map(Car::getCategory)
                .map(Category::getPrice);
    }

    private long calculateAmountOfDays(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return Math.abs(duration.toDays());
    }

    private BigDecimal calculateOrderSum(long amountOfDays, BigDecimal price) {
        return new BigDecimal(price.intValue() * amountOfDays);
    }

    private boolean isRentalTimeChanged(OrderUpdateRequestDto dto, RentalTime existingTime) {
        return dto.getStartRentalDate() != existingTime.getStartRentalDate() ||
               dto.getEndRentalDate() != existingTime.getEndRentalDate();
    }
}
