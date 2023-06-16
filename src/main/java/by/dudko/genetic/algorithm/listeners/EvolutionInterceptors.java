package by.dudko.genetic.algorithm.listeners;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.ArrayList;
import java.util.List;

public class EvolutionInterceptors<G extends Gene<?, G>, F> implements EvolutionInterceptor<G, F> {
    private final List<SelectionListener<G, F>> selectionListeners = new ArrayList<>();
    private final List<CrossoverListener<G, F>> crossoverListeners = new ArrayList<>();
    private final List<MutationListener<G, F>> mutationListeners = new ArrayList<>();
    private final List<ReplacementListener<G, F>> replacementListeners = new ArrayList<>();

    @Override
    public void beforeSelection(GeneticAlgorithm<G, F> geneticAlgorithm) {
        selectionListeners.forEach(listener -> listener.beforeSelection(geneticAlgorithm));
    }

    @Override
    public void beforeCrossover(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> parents) {
        crossoverListeners.forEach(listener -> listener.beforeCrossover(geneticAlgorithm, parents));
    }

    @Override
    public void beforeMutation(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> offspring) {
        mutationListeners.forEach(listener -> listener.beforeMutation(geneticAlgorithm, offspring));

    }

    @Override
    public void beforeReplacement(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> mutatedOffspring) {
        replacementListeners.forEach(listener -> listener.beforeReplacement(geneticAlgorithm, mutatedOffspring));

    }

    public void addSelectionListener(SelectionListener<G, F> selectionListener) {
        if (selectionListener != null) {
            selectionListeners.add(selectionListener);
        }
    }

    public void addCrossoverListener(CrossoverListener<G, F> crossoverListener) {
        if (crossoverListener != null) {
            crossoverListeners.add(crossoverListener);
        }
    }

    public void addMutationListener(MutationListener<G, F> mutationListener) {
        if (mutationListener != null) {
            mutationListeners.add(mutationListener);
        }
    }

    public void addReplacementListener(ReplacementListener<G, F> replacementListener) {
        if (replacementListener != null) {
            replacementListeners.add(replacementListener);
        }
    }

    public void registerEvolutionInterceptor(EvolutionInterceptor<G, F> evolutionInterceptor) {
        selectionListeners.add(evolutionInterceptor);
        crossoverListeners.add(evolutionInterceptor);
        mutationListeners.add(evolutionInterceptor);
        replacementListeners.add(evolutionInterceptor);
    }
}
