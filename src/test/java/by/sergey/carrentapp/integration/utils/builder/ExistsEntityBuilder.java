package by.sergey.carrentapp.integration.utils.builder;

import by.sergey.carrentapp.domain.entity.*;
import by.sergey.carrentapp.domain.model.*;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;

@UtilityClass
public class ExistsEntityBuilder {

    public static Accident getExistAccident(){
        return Accident.builder()
                .id(EXISTS_ACCIDENT_ID)
                .order(getExistOrder())
                .accidentDate(LocalDateTime.of(2022,10,01, 12,35,0))
                .description("speeding fine")
                .damage(BigDecimal.valueOf(50).setScale(2))
                .build();
    }

    public static Brand getExistBrand() {
        return Brand.builder()
                .id(EXISTS_BRAND_ID)
                .name("Audi")
                .build();
    }

    public static Car getExistCar() {
        return Car.builder()
                .id(EXISTS_CAR_ID)
                .brand(getExistBrand())
                .model(getExistModel())
                .category(getExistCategory())
                .color(Color.BLACK)
                .year(2020)
                .carNumber("7594AB-7")
                .vin("1D8GT58KX8W109715")
                .repaired(true)
                .build();
    }

    public static Category getExistCategory() {
        return Category.builder()
                .id(EXISTS_CATEGORY_ID)
                .name("ECONOMY")
                .price(BigDecimal.valueOf(50).setScale(2, RoundingMode.UNNECESSARY))
                .build();
    }

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(EXISTS_DRIVER_LICENSE_ID)
                .userDetails(getExistUserDetails())
                .number("AB12345")
                .issueDate(LocalDate.of(2015,3,2))
                .expirationDate(LocalDate.of(2025,3,1))
                .build();
    }

    public static Model getExistModel() {
        return Model.builder()
                .id(EXISTS_MODEL_ID)
                .brand(getExistBrand())
                .name("A6")
                .transmission(Transmission.MANUAL)
                .engineType(EngineType.DIESEL)
                .build();
    }

    public static Order getExistOrder() {
        return Order.builder()
                .id(EXISTS_ORDER_ID)
                .user(getExistUser())
                .car(getExistCar())
                .date(LocalDate.of(2022,7,10))
                .passport("MP1234567")
                .orderStatus(OrderStatus.APPROVED)
                .sum(BigDecimal.valueOf(150).setScale(2))
                .build();
    }

    public static RentalTime getExistRentalTime() {
        return RentalTime.builder()
                .id(EXISTS_RENTAL_TIME_ID)
                .order(getExistOrder())
                .startRentalDate(LocalDateTime.of(2022,7,11,0,0,0))
                .endRentalDate(LocalDateTime.of(2022,7,12,23,59,0))
                .build();
    }

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .id(EXISTS_USER_DETAILS_ID)
                .name("Sergey")
                .surname("Ivanon")
                .userContact(UserContact.builder()
                        .address("Minsk")
                        .phone("+37533 1234567")
                        .build())
                .birthday(LocalDate.of(1990, 8, 10))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();

    }

    public static User getExistUser() {
        return User.builder()
                .id(EXISTS_USER_ID)
                .name("admin")
                .email("admin@gmail.com")
                .password("admin")
                .role(Role.ADMIN)
                .build();
    }
}
