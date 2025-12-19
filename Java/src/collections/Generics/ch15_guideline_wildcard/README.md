# Chapter 15: 📐 **Guidelines for Wildcard Use**

## The Art of Balancing Flexibility and Type Safety

After exploring the mechanics of wildcards, it's crucial to understand when and how to use them effectively. This chapter provides practical guidelines, design patterns, and best practices for using wildcards in real-world Java code.

## 🧠 **PECS: Producer Extends, Consumer Super**

### The Fundamental Principle

**PECS** stands for **"Producer Extends, Consumer Super"** - a mnemonic coined by Joshua Bloch in *Effective Java*. It's the most important guideline for using wildcards correctly.

```java
import java.util.*;
import java.util.function.*;

public class PECSPrinciple {
    
    // PECS in a nutshell:
    // - Use ? extends T when you GET/PRODUCE values (PRODUCER)
    // - Use ? super T when you PUT/CONSUME values (CONSUMER)
    
    static class Animal {
        String name;
        Animal(String name) { this.name = name; }
    }
    
    static class Dog extends Animal {
        Dog(String name) { super(name); }
        void bark() { System.out.println(name + " says: Woof!"); }
    }
    
    static class Cat extends Animal {
        Cat(String name) { super(name); }
        void meow() { System.out.println(name + " says: Meow!"); }
    }
    
    // PRODUCER EXAMPLE: Reading from a collection
    public static void feedAllAnimals(List<? extends Animal> animals) {
        // animals is a PRODUCER - we read/consume animals from it
        for (Animal animal : animals) {
            System.out.println("Feeding: " + animal.name);
        }
        
        // ❌ CANNOT write to a producer
        // animals.add(new Dog("Rex"));  // Compile error
    }
    
    // CONSUMER EXAMPLE: Writing to a collection
    public static void addDogsToKennel(List<? super Dog> kennel, Dog... dogs) {
        // kennel is a CONSUMER - we write/produce dogs to it
        for (Dog dog : dogs) {
            kennel.add(dog);
            System.out.println("Added " + dog.name + " to kennel");
        }
        
        // ❌ CANNOT read specific type from a consumer
        // Dog firstDog = kennel.get(0);  // Compile error
        // Can only read as Object: Object obj = kennel.get(0);
    }
    
    // BOTH PRODUCER AND CONSUMER: Use exact type
    public static void trainDogs(List<Dog> dogs) {
        // dogs is both producer and consumer
        for (Dog dog : dogs) {  // PRODUCER: reading
            dog.bark();
        }
        dogs.add(new Dog("New Dog"));  // CONSUMER: writing
    }
    
    // Real-world example: Collections.copy()
    // Signature: public static <T> void copy(List<? super T> dest, List<? extends T> src)
    public static <T> void pecsCopy(List<? super T> dest, List<? extends T> src) {
        // dest is CONSUMER (? super T) - we write T to it
        // src is PRODUCER (? extends T) - we read T from it
        for (T item : src) {
            dest.add(item);
        }
    }
    
    // Another example: max() with comparator
    public static <T> T pecsMax(Collection<? extends T> coll, Comparator<? super T> comp) {
        // coll is PRODUCER (? extends T) - we read T from it
        // comp is CONSUMER (? super T) - it consumes T (and possibly supertypes) to compare
        T max = null;
        for (T item : coll) {
            if (max == null || comp.compare(item, max) > 0) {
                max = item;
            }
        }
        return max;
    }
    
    public static void main(String[] args) {
        // Producer example
        List<Dog> dogs = Arrays.asList(new Dog("Buddy"), new Dog("Max"));
        List<Cat> cats = Arrays.asList(new Cat("Whiskers"), new Cat("Mittens"));
        
        feedAllAnimals(dogs);  // ✅ List<Dog> is List<? extends Animal>
        feedAllAnimals(cats);  // ✅ List<Cat> is List<? extends Animal>
        
        // Consumer example
        List<Animal> animalShelter = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        
        addDogsToKennel(animalShelter, new Dog("Rex"), new Dog("Fido"));
        addDogsToKennel(objectList, new Dog("Spot"));
        
        // Copy example (PECS in action)
        List<Animal> destination = new ArrayList<>();
        List<Dog> source = Arrays.asList(new Dog("A"), new Dog("B"));
        pecsCopy(destination, source);  // dest: ? super Dog, src: ? extends Dog
        System.out.println("Copied " + destination.size() + " animals");
        
        // Max example
        List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
        Comparator<Number> numberComparator = Comparator.comparingInt(Number::intValue);
        Integer max = pecsMax(numbers, numberComparator);  // Comparator<Number> is Comparator<? super Integer>
        System.out.println("Max number: " + max);
    }
}
```

### When to Apply PECS

| Scenario | Wildcard to Use | Reason |
|----------|----------------|--------|
| Parameter provides elements | `? extends T` | Producer - you get values out |
| Parameter receives elements | `? super T` | Consumer - you put values in |
| Parameter both provides and receives | `<T>` (no wildcard) | Need exact type for both operations |
| Parameter is used for both but operations are independent | Consider separate methods | Split producer and consumer roles |

## 🤔 **Wildcards vs. Type Parameters**

### Choosing the Right Tool

```java
import java.util.*;
import java.util.function.*;

public class WildcardsVsTypeParams {
    
    // SCENARIO 1: When you need to refer to the type
    
    // ❌ Wildcards can't refer to the type
    // public static ? getFirst(List<?> list) {  // Can't use ? as return type
    //     return list.get(0);
    // }
    
    // ✅ Type parameters can
    public static <T> T getFirst(List<T> list) {
        return list.get(0);
    }
    
    // SCENARIO 2: When you need multiple parameters of the same type
    
    // ❌ Wildcards can't ensure same type
    public static void wildcardSwap(List<?> a, List<?> b) {
        // a and b might have different element types!
        // Can't swap between them safely
    }
    
    // ✅ Type parameters can
    public static <T> void typeParamSwap(List<T> a, List<T> b) {
        // a and b have the same element type T
        // Can implement swap if needed
    }
    
    // SCENARIO 3: Maximum flexibility in parameters
    
    // ✅ Wildcards are more flexible
    public static void printAll(List<?> list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
    
    // ❌ Type parameter less flexible (but allows more operations)
    public static <T> void printAllTyped(List<T> list) {
        for (T obj : list) {
            System.out.println(obj);
        }
        // Can also do: list.add(somethingOfTypeT);
    }
    
    // SCENARIO 4: Method chaining and fluent APIs
    
    // ✅ Type parameters work better
    public static class Builder<T> {
        private T value;
        
        public Builder<T> setValue(T value) {
            this.value = value;
            return this;
        }
        
        public T build() {
            return value;
        }
    }
    
    // ❌ Wildcards break chaining
    // public static class WildcardBuilder {
    //     private Object value;
    //     
    //     public WildcardBuilder setValue(? value) {  // Can't do this
    //         this.value = value;
    //         return this;
    //     }
    // }
    
    // SCENARIO 5: Multiple type bounds
    
    // ✅ Type parameters support multiple bounds
    public static <T extends Comparable<T> & Serializable> void process(T item) {
        // Can use both Comparable and Serializable methods
    }
    
    // ❌ Wildcards don't support multiple bounds directly
    // public static void processWildcard(? extends Comparable<?> & Serializable item) {
    //     // Not possible
    // }
    
    // SCENARIO 6: Static utility methods
    
    // Both work, but wildcards often simpler for utilities
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    // Equivalent with type parameter
    public static <T> boolean isEmptyTyped(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
    
    // Decision table:
    public static void decisionTable() {
        /*
        Choose WILDCARDS when:
        - You don't need to refer to the type in the method body
        - You want maximum flexibility for callers
        - You're implementing a simple utility method
        - You're following PECS (Producer Extends, Consumer Super)
        
        Choose TYPE PARAMETERS when:
        - You need to refer to the type (return type, local variables)
        - You have multiple parameters that must have the same type
        - You're implementing a fluent API
        - You need multiple bounds (T extends A & B)
        - You're writing a generic class (not just a method)
        */
    }
    
    // Best practice: Combine both when appropriate
    public static <T> List<T> filter(Collection<? extends T> source, 
                                    Predicate<? super T> predicate) {
        // source: PRODUCER of T (use extends)
        // predicate: CONSUMER of T (use super)
        // return: exact type T
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
```

## 🏆 **Best Practices for Clean, Flexible APIs**

### API Design Guidelines

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class APIDesignBestPractices {
    
    // RULE 1: Use wildcards to increase API flexibility
    
    // ❌ Rigid API - only accepts List<String>
    public static void processStrings(List<String> strings) {
        for (String s : strings) {
            System.out.println(s.toUpperCase());
        }
    }
    
    // ✅ Flexible API - accepts List of any type
    public static void processAnyList(List<?> list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
    
    // ✅ Even better: Use bounded wildcard when possible
    public static void processAnyCharSequence(List<? extends CharSequence> sequences) {
        for (CharSequence cs : sequences) {
            System.out.println(cs.length());
        }
    }
    
    // RULE 2: Follow PECS in method signatures
    
    // Good examples from Java API:
    // - Collections.copy(List<? super T> dest, List<? extends T> src)
    // - Collections.max(Collection<? extends T> coll, Comparator<? super T> comp)
    // - List.sort(Comparator<? super E> c)
    // - Stream.map(Function<? super T, ? extends R> mapper)
    
    // RULE 3: Avoid wildcards in return types (usually)
    
    // ❌ Usually bad - caller can't do much with List<?>
    public static List<?> getData() {
        return Arrays.asList(1, 2, 3);
    }
    
    // ✅ Better: Return concrete type
    public static List<String> getStrings() {
        return Arrays.asList("A", "B", "C");
    }
    
    // ✅ Sometimes appropriate: Factory methods for empty collections
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }
    
    // RULE 4: Document wildcard constraints
    
    /**
     * Filters a collection based on a predicate.
     * 
     * @param <T> the type of elements in the source collection
     * @param source the source collection (PRODUCER of T)
     * @param predicate the predicate to apply (CONSUMER of T - can test T or supertypes)
     * @return a new list containing elements that match the predicate
     */
    public static <T> List<T> filter(Collection<? extends T> source, 
                                    Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    // RULE 5: Use helper methods for complex wildcard operations
    
    // Complex operation with wildcard capture
    public static void reverse(List<?> list) {
        reverseHelper(list);
    }
    
    private static <T> void reverseHelper(List<T> list) {
        Collections.reverse(list);
    }
    
    // RULE 6: Consider performance for primitive types
    
    // ❌ Wildcards with boxed types can be inefficient
    public static double sumNumbers(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number n : numbers) {
            sum += n.doubleValue();  // Boxing/unboxing overhead
        }
        return sum;
    }
    
    // ✅ Consider specialized methods for performance-critical code
    public static int sumInts(int[] ints) {
        int sum = 0;
        for (int n : ints) {
            sum += n;  // No boxing
        }
        return sum;
    }
    
    // ✅ Or use streams for flexibility with reasonable performance
    public static double sumWithStreams(List<? extends Number> numbers) {
        return numbers.stream()
            .mapToDouble(Number::doubleValue)
            .sum();
    }
    
    // RULE 7: Test with edge cases
    
    public static void testEdgeCases() {
        // Test with empty collections
        List<?> empty = Collections.emptyList();
        processAnyList(empty);
        
        // Test with null elements
        List<String> withNulls = Arrays.asList("A", null, "C");
        processAnyList(withNulls);
        
        // Test with mixed-type lists through common supertype
        List<Object> mixed = Arrays.asList("String", 123, 3.14, true);
        processAnyList(mixed);
        
        // Test with wildcard capture
        List<?> wild = Arrays.asList(1, 2, 3);
        reverse(wild);
    }
    
    // RULE 8: Balance flexibility with simplicity
    
    // ❌ Overly complex - too many wildcards
    public static <T, R> List<R> overlyComplex(
        List<? extends List<? extends T>> nested,
        Function<? super T, ? extends R> mapper,
        Predicate<? super R> filter) {
        // Hard to understand and use
        return Collections.emptyList();
    }
    
    // ✅ Simpler and clearer
    public static <T, R> List<R> mapAndFilter(
        List<T> list,
        Function<T, R> mapper,
        Predicate<R> filter) {
        return list.stream()
            .map(mapper)
            .filter(filter)
            .collect(Collectors.toList());
    }
    
    // Sometimes you need the complexity, but document it well!
    /**
     * Transforms a nested collection structure.
     * 
     * @param nested a list of lists where inner lists produce elements of type T
     * @param mapper function that consumes T (or supertype) and produces R (or subtype)
     * @param filter predicate that consumes R (or supertype)
     * @return filtered and transformed results
     */
    public static <T, R> List<R> properlyDocumentedComplex(
        List<? extends List<? extends T>> nested,
        Function<? super T, ? extends R> mapper,
        Predicate<? super R> filter) {
        List<R> result = new ArrayList<>();
        for (List<? extends T> inner : nested) {
            for (T item : inner) {
                R mapped = mapper.apply(item);
                if (filter.test(mapped)) {
                    result.add(mapped);
                }
            }
        }
        return result;
    }
}
```

## 📋 **Decision Framework for Wildcard Usage**

### Flowchart Implementation

```java
import java.util.*;

public class WildcardDecisionFramework {
    
    // Decision helper method
    public static String chooseWildcardPattern(String scenario) {
        Map<String, String> decisions = new HashMap<>();
        
        decisions.put("READ_ONLY", 
            "Use ? extends T (Producer)\n" +
            "Example: void process(List<? extends Number> numbers)");
        
        decisions.put("WRITE_ONLY", 
            "Use ? super T (Consumer)\n" +
            "Example: void addAll(List<? super Integer> list, Integer... values)");
        
        decisions.put("READ_AND_WRITE", 
            "Use <T> (no wildcard)\n" +
            "Example: <T> void swap(List<T> list, int i, int j)");
        
        decisions.put("MAXIMUM_FLEXIBILITY", 
            "Use ? (unbounded)\n" +
            "Example: int size(Collection<?> collection)");
        
        decisions.put("API_PARAMETER", 
            "Consider PECS:\n" +
            "- Parameter provides values → ? extends T\n" +
            "- Parameter receives values → ? super T\n" +
            "- Parameter does both → <T>\n" +
            "- Parameter neither → ?");
        
        decisions.put("RETURN_TYPE", 
            "Avoid wildcards in return types (usually)\n" +
            "Exception: Factory methods like Collections.emptyList()");
        
        decisions.put("COMPLEX_OPERATION", 
            "Use helper method with type parameter\n" +
            "Example: \n" +
            "  public void reverse(List<?> list) {\n" +
            "    reverseHelper(list);\n" +
            "  }\n" +
            "  private <T> void reverseHelper(List<T> list) {\n" +
            "    Collections.reverse(list);\n" +
            "  }");
        
        return decisions.getOrDefault(scenario, 
            "Consider:\n" +
            "1. What operations do you need?\n" +
            "2. Is it a producer, consumer, or both?\n" +
            "3. Do you need to refer to the type?\n" +
            "4. How flexible should the API be?");
    }
    
    // Practical examples of each decision
    public static class Examples {
        
        // 1. READ_ONLY: ? extends T
        public static double average(List<? extends Number> numbers) {
            double sum = 0.0;
            for (Number n : numbers) {
                sum += n.doubleValue();
            }
            return numbers.isEmpty() ? 0 : sum / numbers.size();
        }
        
        // 2. WRITE_ONLY: ? super T
        public static void fill(List<? super Integer> list, int value, int count) {
            for (int i = 0; i < count; i++) {
                list.add(value);
            }
        }
        
        // 3. READ_AND_WRITE: <T>
        public static <T> void replaceAll(List<T> list, T oldVal, T newVal) {
            for (int i = 0; i < list.size(); i++) {
                if (Objects.equals(list.get(i), oldVal)) {
                    list.set(i, newVal);
                }
            }
        }
        
        // 4. MAXIMUM_FLEXIBILITY: ?
        public static boolean containsNull(Collection<?> collection) {
            for (Object obj : collection) {
                if (obj == null) {
                    return true;
                }
            }
            return false;
        }
        
        // 5. API_PARAMETER: Follow PECS
        public static <T> void copy(List<? super T> dest, List<? extends T> src) {
            dest.addAll(src);
        }
        
        // 6. RETURN_TYPE: Avoid wildcards
        public static <T> List<T> singletonList(T item) {
            List<T> list = new ArrayList<>();
            list.add(item);
            return list;
        }
        
        // 7. COMPLEX_OPERATION: Helper method
        public static void rotate(List<?> list, int distance) {
            rotateHelper(list, distance);
        }
        
        private static <T> void rotateHelper(List<T> list, int distance) {
            if (list.isEmpty()) return;
            int size = list.size();
            distance = distance % size;
            if (distance < 0) distance += size;
            
            Collections.rotate(list, distance);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Wildcard Decision Framework ===\n");
        
        System.out.println("SCENARIO: READ_ONLY");
        System.out.println(chooseWildcardPattern("READ_ONLY"));
        System.out.println("Example result: " + 
            Examples.average(Arrays.asList(1, 2, 3, 4, 5)));
        
        System.out.println("\nSCENARIO: WRITE_ONLY");
        System.out.println(chooseWildcardPattern("WRITE_ONLY"));
        List<Number> numbers = new ArrayList<>();
        Examples.fill(numbers, 42, 3);
        System.out.println("Example result: " + numbers);
        
        System.out.println("\nSCENARIO: API_PARAMETER");
        System.out.println(chooseWildcardPattern("API_PARAMETER"));
        List<String> src = Arrays.asList("A", "B", "C");
        List<Object> dest = new ArrayList<>();
        Examples.copy(dest, src);
        System.out.println("Copy example: " + dest);
        
        // Interactive decision helper
        System.out.println("\n=== Quick Decision Guide ===");
        System.out.println("Ask yourself:");
        System.out.println("1. Do I only READ from the parameter? → ? extends T");
        System.out.println("2. Do I only WRITE to the parameter? → ? super T");
        System.out.println("3. Do I both READ and WRITE? → <T> (no wildcard)");
        System.out.println("4. Do I not care about the type at all? → ?");
        System.out.println("5. Is it a return type? → Avoid wildcards");
        System.out.println("6. Is the operation complex? → Use helper method");
    }
}
```

## 🔧 **Common Anti-Patterns and How to Fix Them**

### Code Smells and Solutions

```java
import java.util.*;
import java.util.function.*;

public class WildcardAntiPatterns {
    
    // ANTI-PATTERN 1: Raw types instead of wildcards
    public static class RawTypeExample {
        // ❌ Using raw type
        public static void processRaw(List list) {
            for (Object obj : list) {
                System.out.println(obj);
            }
            list.add("anything");  // UNSAFE!
        }
        
        // ✅ Use wildcard instead
        public static void processSafe(List<?> list) {
            for (Object obj : list) {
                System.out.println(obj);
            }
            // list.add("anything");  // Compile error - SAFE!
        }
    }
    
    // ANTI-PATTERN 2: Unnecessary type parameters
    public static class UnnecessaryTypeParam {
        // ❌ Type parameter not needed
        public static <T> void printAll(List<T> list) {
            for (Object obj : list) {  // Using Object anyway
                System.out.println(obj);
            }
        }
        
        // ✅ Wildcard is simpler
        public static void printAllSimple(List<?> list) {
            for (Object obj : list) {
                System.out.println(obj);
            }
        }
    }
    
    // ANTI-PATTERN 3: Wildcard in return type
    public static class WildcardReturnType {
        // ❌ Wildcard in return type
        public static List<?> getList() {
            return Arrays.asList(1, 2, 3);
        }
        
        // Problem: Caller can't do anything useful
        public static void test() {
            List<?> list = getList();
            // list.add(4);  // Compile error
            // Integer i = list.get(0);  // Compile error
        }
        
        // ✅ Better: Return specific type or use type parameter
        public static List<Integer> getIntList() {
            return Arrays.asList(1, 2, 3);
        }
        
        public static <T> List<T> getSingleton(T item) {
            List<T> list = new ArrayList<>();
            list.add(item);
            return list;
        }
    }
    
    // ANTI-PATTERN 4: Ignoring PECS
    public static class IgnoringPECS {
        // ❌ Not following PECS
        public static <T> void copyBad(List<T> dest, List<T> src) {
            for (T item : src) {
                dest.add(item);
            }
        }
        
        // Problem: Can't copy List<Dog> to List<Animal>
        public static void test() {
            List<Dog> dogs = Arrays.asList(new Dog("Rex"));
            List<Animal> animals = new ArrayList<>();
            // copyBad(animals, dogs);  // Compile error!
        }
        
        // ✅ Following PECS
        public static <T> void copyGood(List<? super T> dest, List<? extends T> src) {
            for (T item : src) {
                dest.add(item);
            }
        }
        
        public static void testGood() {
            List<Dog> dogs = Arrays.asList(new Dog("Rex"));
            List<Animal> animals = new ArrayList<>();
            copyGood(animals, dogs);  // Works!
        }
    }
    
    // ANTI-PATTERN 5: Overusing wildcards
    public static class OverusingWildcards {
        // ❌ Too many wildcards - hard to understand
        public static <T> void confusing(
            List<? extends List<? extends T>> outer,
            Function<? super T, ? extends T> func) {
            // What does this even do?
        }
        
        // ✅ Simplify when possible
        public static <T> void clearer(
            List<List<T>> outer,
            Function<T, T> func) {
            // Much clearer
            for (List<T> inner : outer) {
                for (int i = 0; i < inner.size(); i++) {
                    inner.set(i, func.apply(inner.get(i)));
                }
            }
        }
    }
    
    // ANTI-PATTERN 6: Not using helper methods for capture
    public static class NoCaptureHelper {
        // ❌ Trying to work with wildcards directly
        // public static void swap(List<?> list, int i, int j) {
        //     ? temp = list.get(i);  // Can't do this
        //     list.set(i, list.get(j));
        //     list.set(j, temp);
        // }
        
        // ✅ Use helper method
        public static void swap(List<?> list, int i, int j) {
            swapHelper(list, i, j);
        }
        
        private static <T> void swapHelper(List<T> list, int i, int j) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
    
    // ANTI-PATTERN 7: Mixing wildcards with arrays
    public static class WildcardArrays {
        // ❌ Creating arrays of wildcard parameterized types
        public static void problematic() {
            // List<?>[] array = new List<?>[10];  // Warning: unchecked
            // array[0] = new ArrayList<String>();
            // array[1] = new ArrayList<Integer>();
            // Problem: Can lead to heap pollution
        }
        
        // ✅ Better: Use collections instead
        public static void better() {
            List<List<?>> listOfLists = new ArrayList<>();
            listOfLists.add(new ArrayList<String>());
            listOfLists.add(new ArrayList<Integer>());
            // Type safe
        }
    }
    
    // Helper class
    static class Animal {}
    static class Dog extends Animal {
        String name;
        Dog(String name) { this.name = name; }
    }
    
    // Fixing anti-patterns in practice
    public static class Fixes {
        // From: Raw type method
        // To: Wildcard method
        public static int safeSize(Collection<?> collection) {
            return collection == null ? 0 : collection.size();
        }
        
        // From: Unbounded wildcard return
        // To: Generic method
        public static <T> List<T> safeEmptyList() {
            return new ArrayList<>();
        }
        
        // From: Complex wildcard signature
        // To: Simplified with documentation
        /**
         * Applies a transformation to nested collections.
         */
        public static <T, R> List<R> transformNested(
            List<List<T>> nested,
            Function<T, R> transformer) {
            List<R> result = new ArrayList<>();
            for (List<T> inner : nested) {
                for (T item : inner) {
                    result.add(transformer.apply(item));
                }
            }
            return result;
        }
    }
}
```

## 📊 **Performance Considerations Summary**

### Wildcards vs Type Parameters Performance

```java
import java.util.*;
import java.util.function.Function;

public class PerformanceSummary {
    
    // Key insights:
    // 1. Wildcards are a COMPILE-TIME feature
    // 2. They have NO runtime performance cost
    // 3. All wildcard information is erased at runtime
    
    public static void demonstrateTypeErasure() {
        // At compile time:
        List<String> stringList = new ArrayList<>();
        List<?> wildcardList = stringList;
        
        // After type erasure (at runtime):
        // Both become just 'List'
        // No difference in performance
        
        // The only performance considerations are:
        // 1. Boxing/unboxing with primitive wrappers
        // 2. Collection vs array access
        // 3. Algorithm complexity
        
        // Not wildcards themselves
    }
    
    // Real performance tip: Avoid unnecessary boxing
    public static class BoxingPerformance {
        // ❌ May cause boxing overhead
        public static double sumWithWildcard(List<? extends Number> numbers) {
            double sum = 0.0;
            for (Number n : numbers) {
                sum += n.doubleValue();  // May involve unboxing
            }
            return sum;
        }
        
        // ✅ Specialized version avoids boxing
        public static int sumInts(int[] ints) {
            int sum = 0;
            for (int n : ints) {
                sum += n;  // No boxing
            }
            return sum;
        }
        
        // ✅ Use streams for good balance
        public static double sumWithStream(List<? extends Number> numbers) {
            return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
        }
    }
    
    // Memory considerations
    public static class MemoryConsiderations {
        // Wildcards don't affect memory usage
        // But the choice of collection type does
        
        // ArrayList<?> and ArrayList<String> use same memory
        // But ArrayList<Integer> uses more than int[]
        
        public static void compareMemory() {
            // Approximately:
            // int[1000] - 4KB + overhead
            // Integer[1000] - 16KB + overhead (4 bytes per reference + 12-16 bytes per Integer)
            // List<Integer> with 1000 elements - even more overhead
            
            // Moral: Use primitives arrays for large datasets of primitives
            // Use wildcards for flexibility when needed, not for performance
        }
    }
    
    // Benchmark helper
    public static void benchmarkWildcardVsTyped() {
        int iterations = 1000000;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            numbers.add(i);
        }
        
        // Test 1: Wildcard method
        long start = System.nanoTime();
        int sum1 = sumWildcard(numbers);
        long time1 = System.nanoTime() - start;
        
        // Test 2: Typed method
        start = System.nanoTime();
        int sum2 = sumTyped(numbers);
        long time2 = System.nanoTime() - start;
        
        // Test 3: Primitive array
        int[] array = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            array[i] = i;
        }
        start = System.nanoTime();
        int sum3 = sumArray(array);
        long time3 = System.nanoTime() - start;
        
        System.out.println("Wildcard sum: " + time1 + " ns");
        System.out.println("Typed sum: " + time2 + " ns");
        System.out.println("Array sum: " + time3 + " ns");
        System.out.println("Difference is due to collection vs array, not wildcards");
    }
    
    private static int sumWildcard(List<? extends Number> numbers) {
        int sum = 0;
        for (Number n : numbers) {
            sum += n.intValue();
        }
        return sum;
    }
    
    private static int sumTyped(List<Integer> numbers) {
        int sum = 0;
        for (Integer n : numbers) {
            sum += n;
        }
        return sum;
    }
    
    private static int sumArray(int[] array) {
        int sum = 0;
        for (int n : array) {
            sum += n;
        }
        return sum;
    }
    
    public static void main(String[] args) {
        benchmarkWildcardVsTyped();
    }
}
```

## 🎓 **Summary: When to Use Which Pattern**

### Quick Reference Guide

| Use Case | Pattern | Example |
|----------|---------|---------|
| **Read-only parameter** | `? extends T` | `void process(List<? extends Number> nums)` |
| **Write-only parameter** | `? super T` | `void addAll(List<? super Integer> list)` |
| **Read-write parameter** | `<T>` (no wildcard) | `<T> void swap(List<T> list, int i, int j)` |
| **Don't care about type** | `?` | `int size(Collection<?> coll)` |
| **Maximum flexibility** | Combine PECS | `<T> void copy(List<? super T> dest, List<? extends T> src)` |
| **Complex operation** | Helper method | `reverse(List<?> list)` with `reverseHelper(List<T> list)` |
| **Return type** | Avoid wildcards (usually) | `<T> List<T> singleton(T item)` |
| **API design** | Document PECS | Javadoc explaining producer/consumer roles |

### Final Checklist Before Using Wildcards

1. **✓ Identify the role**: Producer, consumer, or both?
2. **✓ Apply PECS**: Extends for producers, super for consumers
3. **✓ Consider alternatives**: Type parameters might be better
4. **✓ Document constraints**: Especially in public APIs
5. **✓ Test edge cases**: Empty collections, null values, different subtypes
6. **✓ Check performance**: For primitive types, consider arrays
7. **✓ Keep it simple**: Don't overcomplicate with nested wildcards
8. **✓ Follow conventions**: Match patterns used in Java Collections API

## 🚀 **Next Steps**

You've now mastered the art of using wildcards effectively! But to truly understand how generics work in Java, you need to know what happens under the hood. In [Chapter 16](#16-type-erasure), we'll dive deep into **Type Erasure** - how generics are really implemented in Java, why certain restrictions exist, and how to work within these constraints.

> 💡 **Final Practice Exercise**: 
> 1. Review a codebase you work with and identify 3 places where wildcards could improve flexibility
> 2. Refactor a method to follow PECS principle
> 3. Create a utility class with methods that demonstrate each wildcard pattern
> 4. Write documentation for a complex generic API using the guidelines from this chapter
> 5. Benchmark different approaches to a problem and choose the best based on clarity AND performance