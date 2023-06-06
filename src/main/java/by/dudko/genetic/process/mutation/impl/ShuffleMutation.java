package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.mutation.ChromosomeMutation;

import java.util.ArrayList;
import java.util.Collections;

public class ShuffleMutation<G extends Gene<?, G>> implements ChromosomeMutation<G> {
    @Override
    public Chromosome<G> mutateChromosome(Chromosome<G> chromosome) {
        var genes = new ArrayList<>(chromosome.getGenes());
        Collections.shuffle(genes);
        return chromosome.newInstance(genes);
    }
}
