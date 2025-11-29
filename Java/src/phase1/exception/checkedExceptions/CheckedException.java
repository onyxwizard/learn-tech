package phase1.exception.checkedExceptions;

// CheckedExceptionsDemo.java
// Professional documentation & safe demonstration of key checked exceptions.
// Emphasizes: *These are not bugs — they are expected, recoverable signals.*
// Each method returns a description of the failure + recommended handling strategy.

public class CheckedException {

  // 16. ClassNotFoundException
  // Why Checked? → External environment issue: class may be missing at runtime.
  // When Occurs: Dynamic class loading (e.g., plugins, JDBC drivers).
  // Handling Strategy: Log, fallback to default impl, or fail fast with user
  // message.
  static String demo16_ClassNotFoundException() {
    String className = "com.example.MissingPlugin";
    // Class.forName(className); ← throws ClassNotFoundException if not in classpath
    return """
        ClassNotFoundException: Class '%s' not found in classpath.
        ✅ Handle by:
        - Logging + falling back to default implementation
        - Alerting user (e.g., 'Plugin not installed')
        - Avoid dynamic loading if possible (prefer DI)""".formatted(className);
  }

  // 17. CloneNotSupportedException
  // Why Checked? → Contract violation: object doesn't support cloning.
  // When Occurs: Calling clone() on object whose class didn't implement
  // Cloneable.
  // Handling Strategy: Prefer copy constructors or builders; avoid clone().
  static String demo17_CloneNotSupportedException() {
    // obj.clone(); ← throws if class doesn't implement Cloneable
    return """
        CloneNotSupportedException: Object does not support cloning.
        ⚠️ Legacy API — avoid clone().
        ✅ Modern alternative:
        - Use copy constructor: new User(original)
        - Use Builder pattern or serialization-free deep copy utilities""";
  }

  // 18. IllegalAccessException
  // Why Checked? → Reflective access denied (security or visibility).
  // When Occurs: Accessing private/protected members via reflection without
  // setAccessible(true).
  // Handling Strategy: Adjust access (if safe), or redesign to avoid reflection.
  static String demo18_IllegalAccessException() {
    // field.get(obj); ← throws if field is private and not made accessible
    return """
        IllegalAccessException: Reflection attempted on inaccessible member.
        ✅ Handle by:
        - Calling field.setAccessible(true) *only if safe* (security risk!)
        - Prefer direct access or public APIs
        - Use java.lang.invoke.MethodHandles for safer reflection""";
  }

  // 19. InstantiationException
  // Why Checked? → External: class is abstract/interface — cannot instantiate.
  // When Occurs: clazz.newInstance() on interface/abstract class or no no-arg
  // constructor.
  // Handling Strategy: Validate before instantiation; use factories.
  static String demo19_InstantiationException() {
    // Class.forName("java.util.List").newInstance(); ← throws: List is interface
    return """
        InstantiationException: Cannot instantiate abstract/interface/no-arg-ctor class.
        ✅ Prevent by:
        - Checking Modifier.isInterface(clazz.getModifiers())
        - Using factory methods (e.g., List.of(), new ArrayList<>())
        - Prefer dependency injection over newInstance()""";
  }

  // 20. InterruptedException
  // Why Checked? → Critical cooperative cancellation signal — must be respected.
  // When Occurs: Thread interrupted during blocking op (sleep(), wait(), I/O).
  // Handling Strategy: Restore interrupt status + propagate or clean shutdown.
  static String demo20_InterruptedException() {
    // Thread.sleep(1000); ← throws if thread.interrupt() was called
    return """
        InterruptedException: Thread was interrupted (e.g., cancellation request).
        🚨 Never ignore! Best practices:
        - Restore interrupt: Thread.currentThread().interrupt();
        - Propagate as checked, or
        - Initiate graceful shutdown (close resources, exit loop)
        Example:
          try { Thread.sleep(1000); }
          catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return; // or throw new RuntimeException(e);
          }""";
  }

  // 21. NoSuchFieldException
  // Why Checked? → Reflective programming: field name may change (e.g.,
  // obfuscation).
  // When Occurs: getDeclaredField("unknownField").
  // Handling Strategy: Use default value, log warning, or validate schema first.
  static String demo21_NoSuchFieldException() {
    String fieldName = "legacyId";
    // clazz.getDeclaredField(fieldName); ← throws if field doesn't exist
    return """
        NoSuchFieldException: Field '%s' not found in class.
        ✅ Handle by:
        - Providing fallback (e.g., use 'id' if 'legacyId' missing)
        - Validating against known schema (e.g., JSON mapping config)
        - Avoid reflection — use annotations or code generation (e.g., Lombok)""".formatted(fieldName);
  }

  // 22. NoSuchMethodException
  // Why Checked? → Reflective programming: method signature mismatch.
  // When Occurs: getMethod("run", int.class) when only run() exists.
  // Handling Strategy: Log + adapt (e.g., try no-arg version), or fail fast with
  // context.
  static String demo22_NoSuchMethodException() {
    String methodName = "process";
    Class<?>[] args = { String.class };
    // clazz.getMethod(methodName, args); ← throws if no matching method
    return """
        NoSuchMethodException: No method '%s(%s)' found.
        ✅ Handle by:
        - Trying alternative signatures (e.g., no-arg version)
        - Using method lookup with getMethods() + filtering
        - Prefer compile-time safety: interfaces, lambdas, or annotation processors""".formatted(
        methodName,
        String.join(", ", java.util.Arrays.stream(args).map(Class::getSimpleName).toArray(String[]::new)));
  }

  // ✅ Main: Safe execution — no exceptions thrown, only insights printed.
  public static void main(String[] args) {
    System.out.println("✅ Checked Exceptions — Expected, Recoverable Signals");
    System.out.println("💡 Rule: *Catch when you can respond meaningfully.* Otherwise, propagate + cleanup.");
    System.out.println("📦 These arise from reflection, class loading, threading — not bugs!\n");

    System.out.println("16. " + demo16_ClassNotFoundException());
    System.out.println();
    System.out.println("17. " + demo17_CloneNotSupportedException());
    System.out.println();
    System.out.println("18. " + demo18_IllegalAccessException());
    System.out.println();
    System.out.println("19. " + demo19_InstantiationException());
    System.out.println();
    System.out.println("20. " + demo20_InterruptedException());
    System.out.println();
    System.out.println("21. " + demo21_NoSuchFieldException());
    System.out.println();
    System.out.println("22. " + demo22_NoSuchMethodException());

    System.out.println("\n📌 Pro Tip: In modern Java (17+), prefer:\n" +
        "   • Records, Records, Records (for immutable data)\n" +
        "   • Optional<T> over null\n" +
        "   • try-with-resources over manual finally\n" +
        "   • Structured Concurrency (JEP 428/437) over raw threads\n" +
        "— to reduce reliance on checked exceptions.");
  }
}