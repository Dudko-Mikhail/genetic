package by.dudko.genetic.model.chromosome;

import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.model.gene.Gene;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface Chromosome<T> { // todo Можно ещё добавить фабричные методы от varArgs
    int length();

    BaseGene<T> getGene(int index);

    List<BaseGene<T>> getGenes();

    BaseGene<T> replaceGene(int index, BaseGene<T> newGene);

    Chromosome<T> newInstance(Collection<? extends BaseGene<T>> genes);

    default Chromosome<T> newInstance(BaseGene<T>[] genes) {
        return newInstance(Arrays.stream(genes)
                .toList());
    }

    default Gene<T> newGene(T value) {
        Gene<T> gene = getGene(0);
        return gene != null ? gene.newInstance(value) : null;
    }
}
