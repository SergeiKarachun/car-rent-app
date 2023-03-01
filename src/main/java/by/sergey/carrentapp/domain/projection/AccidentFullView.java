package by.sergey.carrentapp.domain.projection;

import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccidentFullView {
    Long getId();
    LocalDate getAccidentDate();
    String getDescription();
    BigDecimal getDamage();
    Long getOrdeId();
    String getBrandName();
    String getModelName();
    String getCarNumber();
    String getFirstname();
    String getSurname();

    @Value("#{target.firstname + ' ' + target.surname}")
    String getFullName();
}
