package by.sergey.carrentapp.utils.predicate;

public interface PredicateBuilder<R, T> {
    R build(T requestFilter);
}
