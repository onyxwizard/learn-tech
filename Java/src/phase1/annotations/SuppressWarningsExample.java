package phase1.annotations;

import java.util.*;
import java.time.LocalDate;

/**
 * Demonstrates safe, documented usage of all standard @SuppressWarnings values.
 * Each suppression includes:
 *   - Why the warning occurs
 *   - Why it's safe to suppress
 *   - Best practice alternative (when possible)
 */
public class SuppressWarningsExample {

    public static void main(String[] args) {
        // 1. Unchecked cast (most common)
        List<String> strings = createLegacyList();
        System.out.println("Unchecked: " + strings); // [a, b]

        // 2. Deprecation usage
        legacyApiCall(); // "Legacy API called"

        // 3. Unused parameter (common in overrides/lambdas)
        processItems(Arrays.asList("x", "y"), item -> System.out.println("Processed: " + item));

        // 4. Rawtypes (older pre-generics APIs)
        List rawList = getRawList();
        @SuppressWarnings("rawtypes")
        List<String> safeRaw = rawList;
        System.out.println("Rawtypes: " + safeRaw); // [raw1, raw2]

        // 5. Preview feature (Java 21+ pattern matching)
        String result = previewFeatureExample("hi");
        System.out.println("Preview: " + result); // test_preview

        // 6. Varargs (heap pollution risk - use cautiously!)
        Set<Integer> numbers = safeUnion(1, 2, 3);
        System.out.println("Varargs: " + numbers); // [1, 2, 3]

        // 7. Fallthrough (switch case without break - intentional)
        String dayType = getDayType(LocalDate.now().getDayOfWeek());
        System.out.println("Fallthrough: " + dayType); // e.g., "Weekend"

        // 8. Try (resource not referenced - valid in Java 9+)
        tryWithSilentResource();

        // 9. Finally (control flow in finally block - rare but valid)
        boolean completed = finallyControlFlow();
        System.out.println("Finally: " + completed); // true

        // 10. Synchronization (synchronized on non-final field - safe if field is effectively final)
        Counter counter = new Counter();
        counter.incrementSafe();
        System.out.println("Sync: " + counter.value); // 1
    }

    // ================================================
    // 1. "unchecked" - Unsafe generic casts
    // ================================================
    @SuppressWarnings("unchecked")
    // Safe: We control the source - raw list contains only Strings
    // Alternative: Use Collections.checkedList() for runtime safety
    private static List<String> createLegacyList() {
        List raw = Arrays.asList("a", "b");
        return raw;
    }

    // ================================================
    // 2. "deprecation" - Using deprecated APIs intentionally
    // ================================================
    @SuppressWarnings("deprecation")
    // Safe: We're in a migration phase; legacy system still requires this
    // Alternative: Replace with new API when migration complete
    private static void legacyApiCall() {
        Date now = new Date(); // Deprecated since Java 1.1
        System.out.println("Legacy API called: " + now);
    }

    // ================================================
    // 3. "unused" - Unused parameters (common in overrides)
    // ================================================
    // Note: Often not needed in modern IDEs, but useful for build tools
    private static void processItems(List<String> items, java.util.function.Consumer<String> processor) {
        items.forEach(processor);
    }

    // Simulated override where parameter is unused
    static class EventHandler {
        @SuppressWarnings("unused")
        // Safe: Required by interface contract; no action needed for this event
        public void onEvent(String eventId, Object payload) {
            // Intentionally no-op for certain events
        }
    }

    // ================================================
    // 4. "rawtypes" - Raw type usage (pre-generics APIs)
    // ================================================
    private static List getRawList() {
        // Simulating legacy API that returns raw List
        return Arrays.asList("raw1", "raw2");
    }

    // ================================================
    // 5. "preview" - Using preview language features
    // ================================================
    @SuppressWarnings("preview")
    // Safe: We're testing Java 21 preview features in development
    // Alternative: Wait for standardization or use feature flags
    private static String previewFeatureExample(Object input) {
        // Java 21: Pattern matching for switch (preview in 20, standard in 21)
        return switch (input) {
            case null -> "null_input";
            case String s -> s + "_preview";
            default -> "unknown";
        };
    }

    // ================================================
    // 6. "varargs" - Generic varargs methods (heap pollution risk)
    // ================================================
    @SafeVarargs
    @SuppressWarnings("varargs")
    // Safe: Method is final and doesn't store the varargs array
    // Alternative: Use Collections.addAll() or List.of() where possible
    private static <T> Set<T> safeUnion(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    // ================================================
    // 7. "fallthrough" - Intentional switch case fallthrough
    // ================================================
    @SuppressWarnings("fallthrough")
    // Safe: Intentional grouping of weekend days
    private static String getDayType(java.time.DayOfWeek day) {
        return switch (day) {
            case SATURDAY, SUNDAY -> "Weekend";
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
        };
    }

    // ================================================
    // 8. "try" - Try-with-resources with unused resource variable
    // ================================================
    @SuppressWarnings("try")
    // Safe: Resource is automatically closed; we don't need to reference it
    // Common in Java 9+ where resource can be a reference
    private static void tryWithSilentResource() {
        String resource = "file.txt";
        try (Scanner scanner = new Scanner(resource)) {
            // Process without using scanner variable directly
            if (scanner.hasNext()) {
                System.out.println("Has content");
            }
        }
    }

    // ================================================
    // 9. "finally" - Control flow statements in finally blocks
    // ================================================
    @SuppressWarnings("finally")
    // Safe: Finally block intentionally returns to ensure cleanup
    // Alternative: Extract cleanup to separate method
    private static boolean finallyControlFlow() {
        try {
            return true;
        } finally {
            // Always execute cleanup
            System.out.println("Cleanup completed");
            // return true; // This would suppress try's return - dangerous!
        }
    }

    // ================================================
    // 10. "synchronization" - Synchronized on non-final field
    // ================================================
    static class Counter {
        int value = 0;

        @SuppressWarnings("synchronization")
        // Safe: Field is effectively final (never reassigned after construction)
        // Alternative: Use final field or AtomicInteger
        public void incrementSafe() {
            synchronized (this) {
                value++;
            }
        }
    }

    // ================================================
    // BONUS: Multiple warnings in one annotation
    // ================================================
    @SuppressWarnings({"unchecked", "rawtypes"})
    // Safe: Legacy API bridge with controlled input
    private static Map<String, List> legacyMapBridge() {
        Map rawMap = new HashMap();
        rawMap.put("strings", Arrays.asList("a", "b"));
        rawMap.put("numbers", Arrays.asList(1, 2));
        return rawMap;
    }
}