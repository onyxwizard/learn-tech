package phase1.basics.arrays.demo;

import phase1.basics.arrays.StudentGradeManager;

public class StudentGradeManagerDemo {
    public static void main(String[] args) {
        StudentGradeManager manager = new StudentGradeManager();

        // 🎓 Header
        System.out.println("🎓 STUDENT GRADE MANAGER — ARRAY CONCEPTS DEMO 🎓");
        System.out.println();

        // 1️⃣ Raw Grades
        manager.printAllGrades();
        manager.printSeparator();

        // 2️⃣ Traditional Stats (using loops)
        manager.calculateStats();
        manager.printSeparator();

        // 3️⃣ Array Copy Demo
        manager.demonstrateArrayCopy(0);
        manager.printSeparator();

        // 4️⃣ Arrays Utility (deepToString, sort)
        manager.demonstrateArraysUtility();
        manager.printSeparator();

        // 5️⃣ Stream-Based Stats (Optional — for learning)
        manager.calculateStatsWithStreams();
        manager.printSeparator();

        // 6️⃣ Efficient Stream Stats (summaryStatistics)
        manager.calculateStatsEfficiently();
        manager.printSeparator();

        // ✅ Footer
        System.out.println("✅ All array concepts demonstrated: jagged arrays, loops, \n" +
                "   System.arraycopy(), Arrays utilities, and Stream API!");
    }
}