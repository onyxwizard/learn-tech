package phase1.optional.basic;
import java.util.Optional;

class Student {
    private final String name;
    private final int age;
    private final int section;

    public Student(String name, int age, int section) {
        // ✅ Enforce invariants at construction — fail fast!
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (age < 1) {
            throw new IllegalArgumentException("Age must be at least 1");
        }
        if (section < 1) {
            throw new IllegalArgumentException("Section must be at least 1");
        }
        this.name = name;
        this.age = age;
        this.section = section;
    }

    String getName() { return name; }
    int getAge() { return age; }
    int getSection() { return section; }

    @Override
    public String toString() {
        return "Student[name='%s', age=%d, section=%d]".formatted(name, age, section);
    }
}

class RecordCheck {
    private final Optional<Student> student;

    private RecordCheck(Optional<Student> student) {
        this.student = student;
    }

    // Factory: safe wrapping of nullable student
    public static RecordCheck fromNullable(Student student) {
        return new RecordCheck(Optional.ofNullable(student));
    }

    // Business method: "Do we have a valid student?"
    public boolean hasStudent() {
        return student.isPresent();  // or use .isEmpty() for Java 11+
    }

    // Safe access
    public Optional<Student> getStudent() {
        return student;
    }
}

public class Basic {
  public static void main(String[] args) {
        try {
            // ✅ Student validates itself
            Student stu = new Student("AK", 1, 1);  // valid
            RecordCheck check = RecordCheck.fromNullable(stu);
            System.out.println("✅ " + check.getStudent().orElseThrow());

            // Try invalid
            //Student invalid = new Student("", 0, -1);  // throws!
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation failed: " + e.getMessage());
        } finally {
            System.out.println("🔚 End of Program!");
        }
    }
}
