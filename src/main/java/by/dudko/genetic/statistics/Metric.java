package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.Function;

public interface Metric<G extends Gene<?, G>, F, T> extends Function<Population<G, F>, T> {
    default Function<? super T, String> formatter() {
        return Object::toString;
    }

    default String stringify(Population<G, F> population) {
        return formatter().apply(apply(population));
    }
}
