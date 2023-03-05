package by.sergey.carrentapp.domain.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface DriverLicenseFullView {

    Long getId();

    String getNumber();

    LocalDate getIssueDate();

    LocalDate getExpirationDate();

    String getFirstname();

    String getSurname();

    String getPhone();

    @Value("#{target.firstname + ' ' + target.surname}")
    String getFullName();
}
