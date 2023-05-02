package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.Population;

public abstract class PopulationCrossover<T> {
    private ChromosomeCrossover<T> chromosomeCrossover;

    protected PopulationCrossover(ChromosomeCrossover<T> chromosomeCrossover) {
        this.chromosomeCrossover = chromosomeCrossover;
    }

    public abstract Population<T, ?> performCrossover(Population<T, ?> population, int newGenerationSize);

    public ChromosomeCrossover<T> getChromosomeCrossover() {
        return chromosomeCrossover;
    }

    public void setChromosomeCrossover(ChromosomeCrossover<T> chromosomeCrossover) {
        this.chromosomeCrossover = chromosomeCrossover;
    }
}
