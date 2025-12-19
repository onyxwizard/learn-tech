# Chapter 19: 🛠️ **Erasure of Generic Methods**

## Method-Level Type Parameter Erasure

While class-level generic type parameters are erased to their bounds, method-level type parameters follow similar but distinct rules. Understanding how generic methods are erased is crucial for debugging, performance optimization, and avoiding common pitfalls.

## 🔧 **Method-Level Type Parameter Erasure**

### How Generic Methods are Transformed

```java
import java.util.*;
import java.lang.reflect.*;

public class MethodErasure {
    
    // Generic method with unbounded type parameter
    public static <T> T identity(T input) {
        return input;
    }
    
    // After erasure:
    // public static Object identity(Object input) {
    //     return input;
    // }
    // The compiler inserts: T result = (T) identity(obj);
    
    // Generic method with bounded type parameter
    public static <T extends Number> double sum(List<T> numbers) {
        double total = 0.0;
        for (T num : numbers) {
            total += num.doubleValue();
        }
        return total;
    }
    
    // After erasure:
    // public static double sum(List numbers) {
    //     double total = 0.0;
    //     for (Object obj : numbers) {
    //         Number num = (Number) obj;  // Cast to bound
    //         total += num.doubleValue();
    //     }
    //     return total;
    // }
    
    // Generic method with multiple type parameters
    public static <K, V> Map<V, List<K>> invertMap(Map<K, V> map) {
        Map<V, List<K>> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.computeIfAbsent(entry.getValue(), k -> new ArrayList<>())
                  .add(entry.getKey());
        }
        return result;
    }
    
    // After erasure:
    // public static Map invertMap(Map map) {
    //     Map result = new HashMap();
    //     for (Map.Entry entry : (Set<Map.Entry>)map.entrySet()) {
    //         Object value = entry.getValue();
    //         List keys = (List) result.get(value);
    //         if (keys == null) {
    //             keys = new ArrayList();
    //             result.put(value, keys);
    //         }
    //         keys.add(entry.getKey());
    //     }
    //     return result;
    // }
    
    // Using reflection to examine method erasure
    public static void examineMethodErasure() throws Exception {
        System.out.println("=== Method Erasure Examination ===\n");
        
        // Get Method objects
        Method identityMethod = MethodErasure.class.getMethod("identity", Object.class);
        Method sumMethod = MethodErasure.class.getMethod("sum", List.class);
        Method invertMapMethod = MethodErasure.class.getMethod("invertMap", Map.class);
        
        System.out.println("identity method:");
        System.out.println("  Generic signature: " + identityMethod.toGenericString());
        System.out.println("  Erased signature:  " + identityMethod);
        System.out.println("  Type parameters:   " + 
            Arrays.toString(identityMethod.getTypeParameters()));
        
        System.out.println("\nsum method:");
        System.out.println("  Generic signature: " + sumMethod.toGenericString());
        System.out.println("  Erased signature:  " + sumMethod);
        
        System.out.println("\ninvertMap method:");
        System.out.println("  Generic signature: " + invertMapMethod.toGenericString());
        System.out.println("  Erased signature:  " + invertMapMethod);
        
        // Test runtime behavior
        System.out.println("\n=== Runtime Behavior Test ===");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Call through reflection (bypasses compiler type checking)
        Object rawResult = sumMethod.invoke(null, numbers);
        System.out.println("Sum via reflection: " + rawResult);
        
        // Normal call (compiler inserts casts)
        double typedResult = sum(numbers);
        System.out.println("Sum via normal call: " + typedResult);
        
        // The bytecode for both is essentially the same!
    }
    
    // Generic instance method in a non-generic class
    public class Utility {
        public <T> List<T> filter(List<T> list, Predicate<T> predicate) {
            List<T> result = new ArrayList<>();
            for (T item : list) {
                if (predicate.test(item)) {
                    result.add(item);
                }
            }
            return result;
        }
        
        // After erasure:
        // public List filter(List list, Predicate predicate) {
        //     List result = new ArrayList();
        //     for (Object item : list) {
        //         if (predicate.test(item)) {
        //             result.add(item);
        //         }
        //     }
        //     return result;
        // }
    }
    
    // Generic instance method in a generic class
    class Container<T> {
        private T value;
        
        // Method using class type parameter
        public T getValue() {
            return value;
        }
        
        // Method with its own type parameter
        public <E> List<E> createList(E element, int count) {
            List<E> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(element);
            }
            return list;
        }
        
        // After erasure of Container<String>:
        // class Container {
        //     private Object value;
        //     
        //     public Object getValue() {
        //         return value;
        //     }
        //     
        //     public List createList(Object element, int count) {
        //         List list = new ArrayList();
        //         for (int i = 0; i < count; i++) {
        //             list.add(element);
        //         }
        //         return list;
        //     }
        // }
    }
    
    // Static generic methods cannot use class type parameters
    // public static T staticMethod() { return null; }  // ❌ Compile error
    
    // But static methods can have their own type parameters
    public static <T> Container<T> createContainer(T value) {
        Container<T> container = new Container<>();
        container.value = value;
        return container;
    }
    
    // After erasure:
    // public static Container createContainer(Object value) {
    //     Container container = new Container();
    //     container.value = value;
    //     return container;
    // }
    
    interface Predicate<T> {
        boolean test(T t);
    }
    
    // Varargs and generics
    @SafeVarargs
    public static <T> List<T> combineLists(List<T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    
    // After erasure:
    // public static List combineLists(List[] lists) {
    //     List result = new ArrayList();
    //     for (List list : lists) {
    //         result.addAll(list);
    //     }
    //     return result;
    // }
    // Warning: Possible heap pollution from parameterized vararg type
    
    public static void main(String[] args) throws Exception {
        examineMethodErasure();
    }
}
```

## 🔄 **Differences from Class Erasure**

### Key Distinctions Between Method and Class Erasure

```java
import java.util.*;
import java.lang.reflect.*;

public class ErasureDifferences {
    
    // Difference 1: Scope of type parameters
    
    // Class type parameters are available to all instance members
    class ClassLevel<T> {
        private T field;           // Uses class type parameter
        
        public T getField() {      // Uses class type parameter
            return field;
        }
        
        // Method can't redeclare T (already in scope)
        // public <T> void method(T param) { }  // ❌ Different T!
        
        // But method can have its own type parameters
        public <U> U process(U input) {
            return input;  // U is method type parameter
        }
    }
    
    // After erasure of ClassLevel<String>:
    // class ClassLevel {
    //     private Object field;
    //     
    //     public Object getField() {
    //         return field;
    //     }
    //     
    //     public Object process(Object input) {
    //         return input;
    //     }
    // }
    
    // Difference 2: Bridge methods are handled differently
    
    interface GenericInterface<T> {
        T process(T input);
    }
    
    class StringProcessor implements GenericInterface<String> {
        // This method overrides GenericInterface<String>.process(String)
        @Override
        public String process(String input) {
            return input.toUpperCase();
        }
        
        // Bridge method generated by compiler:
        // public Object process(Object input) {
        //     return process((String) input);
        // }
    }
    
    // For generic methods, bridge methods are only needed when
    // overriding a generic method in a non-generic way
    
    class GenericMethodHolder {
        public <T> T process(T input) {
            return input;
        }
    }
    
    class StringMethodHolder extends GenericMethodHolder {
        // This is NOT an override! It's a new generic method
        // with the same erasure but different type parameters
        @Override
        public <T> T process(T input) {
            return input;  // Actually re-declares T
        }
        
        // No bridge method needed because both have
        // the same erased signature: Object process(Object)
    }
    
    // Difference 3: Static context
    
    class StaticContext<T> {
        private T instanceField;
        
        // Static methods cannot use class type parameters
        // public static T staticMethod() { return null; }  // ❌
        
        // But can have their own type parameters
        public static <S> S staticGeneric(S input) {
            return input;
        }
        
        // Instance methods can mix class and method type parameters
        public <U> List<U> combine(List<U> list, T element) {
            List<U> result = new ArrayList<>(list);
            result.add((U) element);  // Unsafe cast in practice
            return result;
        }
    }
    
    // Difference 4: Type parameter bounds can be different
    
    class BoundsExample {
        // Class with bounded type parameter
        class BoundedClass<T extends Number> {
            T number;
            
            public double getDouble() {
                return number.doubleValue();
            }
        }
        
        // Method with bounded type parameter
        public <T extends Number> double sumNumbers(List<T> numbers) {
            double total = 0.0;
            for (T num : numbers) {
                total += num.doubleValue();
            }
            return total;
        }
        
        // After erasure:
        // class BoundedClass {
        //     Number number;  // T becomes Number
        //     
        //     public double getDouble() {
        //         return number.doubleValue();
        //     }
        // }
        // 
        // Method sumNumbers erases to:
        // public double sumNumbers(List numbers) {
        //     double total = 0.0;
        //     for (Object obj : numbers) {
        //         Number num = (Number) obj;  // Cast to bound
        //         total += num.doubleValue();
        //     }
        //     return total;
        // }
    }
    
    // Difference 5: Overloading possibilities
    
    class OverloadingExample {
        // These methods overload successfully because
        // they have different erasures (List vs Set)
        public <T> void process(List<T> list) {
            System.out.println("Processing list");
        }
        
        public <T> void process(Set<T> set) {
            System.out.println("Processing set");
        }
        
        // These would NOT compile - same erasure (List)
        // public <T> void processStrings(List<String> list) { }
        // public <T> void processNumbers(List<Integer> list) { }
        
        // But these CAN compile with different bounds
        public <T extends CharSequence> void processCharSequence(List<T> list) {
            System.out.println("Processing CharSequence list");
        }
        
        public <T extends Number> void processNumber(List<T> list) {
            System.out.println("Processing Number list");
        }
        // Different erasures: processCharSequence(List) vs processNumber(List)
        // But the bounds are part of the method signature
        
        // Actually, the above still has erasure conflict!
        // Both erase to: void processCharSequence(List) and void processNumber(List)
        // The bounds are not part of the erased signature
        
        // Correct approach: Different method names or type tokens
        public void processStringList(List<String> list) { }
        public void processIntegerList(List<Integer> list) { }
    }
    
    // Using reflection to demonstrate differences
    public static void examineDifferences() throws Exception {
        System.out.println("=== Differences Between Class and Method Erasure ===\n");
        
        // Examine class type parameters
        Class<?> boundedClass = Class.forName("ErasureDifferences$BoundsExample$BoundedClass");
        TypeVariable<?>[] classTypeParams = boundedClass.getTypeParameters();
        System.out.println("Class type parameters:");
        for (TypeVariable<?> param : classTypeParams) {
            System.out.println("  " + param.getName() + 
                " extends " + Arrays.toString(param.getBounds()));
        }
        
        // Examine method type parameters
        Method sumMethod = BoundsExample.class.getMethod("sumNumbers", List.class);
        TypeVariable<?>[] methodTypeParams = sumMethod.getTypeParameters();
        System.out.println("\nMethod type parameters:");
        for (TypeVariable<?> param : methodTypeParams) {
            System.out.println("  " + param.getName() + 
                " extends " + Arrays.toString(param.getBounds()));
        }
        
        // Show that static methods can't use class type parameters
        System.out.println("\nStatic method in generic class:");
        Method staticMethod = StaticContext.class.getMethod("staticGeneric", Object.class);
        System.out.println("  " + staticMethod.toGenericString());
        System.out.println("  Has type parameters: " + 
            (staticMethod.getTypeParameters().length > 0));
        
        // Demonstrate overloading with different structures
        System.out.println("\nOverloading with different generic structures:");
        Method[] methods = OverloadingExample.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("process")) {
                System.out.println("  " + method.toGenericString());
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        examineDifferences();
    }
}
```

## ⚠️ **Overloading and Overriding Implications**

### The Challenges with Method Erasure

```java
import java.util.*;
import java.lang.reflect.*;

public class OverloadingOverriding {
    
    // PROBLEM 1: Overloading with same erasure
    
    public static class OverloadingProblems {
        // These conflict - same erasure (List)
        // public static <T> void process(List<T> list) { }
        // public static <E> void process(List<E> list) { }  // ❌
        
        // These also conflict - same erasure (List)
        // public static void processStrings(List<String> list) { }
        // public static void processNumbers(List<Integer> list) { }  // ❌
        
        // But these work - different erasures (List vs Set)
        public static <T> void process(List<T> list) {
            System.out.println("Processing List");
        }
        
        public static <T> void process(Set<T> set) {
            System.out.println("Processing Set");
        }
        
        // These work too - different number of parameters
        public static <T> void process(List<T> list, T element) {
            System.out.println("Processing List with element");
        }
        
        // Wildcards create different signatures
        public static void processWildcard(List<?> list) {
            System.out.println("Processing wildcard List");
        }
        // But be careful: process(List<T>) and process(List<?>) 
        // have same erasure and can't coexist
        
        // Workaround: Use type tokens
        public static void processWithToken(List<?> list, Class<?> type) {
            System.out.println("Processing List of " + type);
        }
    }
    
    // PROBLEM 2: Overriding with generic methods
    
    public static class OverridingProblems {
        static class Base {
            public <T> T process(T input) {
                System.out.println("Base.process");
                return input;
            }
        }
        
        static class Derived extends Base {
            // This overrides Base.process
            @Override
            public <T> T process(T input) {
                System.out.println("Derived.process");
                return input;
            }
            
            // Actually, this is tricky: The type parameter T
            // in Derived.process is independent of Base.process's T
            // Both erase to Object process(Object)
        }
        
        static class StringDerived extends Base {
            // This does NOT override Base.process!
            // It's a new method with the same name
            public String process(String input) {
                System.out.println("StringDerived.process(String)");
                return input;
            }
            
            // Base still has: Object process(Object)
            // StringDerived has: String process(String)
            // AND inherits: Object process(Object) from Base
            
            // Test this:
            public static void test() {
                StringDerived sd = new StringDerived();
                sd.process("hello");  // Calls StringDerived.process(String)
                sd.process(123);      // Calls Base.process(Object) via inheritance
            }
        }
    }
    
    // PROBLEM 3: Bridge methods in overriding
    
    public static class BridgeMethodOverriding {
        interface Processor<T> {
            T process(T input);
        }
        
        static class StringProcessor implements Processor<String> {
            @Override
            public String process(String input) {
                return input.toUpperCase();
            }
            
            // Bridge method generated:
            // public Object process(Object input) {
            //     return process((String) input);
            // }
        }
        
        static class LengthProcessor extends StringProcessor {
            // This overrides the bridge method!
            @Override
            public String process(String input) {
                return input + " length: " + input.length();
            }
            
            // The bridge method is inherited from StringProcessor
            // It now delegates to this.process(String)
        }
        
        // Using reflection to examine
        public static void examine() throws Exception {
            System.out.println("StringProcessor methods:");
            Method[] methods = StringProcessor.class.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println("  " + method.getName() + 
                    " [Bridge: " + method.isBridge() + "]");
            }
            
            System.out.println("\nLengthProcessor methods:");
            methods = LengthProcessor.class.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println("  " + method.getName() + 
                    " [Bridge: " + method.isBridge() + "]");
            }
        }
    }
    
    // PROBLEM 4: Covariant returns with generics
    
    public static class CovariantReturns {
        static class Base<T> {
            public T create() {
                System.out.println("Base.create");
                return null;
            }
        }
        
        static class Derived extends Base<String> {
            @Override
            public String create() {  // Covariant return - allowed
                System.out.println("Derived.create");
                return "Hello";
            }
            
            // Bridge method generated:
            // public Object create() {
            //     return create();  // Calls Derived.create()
            // }
        }
        
        // More complex case
        static class GenericBase<T> {
            public List<T> getList() {
                return new ArrayList<>();
            }
        }
        
        static class StringListDerived extends GenericBase<String> {
            @Override
            public ArrayList<String> getList() {  // Covariant return
                return new ArrayList<>();
            }
            
            // Bridge: List getList() calls ArrayList getList()
        }
    }
    
    // PROBLEM 5: Multiple inheritance with generic methods
    
    public static class MultipleInheritance {
        interface A {
            <T> T process(T input);
        }
        
        interface B {
            <T> T process(T input);
        }
        
        static class Impl implements A, B {
            // Implements both A.process and B.process
            // with a single method
            @Override
            public <T> T process(T input) {
                return input;
            }
        }
        
        // What if interfaces have different bounds?
        interface C {
            <T extends Number> T process(T input);
        }
        
        interface D {
            <T extends CharSequence> T process(T input);
        }
        
        // Can't implement both - no single method can satisfy
        // both bounds simultaneously
        // static class Impl2 implements C, D { }  // ❌
        
        // Workaround: Use different method names or type tokens
    }
    
    // Using reflection to analyze overloading/overriding
    
    public static void analyzeWithReflection() throws Exception {
        System.out.println("=== Analyzing Overloading and Overriding ===\n");
        
        // Check overloaded methods
        System.out.println("Overloaded methods in OverloadingProblems:");
        Method[] methods = OverloadingProblems.class.getDeclaredMethods();
        Map<String, List<Method>> methodMap = new HashMap<>();
        
        for (Method method : methods) {
            methodMap.computeIfAbsent(method.getName(), k -> new ArrayList<>())
                     .add(method);
        }
        
        for (Map.Entry<String, List<Method>> entry : methodMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("  Method " + entry.getKey() + " is overloaded:");
                for (Method method : entry.getValue()) {
                    System.out.println("    " + method.toGenericString());
                }
            }
        }
        
        // Check overriding
        System.out.println("\nOverriding in StringDerived:");
        Method baseMethod = OverridingProblems.Base.class.getMethod("process", Object.class);
        Method derivedMethod = OverridingProblems.StringDerived.class.getMethod("process", String.class);
        
        System.out.println("  Base method: " + baseMethod);
        System.out.println("  Derived method: " + derivedMethod);
        System.out.println("  Does derived override base? " + 
            Modifier.isPublic(derivedMethod.getModifiers()) && 
            derivedMethod.getName().equals(baseMethod.getName()));
        // Actually, StringDerived.process(String) doesn't override Base.process(Object)
        
        // Check bridge methods
        System.out.println("\nBridge methods in StringProcessor:");
        Method[] spMethods = BridgeMethodOverriding.StringProcessor.class.getDeclaredMethods();
        for (Method method : spMethods) {
            if (method.isBridge()) {
                System.out.println("  Bridge method: " + method);
                System.out.println("    Parameter types: " + 
                    Arrays.toString(method.getParameterTypes()));
                System.out.println("    Return type: " + method.getReturnType());
                
                // Find the method it bridges to
                for (Method m : spMethods) {
                    if (!m.isBridge() && m.getName().equals(method.getName())) {
                        System.out.println("    Bridges to: " + m);
                    }
                }
            }
        }
    }
    
    // Best practices for overloading/overriding with generics
    
    public static class BestPractices {
        // 1. Avoid overloading with same erasure
        //    Use different method names instead
        
        // 2. Document overriding relationships clearly
        
        // 3. Use @Override annotation to catch errors
        
        // 4. Be cautious with bridge methods - they can affect performance
        
        // 5. Test with reflection to understand actual method signatures
        
        // 6. Consider using abstract classes instead of interfaces
        //    when you need complex generic method signatures
        
        // Example: Clear design without overloading conflicts
        public static class CollectionProcessor {
            public void processStrings(List<String> strings) {
                // Process strings
            }
            
            public void processIntegers(List<Integer> integers) {
                // Process integers
            }
            
            public <T> void processAny(List<T> list, Processor<T> processor) {
                // Generic processing
            }
        }
        
        interface Processor<T> {
            void process(T item);
        }
    }
    
    public static void main(String[] args) throws Exception {
        BridgeMethodOverriding.examine();
        analyzeWithReflection();
        
        // Test the problems
        System.out.println("\n=== Testing OverridingProblems ===");
        OverridingProblems.StringDerived.test();
    }
}
```

## 🔍 **Debugging Generic Method Issues**

### Tools and Techniques for Troubleshooting

```java
import java.util.*;
import java.lang.reflect.*;

public class DebuggingGenericMethods {
    
    // Common issues and how to debug them
    
    // ISSUE 1: Type inference failures
    
    public static class TypeInferenceIssues {
        public static <T> T problematicInference(T a, T b) {
            return a;  // Which T is inferred?
        }
        
        public static void test() {
            // This compiles - T is inferred as Number
            Number result = problematicInference(123, 45.67);
            System.out.println("Inferred type: " + result.getClass());
            
            // This also compiles - T is inferred as Serializable & Comparable
            Comparable<?> comp = problematicInference("hello", 123);
            System.out.println("Inferred type: " + comp.getClass());
            
            // Debugging: Use explicit type witness
            String explicit = DebuggingGenericMethods.<String>problematicInference(
                "hello", "world");
            System.out.println("Explicit type: " + explicit);
        }
    }
    
    // ISSUE 2: Unexpected ClassCastException
    
    public static class CastExceptionIssues {
        public static <T> List<T> unsafeCast(List<?> list) {
            // ⚠️ Unsafe cast!
            @SuppressWarnings("unchecked")
            List<T> result = (List<T>) list;
            return result;
        }
        
        public static void test() {
            List<Object> mixed = Arrays.asList("hello", 123, 45.67);
            
            try {
                List<String> strings = unsafeCast(mixed);
                String first = strings.get(0);  // Works
                String second = strings.get(1);  // ClassCastException!
                System.out.println(second.toUpperCase());
            } catch (ClassCastException e) {
                System.out.println("Caught: " + e);
                System.out.println("Cause: Unsafe cast from List<?> to List<String>");
            }
            
            // Debugging: Use checked collections
            List<String> safe = Collections.checkedList(new ArrayList<>(), String.class);
            safe.addAll(Arrays.asList("a", "b", "c"));
            // safe.add(123);  // Throws ClassCastException immediately
        }
    }
    
    // ISSUE 3: Bridge method confusion in stack traces
    
    public static class StackTraceIssues {
        interface Processor<T> {
            T process(T input);
        }
        
        static class StringProcessor implements Processor<String> {
            @Override
            public String process(String input) {
                if (input == null) {
                    throw new IllegalArgumentException("Input cannot be null");
                }
                return input.toUpperCase();
            }
        }
        
        public static void test() {
            Processor processor = new StringProcessor();  // Raw type
            
            try {
                processor.process(null);  // Calls bridge method
            } catch (Exception e) {
                System.out.println("Stack trace with bridge method:");
                e.printStackTrace(System.out);
                
                // The stack trace shows:
                // StringProcessor.process(Object)  <-- Bridge method
                //   StringProcessor.process(String) <-- Actual method
            }
        }
    }
    
    // ISSUE 4: Performance overhead from casts
    
    public static class PerformanceIssues {
        // Generic method with many casts
        public static <T extends Comparable<T>> T findMax(List<T> list) {
            if (list.isEmpty()) throw new NoSuchElementException();
            
            T max = list.get(0);
            for (T item : list) {
                if (item.compareTo(max) > 0) {
                    max = item;
                }
            }
            return max;
        }
        
        // After erasure:
        // public static Comparable findMax(List list) {
        //     if (list.isEmpty()) throw new NoSuchElementException();
        //     
        //     Comparable max = (Comparable) list.get(0);
        //     for (Object item : list) {
        //         Comparable comp = (Comparable) item;
        //         if (comp.compareTo(max) > 0) {
        //             max = comp;
        //         }
        //     }
        //     return max;
        // }
        // Each iteration does: Object -> Comparable cast
        
        public static void benchmark() {
            int size = 1000000;
            List<Integer> numbers = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                numbers.add(i);
            }
            
            long start = System.nanoTime();
            Integer max = findMax(numbers);
            long time = System.nanoTime() - start;
            
            System.out.println("Max: " + max);
            System.out.println("Time: " + time + " ns");
            System.out.println("Note: Each iteration has cast overhead");
            
            // Compare with specialized method
            start = System.nanoTime();
            int maxInt = findMaxInt(numbers);
            long timeInt = System.nanoTime() - start;
            
            System.out.println("\nSpecialized method time: " + timeInt + " ns");
            System.out.println("Speedup: " + (double) time / timeInt + "x");
        }
        
        // Specialized version avoids casts
        public static int findMaxInt(List<Integer> list) {
            if (list.isEmpty()) throw new NoSuchElementException();
            
            int max = list.get(0);
            for (int item : list) {  // Autounboxing, but no casts
                if (item > max) {
                    max = item;
                }
            }
            return max;
        }
    }
    
    // Debugging tools
    
    public static class DebugTools {
        // Tool 1: Print generic signature
        public static void printGenericSignature(Method method) {
            System.out.println("Method: " + method.getName());
            System.out.println("  Generic: " + method.toGenericString());
            System.out.println("  Erased:  " + method);
            System.out.println("  Type params: " + 
                Arrays.toString(method.getTypeParameters()));
            System.out.println("  Is bridge: " + method.isBridge());
            System.out.println("  Is synthetic: " + method.isSynthetic());
        }
        
        // Tool 2: Trace method calls
        public static <T> T traceCall(String methodName, Supplier<T> call) {
            System.out.println("Calling: " + methodName);
            long start = System.nanoTime();
            try {
                T result = call.get();
                long time = System.nanoTime() - start;
                System.out.println("  Result: " + result + " (took " + time + " ns)");
                return result;
            } catch (Exception e) {
                long time = System.nanoTime() - start;
                System.out.println("  Exception: " + e + " (took " + time + " ns)");
                throw e;
            }
        }
        
        // Tool 3: Check type safety
        public static void checkTypeSafety(Object obj, Class<?> expectedType) {
            if (obj != null && !expectedType.isInstance(obj)) {
                throw new ClassCastException(
                    "Expected " + expectedType + ", got " + obj.getClass());
            }
        }
        
        // Tool 4: Count casts in bytecode (simplified)
        public static void estimateCastOverhead(Method method) {
            // This is a simplification - real analysis would require
            // bytecode analysis tools like ASM
            System.out.println("Estimating cast overhead for: " + method.getName());
            
            // Count type parameters and bounds
            int typeParams = method.getTypeParameters().length;
            System.out.println("  Type parameters: " + typeParams);
            
            // Check return type
            if (!method.getReturnType().equals(Object.class)) {
                System.out.println("  Return type requires cast from Object");
            }
            
            // Check parameters
            for (Class<?> paramType : method.getParameterTypes()) {
                if (paramType.equals(Object.class)) {
                    System.out.println("  Parameter requires cast to actual type");
                }
            }
        }
    }
    
    // Using the debugging tools
    
    public static void useDebugTools() throws Exception {
        System.out.println("=== Using Debug Tools ===\n");
        
        // Tool 1: Print generic signature
        Method maxMethod = PerformanceIssues.class.getMethod("findMax", List.class);
        DebugTools.printGenericSignature(maxMethod);
        
        // Tool 2: Trace calls
        System.out.println("\nTracing method calls:");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        DebugTools.traceCall("findMax", () -> PerformanceIssues.findMax(numbers));
        
        // Tool 3: Check type safety
        System.out.println("\nChecking type safety:");
        Object obj = "hello";
        DebugTools.checkTypeSafety(obj, String.class);
        
        try {
            DebugTools.checkTypeSafety(123, String.class);
        } catch (ClassCastException e) {
            System.out.println("Caught expected: " + e.getMessage());
        }
        
        // Tool 4: Estimate cast overhead
        System.out.println("\nEstimating cast overhead:");
        DebugTools.estimateCastOverhead(maxMethod);
    }
    
    // Common patterns for avoiding issues
    
    public static class AvoidancePatterns {
        // Pattern 1: Use type tokens for runtime type checking
        public static <T> List<T> safeCast(List<?> list, Class<T> type) {
            List<T> result = new ArrayList<>();
            for (Object obj : list) {
                if (type.isInstance(obj)) {
                    result.add(type.cast(obj));
                } else {
                    throw new ClassCastException(
                        "Expected " + type + ", got " + obj.getClass());
                }
            }
            return result;
        }
        
        // Pattern 2: Avoid unnecessary generics
        // Sometimes, wildcards or raw types are sufficient
        public static void printAll(List<?> list) {
            for (Object obj : list) {
                System.out.println(obj);
            }
        }
        
        // Pattern 3: Use specialized methods for performance
        public static int sumInts(List<Integer> ints) {
            int sum = 0;
            for (int num : ints) {  // Autounboxing but no casts
                sum += num;
            }
            return sum;
        }
        
        // Pattern 4: Document assumptions
        /**
         * Casts a list to List<T>. Caller must ensure the list
         * only contains elements of type T.
         * 
         * @throws ClassCastException if list contains wrong types
         */
        @SuppressWarnings("unchecked")
        public static <T> List<T> uncheckedCast(List<?> list) {
            return (List<T>) list;
        }
        
        // Pattern 5: Use @SuppressWarnings with justification
        @SuppressWarnings("unchecked")
        public static <T> T[] createArray(Class<T> type, int size) {
            // This is safe because we're creating a new array
            return (T[]) java.lang.reflect.Array.newInstance(type, size);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Debugging Generic Method Issues ===\n");
        
        TypeInferenceIssues.test();
        System.out.println();
        
        CastExceptionIssues.test();
        System.out.println();
        
        StackTraceIssues.test();
        System.out.println();
        
        PerformanceIssues.benchmark();
        System.out.println();
        
        useDebugTools();
    }
}
```

## 📊 **Method Erasure Summary Table**

| Aspect | Before Erasure | After Erasure | Notes |
|--------|----------------|---------------|-------|
| Unbounded type parameter `<T>` | `T` | `Object` | Most common case |
| Bounded type parameter `<T extends Number>` | `T` | `Number` (first bound) | Only first bound remains |
| Return type `<T> T method()` | `T` | `Object` | Cast inserted at call site |
| Parameter type `void method(T param)` | `T` | `Object` | Cast may be needed in method body |
| Varargs `T...` | `T[]` | `Object[]` | Warning: possible heap pollution |
| Multiple type params `<K, V>` | `K, V` | `Object, Object` | Each becomes `Object` |
| Wildcard `<? extends T>` | `? extends T` | `Object` | Wildcard info lost |
| Static generic method | Can have own type params | Same as instance methods | Cannot use class type params |
| Bridge methods | Not in source | Added by compiler | Preserve polymorphism |

## 🎓 **Key Takeaways**

1. **Method type parameters are erased independently** of class type parameters
2. **Static generic methods cannot use class type parameters** but can have their own
3. **Bridge methods are generated** when generic methods are overridden in a type-specific way
4. **Overloading with same erasure is not allowed** - causes compile errors
5. **Covariant returns work with generics** through bridge methods
6. **Debugging requires understanding** of both source and erased signatures
7. **Performance can be affected** by casts inserted during erasure
8. **Use @SuppressWarnings judiciously** and document why it's safe

## 🚀 **Next Steps**

Now that you understand how generic methods are erased, let's explore the full implications of type erasure. In [Chapter 19](#19-effects-of-erasure--bridge-methods), we'll examine **Effects of Erasure & Bridge Methods** in depth - how polymorphism is preserved after erasure, debugging generics in bytecode, and understanding compiler-generated methods.

> 💡 **Practice Exercise**: 
> 1. Write a generic method and use `javap -c -v` to examine the bytecode and find inserted casts
> 2. Create a class hierarchy with generic methods and use reflection to find bridge methods
> 3. Benchmark a generic method vs a specialized version to measure cast overhead
> 4. Write a utility that uses reflection to print the erasure of any method
> 5. Create an example that demonstrates why overloading with same erasure fails