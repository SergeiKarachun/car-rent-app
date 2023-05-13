package by.sergey.carrentapp.integration.utils.builder;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.car.CarCreateRequestDto;
import by.sergey.carrentapp.domain.dto.car.CarUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.model.ModelCreateRequestDto;
import by.sergey.carrentapp.domain.dto.model.ModelUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.order.OrderCreateRequestDto;
import by.sergey.carrentapp.domain.dto.user.LoginRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsUpdateRequestDto;
import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestDtoBuilder {

    public static BrandCreateUpdateRequestDto createBrandUpdateRequestDto() {
        return new BrandCreateUpdateRequestDto("Honda");
    }

    public static ModelCreateRequestDto createModelCreateRequestDto(Long brandId) {
        return new ModelCreateRequestDto(brandId, "CR-V", Transmission.AUTOMATIC, EngineType.FUEL);
    }

    public static ModelUpdateRequestDto createModelUpdateRequestDto() {
        return new ModelUpdateRequestDto("S-2000", Transmission.MANUAL, EngineType.FUEL);
    }

    public static CategoryCreateUpdateRequestDto categoryCreateUpdateRequestDto() {
        return new CategoryCreateUpdateRequestDto("VIP", BigDecimal.valueOf(250));
    }

    public static CarCreateRequestDto createCarRequestDto(Long brandId, Long modelId, Long categoryId) {
        return new CarCreateRequestDto(brandId, modelId, categoryId,
                Color.BLACK, 2020, "7718XX-7", "VF9KZXCY6RTXV1111", true, null);
    }

    public static CarUpdateRequestDto updateCarRequestDto(Long modelId) {
        return new CarUpdateRequestDto(modelId, 1L, Color.RED, 2015, "1877XX-7", true, null);
    }

    public static UserCreateRequestDto createUserRequestDto() {
        return new UserCreateRequestDto(
                "test@gmail.com",
                "test",
                "testTest1",
                "Ivan",
                "Ivanov",
                "Minsk",
                "+37533123-45-67",
                LocalDate.of(1989, 1, 1),
                "AB123456",
                LocalDate.of(2020, 8, 9),
                LocalDate.of(2030, 8, 9));
    }

    public static LoginRequestDto createLoginRequestDto() {
        return new LoginRequestDto("admin@gmail.com", "admin");
    }

    public static UserUpdateRequestDto updateUserRequestDto() {
        return new UserUpdateRequestDto("test1",
                "test@gmail.com",
                Role.ADMIN);
    }

    public static UserDetailsUpdateRequestDto createUserDetailsUpdateRequestDto() {
        return new UserDetailsUpdateRequestDto("Ivan-new",
                "Ivanov-new",
                "Moscow",
                "+37529123-45-67");
    }

    public static OrderCreateRequestDto createOrderRequestDto(Long userId, Long carId) {
        return new OrderCreateRequestDto(userId, carId, "passport",
                LocalDateTime.of(2023, 5, 13, 12, 00),
                LocalDateTime.of(2023, 5, 16, 12, 00));
    }

    public static OrderCreateRequestDto createOrderRequestDtoWithNecessaryData(Long userId, Long carId, LocalDateTime start, LocalDateTime end) {
        return new OrderCreateRequestDto(userId, carId, "passport", start, end);
    }
}
