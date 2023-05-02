package by.dudko.genetic.model.chromosome.impl;

import by.dudko.genetic.model.gene.Gene;

import java.util.List;

public class MultiTypeChromosome { // todo предоставить безопасное получение гена с конкретным типом по индексу
    private final List<Gene<Object>> genes;

    public MultiTypeChromosome(List<Gene<Object>> genes) {
        this.genes = genes;
    }
}
