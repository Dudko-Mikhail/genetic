package by.dudko.genetic.model.chromosome;

import by.dudko.genetic.model.gene.Gene;

import java.util.List;

public interface Chromosome<T> {
    int length();

    Gene<T> getGene(int index);

    List<Gene<T>> getGenes();

    Gene<T> replaceGene(int index, Gene<T> newGene);

//    Chromosome<T, F> withFitness(F fitness);  // todo think about immutable class

//    F getFitness();

//    boolean isEvaluated();

//    int getAge();
}
