package by.sergey.carrentapp.mapper;

public interface ResponseMapper<F, T> extends Mapper<F, T> {

    T mapToDto(F entity);
}
