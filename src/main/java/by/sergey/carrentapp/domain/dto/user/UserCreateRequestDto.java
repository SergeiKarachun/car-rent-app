package by.sergey.carrentapp.domain.dto.user;

import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
public class UserCreateRequestDto {
    @NotBlank(message = "Email is required")
    @Email
    String email;
    @NotBlank(message = "Username is required")
    @Size(min = 2, message = "Username should have at least 2 characters")
    String username;
    @NotBlank
    @Size(min = 8, message = "Password should have at least 8 characters")
    @Pattern(regexp = "(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password should have at least 8 characters")
    String password;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Surname is required")
    String surname;
    @NotBlank(message = "Address is required")
    String address;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "(\\+?(375|80)?\\s?)?\\(?(17|29|33|44|25)\\)?\\s?(\\d{3})[-|\\s]?(\\d{2})[-|\\s]?(\\d{2})", message = "{phone.pattern}")
    String phone;
    @NotNull
    LocalDate birthday;
    @NotBlank(message = "{licensenumber.notempty}")
    @Size(min = 4, message = "{licensenumber.size}")
    String driverLicenseNumber;
    @NotNull
    LocalDate driverLicenseIssueDate;
    @NotNull
    LocalDate driverLicenseExpirationDate;

}
