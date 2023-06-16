package by.dudko.genetic.model.chromosome;

import by.dudko.genetic.model.gene.Gene;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface Chromosome<G extends Gene<?, G>> {
    int length();

    G getGene(int index);

    List<G> getGenes();

    default <T> List<T> getValues(Class<T> type) {
        return getGenes().stream()
                .map(Gene::getValue)
                .map(type::cast)
                .toList();
    }

    Chromosome<G> withGeneReplaced(int index, G newGene);

    Chromosome<G> newInstance(Collection<? extends G> genes);

    default Chromosome<G> newInstance(G[] genes) {
        return newInstance(Arrays.stream(genes)
                .toList());
    }
}
