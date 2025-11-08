package phase1.basics.arrays.streamtypes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectStreamLab {

    // ───────────────────────────────
    // 🖨️ Display
    // ───────────────────────────────

    public void list(String[] arr) {
        System.out.print("Elements: [");
        Arrays.stream(arr).forEach(value -> System.out.print("\"" + value + "\" "));
        System.out.println("]");
    }

    // ───────────────────────────────
    // 🔢 Counting (only numeric op available)
    // ───────────────────────────────
    public void countVal(String[] arr) {
        System.out.println("Count: " + Arrays.stream(arr).count());
    }

    // ───────────────────────────────
    // 🔍 Filtering: filter()
    // ───────────────────────────────
    public void demonstrateFilter(String[] arr) {
        System.out.print("Words with length > 5: ");
        Arrays.stream(arr)
                .filter(s -> s.length() > 5)
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();

        System.out.print("Words starting with 'a' (case-insensitive): ");
        Arrays.stream(arr)
                .filter(s -> s.toLowerCase().startsWith("a"))
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();
    }

    // ───────────────────────────────
    // 🔄 Transformation: map()
    // ───────────────────────────────
    public void demonstrateMap(String[] arr) {
        System.out.print("Uppercase: ");
        Arrays.stream(arr)
                .map(String::toUpperCase)
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();

        System.out.print("Lengths: ");
        Arrays.stream(arr)
                .map(String::length)
                .forEach(len -> System.out.print(len + " "));
        System.out.println();

        // Map to another object type
        System.out.print("As char counts: ");
        Arrays.stream(arr)
                .map(s -> s + " (" + s.length() + ")")
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();
    }

    // ───────────────────────────────
    // ✅ Matching: allMatch(), anyMatch(), noneMatch()
    // ───────────────────────────────
    public void demonstrateMatching(String[] arr) {
        boolean allNonEmpty = Arrays.stream(arr).allMatch(s -> !s.isEmpty());
        boolean anyStartsWithB = Arrays.stream(arr).anyMatch(s -> s.startsWith("B"));
        boolean noneNull = Arrays.stream(arr).noneMatch(Objects::isNull);

        System.out.println("All non-empty? " + allNonEmpty);
        System.out.println("Any starts with 'B'? " + anyStartsWithB);
        System.out.println("None null? " + noneNull);
    }

    // ───────────────────────────────
    // 📦 Collecting Results
    // ───────────────────────────────
    public void demonstrateCollect(String[] arr) {
        // To List
        List<String> list = Arrays.stream(arr)
                .collect(Collectors.toList());
        System.out.println("As List: " + list);

        // To Set (unique)
        Set<String> set = Arrays.stream(arr)
                .collect(Collectors.toSet());
        System.out.println("As Set (unique): " + set);

        // To sorted List
        List<String> sorted = Arrays.stream(arr)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Sorted List: " + sorted);

        // Group by length
        Map<Integer, List<String>> grouped = Arrays.stream(arr)
                .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + grouped);
    }

    // ───────────────────────────────
    // 🔤 Min / Max (requires Comparator)
    // ───────────────────────────────
    public void demonstrateMinMax(String[] arr) {
        Optional<String> shortest = Arrays.stream(arr)
                .min(Comparator.comparing(String::length));
        Optional<String> longest = Arrays.stream(arr)
                .max(Comparator.comparing(String::length));
        Optional<String> firstAlphabetical = Arrays.stream(arr)
                .min(Comparator.naturalOrder());

        System.out.println("Shortest word: " + shortest.orElse("N/A"));
        System.out.println("Longest word: " + longest.orElse("N/A"));
        System.out.println("First alphabetical: " + firstAlphabetical.orElse("N/A"));
    }

    // ───────────────────────────────
    // 🧹 Other Useful Ops
    // ───────────────────────────────
    public void demonstrateDistinctAndSorted(String[] arr) {
        System.out.print("Distinct & sorted: ");
        Arrays.stream(arr)
                .distinct()
                .sorted()
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();
    }

    public void demonstrateLimitAndSkip(String[] arr) {
        System.out.print("First 2 elements: ");
        Arrays.stream(arr)
                .limit(2)
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();

        System.out.print("Skip first 2: ");
        Arrays.stream(arr)
                .skip(2)
                .forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println();
    }

    // ───────────────────────────────
    // ➗ Reduction: reduce()
    // ───────────────────────────────
    public void demonstrateReduce(String[] arr) {
        // Concatenate with delimiter
        String joined = Arrays.stream(arr)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        System.out.println("Joined: \"" + joined + "\"");

        // Find longest word via reduce
        Optional<String> longest = Arrays.stream(arr)
                .reduce((a, b) -> a.length() >= b.length() ? a : b);
        System.out.println("Longest via reduce: " + longest.orElse("N/A"));
    }
    }
