package by.sergey.carrentapp.domain.projection;

import by.sergey.carrentapp.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface OrderFullView {

    Long getId();
    LocalDate getDate();
    String getPassport();
    OrderStatus getOrderStatus();
    BigDecimal getSum();
    LocalDateTime getStartRentalTime();
    LocalDateTime getEndRentalTime();
    String getCarNumber();
    String getBrandName();
    String getModelName();
    String getFirstname();

    String getSurname();
    String getPhone();
    //boolean getAccidents();

}
