package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.function.BinaryOperator;

public interface ChromosomeCrossover<T> extends BinaryOperator<Chromosome<T>> { // todo негде получать информацию о поколении, если добавить эту информацию в хромосому
}
