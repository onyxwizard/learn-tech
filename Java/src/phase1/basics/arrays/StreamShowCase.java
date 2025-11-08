package phase1.basics.arrays;

import java.util.*;
import java.util.stream.*;

public class StreamShowCase {

    public static void main(String[] args) {
        System.out.println("🌊 STREAM SHOWCASE — All Array Types & Stream Methods\n");
        System.out.println("=".repeat(70));

        // ───────────────────────────────
        // 1. IntStream (from int[])
        // ───────────────────────────────
        int[] intArray = {10, 20, 30, 40, 50};
        System.out.println("🔢 IntStream (int[])");
        demonstrateIntStream(intArray);
        System.out.println("-".repeat(50));

        // ───────────────────────────────
        // 2. LongStream (from long[])
        // ───────────────────────────────
        long[] longArray = {100L, 200L, 300L, 400L};
        System.out.println("🔢 LongStream (long[])");
        demonstrateLongStream(longArray);
        System.out.println("-".repeat(50));

        // ───────────────────────────────
        // 3. DoubleStream (from double[])
        // ───────────────────────────────
        double[] doubleArray = {88.5, 92.0, 79.5, 95.0};
        System.out.println("📊 DoubleStream (double[])");
        demonstrateDoubleStream(doubleArray);
        System.out.println("-".repeat(50));

        // ───────────────────────────────
        // 4. Stream<T> (from String[])
        // ───────────────────────────────
        String[] stringArray = {"apple", "banana", "cherry", "date"};
        System.out.println("🔤 Stream<String> (String[])");
        demonstrateObjectStream(stringArray);
        System.out.println("-".repeat(50));

        // ───────────────────────────────
        // 5. Flattening 2D Arrays (Jagged)
        // ───────────────────────────────
        double[][] jaggedGrades = {
                {88.5, 92.0},
                {95.0, 87.5, 90.0},
                {100.0}
        };
        System.out.println("🧩 Flattening 2D Arrays (Jagged double[][])");
        demonstrateFlattening(jaggedGrades);
        System.out.println("=".repeat(70));

        System.out.println("\n✅ All stream types and methods demonstrated!");
    }

    // ───────────────────────────────
    // IntStream Methods
    // ───────────────────────────────
    private static void demonstrateIntStream(int[] arr) {
        IntStream stream = Arrays.stream(arr);

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Sum: " + stream.sum());

        // Recreate stream (consumed above)
        OptionalDouble avg = Arrays.stream(arr).average();
        System.out.println("Average: " + avg.orElse(0.0));

        OptionalInt min = Arrays.stream(arr).min();
        OptionalInt max = Arrays.stream(arr).max();
        System.out.println("Min: " + min.orElse(0) + " | Max: " + max.orElse(0));

        IntSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.println("Stats: " + stats);

        // Mapping
        System.out.print("Map to Long: ");
        Arrays.stream(arr)
                .mapToLong(x -> x * 10L)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        System.out.print("Map to Double: ");
        Arrays.stream(arr)
                .mapToDouble(x -> x / 2.0)
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();

        System.out.print("Map to String: ");
        Arrays.stream(arr)
                .mapToObj(x -> "Num:" + x)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        // Range example
        System.out.print("IntStream.range(1,4): ");
        IntStream.range(1, 4).forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    // ───────────────────────────────
    // LongStream Methods
    // ───────────────────────────────
    private static void demonstrateLongStream(long[] arr) {
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Sum: " + Arrays.stream(arr).sum());
        System.out.println("Average: " + Arrays.stream(arr).average().orElse(0.0));

        OptionalLong min = Arrays.stream(arr).min();
        OptionalLong max = Arrays.stream(arr).max();
        System.out.println("Min: " + min.orElse(0L) + " | Max: " + max.orElse(0L));

        LongSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.println("Stats: " + stats);

        // Range
        System.out.print("LongStream.range(10,13): ");
        LongStream.range(10, 13).forEach(x -> System.out.print(x + " "));
        System.out.println();

        // Mapping
        System.out.print("Map to Double: ");
        Arrays.stream(arr)
                .mapToDouble(x -> x / 100.0)
                .forEach(x -> System.out.printf("%.2f ", x));
        System.out.println();
    }

    // ───────────────────────────────
    // DoubleStream Methods
    // ───────────────────────────────
    private static void demonstrateDoubleStream(double[] arr) {
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Sum: " + Arrays.stream(arr).sum());
        System.out.println("Average: " + Arrays.stream(arr).average().orElse(0.0));

        OptionalDouble min = Arrays.stream(arr).min();
        OptionalDouble max = Arrays.stream(arr).max();
        System.out.println("Min: " + min.orElse(0.0) + " | Max: " + max.orElse(0.0));

        DoubleSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.println("Stats: " + stats);

        // Mapping
        System.out.print("Map to Int (rounded): ");
        Arrays.stream(arr)
                .mapToInt(x -> (int) Math.round(x))
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        System.out.print("Map to String: ");
        Arrays.stream(arr)
                .mapToObj(x -> String.format("%.1f", x))
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    // ───────────────────────────────
    // Stream<T> Methods (Object Stream)
    // ───────────────────────────────
    private static void demonstrateObjectStream(String[] arr) {
        System.out.println("Array: " + Arrays.toString(arr));

        // Filter
        System.out.print("Words > 5 chars: ");
        Arrays.stream(arr)
                .filter(s -> s.length() > 5)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        // Map
        System.out.print("Lengths: ");
        Arrays.stream(arr)
                .map(String::length)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        // Min/Max with Comparator
        Optional<String> shortest = Arrays.stream(arr).min(Comparator.comparing(String::length));
        Optional<String> longest = Arrays.stream(arr).max(Comparator.comparing(String::length));
        System.out.println("Shortest: " + shortest.orElse("N/A") + " | Longest: " + longest.orElse("N/A"));

        // Collect to List
        List<String> list = Arrays.stream(arr)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("Uppercase List: " + list);

        // Count
        long count = Arrays.stream(arr).count();
        System.out.println("Total words: " + count);
    }

    // ───────────────────────────────
    // Flattening 2D Arrays
    // ───────────────────────────────
    private static void demonstrateFlattening(double[][] jagged) {
        System.out.println("Original:");
        for (int i = 0; i < jagged.length; i++) {
            System.out.println("  Student " + i + ": " + Arrays.toString(jagged[i]));
        }

        // Flatten to single DoubleStream
        DoubleStream allGrades = Arrays.stream(jagged)
                .flatMapToDouble(Arrays::stream);

        DoubleSummaryStatistics global = allGrades.summaryStatistics();
        System.out.printf("Flattened Stats → Count: %d, Avg: %.1f, Min: %.1f, Max: %.1f%n",
                global.getCount(), global.getAverage(), global.getMin(), global.getMax());

        // Count grades >= 90
        long highGrades = Arrays.stream(jagged)
                .flatMapToDouble(Arrays::stream)
                .filter(g -> g >= 90)
                .count();
        System.out.println("Grades ≥ 90: " + highGrades);
    }
}
