package by.dudko.genetic.model.chromosome.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.*;

public class BaseChromosome<G extends Gene<?, G>> implements Chromosome<G> {
    private final List<G> genes;

    public BaseChromosome(Collection<? extends G> genes) { // todo нужны ли проверки в конструкторе
        this.genes = new ArrayList<>(Objects.requireNonNull(genes));
    }

    @SafeVarargs
    public BaseChromosome(G... genes) { // todo нужны ли проверки в конструкторе
        Objects.requireNonNull(genes);
        this.genes = new ArrayList<>(List.of(genes));
    }

    @Override
    public int length() {
        return genes.size();
    }

    @Override
    public G getGene(int index) {
        return genes.get(index);
    }

    @Override
    public List<G> getGenes() {
        return Collections.unmodifiableList(genes);
    }

    @Override
    public G replaceGene(int index, G newGene) {
        return genes.set(index, newGene);
    }

    @Override
    public BaseChromosome<G> newInstance(Collection<? extends G> genes) {
        return new BaseChromosome<G>(genes);
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}