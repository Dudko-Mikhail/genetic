package by.dudko.genetic.model.chromosome;

import by.dudko.genetic.model.gene.Gene;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface Chromosome<G extends Gene<?, G>> { // todo Можно ещё добавить фабричные методы от varArgs
    int length();

    G getGene(int index);

    List<G> getGenes();

    G replaceGene(int index, G newGene);

    Chromosome<G> newInstance(Collection<? extends G> genes);

    default Chromosome<G> newInstance(G[] genes) {
        return newInstance(Arrays.stream(genes)
                .toList());
    }
}
