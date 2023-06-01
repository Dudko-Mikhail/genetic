package by.dudko.genetic.model.gene;

public interface Gene<T> {
    T getValue();

    Gene<T> newInstance(T value);
}
