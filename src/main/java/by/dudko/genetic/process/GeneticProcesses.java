package by.dudko.genetic.process;

import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;

public class GeneticProcesses { // todo Внедрить в алгоритм или удалить. Параметризовать. Builder + реализовать логику
    Selection<?, ?> selection;
    PopulationCrossover<?, ?> crossover;
    PopulationMutation<?, ?> mutation;
    Replacement<?, ?> replacement;
}
