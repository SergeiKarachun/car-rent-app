package by.sergey.carrentapp.domain.projection;

import java.util.List;

public interface BrandFullView {

    Long getId();

    String getName();

    List<ModelView> getModels();

    List<CarView> getCars();


}
