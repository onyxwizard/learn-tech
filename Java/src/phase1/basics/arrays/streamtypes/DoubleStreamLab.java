package phase1.basics.arrays.streamtypes;

import java.util.*;
import java.util.stream.*;

public class DoubleStreamLab {

    // ───────────────────────────────
    // 🔢 Core Aggregations
    // ───────────────────────────────
    public void aggregate(double[] arr) {
        System.out.println("Sum: " + Arrays.stream(arr).sum());
    }

    public void avg(double[] arr) {
        System.out.println("Average: " + Arrays.stream(arr).average().orElse(0.0));
    }

    public void countVal(double[] arr) {
        System.out.println("Count: " + Arrays.stream(arr).count());
    }

    public void minimum(double[] arr) {
        System.out.println("Minimum: " + Arrays.stream(arr).min().orElse(Double.MAX_VALUE));
    }

    public void maximum(double[] arr) {
        System.out.println("Maximum: " + Arrays.stream(arr).max().orElse(Double.MIN_VALUE));
    }

    public void stats(double[] arr) {
        DoubleSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.printf("Min: %.2f | Max: %.2f | Avg: %.2f | Count: %d | Sum: %.2f%n",
                stats.getMin(), stats.getMax(), stats.getAverage(), stats.getCount(), stats.getSum());
        System.out.println("Full Stats: " + stats);
    }

    public void list(double[] arr) {
        System.out.print("Elements: [");
        Arrays.stream(arr).forEach(value -> System.out.print(value + " "));
        System.out.println("]");
    }

    // ───────────────────────────────
    // 🔄 Transformation: map()
    // ───────────────────────────────
    public void demonstrateMap(double[] arr) {
        System.out.print("Values + 10.5: ");
        Arrays.stream(arr)
                .map(x -> x + 10.5)
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();
    }

    // ───────────────────────────────
    // 🔍 Filtering: filter()
    // ───────────────────────────────
    public void demonstrateFilter(double[] arr) {
        System.out.print("Grades ≥ 90: ");
        Arrays.stream(arr)
                .filter(x -> x >= 90.0)
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();

        // Even integer values (e.g., 88.0, 90.0)
        System.out.print("Even whole numbers: ");
        Arrays.stream(arr)
                .filter(x -> x == Math.floor(x) && ((int) x) % 2 == 0)
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();
    }

    // ───────────────────────────────
    // ✅ Matching: allMatch(), anyMatch(), noneMatch()
    // ───────────────────────────────
    public void demonstrateMatching(double[] arr) {
        boolean allPositive = Arrays.stream(arr).allMatch(x -> x > 0);
        boolean anyHigh = Arrays.stream(arr).anyMatch(x -> x >= 90.0);
        boolean noneNegative = Arrays.stream(arr).noneMatch(x -> x < 0);

        System.out.println("All positive? " + allPositive);
        System.out.println("Any grade ≥ 90? " + anyHigh);
        System.out.println("None negative? " + noneNegative);
    }

    // ───────────────────────────────
    // 📦 Collecting Results
    // ───────────────────────────────
    public void demonstrateCollect(double[] arr) {
        // To List<Double>
        List<Double> list = Arrays.stream(arr)
                .boxed() // required for collection
                .collect(Collectors.toList());
        System.out.println("As List: " + list);

        // To Set (unique values)
        Set<Double> set = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toSet());
        System.out.println("As Set (unique): " + set);

        // Back to double[]
        double[] newArray = Arrays.stream(arr)
                .map(x -> x * 1.1) // e.g., 10% bonus
                .toArray();
        System.out.println("With 10% bonus: " + Arrays.toString(newArray));
    }

    // ───────────────────────────────
    // ➗ Reduction: reduce()
    // ───────────────────────────────
    public void demonstrateReduce(double[] arr) {
        // Sum using reduce
        double sum = Arrays.stream(arr)
                .reduce(0.0, Double::sum);
        System.out.println("Sum via reduce: " + sum);

        // Product
        double product = Arrays.stream(arr)
                .reduce(1.0, (a, b) -> a * b);
        System.out.println("Product: " + product);
    }

    // ───────────────────────────────
    // 🧹 Other Useful Ops
    // ───────────────────────────────
    public void demonstrateDistinctAndSorted(double[] arr) {
        System.out.print("Distinct & sorted: ");
        Arrays.stream(arr)
                .distinct()
                .sorted()
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();
    }

    public void demonstrateLimitAndSkip(double[] arr) {
        System.out.print("First 2 elements: ");
        Arrays.stream(arr)
                .limit(2)
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();

        System.out.print("Skip first 2: ");
        Arrays.stream(arr)
                .skip(2)
                .forEach(x -> System.out.printf("%.1f ", x));
        System.out.println();
    }

}