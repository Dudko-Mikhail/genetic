package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.BinaryOperator;

public interface ChromosomeCrossover<G extends Gene<?, G>> extends BinaryOperator<Chromosome<G>> { // todo негде получать информацию о поколении, если добавить эту информацию в хромосому
}
