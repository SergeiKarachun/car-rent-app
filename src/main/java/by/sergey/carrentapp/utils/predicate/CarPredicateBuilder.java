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
                .add(requestFilter.getBrandName(), car.brand.name::containsIgnoreCase)
                .add(requestFilter.getModelName(), car.model.name::containsIgnoreCase)
                .add(requestFilter.getCategoryName(), car.category.name::eq)
                .add(requestFilter.getTransmission(), car.model.transmission::eq)
                .add(requestFilter.getEngineType(), car.model.engineType::eq)
                .buildAnd();
    }
}
