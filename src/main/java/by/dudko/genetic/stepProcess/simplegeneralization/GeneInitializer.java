package by.dudko.genetic.stepProcess.simplegeneralization;

import by.dudko.genetic.model.gene.Gene;

import java.util.function.Supplier;

public interface GeneInitializer<T> extends Supplier<Gene<T>>{
}
