package by.sergey.carrentapp.utils.predicate;

import by.sergey.carrentapp.domain.dto.filterdto.CarFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static by.sergey.carrentapp.domain.entity.QCar.*;

@Component
public class CarPredicateBuilder implements PredicateBuilder<Predicate, CarFilter>{
    @Override
    public Predicate build(CarFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getColor(), car.color::eq)
                .add(requestFilter.getYear(), car.year::goe)
                .add(requestFilter.getBrandNames(), car.brand.name::in)
                .add(requestFilter.getModelNames(), car.model.name::in)
                .add(requestFilter.getCategoryNames(), car.category.name::in)
                .add(requestFilter.getTransmission(), car.model.transmission::eq)
                .add(requestFilter.getEngineType(), car.model.engineType::eq)
                .buildAnd();
    }
}
