package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

public interface ChromosomeMutation<G extends Gene<?, G>> { // todo негде получать информацию о поколении, если добавить эту информацию в хромосому
    Chromosome<G> mutateChromosome(Chromosome<G> chromosome);
}
