package by.dudko.genetic.process.test;

public interface ArithmeticOperations<T> {
    T sum(T a, T b);

    T divide(T a, T b);

    T multiply(T a, T b);

    T subtract(T a, T b);

    int compare(T a, T b);

    T newInstance(T min, T max);
}
