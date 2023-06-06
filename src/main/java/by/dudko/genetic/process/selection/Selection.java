package by.dudko.genetic.process.selection;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

public interface Selection<G extends Gene<?, G>, F> { // todo селекция при нулевом размере популяции
    Population<G, F> select(Population<G, F> population, int selectedPopulationSize);
}
