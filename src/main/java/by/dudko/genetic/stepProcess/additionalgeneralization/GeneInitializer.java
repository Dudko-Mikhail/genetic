package by.dudko.genetic.stepProcess.additionalgeneralization;

import by.dudko.genetic.model.gene.Gene;

import java.util.function.Supplier;

public interface GeneInitializer<V, T extends Gene<V>> extends Supplier<T> {
    // todo пример как переделывать классы при расширенной параметризации генов (выбор конкретной реализации гена)
}
