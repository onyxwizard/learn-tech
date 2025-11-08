package phase1.basics.arrays.demo;

import phase1.basics.arrays.streamtypes.DoubleStreamLab;
import phase1.basics.arrays.streamtypes.IntStreamLab;
import phase1.basics.arrays.streamtypes.LongStreamLab;
import phase1.basics.arrays.streamtypes.ObjectStreamLab;

public class StreamDemo {
    public static void main(String[] args) {
        // Test array with variety: positives, negatives, duplicates, evens/odds
        int[] numbers = {10, -5, 20, 15, 20, 0, 7, 14};

        System.out.println("🧪 IntStreamLab — Comprehensive Stream Operations Demo");
        System.out.println("=".repeat(60));
        System.out.println("Test Array: " + java.util.Arrays.toString(numbers));
        System.out.println();

        IntStreamLab streamIntLab = new IntStreamLab();

        // 🔢 Core Aggregations
        System.out.println("📊 Core Aggregations:");
        streamIntLab.aggregate(numbers);
        streamIntLab.avg(numbers);
        streamIntLab.countVal(numbers);
        streamIntLab.minimum(numbers);
        streamIntLab.maximum(numbers);
        streamIntLab.stats(numbers);
        System.out.println();

        // 🖨️ Display
        System.out.println("🖨️  Raw Elements:");
        streamIntLab.list(numbers);
        System.out.println();

        // 🔄 Transform & Filter
        System.out.println("🔄 Transformations & Filtering:");
        streamIntLab.demonstrateMap(numbers);
        streamIntLab.demonstrateFilter(numbers);
        streamIntLab.demonstrateDistinctAndSorted(numbers);
        streamIntLab.demonstrateLimitAndSkip(numbers);
        System.out.println();

        // ✅ Matching
        System.out.println("✅ Matching Operations:");
        streamIntLab.demonstrateMatching(numbers);
        System.out.println();

        // 📦 Collection & Reduction
        System.out.println("📦 Collection & Reduction:");
        streamIntLab.demonstrateCollect(numbers);
        streamIntLab.demonstrateReduce(numbers);
        System.out.println();

        System.out.println("✅ IntStreamLab demo completed successfully!");



        // Test array with variety: positives, negatives, duplicates, high/low grades
        double[] grades = {88.5, -5.0, 92.0, 79.5, 92.0, 0.0, 100.0, 85.5};

        System.out.println("📊 DoubleStreamLab — Comprehensive Stream Operations Demo");
        System.out.println("=".repeat(60));
        System.out.println("Test Array: " + java.util.Arrays.toString(grades));
        System.out.println();

        DoubleStreamLab streamDoubleLab = new DoubleStreamLab();

        // 🔢 Core Aggregations
        System.out.println("📊 Core Aggregations:");
        streamDoubleLab.aggregate(grades);
        streamDoubleLab.avg(grades);
        streamDoubleLab.countVal(grades);
        streamDoubleLab.minimum(grades);
        streamDoubleLab.maximum(grades);
        streamDoubleLab.stats(grades);
        System.out.println();

        // 🖨️ Display
        System.out.println("🖨️  Raw Elements:");
        streamDoubleLab.list(grades);
        System.out.println();

        // 🔄 Transform & Filter
        System.out.println("🔄 Transformations & Filtering:");
        streamDoubleLab.demonstrateMap(grades);
        streamDoubleLab.demonstrateFilter(grades);
        streamDoubleLab.demonstrateDistinctAndSorted(grades);
        streamDoubleLab.demonstrateLimitAndSkip(grades);
        System.out.println();

        // ✅ Matching
        System.out.println("✅ Matching Operations:");
        streamDoubleLab.demonstrateMatching(grades);
        System.out.println();

        // 📦 Collection & Reduction
        System.out.println("📦 Collection & Reduction:");
        streamDoubleLab.demonstrateCollect(grades);
        streamDoubleLab.demonstrateReduce(grades);
        System.out.println();
        System.out.println();
        System.out.println("✅ DoubleStreamLab demo completed successfully!");


        // Test array with variety: positives, negatives, duplicates, large values
        long[] bigNumbers = {1001L, -500L, 2002L, 1500L, 2002L, 0L, 700L, 1400L};

        System.out.println("🔢 LongStreamLab — Comprehensive Stream Operations Demo");
        System.out.println("=".repeat(60));
        System.out.println("Test Array: " + java.util.Arrays.toString(bigNumbers));
        System.out.println();

        LongStreamLab streamLongLab = new LongStreamLab();

        // 🔢 Core Aggregations
        System.out.println("📊 Core Aggregations:");
        streamLongLab.aggregate(bigNumbers);
        streamLongLab.avg(bigNumbers);
        streamLongLab.countVal(bigNumbers);
        streamLongLab.minimum(bigNumbers);
        streamLongLab.maximum(bigNumbers);
        streamLongLab.stats(bigNumbers);
        System.out.println();

        // 🖨️ Display
        System.out.println("🖨️  Raw Elements:");
        streamLongLab.list(bigNumbers);
        System.out.println();

        // 🔄 Transform & Filter
        System.out.println("🔄 Transformations & Filtering:");
        streamLongLab.demonstrateMap(bigNumbers);
        streamLongLab.demonstrateFilter(bigNumbers);
        streamLongLab.demonstrateDistinctAndSorted(bigNumbers);
        streamLongLab.demonstrateLimitAndSkip(bigNumbers);
        System.out.println();

        // ✅ Matching
        System.out.println("✅ Matching Operations:");
        streamLongLab.demonstrateMatching(bigNumbers);
        System.out.println();

        // 📦 Collection & Reduction
        System.out.println("📦 Collection & Reduction:");
        streamLongLab.demonstrateCollect(bigNumbers);
        streamLongLab.demonstrateReduce(bigNumbers);
        System.out.println();

        System.out.println("✅ LongStreamLab demo completed successfully!");
        System.out.println();

        // Test array with variety: duplicates, empty, mixed case, different lengths
        String[] words = {"apple", "Banana", "cherry", "date", "apple", "", "Avocado", "Fig"};

        System.out.println("🔤 ObjectStreamLab — Comprehensive Stream Operations Demo");
        System.out.println("=".repeat(60));
        System.out.println("Test Array: " + java.util.Arrays.toString(words));
        System.out.println();

        ObjectStreamLab streamObjectLab = new ObjectStreamLab();

        // 🖨️ Display & Count
        System.out.println("📊 Basic Operations:");
        streamObjectLab.list(words);
        streamObjectLab.countVal(words);
        System.out.println();

        // 🔍 Filter & Transform
        System.out.println("🔄 Filtering & Transformations:");
        streamObjectLab.demonstrateFilter(words);
        streamObjectLab.demonstrateMap(words);
        streamObjectLab.demonstrateDistinctAndSorted(words);
        streamObjectLab.demonstrateLimitAndSkip(words);
        System.out.println();

        // ✅ Matching
        System.out.println("✅ Matching Operations:");
        streamObjectLab.demonstrateMatching(words);
        System.out.println();

        // 📦 Collection, Min/Max, Reduction
        System.out.println("📦 Collection, Min/Max & Reduction:");
        streamObjectLab.demonstrateCollect(words);
        streamObjectLab.demonstrateMinMax(words);
        streamObjectLab.demonstrateReduce(words);
        System.out.println();

        System.out.println("✅ ObjectStreamLab demo completed successfully!");
    }
}