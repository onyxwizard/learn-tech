package phase1.basics.basicproject.lab;

import phase1.basics.basicproject.model.Student;

public class GradeAnalyzer {

    public double average = 0.0;
    String grade = "";
    public void analyzeStudent(Student s){
        var arr = s.grades;
        var sum = 0.0;
        var count = 0.0;
        for (int i=0;i<arr.length;i++){
            sum+=arr[i];
            count = i+1;
        }
        this.average = count==0? 0.0 : sum/count;
        System.out.println("Sum : "+sum + "→ Avg: " + String.format("%.1f", average));
    }
    public String getLetterGrade(double average){
        int gd = (int)average/10;
        return switch(gd){
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            case 5 -> "E";
            default -> {
                if (average < 0) {
                    yield "Invalid"; // Yield is required inside this block
                } else {
                    yield "Fail"; // Yield is required inside this block
                }
            }
        };
    }

    public boolean isPassing(double average){
        return this.average >= 60.00;
    }


    public void generateReport(Student[] students) {
        // Handle empty input
        if (students == null || students.length == 0) {
            System.out.println("📊 No students to analyze.");
            return;
        }

        System.out.println("📊 STUDENT GRADE REPORT");
        System.out.println("=".repeat(40)); // Expression: string repetition
        System.out.println("Processing " + students.length + " students...\n");

        // Process each student
        for (var i = 0; i < students.length; i++) {
            var student = students[i];

            // Skip invalid students (defensive)
            if (student == null || student.grades == null) {
                System.out.println("⚠️  Skipping invalid student at index " + i);
                continue; // branching with continue
            }

            // Analyze student
            analyzeStudent(student); // sets this.average
            var letter = getLetterGrade(this.average);
            var passing = isPassing(this.average);

            // Build grade list string
            var gradesStr = "[";
            for (var j = 0; j < student.grades.length; j++) {
                gradesStr += student.grades[j];
                if (j < student.grades.length - 1) gradesStr += ", ";
            }
            gradesStr += "]";

            // Print student report line
            System.out.println("✅ " + student.name + " (Grades: " + gradesStr + ")");
            System.out.println("   → Avg: " + String.format("%.1f", this.average) + " | Letter: " + letter);
            System.out.println("   → Status: " + (passing ? "Passing" : "Failing") + "\n");
        }
        calculateClassSummary(students);
    }

    public void calculateClassSummary(Student[] students) {
        var totalStudents = students.length;
        var passingCount = 0;
        var highestAvg = Double.NEGATIVE_INFINITY;
        var lowestAvg = Double.POSITIVE_INFINITY;
        var highestName = "";
        var lowestName = "";

        // Loop through all students to compute stats
        for (var i = 0; i < students.length; i++) {
            var student = students[i];
            if (student == null || student.grades == null || student.grades.length == 0) {
                totalStudents--; // don't count invalid
                continue;
            }

            analyzeStudent(student); // recompute avg (or better: store it per student)
            var avg = this.average;

            // Update passing count
            if (isPassing(avg)) {
                passingCount++;
            }

            // Update highest
            if (avg > highestAvg) {
                highestAvg = avg;
                highestName = student.name;
            }

            // Update lowest
            if (avg < lowestAvg) {
                lowestAvg = avg;
                lowestName = student.name;
            }
        }

        // Avoid division by zero
        var passingRate = totalStudents > 0 ? (passingCount * 100.0) / totalStudents : 0.0;

        // Print summary
        System.out.println("📈 CLASS SUMMARY");
        System.out.println("Total Students: " + totalStudents);
        System.out.println("Passing Rate: " + String.format("%.1f", passingRate) + "%");
        if (totalStudents > 0) {
            System.out.println("Highest Avg: " + String.format("%.1f", highestAvg) + " (" + highestName + ")");
            System.out.println("Lowest Avg: " + String.format("%.1f", lowestAvg) + " (" + lowestName + ")");
        }
    }
}
