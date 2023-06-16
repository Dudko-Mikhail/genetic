package by.dudko.genetic.example.salesman;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class DataReader {
    private final Path path;

    public DataReader(String fileName) {
        Path path = Path.of(fileName);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Given file doesn't exists. %s".formatted(fileName));
        }
        this.path = path;
    }

    public int[][] readGraph(int dimension) {
        int requiredSize = dimension * dimension;
        int[][] graph = new int[dimension][dimension];
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            skipLines(7, reader);
            ArrayList<Integer> data = new ArrayList<>(requiredSize);
            while (reader.ready()) {
                String[] lineData = reader.readLine().trim()
                        .split("\\s+");
                for (var obj: lineData) {
                    if (isDigit(obj)) {
                        data.add(Integer.valueOf(obj));
                    }
                }
            }
            if (data.size() != requiredSize) {
                throw new RuntimeException("Something bad happend. Required size %d, actual size %s"
                        .formatted(requiredSize, data.size()));
            }
            for (int i = 0, counter = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++, counter++) {
                    graph[i][j] = data.get(counter);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Arrays.stream(graph)
                .forEach(arr -> System.out.println(Arrays.toString(arr)));
        return graph;
    }

    private static void skipLines(int number, BufferedReader reader) {
        for (int i = 0; i < number; i++) {
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean isDigit(String text) {
        return Pattern.matches("\\d+", text);
    }
}
