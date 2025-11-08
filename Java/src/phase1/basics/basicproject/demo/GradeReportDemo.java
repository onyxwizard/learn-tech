package phase1.basics.basicproject.demo;

import phase1.basics.basicproject.lab.GradeAnalyzer;
import phase1.basics.basicproject.model.Student;

public class GradeReportDemo {
    public static void main(String[] args) {
        // 🧪 Test data: array of students (each has a jagged grades array)
        var students = new Student[] {
                new Student("Alice", new int[]{88, 92, 79}),
                new Student("Bob", new int[]{95, 87}),
                new Student("Charlie", new int[]{76, 82, 90, 88})
        };

        // 🚀 Generate full grade report
        var analyzer = new GradeAnalyzer();
        analyzer.generateReport(students);
    }
}