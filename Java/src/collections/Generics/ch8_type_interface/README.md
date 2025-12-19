# Chapter 8: 🕵️ **Type Inference**

## Letting the Compiler Do the Work

Type inference is Java's ability to automatically deduce the type arguments for generic methods and generic class instantiations. This feature reduces verbosity and makes code cleaner while maintaining type safety.

## 🧠 **How the Compiler Infers Types**

### Basic Type Inference

```java
import java.util.*;

public class BasicInference {
    public static void main(String[] args) {
        // Before Java 7 - explicit type arguments
        List<String> list1 = new ArrayList<String>();
        Map<String, List<Integer>> map1 = new HashMap<String, List<Integer>>();
        
        // Java 7+ - diamond operator <>
        List<String> list2 = new ArrayList<>();  // Compiler infers String
        Map<String, List<Integer>> map2 = new HashMap<>();  // Compiler infers types
        
        // The compiler looks at the left side (target type) to infer the right side
    }
    
    // Generic method type inference
    public static <T> List<T> emptyList() {
        return new ArrayList<T>();
    }
    
    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<K, V>();
    }
}
```

## 💎 **The Diamond Operator (`<>`)**

### Evolution of Type Inference

```java
public class DiamondOperator {
    
    // Pre-Java 5: Raw types (no generics)
    public void preJava5() {
        List list = new ArrayList();  // Raw type
        list.add("String");
        list.add(123);  // Mixed types allowed
        String s = (String) list.get(0);  // Cast required
    }
    
    // Java 5-6: Explicit type parameters
    public void java5to6() {
        List<String> list = new ArrayList<String>();  // Redundant type arguments
        Map<String, List<Integer>> map = 
            new HashMap<String, List<Integer>>();  // Very verbose
    }
    
    // Java 7+: Diamond operator
    public void java7plus() {
        List<String> list = new ArrayList<>();  // Clean and concise
        Map<String, List<Integer>> map = new HashMap<>();  // Type inference
        
        // Nested generics with diamond
        List<List<Map<String, Integer>>> complex = new ArrayList<>();
    }
    
    // Anonymous inner classes with diamond (Java 9+)
    public void anonymousClass() {
        // Java 8 and earlier: required explicit type
        // Comparator<String> comp = new Comparator<String>() { ... };
        
        // Java 9+: diamond works with anonymous classes
        Comparator<String> comp = new Comparator<>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        };
    }
}
```

### Limitations of the Diamond Operator

```java
public class DiamondLimitations {
    
    // 1. Cannot use with anonymous class in Java 8 and earlier
    public void limitation1() {
        // Java 8: ❌ Doesn't compile
        // List<String> list = new ArrayList<>() {
        //     @Override
        //     public boolean add(String s) {
        //         System.out.println("Adding: " + s);
        //         return super.add(s);
        //     }
        // };
        
        // Java 9+: ✅ Works fine
        List<String> list = new ArrayList<>() {
            @Override
            public boolean add(String s) {
                System.out.println("Adding: " + s);
                return super.add(s);
            }
        };
    }
    
    // 2. Cannot infer from constructor arguments alone
    public void limitation2() {
        // ❌ This doesn't work - cannot infer from parameters
        // List<String> list = new ArrayList<>("A", "B", "C");
        
        // ✅ Use Arrays.asList or List.of instead
        List<String> list1 = new ArrayList<>(Arrays.asList("A", "B", "C"));
        List<String> list2 = new ArrayList<>(List.of("A", "B", "C"));
    }
    
    // 3. Cannot use in variable declaration without initialization
    public void limitation3() {
        // ❌ Cannot use diamond without initialization
        // List<> list;  // Compile error
        
        // ✅ Must provide full type or initialize
        List<String> list1;  // Declaration only
        List<String> list2 = new ArrayList<>();  // With initialization
    }
    
    // 4. Cannot use with var (Java 10+)
    public void limitation4() {
        // ❌ Diamond with var is redundant
        // var list = new ArrayList<>();  // Inferred as ArrayList<Object>
        
        // ✅ Better: specify type or use constructor arguments
        var list1 = new ArrayList<String>();  // ArrayList<String>
        var list2 = new ArrayList<>(List.of("A", "B"));  // ArrayList<String>
    }
}
```

## 🎯 **Target Typing**

### How Target Typing Works

```java
import java.util.function.*;

public class TargetTyping {
    
    // The context in which an expression appears influences its type
    public static void main(String[] args) {
        // Example 1: Assignment context
        List<String> strings = new ArrayList<>();  // Target: List<String>
        
        // Example 2: Method argument context
        processList(new ArrayList<>());  // Target: List<String> from parameter
        
        // Example 3: Return context
        List<Integer> numbers = createList();  // Target: List<Integer>
        
        // Example 4: Cast context
        Object obj = (List<String>) new ArrayList<>();  // Target: List<String>
        
        // Example 5: Lambda expressions (Java 8+)
        Predicate<String> isEmpty = String::isEmpty;  // Target: Predicate<String>
        Consumer<String> printer = System.out::println;  // Target: Consumer<String>
    }
    
    public static void processList(List<String> list) {
        list.add("Hello");
    }
    
    public static List<Integer> createList() {
        return new ArrayList<>();  // Target: List<Integer> from return type
    }
    
    // Advanced target typing with streams
    public static void streamExample() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // Target typing in stream operations
        List<String> upperCaseNames = names.stream()
            .map(String::toUpperCase)  // Target: Function<? super String, ? extends R>
            .collect(Collectors.toList());  // Target: Collector
        
        // The compiler infers all types from context
    }
}
```

## 🔍 **Type Inference in Method Calls**

### Generic Method Type Inference

```java
public class MethodInference {
    
    // Simple generic method
    public static <T> T identity(T input) {
        return input;
    }
    
    // Generic method with multiple parameters
    public static <T> boolean equals(T a, T b) {
        return a.equals(b);
    }
    
    // Generic method with bounded type parameter
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
    
    // Generic method returning collection
    public static <T> List<T> singletonList(T item) {
        List<T> list = new ArrayList<>();
        list.add(item);
        return list;
    }
    
    public static void main(String[] args) {
        // Inference from argument types
        String s1 = identity("Hello");  // T inferred as String
        Integer i1 = identity(123);     // T inferred as Integer
        
        // Inference with multiple arguments
        boolean b1 = equals("A", "B");    // T inferred as String
        boolean b2 = equals(1, 2);        // T inferred as Integer
        // boolean b3 = equals("A", 1);   // ❌ Compile error - incompatible types
        
        // Inference with bounds
        String maxString = max("Apple", "Banana");  // T inferred as String
        Integer maxInt = max(5, 10);                // T inferred as Integer
        
        // Inference with collections
        List<String> strings = singletonList("Test");  // T inferred as String
        List<Integer> numbers = singletonList(42);     // T inferred as Integer
    }
    
    // Complex inference scenario
    public static <T> void addToList(List<T> list, T item) {
        list.add(item);
    }
    
    public static void testComplexInference() {
        List<String> strings = new ArrayList<>();
        addToList(strings, "Hello");  // T inferred as String
        
        List<Number> numbers = new ArrayList<>();
        addToList(numbers, 123);      // T inferred as Number (Integer autoboxed)
        addToList(numbers, 3.14);     // T inferred as Number (Double autoboxed)
    }
}
```

### Type Inference with Wildcards

```java
public class WildcardInference {
    
    // Method with wildcard parameter
    public static void printList(List<?> list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
    
    // Method with bounded wildcard
    public static double sumNumbers(List<? extends Number> numbers) {
        double total = 0.0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }
    
    // Method with lower bounded wildcard
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
    
    public static void main(String[] args) {
        // Inference works with wildcards too
        List<String> strings = Arrays.asList("A", "B", "C");
        printList(strings);  // List<?> inferred from argument
        
        List<Integer> integers = Arrays.asList(1, 2, 3);
        double sum1 = sumNumbers(integers);  // List<? extends Number> inferred
        
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        double sum2 = sumNumbers(doubles);   // Same method, different inference
        
        List<Number> numbers = new ArrayList<>();
        addIntegers(numbers);  // List<? super Integer> inferred
        
        List<Object> objects = new ArrayList<>();
        addIntegers(objects);  // Also works - Object is super of Integer
    }
}
```

## ⚡ **Explicit Type Arguments (Type Witness)**

### When Inference Needs Help

```java
public class TypeWitness {
    
    // Generic method that might need explicit type arguments
    public static <T> T parse(String input, Function<String, T> parser) {
        return parser.apply(input);
    }
    
    // Generic method with multiple type parameters
    public static <T, R> R transform(T input, Function<T, R> transformer) {
        return transformer.apply(input);
    }
    
    // Generic method where inference might fail
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }
    
    public static void main(String[] args) {
        // Usually, inference works fine
        Integer i = parse("123", Integer::parseInt);  // T inferred as Integer
        Double d = parse("3.14", Double::parseDouble); // T inferred as Double
        
        // But sometimes we need to provide explicit type arguments
        // Example 1: Ambiguous context
        Object obj1 = TypeWitness.<Integer>parse("123", Integer::parseInt);
        
        // Example 2: Chained method calls
        List<String> list1 = TypeWitness.<String>emptyList();
        list1.add("Hello");
        
        // Example 3: Method reference ambiguity
        List<Integer> lengths = Arrays.asList("A", "BB", "CCC").stream()
            .<Integer>map(String::length)  // Explicit type for map
            .collect(Collectors.toList());
        
        // Example 4: Multiple type parameters
        String result = TypeWitness.<Integer, String>transform(123, Object::toString);
        
        // Example 5: Constructor reference
        List<String> list2 = TypeWitness.<ArrayList<String>>transform(
            Arrays.asList("A", "B"), 
            ArrayList::new
        );
    }
    
    // Real-world example: Optional
    public static void optionalExample() {
        // Without type witness
        Optional<String> opt1 = Optional.of("Hello");
        
        // With type witness (rarely needed)
        Optional<String> opt2 = Optional.<String>of("World");
        
        // Needed for empty() in some contexts
        Optional<String> opt3 = Optional.empty();  // Usually infers correctly
        // But sometimes:
        Optional<String> opt4 = Optional.<String>empty();
    }
}
```

## 🔄 **Type Inference in Chained Calls**

### Complex Inference Scenarios

```java
public class ChainedInference {
    
    static class Builder<T> {
        private T value;
        
        public Builder<T> setValue(T value) {
            this.value = value;
            return this;
        }
        
        public T build() {
            return value;
        }
        
        // Generic method in generic class
        public <R> Builder<R> transform(Function<T, R> transformer) {
            Builder<R> newBuilder = new Builder<>();
            newBuilder.setValue(transformer.apply(this.value));
            return newBuilder;
        }
    }
    
    public static void main(String[] args) {
        // Simple chain
        String result1 = new Builder<String>()
            .setValue("Hello")
            .build();
        
        // Chain with transformation
        Integer result2 = new Builder<String>()
            .setValue("Hello")
            .transform(String::length)  // Inference: T=String, R=Integer
            .build();
        
        // Complex chain with multiple transformations
        String result3 = new Builder<String>()
            .setValue("hello")
            .transform(String::toUpperCase)  // T=String, R=String
            .transform(s -> s + " WORLD")    // T=String, R=String
            .build();
    }
    
    // Stream API: The ultimate chained inference
    public static void streamInference() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        
        // Compiler infers types through the entire chain
        Map<Integer, List<String>> groupedByLength = names.stream()
            .filter(name -> name.length() > 3)           // Stream<String>
            .collect(Collectors.groupingBy(
                String::length,                          // Classifier: String -> Integer
                Collectors.mapping(
                    String::toUpperCase,                 // Mapper: String -> String
                    Collectors.toList()                  // Downstream collector
                )
            ));
        
        // Every step has inferred types:
        // 1. names.stream() → Stream<String>
        // 2. filter(Predicate<? super String>) → Stream<String>
        // 3. groupingBy(Function<? super String, ? extends K>, Collector)
        // 4. mapping(Function<? super T, ? extends U>, Collector)
    }
}
```

## 🎯 **Java 8+ Enhanced Inference**

### Improved Inference in Java 8

```java
public class Java8Inference {
    
    // Before Java 8: Inference didn't work well with method context
    public static void preJava8() {
        // Java 7: This might not compile or need explicit types
        // List<String> list = Collections.emptyList();  // Might infer List<Object>
        
        // Workaround: explicit type witness
        List<String> list = Collections.<String>emptyList();
    }
    
    // Java 8: Better inference in method context
    public static void java8Inference() {
        // Java 8: Inference works in method argument context
        List<String> list1 = Collections.emptyList();  // Infers List<String>
        
        // Also works with streams
        List<String> filtered = Arrays.asList("A", "B", "C").stream()
            .filter(s -> s.length() > 1)
            .collect(Collectors.toList());  // Infers List<String>
    }
    
    // Inference with lambda expressions
    public static void lambdaInference() {
        // Lambda parameter types can be inferred
        Function<String, Integer> lengthFunc = s -> s.length();  // s inferred as String
        
        // In method arguments
        List<String> names = Arrays.asList("Alice", "Bob");
        names.sort((s1, s2) -> s1.length() - s2.length());  // s1, s2 inferred as String
        
        // With method references
        List<Integer> lengths = names.stream()
            .map(String::length)  // Infers Function<String, Integer>
            .collect(Collectors.toList());
    }
    
    // Target typing with method references
    public static void methodReferenceInference() {
        // The compiler infers types based on context
        Consumer<String> printer = System.out::println;  // Infers String
        
        Supplier<List<String>> listSupplier = ArrayList::new;  // Infers ArrayList<String>
        
        Function<String, Integer> parser = Integer::parseInt;  // Infers String -> Integer
    }
}
```

### Java 10+: Local Variable Type Inference (`var`)

```java
public class VarInference {
    
    // var can be used for local variables (Java 10+)
    public static void varExamples() {
        // Instead of explicit types
        List<String> explicitList = new ArrayList<>();
        
        // Use var (type inferred from initializer)
        var inferredList = new ArrayList<String>();  // Inferred as ArrayList<String>
        
        // Common uses
        var names = List.of("Alice", "Bob", "Charlie");  // Inferred as List<String>
        var map = new HashMap<String, Integer>();        // Inferred as HashMap<String, Integer>
        var stream = names.stream();                     // Inferred as Stream<String>
        
        // With diamond operator
        var list = new ArrayList<>();  // ❌ Dangerous! Inferred as ArrayList<Object>
        var safeList = new ArrayList<String>();  // ✅ Better
        
        // Limitations
        // var x;  // ❌ Cannot use without initializer
        // var nullVar = null;  // ❌ Cannot infer from null
        // var lambda = x -> x * 2;  // ❌ Cannot infer lambda type
    }
    
    // Best practices with var
    public static void varBestPractices() {
        // ✅ Good: Clear from context
        var entries = new ArrayList<Map.Entry<String, Integer>>();
        var inputStream = new ByteArrayInputStream(new byte[10]);
        
        // ✅ Good: With factory methods
        var list = Collections.emptyList();  // List<Object> - be careful!
        var stringList = Collections.<String>emptyList();  // List<String>
        
        // ❌ Bad: Obscures type
        var data = getData();  // What type is data?
        
        // ✅ Better: When type is obvious
        var username = "JohnDoe";  // Obviously String
        var count = 42;            // Obviously int
        var ratio = 3.14;          // Obviously double
    }
    
    private static Object getData() {
        return "Some data";
    }
    
    // var with generics
    public static void varWithGenerics() {
        // Generic types work with var
        var optional = Optional.of("Hello");  // Optional<String>
        var pair = Map.entry("key", 123);     // Map.Entry<String, Integer>
        
        // Be careful with inference
        var list = new ArrayList<>();         // ArrayList<Object>
        list.add("String");
        list.add(123);  // Allowed!
        
        var typedList = new ArrayList<String>();  // ArrayList<String>
        typedList.add("String");
        // typedList.add(123);  // ❌ Compile error
    }
}
```

## 🧪 **Debugging Type Inference**

### When Inference Goes Wrong

```java
public class DebuggingInference {
    
    // Sometimes inference fails or gives unexpected results
    public static void commonIssues() {
        // Issue 1: Raw types break inference
        List rawList = new ArrayList();
        // var inferredList = rawList;  // Inferred as raw List
        
        // Issue 2: Diamond with var
        var list = new ArrayList<>();  // ArrayList<Object> not ArrayList<String>
        
        // Issue 3: Method overload ambiguity
        // overloadedMethod(Arrays.asList("A", "B"));  // Which overload?
    }
    
    // Using explicit types to debug
    public static void debuggingTips() {
        // When in doubt, add explicit types
        List<String> explicit = new ArrayList<String>();  // No inference needed
        
        // Use type witness
        List<String> list = Collections.<String>emptyList();
        
        // Break down chained calls
        Stream<String> stream = Arrays.asList("A", "B").stream();
        Stream<String> filtered = stream.filter(s -> s.length() > 1);
        List<String> result = filtered.collect(Collectors.toList());
        
        // Check compiler error messages
        // They often suggest adding explicit type arguments
    }
    
    // Using IDE features to check inferred types
    public static void ideFeatures() {
        // Most IDEs can show inferred types on hover
        var list = new ArrayList<String>();  // Hover shows ArrayList<String>
        
        var complex = Arrays.asList("A", "B").stream()
            .map(String::length)
            .filter(len -> len > 1)
            .collect(Collectors.toList());  // Hover shows List<Integer>
    }
}
```

## 📊 **Inference Rules Summary**

### The Algorithm in Simple Terms

1. **Diamond Operator Inference**:
   - Uses the target type (left side of assignment)
   - Or method parameter type
   - Or return type

2. **Generic Method Inference**:
   - Examines method arguments
   - Examines target type
   - Tries to find the most specific type that works

3. **Lambda Expression Inference**:
   - Uses target functional interface
   - Infers parameter types from abstract method signature
   - Infers return type from lambda body

4. **Method Reference Inference**:
   - Similar to lambda inference
   - Additional checks for method compatibility

## 🎓 **Best Practices**

1. **Use diamond operator** (`<>`) when possible for cleaner code
2. **Avoid raw types** - they break inference
3. **Use explicit types** when inference is unclear
4. **Be careful with `var` and diamond** - `new ArrayList<>()` with `var` gives `ArrayList<Object>`
5. **Use type witnesses** (`.<Type>method()`) when compiler needs help
6. **Let the IDE help** - hover to see inferred types
7. **Test edge cases** - inference can behave differently in complex scenarios

## 🚀 **Next Steps**

Type inference makes generics more usable, but sometimes you need even more flexibility. That's where **Wildcards** come in. In [Chapter 9](#9-wildcards), we'll dive deep into the unknown type (`?`), learning how to write APIs that accept and return collections of unknown types while maintaining type safety.

> 💡 **Practice Exercise**: 
> 1. Create a generic method that takes two lists and returns a map, using type inference to avoid explicit type parameters.
> 2. Create a method chain using Stream API with at least 5 operations, and annotate what type is inferred at each step.
> 3. Experiment with `var` and the diamond operator to see what types are inferred in different scenarios.