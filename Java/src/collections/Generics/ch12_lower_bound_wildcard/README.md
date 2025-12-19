# Chapter 12: 🔽 **Lower Bounded Wildcards**

## The "Consumer" Wildcard: Writing with Type Safety

Lower bounded wildcards (`? super T`) are the complement to upper bounded wildcards. While `? extends T` is for reading (producers), `? super T` is for writing (consumers). They allow you to add elements of type `T` (or its subtypes) to a collection when you don't know the exact type, but know it's at least a supertype of `T`.

## 📉 **Understanding `? super T`**

### Basic Concept and Usage

```java
import java.util.*;

public class LowerBoundedBasics {
    
    // Method that can add integers to any list that can hold integers
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        
        // Can also add subtypes of Integer
        list.add(Integer.valueOf(4));  // Same as above
    }
    
    // Method that can add strings to any list that can hold strings
    public static void addStrings(List<? super String> list) {
        list.add("Hello");
        list.add("World");
        
        // Can add null (as with any collection)
        list.add(null);
    }
    
    // Without lower bound - less flexible
    public static void addNumbersExact(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
    
    public static void main(String[] args) {
        // Different lists that can hold integers
        List<Integer> integers = new ArrayList<>();
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        List<Serializable> serializables = new ArrayList<>();
        
        // ✅ All of these work with lower bounded wildcard
        addNumbers(integers);      // Integer is super of Integer
        addNumbers(numbers);       // Number is super of Integer
        addNumbers(objects);       // Object is super of Integer
        addNumbers(serializables); // Serializable is super of Integer
        
        System.out.println("Integers: " + integers);  // [1, 2, 3]
        System.out.println("Numbers: " + numbers);    // [1, 2, 3]
        System.out.println("Objects: " + objects);    // [1, 2, 3]
        
        // ❌ Does NOT work without lower bound
        // addNumbersExact(numbers);  // Compile error!
        // addNumbersExact(objects);  // Compile error!
        
        // Only works with exact List<Integer>
        addNumbersExact(integers);  // OK
        System.out.println("Integers after exact: " + integers);  // [1, 2, 3, 1, 2, 3]
        
        // Demonstration with strings
        List<String> strings = new ArrayList<>();
        List<Object> objects2 = new ArrayList<>();
        List<CharSequence> charSequences = new ArrayList<>();
        
        addStrings(strings);        // String is super of String
        addStrings(objects2);       // Object is super of String
        addStrings(charSequences);  // CharSequence is super of String
        
        System.out.println("Strings: " + strings);          // [Hello, World, null]
        System.out.println("Objects: " + objects2);         // [Hello, World, null]
        System.out.println("CharSequences: " + charSequences); // [Hello, World, null]
    }
}
```

## ✍️ **Writing to Generic Structures**

### Safe Addition with Lower Bounds

```java
import java.util.*;
import java.time.LocalDate;

public class SafeWriting {
    
    static class Employee {
        private String name;
        private double salary;
        
        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }
        
        public String getName() { return name; }
        public double getSalary() { return salary; }
        
        @Override
        public String toString() {
            return name + " ($" + salary + ")";
        }
    }
    
    static class Manager extends Employee {
        private String department;
        
        public Manager(String name, double salary, String department) {
            super(name, salary);
            this.department = department;
        }
        
        public String getDepartment() { return department; }
        
        @Override
        public String toString() {
            return super.toString() + " [Dept: " + department + "]";
        }
    }
    
    static class Executive extends Manager {
        private int stockOptions;
        
        public Executive(String name, double salary, String department, int stockOptions) {
            super(name, salary, department);
            this.stockOptions = stockOptions;
        }
        
        public int getStockOptions() { return stockOptions; }
        
        @Override
        public String toString() {
            return super.toString() + " [Options: " + stockOptions + "]";
        }
    }
    
    // Can add managers to any collection that can hold managers or supertypes
    public static void addManagers(Collection<? super Manager> collection) {
        collection.add(new Manager("Alice", 80000, "Engineering"));
        collection.add(new Manager("Bob", 90000, "Sales"));
        
        // Can also add subtypes of Manager
        collection.add(new Executive("Carol", 150000, "Executive", 10000));
        
        // Cannot add Employee (supertype of Manager) - compile error!
        // collection.add(new Employee("Dave", 50000));
        
        // Cannot add arbitrary objects
        // collection.add("Not an employee");  // Compile error
        
        // Can add null
        collection.add(null);
    }
    
    // Generic method alternative
    public static <T> void addSpecific(Collection<T> collection, T element) {
        collection.add(element);
    }
    
    // Fill a list with default values
    public static <T> void fill(List<? super T> list, T value, int count) {
        for (int i = 0; i < count; i++) {
            list.add(value);
        }
    }
    
    // Copy elements from source to destination
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T element : src) {
            dest.add(element);
        }
    }
    
    public static void main(String[] args) {
        // Test with different collection types
        List<Employee> employees = new ArrayList<>();
        List<Manager> managers = new ArrayList<>();
        List<Executive> executives = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        
        // All of these work
        addManagers(employees);   // Employee is super of Manager
        addManagers(managers);    // Manager is super of Manager
        addManagers(objects);     // Object is super of Manager
        
        // ❌ This doesn't work
        // addManagers(executives);  // Executive is NOT super of Manager
        
        System.out.println("Employees: " + employees);
        System.out.println("Managers: " + managers);
        System.out.println("Objects: " + objects);
        
        // Using fill method
        List<Number> numbers = new ArrayList<>();
        fill(numbers, 42, 3);     // Integer autoboxed to Number
        fill(numbers, 3.14, 2);   // Double autoboxed to Number
        System.out.println("Filled numbers: " + numbers);
        
        // Using copy method (PECS in action!)
        List<Manager> sourceManagers = Arrays.asList(
            new Manager("X", 70000, "IT"),
            new Manager("Y", 75000, "HR")
        );
        
        List<Employee> destEmployees = new ArrayList<>();
        copy(destEmployees, sourceManagers);  // dest: ? super Manager, src: ? extends Manager
        System.out.println("Copied employees: " + destEmployees);
    }
}
```

## 🧱 **Foundation of the PECS Principle**

### Understanding Producer Extends, Consumer Super

```java
import java.util.*;
import java.util.function.*;

public class PECSPrinciple {
    
    // PECS: Producer Extends, Consumer Super
    // - Use ? extends T when you get/read/produce values (PRODUCER)
    // - Use ? super T when you put/write/consume values (CONSUMER)
    
    // Classic example: Collections.copy()
    // Destination consumes elements (write) -> ? super T
    // Source produces elements (read) -> ? extends T
    public static <T> void pecsCopy(List<? super T> dest, List<? extends T> src) {
        for (T element : src) {
            dest.add(element);
        }
    }
    
    // Another example: For-each loop
    // The collection produces elements -> ? extends T
    public static <T> void processElements(Collection<? extends T> producer) {
        for (T element : producer) {
            System.out.println(element);
        }
    }
    
    // Consumer example: Adding elements
    public static <T> void addElements(Collection<? super T> consumer, T... elements) {
        for (T element : elements) {
            consumer.add(element);
        }
    }
    
    // Real-world example: Comparator
    // Comparator consumes two objects to compare them -> ? super T
    public static <T> T max(Collection<? extends T> collection, Comparator<? super T> comparator) {
        if (collection.isEmpty()) {
            throw new NoSuchElementException();
        }
        
        Iterator<? extends T> iterator = collection.iterator();
        T max = iterator.next();
        
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (comparator.compare(current, max) > 0) {
                max = current;
            }
        }
        
        return max;
    }
    
    // Another real example: Predicate
    // Predicate consumes an object to test it -> ? super T
    public static <T> List<T> filter(Collection<? extends T> collection, 
                                    Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T element : collection) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }
    
    static class Animal {
        String name;
        
        Animal(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    static class Dog extends Animal {
        String breed;
        
        Dog(String name, String breed) {
            super(name);
            this.breed = breed;
        }
    }
    
    static class Cat extends Animal {
        String color;
        
        Cat(String name, String color) {
            super(name);
            this.color = color;
        }
    }
    
    public static void main(String[] args) {
        // Example 1: Copy with PECS
        List<Dog> dogs = Arrays.asList(new Dog("Buddy", "Golden"), new Dog("Max", "Labrador"));
        List<Animal> animals = new ArrayList<>();
        
        pecsCopy(animals, dogs);  // Works perfectly!
        System.out.println("Animals after copy: " + animals);
        
        // Example 2: Max with comparator
        List<Integer> numbers = Arrays.asList(1, 5, 3, 9, 2);
        
        // Comparator<Number> works for Integer because Number is super of Integer
        Comparator<Number> numberComparator = Comparator.comparingDouble(Number::doubleValue);
        Integer maxNumber = max(numbers, numberComparator);
        System.out.println("Max number: " + maxNumber);
        
        // Example 3: Filter with predicate
        List<Dog> dogList = Arrays.asList(
            new Dog("Rex", "German Shepherd"),
            new Dog("Bella", "Beagle"),
            new Dog("Charlie", "Poodle")
        );
        
        // Predicate<Animal> works for Dog because Animal is super of Dog
        Predicate<Animal> nameStartsWithB = animal -> animal.name.startsWith("B");
        List<Dog> filteredDogs = filter(dogList, nameStartsWithB);
        System.out.println("Dogs with names starting with B: " + filteredDogs);
        
        // Example 4: Processing elements
        List<Cat> cats = Arrays.asList(new Cat("Whiskers", "Gray"), new Cat("Mittens", "White"));
        processElements(cats);  // Producer: ? extends Animal
        
        // Example 5: Adding elements
        List<Animal> animalList = new ArrayList<>();
        addElements(animalList, new Dog("Rover", "Collie"), new Cat("Felix", "Black"));
        System.out.println("Animal list: " + animalList);
    }
}
```

## 🏗️ **Advanced Patterns**

### 1. **Builder Pattern with Lower Bounds**

```java
import java.util.*;
import java.util.function.Consumer;

public class BuilderPatternWithBounds {
    
    // Generic event builder
    static class EventBuilder<T> {
        private final List<Consumer<? super T>> handlers = new ArrayList<>();
        private final Class<T> eventType;
        
        public EventBuilder(Class<T> eventType) {
            this.eventType = eventType;
        }
        
        // Add handler for this event type or any supertype
        public EventBuilder<T> addHandler(Consumer<? super T> handler) {
            handlers.add(handler);
            return this;
        }
        
        // Build an event dispatcher
        public EventDispatcher<T> build() {
            return new EventDispatcher<>(eventType, handlers);
        }
    }
    
    // Event dispatcher that can handle events and their subtypes
    static class EventDispatcher<T> {
        private final Class<T> eventType;
        private final List<Consumer<? super T>> handlers;
        
        public EventDispatcher(Class<T> eventType, List<Consumer<? super T>> handlers) {
            this.eventType = eventType;
            this.handlers = Collections.unmodifiableList(new ArrayList<>(handlers));
        }
        
        // Dispatch an event (can be T or any subtype)
        public void dispatch(T event) {
            for (Consumer<? super T> handler : handlers) {
                handler.accept(event);
            }
        }
        
        // Get all handlers
        public List<Consumer<? super T>> getHandlers() {
            return handlers;
        }
    }
    
    // Example usage
    static class BaseEvent {
        private final String source;
        
        public BaseEvent(String source) {
            this.source = source;
        }
        
        public String getSource() {
            return source;
        }
    }
    
    static class UserEvent extends BaseEvent {
        private final String username;
        
        public UserEvent(String source, String username) {
            super(source);
            this.username = username;
        }
        
        public String getUsername() {
            return username;
        }
    }
    
    static class LoginEvent extends UserEvent {
        private final LocalDateTime timestamp;
        
        public LoginEvent(String source, String username) {
            super(source, username);
            this.timestamp = LocalDateTime.now();
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
    
    public static void main(String[] args) {
        // Build a dispatcher for UserEvent
        EventBuilder<UserEvent> builder = new EventBuilder<>(UserEvent.class);
        
        // Add handlers that accept UserEvent or supertypes
        builder.addHandler((BaseEvent event) -> {
            System.out.println("BaseEvent handler: " + event.getSource());
        });
        
        builder.addHandler((UserEvent event) -> {
            System.out.println("UserEvent handler: " + event.getUsername());
        });
        
        // Can also add handler for Object (super of everything)
        builder.addHandler((Object event) -> {
            System.out.println("Object handler: " + event.getClass().getSimpleName());
        });
        
        EventDispatcher<UserEvent> dispatcher = builder.build();
        
        // Dispatch different event types
        dispatcher.dispatch(new UserEvent("AuthService", "john_doe"));
        dispatcher.dispatch(new LoginEvent("AuthService", "jane_doe"));
        
        // The dispatcher can also handle subtypes of UserEvent
        System.out.println("Total handlers: " + dispatcher.getHandlers().size());
    }
}
```

### 2. **Pipeline Processing Pattern**

```java
import java.util.*;
import java.util.function.Function;

public class PipelineProcessing {
    
    // Processor that can consume and produce values
    interface Processor<T, R> {
        R process(T input);
    }
    
    // Pipeline builder that uses lower bounds for flexibility
    static class Pipeline<T> {
        private final List<Processor<?, ?>> processors = new ArrayList<>();
        private final Class<T> inputType;
        
        public Pipeline(Class<T> inputType) {
            this.inputType = inputType;
        }
        
        // Add a processor that accepts T or supertype, produces R
        public <U, R> Pipeline<R> addProcessor(Processor<? super U, ? extends R> processor) {
            @SuppressWarnings("unchecked")
            Pipeline<R> newPipeline = (Pipeline<R>) this;
            newPipeline.processors.add(processor);
            return newPipeline;
        }
        
        // Execute pipeline
        public <R> R execute(T input, Class<R> outputType) {
            Object current = input;
            
            for (Processor<?, ?> processor : processors) {
                // This requires careful casting in real implementation
                current = processItem(current, processor);
            }
            
            @SuppressWarnings("unchecked")
            R result = (R) current;
            return result;
        }
        
        private Object processItem(Object input, Processor<?, ?> processor) {
            // In real implementation, you'd need type checking
            return processor.process(input);
        }
    }
    
    // Example processors
    static class StringLengthProcessor implements Processor<CharSequence, Integer> {
        @Override
        public Integer process(CharSequence input) {
            return input.length();
        }
    }
    
    static class DoubleStringProcessor implements Processor<Object, String> {
        @Override
        public String process(Object input) {
            return input.toString() + input.toString();
        }
    }
    
    static class UppercaseProcessor implements Processor<String, String> {
        @Override
        public String process(String input) {
            return input.toUpperCase();
        }
    }
    
    public static void main(String[] args) {
        // Build a pipeline that processes strings
        Pipeline<String> pipeline = new Pipeline<>(String.class);
        
        // Add processors with different bounds
        pipeline.addProcessor(new UppercaseProcessor())          // String -> String
                .addProcessor(new StringLengthProcessor())       // CharSequence -> Integer
                .addProcessor(new DoubleStringProcessor());      // Object -> String
        
        String result = pipeline.execute("hello", String.class);
        System.out.println("Pipeline result: " + result);  // "1010"
    }
}
```

## ⚠️ **Common Pitfalls and Solutions**

### 1. **Reading from Lower Bounded Collections**

```java
import java.util.*;

public class ReadingFromLowerBounds {
    
    public static void demonstrate() {
        List<? super Integer> numbers = new ArrayList<Number>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        
        // ❌ Cannot read as Integer
        // Integer first = numbers.get(0);  // Compile error
        
        // ✅ Can only read as Object
        Object first = numbers.get(0);
        System.out.println("First element (as Object): " + first);
        
        // ❌ Cannot iterate as Integer
        // for (Integer num : numbers) { }  // Compile error
        
        // ✅ Can iterate as Object
        for (Object obj : numbers) {
            System.out.println("Element: " + obj);
        }
        
        // Workaround: Copy to a typed list
        List<Integer> integers = new ArrayList<>();
        for (Object obj : numbers) {
            if (obj instanceof Integer) {
                integers.add((Integer) obj);
            }
        }
        System.out.println("Typed list: " + integers);
        
        // Better: Use bounded wildcard for reading
        List<? extends Number> readableNumbers = Arrays.asList(1, 2, 3);
        Number firstNumber = readableNumbers.get(0);  // OK
        for (Number num : readableNumbers) {          // OK
            System.out.println("Number: " + num);
        }
    }
    
    // Pattern: Use two different views for reading and writing
    public static void separateViews() {
        // Original list
        List<Number> numbers = new ArrayList<>();
        
        // Writing view (consumer)
        List<? super Integer> writingView = numbers;
        writingView.add(1);
        writingView.add(2);
        writingView.add(3);
        
        // Reading view (producer)
        List<? extends Number> readingView = numbers;
        System.out.println("First element: " + readingView.get(0));
        
        // Original list can do both
        numbers.add(4);                      // Write
        Number third = numbers.get(2);       // Read
        System.out.println("Third: " + third);
    }
    
    public static void main(String[] args) {
        demonstrate();
        separateViews();
    }
}
```

### 2. **Type Safety with Lower Bounds**

```java
import java.util.*;

public class TypeSafety {
    
    public static void demonstrateSafety() {
        List<Object> objects = new ArrayList<>();
        List<? super String> strings = objects;
        
        // Can add strings
        strings.add("Hello");
        strings.add("World");
        
        // Can add null
        strings.add(null);
        
        // ❌ Cannot add other types
        // strings.add(123);  // Compile error
        // strings.add(new Object());  // Compile error
        
        // But through the original reference, we can add anything
        objects.add(123);
        objects.add(new Date());
        
        // This shows the list contains mixed types
        System.out.println("Mixed list: " + objects);
        
        // When reading from strings view, we get Object
        for (Object obj : strings) {
            System.out.println("Element: " + obj + " (" + 
                             (obj != null ? obj.getClass().getSimpleName() : "null") + ")");
        }
    }
    
    // Safe pattern: Use private field with controlled access
    static class SafeContainer<T> {
        private List<? super T> storage = new ArrayList<>();
        
        public void add(T item) {
            // Safe cast because we control the access
            @SuppressWarnings("unchecked")
            List<T> typedStorage = (List<T>) storage;
            typedStorage.add(item);
        }
        
        @SuppressWarnings("unchecked")
        public T get(int index) {
            // This is safe if we only add T through our API
            return (T) storage.get(index);
        }
        
        public int size() {
            return storage.size();
        }
    }
    
    public static void main(String[] args) {
        demonstrateSafety();
        
        SafeContainer<String> container = new SafeContainer<>();
        container.add("Hello");
        container.add("World");
        
        System.out.println("First: " + container.get(0));
        System.out.println("Size: " + container.size());
    }
}
```

## 📊 **Real-World Examples from Java API**

### Java Collections Framework Usage

```java
import java.util.*;
import java.util.function.*;

public class JavaAPIExamples {
    
    public static void showExamples() {
        List<String> strings = new ArrayList<>();
        
        // 1. Collections.addAll()
        // Signature: public static <T> boolean addAll(Collection<? super T> c, T... elements)
        Collections.addAll(strings, "A", "B", "C");
        System.out.println("After addAll: " + strings);
        
        // 2. List.replaceAll()
        // Signature: default void replaceAll(UnaryOperator<E> operator)
        // But UnaryOperator extends Function<? super T, ? extends T>
        strings.replaceAll(String::toUpperCase);
        System.out.println("After replaceAll: " + strings);
        
        // 3. List.sort()
        // Signature: default void sort(Comparator<? super E> c)
        strings.sort(Comparator.naturalOrder());
        System.out.println("After sort: " + strings);
        
        // 4. Collection.removeIf()
        // Signature: default boolean removeIf(Predicate<? super E> filter)
        strings.removeIf(s -> s.length() > 1);  // Won't remove anything here
        System.out.println("After removeIf: " + strings);
        
        // 5. Collection.forEach()
        // Signature: default void forEach(Consumer<? super T> action)
        strings.forEach(System.out::println);
        
        // 6. Stream.collect()
        // Signature: <R> R collect(Supplier<R> supplier,
        //               BiConsumer<R, ? super T> accumulator,
        //               BiConsumer<R, R> combiner)
        List<String> collected = strings.stream()
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println("Collected: " + collected);
    }
    
    // Understanding Comparator<? super T>
    public static void comparatorExample() {
        List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
        
        // Comparator<Number> works for Integer
        Comparator<Number> numberComparator = (a, b) -> 
            Double.compare(a.doubleValue(), b.doubleValue());
        
        // This works because Comparator<Number> is a Comparator<? super Integer>
        numbers.sort(numberComparator);
        System.out.println("Sorted numbers: " + numbers);
        
        // Also works with Comparator<Object>
        Comparator<Object> hashComparator = Comparator.comparing(Object::hashCode);
        numbers.sort(hashComparator);
        System.out.println("Sorted by hash: " + numbers);
    }
    
    // Understanding Predicate<? super T>
    public static void predicateExample() {
        List<String> strings = Arrays.asList("Apple", "Banana", "Cherry", "Date");
        
        // Predicate<CharSequence> works for String
        Predicate<CharSequence> startsWithA = cs -> cs.length() > 0 && cs.charAt(0) == 'A';
        
        long count = strings.stream()
            .filter(startsWithA)  // Predicate<? super String>
            .count();
        
        System.out.println("Strings starting with A: " + count);
    }
    
    public static void main(String[] args) {
        showExamples();
        comparatorExample();
        predicateExample();
    }
}
```

## 🎓 **Best Practices for Lower Bounded Wildcards**

### When to Use and How to Use Effectively

```java
import java.util.*;
import java.util.function.*;

public class LowerBoundBestPractices {
    
    // ✅ GOOD: Use lower bounded wildcards when:
    
    // 1. Writing to a collection (consumer)
    public static <T> void addAll(Collection<? super T> target, T... elements) {
        for (T element : elements) {
            target.add(element);
        }
    }
    
    // 2. Accepting functional interfaces that consume values
    public static <T> void forEach(Collection<? extends T> source, 
                                  Consumer<? super T> action) {
        for (T element : source) {
            action.accept(element);
        }
    }
    
    // 3. Implementing generic algorithms that modify collections
    public static <T> void fill(List<? super T> list, T value, int n) {
        for (int i = 0; i < n; i++) {
            list.add(value);
        }
    }
    
    // 4. Working with Comparators, Predicates, Consumers, etc.
    public static <T> T max(Collection<? extends T> collection, 
                           Comparator<? super T> comparator) {
        // Implementation
        return collection.iterator().next();
    }
    
    // ❌ AVOID: Using lower bounded wildcards when:
    
    // 1. You need to read specific types from the collection
    // public static <T> T getFirst(List<? super T> list) {
    //     return list.get(0);  // ❌ Compile error - returns Object
    // }
    
    // Better: Use upper bounded wildcard for reading
    public static <T> T getFirst(List<? extends T> list) {
        return list.get(0);  // ✅ OK
    }
    
    // 2. You need both reading and writing (use exact type instead)
    public static <T> void processList(List<T> list, T newElement) {
        // Can both read and write
        T first = list.get(0);      // Read
        list.add(newElement);       // Write
    }
    
    // 3. In return types (usually confusing)
    // public static List<? super Integer> createList() {
    //     return new ArrayList<Number>();  // Caller can only write Integers
    // }
    
    // Better: Return concrete type or use type parameter
    public static <T> List<T> createList(Class<T> type) {
        return new ArrayList<>();
    }
    
    // Design pattern: Separate interfaces for reading and writing
    interface ReadableList<T> {
        T get(int index);
        int size();
        // Other read operations
    }
    
    interface WritableList<T> {
        void add(T element);
        void set(int index, T element);
        // Other write operations
    }
    
    // Full list implements both
    interface FullList<T> extends ReadableList<T>, WritableList<T> {
        // Combined operations
    }
    
    public static void main(String[] args) {
        // Demonstrate good practices
        List<Number> numbers = new ArrayList<>();
        addAll(numbers, 1, 2, 3, 4.5, 5L);
        System.out.println("Numbers: " + numbers);
        
        List<String> strings = Arrays.asList("A", "B", "C");
        forEach(strings, System.out::println);
        
        List<Object> objects = new ArrayList<>();
        fill(objects, "Hello", 3);
        System.out.println("Filled objects: " + objects);
        
        // Show alternatives
        List<Integer> ints = Arrays.asList(1, 2, 3);
        Integer first = getFirst(ints);
        System.out.println("First: " + first);
    }
}
```

## 📈 **Performance Considerations**

### Boxing vs Primitive Specialization with Lower Bounds

```java
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceWithLowerBounds {
    
    // Using lower bounds with boxed types
    public static void addIntegers(List<? super Integer> list, int count) {
        for (int i = 0; i < count; i++) {
            list.add(i);  // Autoboxing occurs here
        }
    }
    
    // Specialized version for performance
    public static void addIntsToIntList(List<Integer> list, int count) {
        for (int i = 0; i < count; i++) {
            list.add(i);  // Still autoboxing, but list is specifically Integer
        }
    }
    
    // Even better: Use primitive array if possible
    public static int[] createIntArray(int count) {
        int[] array = new int[count];
        for (int i = 0; i < count; i++) {
            array[i] = i;  // No boxing
        }
        return array;
    }
    
    // Modern approach: Use streams
    public static List<Integer> createIntListWithStream(int count) {
        return java.util.stream.IntStream.range(0, count)
            .boxed()  // Boxing happens here
            .collect(Collectors.toList());
    }
    
    public static void benchmark() {
        int count = 1000000;
        
        // Test 1: Lower bound wildcard
        List<Number> list1 = new ArrayList<>();
        long start = System.nanoTime();
        addIntegers(list1, count);
        long time1 = System.nanoTime() - start;
        
        // Test 2: Specific type
        List<Integer> list2 = new ArrayList<>();
        start = System.nanoTime();
        addIntsToIntList(list2, count);
        long time2 = System.nanoTime() - start;
        
        // Test 3: Primitive array
        start = System.nanoTime();
        int[] array = createIntArray(count);
        long time3 = System.nanoTime() - start;
        
        // Test 4: Stream
        start = System.nanoTime();
        List<Integer> list4 = createIntListWithStream(count);
        long time4 = System.nanoTime() - start;
        
        System.out.println("Lower bound wildcard: " + time1 + " ns");
        System.out.println("Specific type: " + time2 + " ns");
        System.out.println("Primitive array: " + time3 + " ns");
        System.out.println("Stream: " + time4 + " ns");
        
        // Note: Performance differences are often due to:
        // 1. Boxing/unboxing overhead
        // 2. List vs array access
        // 3. Stream overhead
        // Lower bounds themselves don't add runtime overhead
    }
    
    public static void main(String[] args) {
        benchmark();
    }
}
```

## 📊 **Summary Table: Lower Bounded Wildcard Capabilities**

| Operation | Allowed with `List<? super T>` | Notes |
|-----------|--------------------------------|-------|
| **Write T** | ✅ Yes | `list.add(t)` where `t` is `T` |
| **Write subtype of T** | ✅ Yes | `list.add(subtype)` |
| **Write null** | ✅ Yes | `list.add(null)` |
| **Write supertype of T** | ❌ No | Cannot add `Object` if `T` is `String` |
| **Read as Object** | ✅ Yes | `Object obj = list.get(0)` |
| **Read as T** | ❌ No | `T t = list.get(0)` doesn't compile |
| **Iterate as Object** | ✅ Yes | `for (Object obj : list)` |
| **Iterate as T** | ❌ No | `for (T t : list)` doesn't compile |
| **Size/Empty** | ✅ Yes | `list.size()`, `list.isEmpty()` |
| **Clear/Remove** | ✅ Yes | `list.clear()`, `list.remove(obj)` |
| **Contains** | ✅ Yes | `list.contains(obj)` takes `Object` |

## 🚀 **Next Steps**

Now that you understand both upper and lower bounded wildcards, it's time to see how they relate to each other in type hierarchies. In [Chapter 13](#13-wildcards-and-subtyping), we'll explore **Wildcards and Subtyping** - understanding the relationships between different wildcard types and how they form a hierarchy of their own.

> 💡 **Practice Exercise**: 
> 1. Create a `CollectionUtils` class with methods that use lower bounded wildcards
> 2. Implement a `copy` method that copies from a producer list to a consumer list
> 3. Create a `fill` method that fills a list with a value n times
> 4. Write a `max` method that takes a comparator with lower bound
> 5. Implement a generic event system where handlers can accept events or supertypes
> 6. Benchmark different approaches to adding primitive values to collections