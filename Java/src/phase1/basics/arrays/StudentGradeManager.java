package phase1.basics.arrays;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class StudentGradeManager {

    private final double[][] studentGrades = {
            {88.5, 92.0, 79.5},        // Student 0
            {95.0, 87.5},              // Student 1
            {76.0, 82.5, 90.0, 88.0},  // Student 2
            {100.0}                    // Student 3
    };

    public void printAllGrades() {
        System.out.println("📚 Raw Grades:");
        for (int i = 0; i < studentGrades.length; i++) {
            System.out.print("Student " + i + ": ");
            for (int j = 0; j < studentGrades[i].length; j++) {
                System.out.print(studentGrades[i][j]);
                if (j < studentGrades[i].length - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public void calculateStats() {
        System.out.println("📊 Per-Student Statistics:");
        for (int i = 0; i < studentGrades.length; i++) {
            double sum = 0.0;
            double minVal = Double.POSITIVE_INFINITY;
            double maxVal = Double.NEGATIVE_INFINITY;

            for (double grade : studentGrades[i]) {
                sum += grade;
                if (grade < minVal) minVal = grade;
                if (grade > maxVal) maxVal = grade;
            }
            double average = sum / studentGrades[i].length;
            System.out.printf("Student %d → Avg: %.1f | Min: %.1f | Max: %.1f%n",
                    i, average, minVal, maxVal);
        }
    }

    public void demonstrateArrayCopy(int studentIndex) {
        System.out.println("📋 Array Copy Demonstration (Student " + studentIndex + "):");
        double[] original = studentGrades[studentIndex];
        double[] copy = new double[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        copy[0] = 0.0; // Modify ONLY the copy

        System.out.println("Original: " + Arrays.toString(original));
        System.out.println("Copy    : " + Arrays.toString(copy) + "  ← First element modified!");
    }

    public void demonstrateArraysUtility() {
        System.out.println("🧰 Arrays Utility Showcase:");
        System.out.println("📊 Full Grade Book (deepToString):");
        System.out.println(Arrays.deepToString(studentGrades));

        // Sort a COPY of Student 2's grades to avoid mutating original
        double[] student2Sorted = Arrays.copyOf(studentGrades[2], studentGrades[2].length);
        Arrays.sort(student2Sorted);
        System.out.println("\n📈 Sorted Grades for Student 2 (after Arrays.sort):");
        System.out.println(Arrays.toString(student2Sorted));
    }

    // 💡 Stream-based stats (recreates stream for each operation)
    public void calculateStatsWithStreams() {
        System.out.println("🌊 Stream-Based Statistics (Recreated Streams):");
        for (int i = 0; i < studentGrades.length; i++) {
            double average = Arrays.stream(studentGrades[i]).average().orElse(0.0);
            double min = Arrays.stream(studentGrades[i]).min().orElse(Double.MAX_VALUE);
            double max = Arrays.stream(studentGrades[i]).max().orElse(Double.MIN_VALUE);

            System.out.printf("Student %d → Avg: %.1f | Min: %.1f | Max: %.1f%n",
                    i, average, min, max);
        }
    }

    // ✅ Efficient stream stats (single pass using summaryStatistics)
    public void calculateStatsEfficiently() {
        System.out.println("⚡ Efficient Stream Statistics (summaryStatistics):");
        for (int i = 0; i < studentGrades.length; i++) {
            DoubleSummaryStatistics stats = Arrays.stream(studentGrades[i])
                    .summaryStatistics();
            System.out.printf("Student %d → Avg: %.1f | Min: %.1f | Max: %.1f%n",
                    i, stats.getAverage(), stats.getMin(), stats.getMax());
        }
    }

    public void printSeparator() {
        System.out.println("──────────────────────────────────────────────────");
    }
}