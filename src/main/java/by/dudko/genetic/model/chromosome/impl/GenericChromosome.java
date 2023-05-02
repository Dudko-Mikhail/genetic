package by.dudko.genetic.model.chromosome.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.*;

public class GenericChromosome<T> implements Chromosome<T> {
    private final List<Gene<T>> genes;

    public GenericChromosome(Collection<? extends Gene<T>> genes) { // todo нужны ли проверки в конструкторе
        Objects.requireNonNull(genes);
        this.genes = new ArrayList<>(genes);
    }

    @SafeVarargs
    public <G extends Gene<T>> GenericChromosome(G... genes) { // todo нужны ли проверки в конструкторе
        Objects.requireNonNull(genes);
        this.genes = new ArrayList<>(List.of(genes));
    }

    @Override
    public int length() {
        return genes.size();
    }

    @Override
    public Gene<T> getGene(int index) {
        return genes.get(index);
    }

    @Override
    public List<Gene<T>> getGenes() {
        return Collections.unmodifiableList(genes);
    }

    @Override
    public Gene<T> replaceGene(int index, Gene<T> newGene) {
        return genes.set(index, newGene);
    }
}