package by.sergey.carrentapp.domain.dto.userdetails;


import lombok.Value;

import java.time.LocalDate;

@Value
public class UserDetailsCreateRequestDto {
    Long userId;
    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
}
