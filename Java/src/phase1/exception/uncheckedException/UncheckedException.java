package phase1.exception.uncheckedException;

// UncheckedExceptionsDemo.java
// Demonstrates all 15 common unchecked (RuntimeException) exceptions.
// Each method shows the *cause*, *why it's unchecked*, and *how to prevent it*.
// All exceptions are *illustrated* but not thrown — returns a safe description instead.

public class UncheckedException {

    // 1. ArithmeticException
    // Why unchecked? → Programming bug (e.g., divide by zero); prevent with validation.
    // Occurs when: performing illegal arithmetic (e.g., 5 / 0).
    // Prevention: Check divisor before division.
    static String demo1_ArithmeticException() {
        int a = 10, b = 0;
        // throw new ArithmeticException("/ by zero"); ← would happen here
        return "ArithmeticException: Attempted division by zero (b = " + b + "). Prevent with: if (b != 0)";
    }

    // 2. ArrayIndexOutOfBoundsException
    // Why unchecked? → Bug: accessing array beyond its bounds.
    // Occurs when: index < 0 or index >= array.length.
    // Prevention: Validate index before access.
    static String demo2_ArrayIndexOutOfBoundsException() {
        int[] arr = {1, 2, 3};
        int index = 5;
        // arr[index]; ← would throw here
        return "ArrayIndexOutOfBoundsException: Tried arr[" + index + "] on array of length " + arr.length + ". Validate index first.";
    }

    // 3. ArrayStoreException
    // Why unchecked? → Bug: storing wrong type in array (type safety violation).
    // Occurs when: Object[] holds Strings, but you assign Integer.
    // Prevention: Use generic collections (e.g., List<String>) or validate types.
    static String demo3_ArrayStoreException() {
        Object[] arr = new String[1];
        // arr[0] = 123; ← would throw: Integer into String[]
        return "ArrayStoreException: Tried to store Integer in String[] via Object[] reference. Prefer List<String>.";
    }

    // 4. ClassCastException
    // Why unchecked? → Bug: invalid downcast without checking type.
    // Occurs when: (String) obj where obj is not a String.
    // Prevention: Use instanceof before casting.
    static String demo4_ClassCastException() {
        Object obj = Integer.valueOf(42);
        // String s = (String) obj; ← would throw
        return "ClassCastException: Casting Integer to String. Prevent with: if (obj instanceof String) ...";
    }

    // 5. IllegalArgumentException
    // Why unchecked? → Caller violated method contract (e.g., negative age).
    // Occurs when: method receives invalid argument (by design, not environment).
    // Prevention: Validate inputs at method entry.
    static String demo5_IllegalArgumentException() {
        int age = -5;
        // if (age < 0) throw new IllegalArgumentException("Age < 0"); ← defensive check
        return "IllegalArgumentException: Invalid argument (e.g., age = " + age + "). Validate early in method.";
    }

    // 6. IllegalMonitorStateException
    // Why unchecked? → Bug: calling wait()/notify() without holding object's monitor.
    // Occurs when: obj.wait() outside synchronized(obj) block.
    // Prevention: Always call wait/notify inside synchronized block.
    static String demo6_IllegalMonitorStateException() {
        // obj.wait(); ← would throw if not in synchronized(obj)
        return "IllegalMonitorStateException: Calling wait() without owning the lock. Wrap in synchronized(obj).";
    }

    // 7. IllegalStateException
    // Why unchecked? → Bug: object used in invalid state (e.g., exhausted iterator).
    // Occurs when: next() called after hasNext() returned false.
    // Prevention: Check state before action (e.g., hasNext()).
    static String demo7_IllegalStateException() {
        // iterator.next() after no more elements
        return "IllegalStateException: Operation on object in invalid state (e.g., next() after end). Check state first.";
    }

    // 8. IllegalThreadStateException
    // Why unchecked? → Bug: thread operation incompatible with current state.
    // Occurs when: calling start() twice on same thread.
    // Prevention: Track thread state or use Executors.
    static String demo8_IllegalThreadStateException() {
        // thread.start(); thread.start(); ← second call throws
        return "IllegalThreadStateException: Invalid thread operation (e.g., start() twice). Use Thread.getState() to check.";
    }

    // 9. IndexOutOfBoundsException (superclass)
    // Why unchecked? → Generalization of array/string index errors — all are bugs.
    // Occurs when: any index-based access is out of range.
    // Prevention: Same as #2 and #14 — validate indices.
    static String demo9_IndexOutOfBoundsException() {
        return "IndexOutOfBoundsException: Abstract parent of ArrayIndexOutOfBounds & StringIndexOutOfBounds. Always validate indices.";
    }

    // 10. NegativeArraySizeException
    // Why unchecked? → Bug: creating array with negative size.
    // Occurs when: new int[-1].
    // Prevention: Validate size before array creation.
    static String demo10_NegativeArraySizeException() {
        int size = -3;
        // new int[size]; ← would throw
        return "NegativeArraySizeException: Attempted array creation with negative size (" + size + "). Validate size >= 0.";
    }

    // 11. NullPointerException (The Classic!)
    // Why unchecked? → Bug: dereferencing null reference.
    // Occurs when: calling method/field on null object.
    // Prevention: Check for null, use Optional, or design for non-null (e.g., @NotNull).
    static String demo11_NullPointerException() {
        String s = null;
        // s.length(); ← would throw
        return "NullPointerException: Called method on null reference. Prevent with: if (s != null) or Objects.requireNonNull(s).";
    }

    // 12. NumberFormatException
    // Why unchecked? → Bug: parsing malformed string (client-side validation missing).
    // Occurs when: Integer.parseInt("abc").
    // Prevention: Validate input format before parsing (e.g., regex), or use try-catch at boundary.
    static String demo12_NumberFormatException() {
        String input = "abc";
        // Integer.parseInt(input); ← would throw
        return "NumberFormatException: Parsing invalid number string: '" + input + "'. Validate format or catch at input boundary.";
    }

    // 13. SecurityException
    // Why unchecked? → Bug or policy violation: code attempted restricted action.
    // Occurs when: applet tries file I/O without permission.
    // Prevention: Respect security policy; avoid privileged ops in untrusted code.
    static String demo13_SecurityException() {
        // System.setSecurityManager(...); then restricted op
        return "SecurityException: Attempted operation denied by SecurityManager (e.g., file access in sandbox). Respect security policy.";
    }

    // 14. StringIndexOutOfBoundsException
    // Why unchecked? → Bug: accessing char beyond string length.
    // Occurs when: str.charAt(100) on short string.
    // Prevention: Use str.length() to validate index.
    static String demo14_StringIndexOutOfBoundsException() {
        String str = "Hi";
        int idx = 5;
        // str.charAt(idx); ← would throw
        return "StringIndexOutOfBoundsException: Tried str.charAt(" + idx + ") on string of length " + str.length() + ". Check bounds.";
    }

    // 15. UnsupportedOperationException
    // Why unchecked? → Bug/design flaw: calling unsupported optional operation.
    // Occurs when: calling add() on immutable List (e.g., List.of(...)).
    // Prevention: Know your collection’s capabilities; use modifiable collections when needed.
    static String demo15_UnsupportedOperationException() {
        // List<String> list = List.of("a"); list.add("b"); ← throws
        return "UnsupportedOperationException: Called optional method (e.g., add()) on immutable collection. Use new ArrayList<>(...) for mutability.";
    }

    // ✅ Main: Runs all demos and prints insights — no exceptions thrown!
    public static void main(String[] args) {
        System.out.println("🔍 Unchecked Exceptions (RuntimeExceptions) — All Are *Programming Bugs*");
        System.out.println("💡 Rule: Fix the code — don’t catch and ignore!\n");

        System.out.println("1. " + demo1_ArithmeticException());
        System.out.println("2. " + demo2_ArrayIndexOutOfBoundsException());
        System.out.println("3. " + demo3_ArrayStoreException());
        System.out.println("4. " + demo4_ClassCastException());
        System.out.println("5. " + demo5_IllegalArgumentException());
        System.out.println("6. " + demo6_IllegalMonitorStateException());
        System.out.println("7. " + demo7_IllegalStateException());
        System.out.println("8. " + demo8_IllegalThreadStateException());
        System.out.println("9. " + demo9_IndexOutOfBoundsException());
        System.out.println("10. " + demo10_NegativeArraySizeException());
        System.out.println("11. " + demo11_NullPointerException());
        System.out.println("12. " + demo12_NumberFormatException());
        System.out.println("13. " + demo13_SecurityException());
        System.out.println("14. " + demo14_StringIndexOutOfBoundsException());
        System.out.println("15. " + demo15_UnsupportedOperationException());

        System.out.println("\n✅ Key Takeaway: Unchecked exceptions = bugs. Write defensive code, validate inputs, and use tools (e.g., @NonNull, Optional) to prevent them.");
    }
}