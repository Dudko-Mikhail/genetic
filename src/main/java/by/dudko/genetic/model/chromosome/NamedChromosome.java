package by.dudko.genetic.model.chromosome;

import by.dudko.genetic.model.gene.Gene;

public interface NamedChromosome<T> {
    Gene<T> getGen(String name);
}
