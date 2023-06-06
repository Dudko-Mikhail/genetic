package by.dudko.genetic.model.gene;

public interface Gene<T, G extends Gene<T, G>> {
    T getValue();

    G newInstance(T value);
}
