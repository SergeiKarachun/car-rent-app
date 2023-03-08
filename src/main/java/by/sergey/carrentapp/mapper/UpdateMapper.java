package by.sergey.carrentapp.mapper;

public interface UpdateMapper<F, T> extends Mapper<F, T> {
    T mapToEntity(F dto, T to);

    void merge(F from, T to);
}
