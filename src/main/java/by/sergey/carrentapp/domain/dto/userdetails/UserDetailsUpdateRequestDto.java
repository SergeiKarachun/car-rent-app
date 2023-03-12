package by.sergey.carrentapp.domain.dto.userdetails;

import lombok.Value;

@Value
public class UserDetailsUpdateRequestDto {
    String name;
    String surname;
    String address;
    String phone;
}
