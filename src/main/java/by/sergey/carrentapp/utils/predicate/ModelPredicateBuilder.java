package by.sergey.carrentapp.utils.predicate;

import by.sergey.carrentapp.domain.dto.filterdto.ModelFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static by.sergey.carrentapp.domain.entity.QModel.model;

@Component
public class ModelPredicateBuilder implements PredicateBuilder<Predicate, ModelFilter>  {
    @Override
    public Predicate build(ModelFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getBrands(), model.brand.name::in)
                .add(requestFilter.getModels(), model.name::in)
                .add(requestFilter.getTransmission(), model.transmission::eq)
                .add(requestFilter.getEngineType(), model.engineType::eq)
                .buildAnd();
    }
}
