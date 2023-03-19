package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.dto.filterdto.ModelFilter;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.utils.predicate.ModelPredicateBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static by.sergey.carrentapp.domain.entity.QModel.model;

@RequiredArgsConstructor
public class FilterModelRepositoryImpl implements FilterModelRepository {

    private final EntityManager entityManager;
    private final ModelPredicateBuilder modelPredicateBuilder;

    @Override
    public List<Model> findAllByFilter(ModelFilter modelFilter) {
        return new JPAQuery<Model>(entityManager)
                .select(model)
                .from(model)
                .where(modelPredicateBuilder.build(modelFilter))
                .fetch();
    }
}
