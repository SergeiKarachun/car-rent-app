package by.sergey.carrentapp.domain.dto.filterdto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;


@Value
@Builder
public class UserDetailsFilter {
    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
}
