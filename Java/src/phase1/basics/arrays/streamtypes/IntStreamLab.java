package phase1.basics.arrays.streamtypes;

import java.util.*;
import java.util.stream.*;

public class IntStreamLab {

    public void aggregate(int[] arr){
        System.out.println("Sum : "+ Arrays.stream(arr).sum());
    }

    public void avg(int[] arr){
        System.out.println("Average : "+ Arrays.stream(arr).average());
    }

    public void countVal(int[] arr){
        System.out.println("Count : "+ Arrays.stream(arr).count());
    }

    public void minimum(int[] arr){
        System.out.println("Minimum : "+ Arrays.stream(arr).min());
    }

    public void maximum(int[] arr){
        System.out.println("Maximum : "+ Arrays.stream(arr).max());
    }

    public void stats(int[] arr){
        IntSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.println("Min: " + stats.getMin() + " | Max: " + stats.getMax()+" | Average: " + stats.getAverage()+" | Count: " + stats.getCount()+" | Sum: " + stats.getSum());
        System.out.println("------------------------------------------------");
        System.out.println("Full Stats: " + stats);
    }

    public void list(int[] arr){
        System.out.println("Printing all elements:");
        System.out.print("[");
        Arrays.stream(arr).forEach(value -> System.out.print(value+","));
        System.out.print("]");
    }

    // ───────────────────────────────
    // 🔄 Transformation: map()
    // ───────────────────────────────
    public void demonstrateMap(int[] arr) {
        System.out.print("Doubled values: ");
        Arrays.stream(arr)
                .map(x -> x * 2)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    // ───────────────────────────────
    // 🔍 Filtering: filter()
    // ───────────────────────────────
    public void demonstrateFilter(int[] arr) {
        System.out.print("Even numbers: ");
        Arrays.stream(arr)
                .filter(x -> x % 2 == 0)
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    // ───────────────────────────────
    // ✅ Matching: allMatch(), anyMatch(), noneMatch()
    // ───────────────────────────────
    public void demonstrateMatching(int[] arr) {
        boolean allPositive = Arrays.stream(arr).allMatch(x -> x > 0);
        boolean anyEven = Arrays.stream(arr).anyMatch(x -> x % 2 == 0);
        boolean noneNegative = Arrays.stream(arr).noneMatch(x -> x < 0);

        System.out.println("All positive? " + allPositive);
        System.out.println("Any even? " + anyEven);
        System.out.println("None negative? " + noneNegative);
    }

    // ───────────────────────────────
    // 📦 Collecting Results
    // ───────────────────────────────
    public void demonstrateCollect(int[] arr) {
        // To List<Integer>
        List<Integer> list = Arrays.stream(arr)
                .boxed() // ← required to collect to List
                .collect(Collectors.toList());
        System.out.println("As List: " + list);

        // To Set
        Set<Integer> set = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toSet());
        System.out.println("As Set (unique): " + set);

        // Back to int[]
        int[] newArray = Arrays.stream(arr)
                .map(x -> x + 10)
                .toArray();
        System.out.println("Mapped to new array: " + Arrays.toString(newArray));
    }

    // ───────────────────────────────
    // ➗ Reduction: reduce()
    // ───────────────────────────────
    public void demonstrateReduce(int[] arr) {
        // Sum using reduce (same as .sum())
        int sum = Arrays.stream(arr)
                .reduce(0, Integer::sum);
        System.out.println("Sum via reduce: " + sum);

        // Product
        int product = Arrays.stream(arr)
                .reduce(1, (a, b) -> a * b);
        System.out.println("Product: " + product);
    }

    // ───────────────────────────────
    // 🧹 Other Useful Ops
    // ───────────────────────────────
    public void demonstrateDistinctAndSorted(int[] arr) {
        System.out.print("Distinct & sorted: ");
        Arrays.stream(arr)
                .distinct()
                .sorted()
                .forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    public void demonstrateLimitAndSkip(int[] arr) {
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
