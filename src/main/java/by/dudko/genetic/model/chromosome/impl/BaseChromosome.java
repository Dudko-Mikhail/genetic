package by.dudko.genetic.model.chromosome.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.*;
import java.util.function.Function;

public class BaseChromosome<G extends Gene<?, G>> implements Chromosome<G> {
    private final List<G> genes;

    public BaseChromosome(Collection<? extends G> genes) {
        this.genes = List.copyOf(Objects.requireNonNull(genes));
    }

    public <T> BaseChromosome(Collection<? extends T> values, Function<T, G> mapper) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(values);
        this.genes = values.stream()
                .map(mapper)
                .toList();
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
    public Chromosome<G> withGeneReplaced(int index, G newGene) {
        List<G> copy = new ArrayList<>(genes);
        copy.set(index, newGene);
        return newInstance(copy);
    }

    @Override
    public BaseChromosome<G> newInstance(Collection<? extends G> genes) {
        return new BaseChromosome<G>(genes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseChromosome<?> that)) return false;

        return genes.equals(that.genes);
    }

    @Override
    public int hashCode() {
        return genes.hashCode();
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}