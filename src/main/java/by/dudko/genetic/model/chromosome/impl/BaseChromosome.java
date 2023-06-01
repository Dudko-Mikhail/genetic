package by.dudko.genetic.model.chromosome.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;

import java.util.*;

public class BaseChromosome<T> implements Chromosome<T> {
    private final List<BaseGene<T>> genes;

    public BaseChromosome(Collection<? extends BaseGene<T>> genes) { // todo нужны ли проверки в конструкторе
        this.genes = new ArrayList<>(Objects.requireNonNull(genes));
    }

    @SafeVarargs
    public <G extends BaseGene<T>> BaseChromosome(G... genes) { // todo нужны ли проверки в конструкторе
        Objects.requireNonNull(genes);
        this.genes = new ArrayList<>(List.of(genes));
    }

    @Override
    public int length() {
        return genes.size();
    }

    @Override
    public BaseGene<T> getGene(int index) {
        return genes.get(index);
    }

    @Override
    public List<BaseGene<T>> getGenes() {
        return Collections.unmodifiableList(genes);
    }

    @Override
    public BaseGene<T> replaceGene(int index, BaseGene<T> newGene) {
        return genes.set(index, newGene);
    }

    @Override
    public Chromosome<T> newInstance(Collection<? extends BaseGene<T>> genes) {
        return new BaseChromosome<>(genes);
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}