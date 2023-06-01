package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.mutation.ChromosomeMutation;

import java.util.ArrayList;
import java.util.Collections;

public class ShuffleMutation<T> implements ChromosomeMutation<T> {
    @Override
    public Chromosome<T> mutateChromosome(Chromosome<T> chromosome) {
        var genes = new ArrayList<>(chromosome.getGenes());
        Collections.shuffle(genes);
        return chromosome.newInstance(genes);
    }
}
