package phase1.basics.arrays.streamtypes;

import java.lang.*;
import java.util.*;
import java.util.stream.*;

public class LongStreamLab {

    // ───────────────────────────────
    // 🔢 Core Aggregations
    // ───────────────────────────────
    public void aggregate(long[] arr) {
        System.out.println("Sum: " + Arrays.stream(arr).sum());
    }

    public void avg(long[] arr) {
        System.out.println("Average: " + Arrays.stream(arr).average().orElse(0.0));
    }

    public void countVal(long[] arr) {
        System.out.println("Count: " + Arrays.stream(arr).count());
    }

    public void minimum(long[] arr) {
        System.out.println("Minimum: " + Arrays.stream(arr).min().orElse(Long.MAX_VALUE));
    }

    public void maximum(long[] arr) {
        System.out.println("Maximum: " + Arrays.stream(arr).max().orElse(Long.MIN_VALUE));
    }

    public void stats(long[] arr) {
        LongSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.printf("Min: %d | Max: %d | Avg: %.2f | Count: %d | Sum: %d%n",
                stats.getMin(), stats.getMax(), stats.getAverage(), stats.getCount(), stats.getSum());
        System.out.println("Full Stats: " + stats);
    }

    public void list(long[] arr) {
        System.out.print("Elements: [");
        Arrays.stream(arr).forEach(value -> System.out.print(value + " "));
        System.out.println("]");
    }

    // ───────────────────────────────
    // 🔄 Transformation: map()
    // ───────────────────────────────
    public void demonstrateMap(long[] arr) {
        System.out.print("Values × 2: ");
        Arrays.stream(arr)
                .map(x -> x * 2L)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    // ───────────────────────────────
    // 🔍 Filtering: filter()
    // ───────────────────────────────
    public void demonstrateFilter(long[] arr) {
        System.out.print("Even numbers: ");
        Arrays.stream(arr)
                .filter(x -> x % 2 == 0)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        System.out.print("Values > 1000: ");
        Arrays.stream(arr)
                .filter(x -> x > 1000L)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    // ───────────────────────────────
    // ✅ Matching: allMatch(), anyMatch(), noneMatch()
    // ───────────────────────────────
    public void demonstrateMatching(long[] arr) {
        boolean allPositive = Arrays.stream(arr).allMatch(x -> x > 0);
        boolean anyLarge = Arrays.stream(arr).anyMatch(x -> x > 1000L);
        boolean noneNegative = Arrays.stream(arr).noneMatch(x -> x < 0);

        System.out.println("All positive? " + allPositive);
        System.out.println("Any value > 1000? " + anyLarge);
        System.out.println("None negative? " + noneNegative);
    }

    // ───────────────────────────────
    // 📦 Collecting Results
    // ───────────────────────────────
    public void demonstrateCollect(long[] arr) {
        // To List<Long>
        List<Long> list = Arrays.stream(arr)
                .boxed() // required for collection
                .collect(Collectors.toList());
        System.out.println("As List: " + list);

        // To Set (unique values)
        Set<Long> set = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toSet());
        System.out.println("As Set (unique): " + set);

        // Back to long[]
        long[] newArray = Arrays.stream(arr)
                .map(x -> x + 100L)
                .toArray();
        System.out.println("Added 100 to each: " + Arrays.toString(newArray));
    }

    // ───────────────────────────────
    // ➗ Reduction: reduce()
    // ───────────────────────────────
    public void demonstrateReduce(long[] arr) {
        // Sum using reduce
        long sum = Arrays.stream(arr)
                .reduce(0L, Long::sum);
        System.out.println("Sum via reduce: " + sum);

        // Product
        long product = Arrays.stream(arr)
                .reduce(1L, (a, b) -> a * b);
        System.out.println("Product: " + product);
    }

    // ───────────────────────────────
    // 🧹 Other Useful Ops
    // ───────────────────────────────
    public void demonstrateDistinctAndSorted(long[] arr) {
        System.out.print("Distinct & sorted: ");
        Arrays.stream(arr)
                .distinct()
                .sorted()
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    public void demonstrateLimitAndSkip(long[] arr) {
        System.out.print("First 2 elements: ");
        Arrays.stream(arr)
                .limit(2)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();

        System.out.print("Skip first 2: ");
        Arrays.stream(arr)
                .skip(2)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }
}
