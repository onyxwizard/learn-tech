package phase1.basics.conditional;


public class BranchingLab {

    // ───────────────────────────────
    // 🔚 UNLABELED BREAK
    // ───────────────────────────────
    public void demonstrateUnlabeledBreak() {
        System.out.println("1️⃣ UNLABELED BREAK (exit innermost loop)");
        int[] numbers = {32, 87, 3, 589, 12, 1076};
        int searchFor = 12;
        int index = -1;

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == searchFor) {
                index = i;
                break; // exits the for-loop
            }
        }

        if (index != -1) {
            System.out.println("Found " + searchFor + " at index " + index);
        } else {
            System.out.println(searchFor + " not found");
        }
        System.out.println();
    }

    // ───────────────────────────────
    // 🔚 LABELED BREAK
    // ───────────────────────────────
    public void demonstrateLabeledBreak() {
        System.out.println("2️⃣ LABELED BREAK (exit outer loop)");
        int[][] matrix = {
                {32, 87, 3},
                {589, 12, 1076},
                {2000, 8, 622}
        };
        int searchFor = 12;
        int row = -1, col = -1;

        search:
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == searchFor) {
                    row = i;
                    col = j;
                    break search; // exits the labeled 'search' loop
                }
            }
        }

        if (row != -1) {
            System.out.println("Found " + searchFor + " at [" + row + "][" + col + "]");
        } else {
            System.out.println(searchFor + " not found");
        }
        System.out.println();
    }

    // ───────────────────────────────
    // 🔁 UNLABELED CONTINUE
    // ───────────────────────────────
    public void demonstrateUnlabeledContinue() {
        System.out.println("3️⃣ UNLABELED CONTINUE (skip rest of current iteration)");
        String text = "peter piper picked a peck of pickled peppers";
        int pCount = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != 'p') {
                continue; // skip to next iteration
            }
            pCount++;
        }

        System.out.println("Number of 'p's: " + pCount);
        System.out.println();
    }

    // ───────────────────────────────
    // 🔁 LABELED CONTINUE
    // ───────────────────────────────
    public void demonstrateLabeledContinue() {
        System.out.println("4️⃣ LABELED CONTINUE (skip to outer loop iteration)");
        String searchIn = "Look for a substring in me";
        String target = "sub";
        boolean found = false;

        int max = searchIn.length() - target.length();
        test:
        for (int i = 0; i <= max; i++) {
            int j = i;
            for (int k = 0; k < target.length(); k++) {
                if (searchIn.charAt(j++) != target.charAt(k)) {
                    continue test; // skip to next 'i' in outer loop
                }
            }
            found = true;
            break test;
        }

        System.out.println("Substring '" + target + "' found? " + found);
        System.out.println();
    }

    // ───────────────────────────────
    // 🔙 RETURN STATEMENT
    // ───────────────────────────────
    public void demonstrateReturn() {
        System.out.println("5️⃣ RETURN STATEMENT");
        System.out.println("Square of 5: " + square(5));
        System.out.println("Is 7 even? " + isEven(7));
        printMessage(); // void return
        System.out.println();
    }

    private int square(int x) {
        return x * x; // returns a value
    }

    private boolean isEven(int x) {
        return x % 2 == 0; // returns a boolean
    }

    private void printMessage() {
        System.out.println("This method returns void");
        return; // optional for void methods
    }

    // ───────────────────────────────
    // 🔄 YIELD STATEMENT (Java 14+)
    // ───────────────────────────────
    public void demonstrateYield() {
        System.out.println("6️⃣ YIELD STATEMENT (in switch expressions)");

        Day today = Day.WEDNESDAY;
        int workDaysLeft = calculateWorkDays(today);
        System.out.println("Work days left after " + today + ": " + workDaysLeft);

        // Compare with arrow form
        int weekendValue = switch (today) {
            case SATURDAY, SUNDAY -> 0;
            default -> 1;
        };
        System.out.println("Weekend value for " + today + ": " + weekendValue);
        System.out.println();
    }

    private int calculateWorkDays(Day day) {
        return switch (day) {
            case SATURDAY, SUNDAY -> 0;
            default -> {
                // Complex logic block
                int remaining = 5 - day.ordinal();
                yield remaining; // yields value from switch expression
            }
        };
    }

    // Enum for yield demo
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    // ───────────────────────────────
    // 🧪 RUN ALL DEMOS
    // ───────────────────────────────
    public void runAll() {
        System.out.println("🔀 BRANCHING STATEMENTS LAB");
        System.out.println("=".repeat(45));
        System.out.println();

        demonstrateUnlabeledBreak();
        demonstrateLabeledBreak();
        demonstrateUnlabeledContinue();
        demonstrateLabeledContinue();
        demonstrateReturn();
        demonstrateYield();

        System.out.println("✅ All branching statements demonstrated!");
    }
}