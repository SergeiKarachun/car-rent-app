package by.sergey.carrentapp.integration.utils.builder;

import by.sergey.carrentapp.domain.entity.*;
import by.sergey.carrentapp.domain.model.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class TestEntityBuilder {

    public static Accident createAccident() {
        return Accident.builder()
                .accidentDate(LocalDate.of(2022, 8, 20).atTime(11, 44))
                .description("Parking - test description")
                .damage(BigDecimal.valueOf(19.89))
                .build();
    }

    public static Brand createBrand(){
        return Brand.builder()
                .name("Ford")
                .build();
    }

    public static Car createCar(){
        return Car.builder()
                .color(Color.BLACK)
                .year(2020)
                .carNumber("7594AB-4")
                .vin("1D8GT58KX8W104515")
                .repaired(true)
                .build();
    }

    public static Category createCategory(){
        return Category.builder()
                .name("VIP")
                .price(BigDecimal.valueOf(500))
                .build();
    }

    public static DriverLicense createDriverLicense(){
        return DriverLicense.builder()
                .number("+37533 2345118")
                .issueDate(LocalDate.of(2015, 7, 19))
                .expirationDate(LocalDate.of(2025, 7, 18))
                .build();
    }

    public static Model createModel(){
        return Model.builder()
                .name("S6")
                .transmission(Transmission.AUTOMATIC)
                .engineType(EngineType.FUEL)
                .build();
    }

    public static Order createOrder(){
        return Order.builder()
                .date(LocalDate.of(2022, 4, 20))
                .passport("MP14569")
                .orderStatus(OrderStatus.APPROVED)
                .sum(BigDecimal.valueOf(452.66))
                .build();
    }

    public static RentalTime createRentalTime(){
        return RentalTime.builder()
                .startRentalDate(LocalDate.of(2023,1,12).atTime(12,00))
                .endRentalDate(LocalDate.of(2023,1,14).atTime(16,40))
                .build();
    }

    public static User createUser() {
        return User.builder()
                .username("newUser")
                .email("igor@gmail.com")
                .password("teStPassword")
                .role(Role.CLIENT)
                .build();
    }

    public static UserContact createUserContact() {
        return UserContact.builder()
                .address("nemiga 15")
                .phone("+37533 3333333")
                .build();
    }

    public static UserDetails createUserDetails () {
        return UserDetails.builder()
                .name("Igor")
                .surname("Igorev")
                .userContact(UserContact.builder()
                        .address("Minsk")
                        .phone("+37533 1111111")
                        .build())
                .birthday(LocalDate.of(1990, 12, 12))
                .registrationDate(LocalDate.of(2022, 1,12))
                .build();
    }


}
