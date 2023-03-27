package by.sergey.carrentapp.domain.dto.filterdto;

import by.sergey.carrentapp.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;


@Value
@Builder
public class OrderFilter {
    String userFirstName;
    String userLastName;
    String carNumber;
    OrderStatus orderStatus;
    BigDecimal sum;
}
