package by.sergey.carrentapp.mapper;

public interface CreateMapper<F, T> extends Mapper<F, T> {

    T mapToEntity(F requestDto);
}
