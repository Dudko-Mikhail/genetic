package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.chromosome.Chromosome;

public interface ChromosomeMutation<T> { // todo негде получать информацию о поколении, если добавить эту информацию в хромосому
    Chromosome<T> mutateChromosome(Chromosome<T> chromosome);
}
