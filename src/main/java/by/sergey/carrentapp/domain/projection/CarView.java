package by.sergey.carrentapp.domain.projection;

import by.sergey.carrentapp.domain.model.Color;

public interface CarView {

    Long getId();

    Color getColor();

    Integer getYear();

    String getCarNumber();

    String getVin();


}
