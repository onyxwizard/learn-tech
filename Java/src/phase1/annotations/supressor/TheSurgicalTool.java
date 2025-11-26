package phase1.annotations.supressor;

import java.util.*;


public class TheSurgicalTool {
    public static void main(String[] args) {
        List<String> safeList = createLegacyList();
        System.out.println(safeList); // [a, b]
    }

    // Safe: legacy API returns raw List; we guarantee String contents
    @SuppressWarnings("unchecked")
    private static List<String> createLegacyList() {
        // Simulate legacy method returning raw List
        List raw = Arrays.asList("a", "b");
        return raw; // unchecked cast
    }
}
