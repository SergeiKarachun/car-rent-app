package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.dto.filterdto.ModelFilter;
import by.sergey.carrentapp.domain.entity.Model;
import java.util.List;

public interface FilterModelRepository {

    List<Model> findAllByFilter(ModelFilter modelFilter);
}
