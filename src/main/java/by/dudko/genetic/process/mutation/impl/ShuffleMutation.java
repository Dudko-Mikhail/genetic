package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.UnaryOperator;

public class ShuffleMutation<G extends Gene<?, G>> implements UnaryOperator<Chromosome<G>> {
    @Override
    public Chromosome<G> apply(Chromosome<G> chromosome) {
        var genes = new ArrayList<>(chromosome.getGenes());
        Collections.shuffle(genes);
        return chromosome.newInstance(genes);
    }
}
