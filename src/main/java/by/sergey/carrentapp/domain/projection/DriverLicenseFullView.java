package by.sergey.carrentapp.domain.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface DriverLicenseFullView {

    Long getId();

    String getNumber();

    LocalDateTime getIssueDate();

    LocalDateTime getExpirationDate();

    String getFirstname();

    String getSurname();

    String getPhone();

    @Value("#{target.firstname + ' ' + target.surname}")
    String getFullName();
}
