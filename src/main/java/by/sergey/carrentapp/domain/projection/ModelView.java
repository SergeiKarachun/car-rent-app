package by.sergey.carrentapp.domain.projection;

import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;

public interface ModelView {

    Long getId();

    String getName();

    Transmission getTransmission();

    EngineType getEngineType();
}
