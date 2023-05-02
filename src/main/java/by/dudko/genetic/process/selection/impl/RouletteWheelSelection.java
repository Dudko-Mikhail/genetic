package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.selection.Selection;

public class RouletteWheelSelection<T, F> implements Selection<T, F> { // todo Нужна возможность складывать значения приспособленности
    @Override
    public Population<T, F> select(Population<T, F> population, int selectedPopulationSize) {
        return null;
    }
}