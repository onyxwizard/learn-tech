# Chapter 13: 🏗️ **Wildcards and Subtyping**

## Understanding the Type Hierarchy of Wildcard Types

Wildcards introduce a new dimension to Java's type system, creating complex but predictable relationships between parameterized types. Understanding these relationships is crucial for designing flexible, type-safe APIs.

## 📊 **The Wildcard Type Hierarchy**

### Visualizing the Relationships

```java
import java.util.*;

public class WildcardHierarchy {
    
    // Basic class hierarchy for examples
    static class Animal {
        public void feed() {
            System.out.println("Feeding animal");
        }
    }
    
    static class Mammal extends Animal {
        public void walk() {
            System.out.println("Mammal walking");
        }
    }
    
    static class Dog extends Mammal {
        public void bark() {
            System.out.println("Dog barking");
        }
    }
    
    static class Cat extends Mammal {
        public void meow() {
            System.out.println("Cat meowing");
        }
    }
    
    static class Bird extends Animal {
        public void fly() {
            System.out.println("Bird flying");
        }
    }
    
    public static void main(String[] args) {
        // The hierarchy:
        // Object
        //   └── Animal
        //         ├── Mammal
        //         │     ├── Dog
        //         │     └── Cat
        //         └── Bird
        
        // Understanding wildcard subtyping:
        
        // 1. Concrete types are invariant
        List<Dog> dogs = new ArrayList<>();
        // List<Animal> animals = dogs;  // ❌ Compile error - invariance
        
        // 2. Upper bounded wildcards are covariant
        List<? extends Mammal> mammals = new ArrayList<Dog>();  // ✅ OK
        List<? extends Animal> animals = new ArrayList<Dog>();  // ✅ OK
        animals = new ArrayList<Bird>();  // ✅ OK - Bird extends Animal
        
        // 3. Lower bounded wildcards are contravariant
        List<? super Dog> dogSupers = new ArrayList<Dog>();    // ✅ OK
        dogSupers = new ArrayList<Mammal>();  // ✅ OK - Mammal is super of Dog
        dogSupers = new ArrayList<Animal>();  // ✅ OK - Animal is super of Dog
        dogSupers = new ArrayList<Object>();  // ✅ OK - Object is super of Dog
        
        // 4. Unbounded wildcards are the supertype of all List<T>
        List<?> anything = new ArrayList<Dog>();  // ✅ OK
        anything = new ArrayList<Animal>();       // ✅ OK
        anything = new ArrayList<Object>();       // ✅ OK
        anything = new ArrayList<String>();       // ✅ OK - completely different type!
    }
    
    // Visual representation of the hierarchy:
    public static void visualizeHierarchy() {
        /*
        Wildcard type hierarchy for List<T> with Animal hierarchy:
        
        List<?>                           (most general)
          ├── List<? extends Object>      (same as List<?>)
          ├── List<? extends Animal>      (covariant)
          │     ├── List<? extends Mammal>
          │     │     ├── List<? extends Dog>
          │     │     └── List<? extends Cat>
          │     └── List<? extends Bird>
          └── List<? super Animal>        (contravariant)
                ├── List<? super Mammal>
                │     ├── List<? super Dog>
                │     └── List<? super Cat>
                └── List<? super Bird>
        
        Key points:
        1. Moving DOWN the extends side makes the type MORE restrictive
        2. Moving DOWN the super side makes the type MORE permissive
        3. List<?> is the supertype of all List<T> for any T
        */
    }
}
```

## 🎭 **Wildcard Type Relationships**

### Subtyping Rules in Detail

```java
import java.util.*;

public class SubtypingRules {
    
    // Helper class hierarchy
    static class A {}
    static class B extends A {}
    static class C extends B {}
    static class D extends C {}
    
    // The subtyping rules for wildcards:
    public static void demonstrateRules() {
        // Rule 1: For any type T, List<T> is a subtype of List<?>
        List<String> strings = new ArrayList<>();
        List<?> wild = strings;  // ✅ OK
        
        // Rule 2: Covariance: If S extends T, then List<S> is a subtype of List<? extends T>
        List<C> cs = new ArrayList<>();
        List<? extends B> bs = cs;  // ✅ OK - C extends B
        List<? extends A> as = cs;  // ✅ OK - C extends A (through B)
        
        // Rule 3: Contravariance: If S extends T, then List<T> is a subtype of List<? super S>
        List<A> as2 = new ArrayList<>();
        List<? super C> cs2 = as2;  // ✅ OK - A is super of C
        
        // Rule 4: Transitivity through extends
        List<D> ds = new ArrayList<>();
        List<? extends C> cs3 = ds;  // ✅ OK - D extends C
        List<? extends B> bs3 = ds;  // ✅ OK - D extends B (through C)
        List<? extends A> as3 = ds;  // ✅ OK - D extends A (through B and C)
        
        // Rule 5: Transitivity through super
        List<Object> objs = new ArrayList<>();
        List<? super A> as4 = objs;  // ✅ OK - Object super A
        List<? super B> bs4 = objs;  // ✅ OK - Object super B
        List<? super C> cs4 = objs;  // ✅ OK - Object super C
        List<? super D> ds4 = objs;  // ✅ OK - Object super D
        
        // Rule 6: No relationship between different concrete types
        // List<B> bs5 = new ArrayList<C>();  // ❌ Compile error
        // List<C> cs5 = new ArrayList<B>();  // ❌ Compile error
        
        // Rule 7: List<? extends T> has no relationship with List<? super T>
        List<? extends B> extendsB = new ArrayList<C>();
        // List<? super B> superB = extendsB;  // ❌ Compile error
        // extendsB = superB;  // ❌ Compile error
        
        // Rule 8: List<?> is the top of the hierarchy
        List<?> top = new ArrayList<A>();
        top = new ArrayList<B>();
        top = new ArrayList<C>();
        top = new ArrayList<D>();
        top = new ArrayList<String>();  // Even completely unrelated types
        
        // But all List<T> can be assigned to List<?>
        List<A> aList = new ArrayList<>();
        List<?> wild2 = aList;  // ✅ OK
    }
    
    // Practical implications of these rules
    public static void practicalImplications() {
        // You can write methods that accept very specific or very general collections
        
        // Most restrictive: only List<C>
        acceptOnlyC(new ArrayList<C>());
        // acceptOnlyC(new ArrayList<B>());  // ❌ Not allowed
        // acceptOnlyC(new ArrayList<D>());  // ❌ Not allowed
        
        // More flexible: any List<? extends B>
        acceptExtendsB(new ArrayList<B>());
        acceptExtendsB(new ArrayList<C>());
        acceptExtendsB(new ArrayList<D>());
        // acceptExtendsB(new ArrayList<A>());  // ❌ A doesn't extend B
        
        // Most flexible for reading: any List<? extends A>
        acceptExtendsA(new ArrayList<A>());
        acceptExtendsA(new ArrayList<B>());
        acceptExtendsA(new ArrayList<C>());
        acceptExtendsA(new ArrayList<D>());
        
        // For writing: any List<? super C>
        acceptSuperC(new ArrayList<C>());
        acceptSuperC(new ArrayList<B>());
        acceptSuperC(new ArrayList<A>());
        acceptSuperC(new ArrayList<Object>());
        // acceptSuperC(new ArrayList<D>());  // ❌ D is not super of C
        
        // Most flexible overall: List<?>
        acceptAny(new ArrayList<A>());
        acceptAny(new ArrayList<B>());
        acceptAny(new ArrayList<C>());
        acceptAny(new ArrayList<D>());
        acceptAny(new ArrayList<String>());
        acceptAny(new ArrayList<Integer>());
    }
    
    // Method signatures demonstrating different wildcard bounds
    public static void acceptOnlyC(List<C> list) {
        // Can both read and write C
        list.add(new C());
        C c = list.get(0);
    }
    
    public static void acceptExtendsB(List<? extends B> list) {
        // Can read B, but cannot write
        B b = list.get(0);
        // list.add(new B());  // ❌ Compile error
        // list.add(new C());  // ❌ Compile error
    }
    
    public static void acceptExtendsA(List<? extends A> list) {
        // Can read A, but cannot write
        A a = list.get(0);
    }
    
    public static void acceptSuperC(List<? super C> list) {
        // Can write C (or subtypes), but can only read Object
        list.add(new C());
        list.add(new D());  // D is subtype of C
        Object obj = list.get(0);
        // C c = list.get(0);  // ❌ Compile error
    }
    
    public static void acceptAny(List<?> list) {
        // Can only read Object, cannot write (except null)
        Object obj = list.get(0);
        // list.add(new Object());  // ❌ Compile error
        list.add(null);  // Only null allowed
    }
}
```

## 🏗️ **Wildcard Type Hierarchies in APIs**

### How Java APIs Use Wildcard Subtyping

```java
import java.util.*;
import java.util.function.*;

public class APIWildcardHierarchies {
    
    // Example 1: Collections.sort() - using Comparator<? super T>
    public static void sortExample() {
        List<Dog> dogs = Arrays.asList(new Dog(), new Dog(), new Dog());
        
        // We can use different comparators thanks to wildcard subtyping:
        
        // 1. Comparator<Dog> - exact type
        Comparator<Dog> byBreed = Comparator.comparing(dog -> dog.breed);
        
        // 2. Comparator<Mammal> - supertype works because ? super Dog
        Comparator<Mammal> bySpecies = Comparator.comparing(mammal -> mammal.species);
        
        // 3. Comparator<Animal> - even more general supertype
        Comparator<Animal> byName = Comparator.comparing(animal -> animal.name);
        
        // 4. Comparator<Object> - most general
        Comparator<Object> byHashCode = Comparator.comparing(Object::hashCode);
        
        // All of these can be used to sort List<Dog>
        dogs.sort(byBreed);      // ✅ OK - Comparator<Dog>
        dogs.sort(bySpecies);    // ✅ OK - Comparator<Mammal> is Comparator<? super Dog>
        dogs.sort(byName);       // ✅ OK - Comparator<Animal> is Comparator<? super Dog>
        dogs.sort(byHashCode);   // ✅ OK - Comparator<Object> is Comparator<? super Dog>
        
        // Why this works: Comparator<Mammal> can compare any two Mammals.
        // Since Dogs are Mammals, it can certainly compare Dogs.
    }
    
    // Example 2: Stream.flatMap() - using Function<? super T, ? extends Stream<? extends R>>
    public static void flatMapExample() {
        List<Mammal> mammals = Arrays.asList(new Dog(), new Cat());
        
        // Function that takes Mammal and returns Stream<Animal>
        Function<Mammal, Stream<Animal>> toAnimals = m -> 
            Stream.of(m, new Bird());  // Can return mixed stream
        
        // Function that takes Animal and returns Stream<Mammal>
        Function<Animal, Stream<Mammal>> toMammals = a -> 
            a instanceof Mammal ? Stream.of((Mammal) a) : Stream.empty();
        
        // Both work because of wildcard subtyping
        List<Animal> allAnimals = mammals.stream()
            .flatMap(toAnimals)  // Function<? super Mammal, ? extends Stream<? extends Animal>>
            .toList();
            
        List<Mammal> allMammals = mammals.stream()
            .flatMap(toMammals)  // Function<? super Mammal, ? extends Stream<? extends Mammal>>
            .toList();
    }
    
    // Example 3: Optional.flatMap() - using Function<? super T, Optional<U>>
    public static void optionalExample() {
        Optional<Dog> dogOpt = Optional.of(new Dog());
        
        // Function from Dog to Optional<Mammal>
        Function<Dog, Optional<Mammal>> toMammal = dog -> Optional.of(dog);
        
        // Function from Animal to Optional<Animal>
        Function<Animal, Optional<Animal>> toAnimal = Optional::of;
        
        // Both work
        Optional<Mammal> mammalOpt = dogOpt.flatMap(toMammal);
        Optional<Animal> animalOpt = dogOpt.flatMap(toAnimal);
        
        // Why: Function<Dog, Optional<Mammal>> is a Function<? super Dog, Optional<Mammal>>
        // Because Dog is a subtype of Dog (trivially) and Optional<Mammal> is a subtype of Optional<? extends Mammal>
    }
    
    // Helper classes for examples
    static class Animal {
        String name;
        String species;
    }
    
    static class Mammal extends Animal {
        String breed;
    }
    
    static class Dog extends Mammal {
        String barkSound;
    }
    
    static class Cat extends Mammal {
        String meowSound;
    }
    
    static class Bird extends Animal {
        String chirpSound;
    }
}
```

## 🔄 **Complex Wildcard Relationships**

### Nested Wildcards and Their Interactions

```java
import java.util.*;
import java.util.function.Function;

public class NestedWildcards {
    
    // Single-level wildcards are straightforward, but nesting creates complexity
    
    public static void demonstrateNestedWildcards() {
        // 1. List of lists with wildcards
        List<List<?>> listOfLists = new ArrayList<>();
        
        // Can add different types of lists
        listOfLists.add(new ArrayList<String>());
        listOfLists.add(new ArrayList<Integer>());
        listOfLists.add(new ArrayList<Dog>());
        
        // But can't add elements to the inner lists through this reference
        // listOfLists.get(0).add("Hello");  // ❌ Compile error
        
        // 2. List of lists with upper bounds
        List<List<? extends Animal>> listOfAnimalLists = new ArrayList<>();
        
        listOfAnimalLists.add(new ArrayList<Dog>());
        listOfAnimalLists.add(new ArrayList<Cat>());
        listOfAnimalLists.add(new ArrayList<Bird>());
        
        // Still can't add to inner lists
        // listOfAnimalLists.get(0).add(new Dog());  // ❌ Compile error
        
        // 3. List of lists with lower bounds
        List<List<? super Dog>> listOfDogSuperLists = new ArrayList<>();
        
        listOfDogSuperLists.add(new ArrayList<Dog>());
        listOfDogSuperLists.add(new ArrayList<Mammal>());
        listOfDogSuperLists.add(new ArrayList<Animal>());
        listOfDogSuperLists.add(new ArrayList<Object>());
        
        // Can add Dogs to the inner lists (if we capture the type)
        addDogToList(listOfDogSuperLists.get(0), new Dog());
        
        // 4. Map with wildcard values
        Map<String, List<? extends Animal>> animalMap = new HashMap<>();
        
        animalMap.put("dogs", new ArrayList<Dog>());
        animalMap.put("cats", new ArrayList<Cat>());
        
        // Can't add to the lists in the map
        // animalMap.get("dogs").add(new Dog());  // ❌ Compile error
        
        // 5. Complex nesting: List of maps
        List<Map<String, ? extends List<? extends Animal>>> complex = new ArrayList<>();
        
        Map<String, List<Dog>> dogMap = new HashMap<>();
        dogMap.put("pets", new ArrayList<Dog>());
        
        Map<String, List<Cat>> catMap = new HashMap<>();
        catMap.put("pets", new ArrayList<Cat>());
        
        // Both can be added
        complex.add(dogMap);
        complex.add(catMap);
    }
    
    // Helper method to work around wildcard capture
    private static <T> void addDogToList(List<T> list, Dog dog) {
        // This only works if T is supertype of Dog
        // We need to check at runtime
        if (list != null) {
            // Unsafe cast, but we know Dog can be added to List<? super Dog>
            @SuppressWarnings("unchecked")
            List<Dog> dogList = (List<Dog>) list;
            dogList.add(dog);
        }
    }
    
    // Better: Use a type-safe approach
    public static void addToDogSuperList(List<? super Dog> list, Dog dog) {
        list.add(dog);  // ✅ Type-safe
    }
    
    // Real-world example: Processing nested collections
    public static int countAllAnimals(List<List<? extends Animal>> animalLists) {
        int count = 0;
        for (List<? extends Animal> animalList : animalLists) {
            count += animalList.size();
            
            // Can read animals
            for (Animal animal : animalList) {
                System.out.println("Found: " + animal.getClass().getSimpleName());
            }
        }
        return count;
    }
    
    // Processing maps with wildcard values
    public static void processAnimalMap(Map<String, ? extends List<? extends Animal>> map) {
        for (Map.Entry<String, ? extends List<? extends Animal>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<? extends Animal> animals = entry.getValue();
            
            System.out.println(key + ": " + animals.size() + " animals");
            
            for (Animal animal : animals) {
                System.out.println("  - " + animal.getClass().getSimpleName());
            }
        }
    }
    
    // Helper classes
    static class Animal {}
    static class Dog extends Animal {}
    static class Cat extends Animal {}
    static class Bird extends Animal {}
}
```

## 🧠 **Implications for Flexible API Design**

### Designing APIs with Wildcard Subtyping

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class APIDesignPrinciples {
    
    // Principle 1: Use the most general type that supports your operations
    
    // ❌ Too restrictive
    public static void processDogs(List<Dog> dogs) {
        // Can only process List<Dog>
    }
    
    // ✅ Better - can process List<Dog>, List<Labrador>, etc.
    public static void processAnyDog(List<? extends Dog> dogs) {
        for (Dog dog : dogs) {
            dog.bark();
        }
    }
    
    // ✅ Even better for utility methods
    public static <T extends Dog> void processDogsGeneric(List<T> dogs) {
        // Can still call dog-specific methods
        for (T dog : dogs) {
            dog.bark();
        }
        // Can also return T
    }
    
    // Principle 2: Use PECS (Producer Extends, Consumer Super) consistently
    
    public static class CollectionUtils {
        
        // PRODUCER: source of elements (use extends)
        public static <T> List<T> filter(List<? extends T> source, Predicate<? super T> predicate) {
            List<T> result = new ArrayList<>();
            for (T item : source) {
                if (predicate.test(item)) {
                    result.add(item);
                }
            }
            return result;
        }
        
        // CONSUMER: destination for elements (use super)
        public static <T> void addAll(List<? super T> destination, Collection<? extends T> source) {
            for (T item : source) {
                destination.add(item);
            }
        }
        
        // Both producer and consumer: use exact type
        public static <T> void replaceAll(List<T> list, UnaryOperator<T> operator) {
            for (int i = 0; i < list.size(); i++) {
                list.set(i, operator.apply(list.get(i)));
            }
        }
    }
    
    // Principle 3: Consider wildcard return types carefully
    
    // ❌ Usually bad - caller can't do much with List<?>
    public static List<?> getAnimals() {
        return Arrays.asList(new Dog(), new Cat());
    }
    
    // ✅ Better - return specific type
    public static List<Animal> getAnimalsTyped() {
        return Arrays.asList(new Dog(), new Cat());
    }
    
    // ✅ Sometimes appropriate for factory methods
    public static List<?> emptyList() {
        return Collections.emptyList();
    }
    
    // Principle 4: Use wildcards in method parameters for flexibility
    
    // Good API design example from Java Collections
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        // Implementation
    }
    
    // Another example: max with comparator
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        // Implementation
        return coll.iterator().next();
    }
    
    // Principle 5: Balance flexibility with usability
    
    // Too flexible can be confusing
    public static <T, U> void confusingMethod(
        List<? extends T> list1,
        List<? super U> list2,
        Function<? super T, ? extends U> func) {
        // Hard to understand and use
    }
    
    // Better: More specific, but still flexible
    public static <T> List<T> transform(
        List<? extends T> source,
        Function<? super T, ? extends T> transformer) {
        
        List<T> result = new ArrayList<>();
        for (T item : source) {
            result.add(transformer.apply(item));
        }
        return result;
    }
    
    // Helper classes
    static class Animal {}
    
    static class Dog extends Animal {
        void bark() {
            System.out.println("Woof!");
        }
    }
    
    static class Cat extends Animal {
        void meow() {
            System.out.println("Meow!");
        }
    }
    
    // Example usage
    public static void demonstrateGoodAPI() {
        List<Dog> dogs = Arrays.asList(new Dog(), new Dog());
        List<Animal> animals = new ArrayList<>();
        
        // Using well-designed APIs
        CollectionUtils.addAll(animals, dogs);  // ✅ Flexible
        
        List<Dog> loudDogs = CollectionUtils.filter(dogs, 
            dog -> true);  // Predicate<? super Dog>
        
        // Copy using PECS
        List<Animal> animalCopy = new ArrayList<>();
        copy(animalCopy, dogs);  // dest: ? super Dog, src: ? extends Dog
    }
}
```

## 📈 **Type Parameter vs Wildcard Trade-offs**

### When to Use Which

```java
import java.util.*;
import java.util.function.*;

public class TypeParamVsWildcard {
    
    // Case 1: When you need to refer to the type multiple times
    
    // ❌ Wildcard can't do this
    // public static void swap(List<?> list, int i, int j) {
    //     ? temp = list.get(i);  // Can't declare variable of type ?
    //     list.set(i, list.get(j));
    //     list.set(j, temp);
    // }
    
    // ✅ Type parameter can
    public static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // But can combine with wildcard
    public static void swapWithWildcard(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }
    
    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // Case 2: When you need multiple type parameters
    
    // ✅ Only type parameters can do this
    public static <K, V> Map<V, List<K>> invertMap(Map<K, V> map) {
        Map<V, List<K>> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.computeIfAbsent(entry.getValue(), k -> new ArrayList<>())
                  .add(entry.getKey());
        }
        return result;
    }
    
    // Case 3: When you need to enforce relationships between types
    
    // ✅ Type parameters can enforce relationships
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
    
    // Can also do with wildcards but more complex
    public static <T extends Comparable<? super T>> T maxWithWildcard(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
    
    // Case 4: When you need flexibility in one direction only
    
    // ✅ Wildcards are simpler for this
    public static void printAll(List<?> list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
    
    // Equivalent with type parameter (more verbose)
    public static <T> void printAllGeneric(List<T> list) {
        for (T obj : list) {
            System.out.println(obj);
        }
    }
    
    // Case 5: When designing fluent APIs
    
    // ✅ Type parameters work better for chaining
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
    
    // Case 6: When you need both reading and writing
    
    // ✅ Use type parameter for full access
    public static <T> void addAndProcess(List<T> list, T newItem, Consumer<T> processor) {
        list.add(newItem);
        processor.accept(newItem);
    }
    
    // With wildcards, you'd need separate methods
    public static void addToList(List<? super String> list, String item) {
        list.add(item);
    }
    
    public static void processList(List<? extends String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }
    
    // Summary table
    public static void summary() {
        /*
        | Requirement                    | Use Type Parameter | Use Wildcard |
        |--------------------------------|-------------------|--------------|
        | Refer to type in method body   | ✅ Yes            | ❌ No        |
        | Multiple type parameters       | ✅ Yes            | ❌ No        |
        | Type relationships/bounds      | ✅ Better         | ⚠️ Possible  |
        | Maximum flexibility            | ⚠️ Possible       | ✅ Better    |
        | Read-only operations           | ✅ Yes            | ✅ Better    |
        | Write-only operations          | ✅ Yes            | ✅ Better    |
        | Both read and write            | ✅ Yes            | ❌ No        |
        | Fluent APIs                    | ✅ Better         | ❌ No        |
        */
    }
}
```

## 🧪 **Advanced Wildcard Patterns**

### Recursive Wildcards and Self-Referential Types

```java
import java.util.*;
import java.util.function.Function;

public class RecursiveWildcards {
    
    // Pattern 1: The "self-type" pattern
    interface Builder<T extends Builder<T>> {
        T name(String name);
        T age(int age);
        Person build();
    }
    
    static class PersonBuilder implements Builder<PersonBuilder> {
        private String name;
        private int age;
        
        @Override
        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        @Override
        public PersonBuilder age(int age) {
            this.age = age;
            return this;
        }
        
        @Override
        public Person build() {
            return new Person(name, age);
        }
    }
    
    static class Person {
        final String name;
        final int age;
        
        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    
    // Pattern 2: Recursive wildcard in method chaining
    interface Processor<T> {
        <R> Processor<R> process(Function<? super T, ? extends R> transformer);
        T getResult();
    }
    
    static class SimpleProcessor<T> implements Processor<T> {
        private final T value;
        
        SimpleProcessor(T value) {
            this.value = value;
        }
        
        @Override
        public <R> Processor<R> process(Function<? super T, ? extends R> transformer) {
            R result = transformer.apply(value);
            return new SimpleProcessor<>(result);
        }
        
        @Override
        public T getResult() {
            return value;
        }
    }
    
    // Pattern 3: Wildcards with recursive type bounds
    interface TreeNode<T extends Comparable<? super T>> {
        T getValue();
        List<? extends TreeNode<T>> getChildren();
        
        default boolean contains(T value) {
            if (getValue().compareTo(value) == 0) {
                return true;
            }
            for (TreeNode<T> child : getChildren()) {
                if (child.contains(value)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    // Pattern 4: Covariant return types with wildcards
    interface AnimalFactory {
        Animal create();
    }
    
    interface DogFactory extends AnimalFactory {
        @Override
        Dog create();  // Covariant return type
        
        // Can also use wildcards
        List<? extends Dog> createPack(int size);
    }
    
    static class Animal {}
    static class Dog extends Animal {}
    
    // Pattern 5: Wildcards in nested generic types
    static class Response<T> {
        private final T data;
        private final List<? extends Throwable> warnings;
        
        Response(T data, List<? extends Throwable> warnings) {
            this.data = data;
            this.warnings = warnings;
        }
        
        public T getData() {
            return data;
        }
        
        public List<? extends Throwable> getWarnings() {
            return warnings;
        }
        
        // Map with wildcard preservation
        public <R> Response<R> map(Function<? super T, ? extends R> mapper) {
            return new Response<>(mapper.apply(data), warnings);
        }
    }
    
    public static void main(String[] args) {
        // Test Pattern 1
        Person person = new PersonBuilder()
            .name("Alice")
            .age(30)
            .build();
        
        // Test Pattern 2
        Processor<String> processor = new SimpleProcessor<>("hello");
        Integer length = processor
            .process(String::length)      // String -> Integer
            .process(i -> i * 2)          // Integer -> Integer
            .getResult();
        
        System.out.println("Processed length: " + length);
        
        // Test Pattern 5
        Response<String> response = new Response<>("data", 
            Arrays.asList(new RuntimeException("Warning 1")));
        
        Response<Integer> mapped = response.map(String::length);
        System.out.println("Mapped response data: " + mapped.getData());
    }
}
```

## 📐 **Testing Wildcard Subtyping**

### Verifying Type Relationships

```java
import java.util.*;
import java.lang.reflect.*;

public class TestingWildcards {
    
    // Utility to test type relationships
    public static void testAssignability() {
        // Test 1: Basic invariance
        test("List<String> to List<Object>", 
            List.class, String.class, 
            List.class, Object.class, 
            false);
        
        // Test 2: Covariance with extends
        test("List<String> to List<? extends CharSequence>",
            List.class, String.class,
            List.class, wildcardExtends(CharSequence.class),
            true);
        
        // Test 3: Contravariance with super
        test("List<Object> to List<? super String>",
            List.class, Object.class,
            List.class, wildcardSuper(String.class),
            true);
        
        // Test 4: Unbounded wildcard
        test("List<String> to List<?>",
            List.class, String.class,
            List.class, wildcardUnbounded(),
            true);
        
        // Test 5: Nested wildcards
        test("List<List<String>> to List<List<?>>",
            List.class, List.class, String.class,
            List.class, List.class, wildcardUnbounded(),
            false);  // Actually true in practice, but hard to test with reflection
    }
    
    // Helper methods for creating wildcard types
    private static Type wildcardExtends(Type bound) {
        return new WildcardTypeImpl(new Type[]{bound}, new Type[0]);
    }
    
    private static Type wildcardSuper(Type bound) {
        return new WildcardTypeImpl(new Type[0], new Type[]{bound});
    }
    
    private static Type wildcardUnbounded() {
        return new WildcardTypeImpl(new Type[0], new Type[]{Object.class});
    }
    
    // Simple test method
    private static void test(String description, 
                           Class<?> fromRaw, Type fromType,
                           Class<?> toRaw, Type toType,
                           boolean expected) {
        
        boolean result = isAssignable(fromRaw, fromType, toRaw, toType);
        System.out.printf("%-50s: %s (expected: %s)%n", 
            description, result ? "PASS" : "FAIL", expected ? "PASS" : "FAIL");
    }
    
    // Overload for nested generics
    private static void test(String description,
                           Class<?> fromRaw1, Class<?> fromRaw2, Type fromInner,
                           Class<?> toRaw1, Class<?> toRaw2, Type toInner,
                           boolean expected) {
        
        // Simplified test - in reality would need proper type construction
        System.out.printf("%-50s: SKIPPED (complex nested)%n", description);
    }
    
    // Simple assignability check
    private static boolean isAssignable(Class<?> fromRaw, Type fromType,
                                       Class<?> toRaw, Type toType) {
        // This is a simplification - real implementation would use TypeToken or similar
        return toRaw.isAssignableFrom(fromRaw);
    }
    
    // Custom WildcardType implementation for testing
    static class WildcardTypeImpl implements WildcardType {
        private final Type[] upperBounds;
        private final Type[] lowerBounds;
        
        WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            this.upperBounds = upperBounds;
            this.lowerBounds = lowerBounds;
        }
        
        @Override public Type[] getUpperBounds() { return upperBounds; }
        @Override public Type[] getLowerBounds() { return lowerBounds; }
        @Override public String toString() {
            if (upperBounds.length > 0 && upperBounds[0] == Object.class && lowerBounds.length == 0) {
                return "?";
            } else if (upperBounds.length > 0) {
                return "? extends " + upperBounds[0].getTypeName();
            } else {
                return "? super " + lowerBounds[0].getTypeName();
            }
        }
    }
    
    // Practical testing without reflection
    public static void practicalTests() {
        System.out.println("\n=== Practical Type Relationship Tests ===");
        
        // These compile-time tests verify the relationships
        List<String> strings = new ArrayList<>();
        List<? extends CharSequence> charSequences = strings;  // ✅ Should compile
        
        List<Object> objects = new ArrayList<>();
        List<? super String> stringSupers = objects;  // ✅ Should compile
        
        // Uncomment to see compile errors
        // List<Object> objects2 = strings;  // ❌ Should not compile
        // List<String> strings2 = objects;  // ❌ Should not compile
        
        System.out.println("All practical tests passed at compile time");
    }
    
    public static void main(String[] args) {
        testAssignability();
        practicalTests();
    }
}
```

## 🎓 **Key Takeaways**

1. **Wildcards create a type hierarchy** independent of the concrete type hierarchy
2. **`List<?>` is the supertype** of all `List<T>` for any `T`
3. **Covariance (`? extends T`)** allows reading but not writing
4. **Contravariance (`? super T`)** allows writing but limited reading
5. **No relationship between `? extends T` and `? super T`** - they're different "directions"
6. **Use wildcards for maximum flexibility** in API parameters
7. **Use type parameters when you need to refer to the type** in the method body
8. **Nested wildcards are complex** but follow the same rules
9. **Test wildcard relationships** both at compile time and with reflection if needed
10. **Balance flexibility with usability** - overly complex wildcards can make APIs hard to use

## 🚀 **Next Steps**

Understanding wildcard subtyping is crucial, but sometimes you need to work around their limitations. In [Chapter 14](#14-wildcard-capture--helper-methods), we'll explore **Wildcard Capture & Helper Methods** - techniques for capturing the unknown type in a generic method and patterns for working around wildcard limitations.

> 💡 **Practice Exercise**: 
> 1. Create a class hierarchy with at least 4 levels and demonstrate all wildcard subtyping relationships
> 2. Design a fluent API using recursive type bounds with wildcards
> 3. Create a method that takes `Map<K, List<? extends V>>` and returns `Map<V, List<? extends K>>`
> 4. Test the assignability of different wildcard types using reflection
> 5. Implement a generic graph traversal algorithm that works with any `Node<T extends Comparable<? super T>>`