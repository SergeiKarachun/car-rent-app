package by.sergey.carrentapp.domain.projection;

import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;

public interface BrandFullView {

    Long getId();

    String getName();

    Long getModelId();

    String getModelName();

    Transmission getTransmission();

    EngineType getEngineType();

}
