package phase1.lambdaexpression.lambdaVariables;

import java.util.*;
import java.util.function.*;

/**
 * 🧠 Comprehensive Demo: Lambda Variable Capture & Method References
 * 
 * Covers:
 *   ✅ Local variable capture (effectively final)
 *   ✅ Instance variable capture (mutable, via `this`)
 *   ✅ Static variable capture (global, mutable)
 *   ✅ All 4 method reference types
 * 
 * Rules Summary:
 *   • Local vars: MUST be effectively final
 *   • Instance/static vars: CAN be mutated (captured by reference)
 *   • Method refs: Use when lambda just delegates to one method
 */
public class LambdaCaptureDemo {

    // ─────────────────────────────────────────────────────
    // 🌐 STATIC VARIABLE (global state — use sparingly!)
    // ─────────────────────────────────────────────────────
    private static String APP_MODE = "development";  // mutable static ✅

    // ─────────────────────────────────────────────────────
    // 👤 INSTANCE VARIABLES (stateful capture)
    // ─────────────────────────────────────────────────────
    private final String userId;          // effectively final (final field)
    private String sessionToken;          // mutable instance field ✅
    private int requestCount;             // mutable counter ✅

    public LambdaCaptureDemo(String userId) {
        this.userId = userId;
        this.sessionToken = UUID.randomUUID().toString();
        this.requestCount = 0;
    }

    // ─────────────────────────────────────────────────────
    // 🚀 MAIN DEMO
    // ─────────────────────────────────────────────────────
    public static void main(String[] args) {
        LambdaCaptureDemo demo = new LambdaCaptureDemo("user_123");
        demo.runAllDemos();
    }

    private void runAllDemos() {
        System.out.println("=".repeat(60));
        System.out.println("🔍 JAVA LAMBDA: VARIABLE CAPTURE & METHOD REFERENCES");
        System.out.println("=".repeat(60) + "\n");

        // 1. Local variable capture (effectively final)
        demoLocalCapture();

        // 2. Instance variable capture (mutable)
        demoInstanceCapture();

        // 3. Static variable capture (mutable global)
        demoStaticCapture();

        // 4. Method references (all 4 types)
        demoMethodReferences();
    }

    // ─────────────────────────────────────────────────────
    // 1️⃣ LOCAL VARIABLE CAPTURE
    // Rule: MUST be effectively final (no reassignment after init)
    // ─────────────────────────────────────────────────────
    private void demoLocalCapture() {
        System.out.println("📦 1. LOCAL VARIABLE CAPTURE (effectively final)");
        System.out.println("-".repeat(50));

        // ✅ Valid: effectively final (assigned once)
        String prefix = "[LOG]";  
        String suffix = " | v1.0";
        
        // Lambda captures VALUES of prefix/suffix at creation time
        Consumer<String> logger = msg -> 
            System.out.println(prefix + " " + msg + suffix);

        logger.accept("Application started");    // [LOG] Application started | v1.0
        logger.accept("Processing request");     // [LOG] Processing request | v1.0

        // ❌ INVALID: Uncomment to see compile error!
        // prefix = "[DEBUG]";  // ← ERROR: variable used in lambda must be final or effectively final
        // Consumer<String> badLogger = msg -> System.out.println(prefix + msg);

        System.out.println();
    }

    // ─────────────────────────────────────────────────────
    // 2️⃣ INSTANCE VARIABLE CAPTURE
    // Rule: Captured via `this`; values can CHANGE after lambda creation
    // ─────────────────────────────────────────────────────
    private void demoInstanceCapture() {
        System.out.println("👤 2. INSTANCE VARIABLE CAPTURE (mutable)");
        System.out.println("-".repeat(50));

        // Lambda captures REFERENCE to `this` → sees live updates
        Supplier<String> userInfo = () -> 
            "User: " + this.userId + " | Token: " + this.sessionToken.substring(0, 8) + "...";

        System.out.println("Before update: " + userInfo.get());
        // → User: user_123 | Token: a1b2c3d4...

        // Update instance field → lambda sees change!
        this.sessionToken = "NEW_TOKEN_" + System.currentTimeMillis();
        System.out.println("After update:  " + userInfo.get());
        // → User: user_123 | Token: NEW_TOKE...

        // Counter example: mutable state
        Runnable incrementer = () -> {
            this.requestCount++;
            System.out.println("Request count: " + this.requestCount);
        };

        incrementer.run(); // 1
        incrementer.run(); // 2
        incrementer.run(); // 3

        System.out.println();
    }

    // ─────────────────────────────────────────────────────
    // 3️⃣ STATIC VARIABLE CAPTURE
    // Rule: Captured by reference; global state (use cautiously!)
    // ─────────────────────────────────────────────────────
    private void demoStaticCapture() {
        System.out.println("🌐 3. STATIC VARIABLE CAPTURE (global, mutable)");
        System.out.println("-".repeat(50));

        // Lambda captures static field reference → sees global updates
        Supplier<String> appInfo = () -> "Mode: " + APP_MODE;

        System.out.println("Start: " + appInfo.get());  // Mode: development

        // Change static variable → lambda reflects change
        APP_MODE = "production";
        System.out.println("After: " + appInfo.get());  // Mode: production

        // Real-world use: config flags
        Supplier<Boolean> isDebug = () -> APP_MODE.equals("development");
        System.out.println("Debug mode? " + isDebug.get()); // false

        System.out.println();
    }

    // ─────────────────────────────────────────────────────
    // 4️⃣ METHOD REFERENCES (All 4 Types)
    // When lambda just calls ONE method → use :: for cleaner code
    // ─────────────────────────────────────────────────────
    private void demoMethodReferences() {
        System.out.println("⚡ 4. METHOD REFERENCES (4 Types)");
        System.out.println("-".repeat(50));

        // ─── 4.1 Static Method Reference ───────────────────
        System.out.println("4.1 📦 Static Method: Integer::parseInt");
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println("  \"42\" → " + parser.apply("42"));  // 42

        // ─── 4.2 Parameter Method Reference ────────────────
        System.out.println("4.2 📥 Parameter Method: String::indexOf");
        BiFunction<String, String, Integer> finder = String::indexOf;
        System.out.println("  \"hello\".indexOf(\"l\") → " + finder.apply("hello", "l")); // 2

        // ─── 4.3 Instance Method (on captured object) ──────
        System.out.println("4.3 👤 Instance Method: System.out::println");
        Consumer<String> printer = System.out::println;
        printer.accept("  → Printed via method ref!");  // (prints line)

        // ─── 4.4 Constructor Reference ─────────────────────
        System.out.println("4.4 🏗️ Constructor: ArrayList::new");
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> list = listFactory.get();
        list.add("lambda");
        list.add("capture");
        System.out.println("  New list: " + list);  // [lambda, capture]

        // ─── Bonus: Complex Constructor ────────────────────
        System.out.println("4.5 🏗️+ Constructor with args: HashMap::new (int)");
        IntFunction<Map<String, Object>> mapFactory = HashMap::new;
        Map<String, Object> map = mapFactory.apply(16);
        map.put("key", "value");
        System.out.println("  Sized map: " + map);  // {key=value}
    }

    // ─────────────────────────────────────────────────────
    // 💡 KEY NOTES & BEST PRACTICES
    // ─────────────────────────────────────────────────────
    /*
    📌 LOCAL VARIABLES:
      • Must be "effectively final" (assigned once, never changed)
      • Captured by VALUE (snapshot at lambda creation)
      • Ensures thread safety — no accidental shared mutability

    📌 INSTANCE VARIABLES:
      • Captured via `this` (reference to enclosing object)
      • Can be mutated → lambda sees latest value
      • Useful for stateful callbacks (e.g., counters, tokens)

    📌 STATIC VARIABLES:
      • Global state — use sparingly (breaks encapsulation)
      • Captured by reference → sees all updates
      • Common for config flags, feature toggles

    📌 METHOD REFERENCES:
      • Use when lambda body is SINGLE method call
      • 4 Forms:
          1. Static:           ClassName::staticMethod
          2. Parameter:        Type::instanceMethod       → (x,y) -> x.method(y)
          3. Captured object:  obj::instanceMethod        → x -> obj.method(x)
          4. Constructor:      ClassName::new             → () -> new ClassName()
      • More readable, less error-prone than lambdas

    🚫 ANTI-PATTERNS:
      • Mutating locals used in lambdas → compile error
      • Overusing statics in lambdas → hidden dependencies
      • Creating custom functional interfaces when JDK has equivalents:
            () → void       → Runnable
            (T) → void      → Consumer<T>
            () → R          → Supplier<R>
            (T) → R         → Function<T,R>
            (T,U) → R       → BiFunction<T,U,R>
    */
}
