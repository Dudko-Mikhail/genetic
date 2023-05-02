package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.gene.Gene;

public interface GeneMutation<T> {
    Gene<T> mutateGen(Gene<T> gene);
}
