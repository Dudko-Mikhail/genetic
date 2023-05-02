package by.dudko.genetic.process.evaluation;

import by.dudko.genetic.model.Population;

public interface StopFunction<F> {
    boolean isTimeToStop(Population<?, F> population); // todo Алгоритм вместо популяции
}
