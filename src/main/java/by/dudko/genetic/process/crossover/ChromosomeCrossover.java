package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.chromosome.Chromosome;

public interface ChromosomeCrossover<T> { // todo негде получать информацию о поколении, если добавить эту информацию в хромосому
    Chromosome<T> cross(Chromosome<T> first, Chromosome<T> second);
}
