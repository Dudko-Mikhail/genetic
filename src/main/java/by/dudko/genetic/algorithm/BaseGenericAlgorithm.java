//package by.dudko.genetic.algorithm;
//
//import by.dudko.genetic.model.Population;
//
//public interface BaseGenericAlgorithm<T> { // todo user or remove this
//
//    default void runAlgorithm() {
//        Population<T> population = initializePopulation();
//        while (isTimeToStop()) {
//            Population<T> selectedIndividuals = performSelection(population);
//            Population<T> offspring = performCrossover(selectedIndividuals);
//            offspring = performMutation(offspring);
//            population = performReplacement(population, offspring);
//            incrementGenerationNumber();
//        }
//    }
//
//    Population<T> initializePopulation();
//
//    Population<T> performSelection(Population<T> population);
//
//    Population<T> performCrossover(Population<T> offspring);
//
//    Population<T> performMutation(Population<T> offspring);
//
//    Population<T> performReplacement(Population<T> oldGeneration, Population<T> offspring);
//
//    boolean isTimeToStop();
//
//    int getGenerationNumber();
//
//    void incrementGenerationNumber();
//}
