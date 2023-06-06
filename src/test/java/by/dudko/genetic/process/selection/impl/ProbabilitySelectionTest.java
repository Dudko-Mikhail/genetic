package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.BooleanGene;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class ProbabilitySelectionTest {
    private final ProbabilitySelection<BooleanGene, Integer> selection = new ProbabilitySelection<>(new Random()) {
        @Override
        public double[] calculateProbabilities(Population<BooleanGene, Integer> population) {
            double[] arr = new double[10];
            Arrays.fill(arr, 0.1);
            return arr;
        }
    };
    private final double[] partialSums = new double[]{0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};

    @ParameterizedTest
    @ArgumentsSource(value = SearchArgumentsProvider.class)
    void linearSearchTest(double value, int expectedResult) {
        Assertions.assertEquals(expectedResult, selection.linearSearch(partialSums, value, 0));
    }

    @ParameterizedTest
    @ArgumentsSource(value = SearchArgumentsProvider.class)
    void binarySearchTest(double value, int expectedResult) {
        Assertions.assertEquals(expectedResult, selection.binarySearch(partialSums, value, 0));
    }

    @Test
    void binarySearchTextResultShouldBeMoreOrEqualToStartIndex() {
        Assertions.assertEquals(4, selection.binarySearch(partialSums, -9, 4));
    }

    static class SearchArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(-2.0, 0),
                    Arguments.of(0.0, 0),
                    Arguments.of(0.1, 1),
                    Arguments.of(0.15, 2),
                    Arguments.of(0.2, 2),
                    Arguments.of(1.0, 10),
                    Arguments.of(2.0, 10)
            );
        }
    }
}
