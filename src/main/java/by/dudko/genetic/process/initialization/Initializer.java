package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.stream.Stream;

public interface Initializer<G extends Gene<?, G>> {
    Stream<Chromosome<G>> produceChromosomes(int populationSize);
}
