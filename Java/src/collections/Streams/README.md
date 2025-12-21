# Java Collections and Streams - Complete Guide & Cheatsheet

## 📋 Overview
**Java Streams** (Java 8+) provide a **declarative, functional approach** to processing collections of data. Streams enable parallel execution, lazy evaluation, and more readable code for data transformation, filtering, and aggregation. This guide covers Streams in the context of Java Collections.


## 🔑 Core Concepts

### ✅ **Stream Characteristics**
- **Not a data structure**: Doesn't store data, operates on source data structures
- **Functional in nature**: Doesn't modify source data, produces new results
- **Lazy evaluation**: Intermediate operations are only executed when terminal operation is invoked
- **Parallelizable**: Can process data in parallel with minimal code changes

### ✅ **Stream vs Collection Comparison**
| Feature | Stream | Collection |
|---------|--------|------------|
| **Data Storage** | ❌ No (operates on source) | ✅ Yes (stores elements) |
| **Traversal** | Once only (consumable) | Multiple times |
| **External vs Internal Iteration** | Internal (declarative) | External (imperative) |
| **Lazy Evaluation** | ✅ Yes | ❌ No |
| **Parallel Processing** | Built-in (`parallelStream()`) | Manual threading required |
| **Data Modification** | ❌ No (produces new stream) | ✅ Yes (can modify) |

### ✅ **Stream Pipeline Components**
1. **Source**: Collection, array, I/O channel, generator function
2. **Intermediate Operations**: `filter()`, `map()`, `sorted()`, `distinct()`, `limit()`
3. **Terminal Operations**: `collect()`, `forEach()`, `reduce()`, `count()`, `findFirst()`

---

## 🚀 Creating Streams

### **From Collections**
```java
// List to Stream
List<String> list = Arrays.asList("A", "B", "C");
Stream<String> listStream = list.stream();

// Set to Stream
Set<Integer> set = Set.of(1, 2, 3);
Stream<Integer> setStream = set.stream();

// Map to Stream
Map<String, Integer> map = Map.of("A", 1, "B", 2);
Stream<String> keyStream = map.keySet().stream();
Stream<Integer> valueStream = map.values().stream();
Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();

// Queue to Stream
Queue<String> queue = new LinkedList<>(List.of("A", "B", "C"));
Stream<String> queueStream = queue.stream();
```

### **From Arrays**
```java
// Primitive arrays
int[] intArray = {1, 2, 3, 4, 5};
IntStream intStream = Arrays.stream(intArray);
IntStream intStreamRange = IntStream.range(1, 6); // 1 to 5

// Object arrays
String[] stringArray = {"A", "B", "C"};
Stream<String> stringStream = Arrays.stream(stringArray);
Stream<String> streamOf = Stream.of("A", "B", "C");
```

### **Specialized Streams**
```java
// Empty stream
Stream<String> emptyStream = Stream.empty();

// Stream from values
Stream<Integer> valueStream = Stream.of(1, 2, 3, 4, 5);

// Infinite streams
Stream<Integer> infinite = Stream.iterate(0, n -> n + 1);
Stream<Double> randomStream = Stream.generate(Math::random);

// Primitive streams
IntStream intStream = IntStream.rangeClosed(1, 10); // 1 to 10
LongStream longStream = LongStream.range(1, 100);
DoubleStream doubleStream = DoubleStream.of(1.1, 2.2, 3.3);
```

### **From Files & I/O**
```java
// Read file lines as stream
try (Stream<String> lines = Files.lines(Paths.get("file.txt"))) {
    lines.forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}

// Read file words
try (Stream<String> words = Pattern.compile("\\W+")
        .splitAsStream(Files.readString(Paths.get("file.txt")))) {
    words.forEach(System.out::println);
}

// Directory listing
try (Stream<Path> paths = Files.list(Paths.get("/path/to/dir"))) {
    paths.forEach(System.out::println);
}
```

---

## 🔄 Intermediate Operations (Non-Terminal)

### **Filtering Operations**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

// filter() - keep elements matching predicate
List<String> longNames = names.stream()
    .filter(name -> name.length() > 4)
    .collect(Collectors.toList()); // ["Alice", "Charlie", "David"]

// distinct() - remove duplicates
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList()); // [1, 2, 3, 4]

// limit() - get first N elements
List<String> firstThree = names.stream()
    .limit(3)
    .collect(Collectors.toList()); // ["Alice", "Bob", "Charlie"]

// skip() - skip first N elements
List<String> afterFirstTwo = names.stream()
    .skip(2)
    .collect(Collectors.toList()); // ["Charlie", "David", "Eve"]

// takeWhile() - take while predicate true (Java 9+)
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> firstUntilFour = nums.stream()
    .takeWhile(n -> n < 4)
    .collect(Collectors.toList()); // [1, 2, 3]

// dropWhile() - drop while predicate true (Java 9+)
List<Integer> afterFour = nums.stream()
    .dropWhile(n -> n < 4)
    .collect(Collectors.toList()); // [4, 5, 6]
```

### **Mapping Operations**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// map() - transform each element
List<String> upperNames = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList()); // ["ALICE", "BOB", "CHARLIE"]

List<Integer> nameLengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList()); // [5, 3, 7]

// flatMap() - flatten nested structures
List<List<String>> nested = Arrays.asList(
    Arrays.asList("A", "B", "C"),
    Arrays.asList("D", "E", "F")
);
List<String> flattened = nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList()); // ["A", "B", "C", "D", "E", "F"]

// mapToInt, mapToLong, mapToDouble (primitive specializations)
int totalLength = names.stream()
    .mapToInt(String::length)
    .sum(); // 15
```

### **Sorting Operations**
```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

// sorted() - natural order
List<String> sortedNames = names.stream()
    .sorted()
    .collect(Collectors.toList()); // ["Alice", "Bob", "Charlie"]

// sorted() with Comparator
List<String> sortedByLength = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList()); // ["Bob", "Alice", "Charlie"]

// Reverse order
List<String> reverseSorted = names.stream()
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList()); // ["Charlie", "Bob", "Alice"]

// Multiple sort criteria
List<Person> people = getPeople();
List<Person> sortedPeople = people.stream()
    .sorted(Comparator
        .comparing(Person::getLastName)
        .thenComparing(Person::getFirstName))
    .collect(Collectors.toList());
```

### **Peek for Debugging**
```java
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .peek(name -> System.out.println("Filtered: " + name))
    .map(String::toUpperCase)
    .peek(name -> System.out.println("Mapped: " + name))
    .collect(Collectors.toList());
// Use peek() for debugging, not for production logic
```

---

## 🎯 Terminal Operations

### **Collection & Reduction**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// collect() - to List
List<String> list = names.stream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toList());

// collect() - to Set
Set<String> set = names.stream()
    .collect(Collectors.toSet());

// collect() - to Map
Map<String, Integer> nameLengthMap = names.stream()
    .collect(Collectors.toMap(
        name -> name,           // key mapper
        String::length          // value mapper
    )); // {Alice=5, Bob=3, Charlie=7}

// collect() - joining strings
String joined = names.stream()
    .collect(Collectors.joining(", ")); // "Alice, Bob, Charlie"

String joinedWithPrefixSuffix = names.stream()
    .collect(Collectors.joining(", ", "[", "]")); // "[Alice, Bob, Charlie]"
```

### **Aggregation Operations**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// count()
long count = numbers.stream()
    .filter(n -> n % 2 == 0)
    .count(); // 2

// min() and max()
Optional<Integer> min = numbers.stream()
    .min(Integer::compare); // Optional[1]

Optional<Integer> max = numbers.stream()
    .max(Integer::compare); // Optional[5]

// sum() (for primitive streams)
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum(); // 15

// average() (for primitive streams)
OptionalDouble avg = numbers.stream()
    .mapToInt(Integer::intValue)
    .average(); // OptionalDouble[3.0]

// summaryStatistics() (for primitive streams)
IntSummaryStatistics stats = numbers.stream()
    .mapToInt(Integer::intValue)
    .summaryStatistics();
// stats.getCount() = 5, getSum() = 15, getMin() = 1, getMax() = 5, getAverage() = 3.0
```

### **Finding & Matching Operations**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// anyMatch() - true if any element matches predicate
boolean hasEven = numbers.stream()
    .anyMatch(n -> n % 2 == 0); // true

// allMatch() - true if all elements match predicate
boolean allPositive = numbers.stream()
    .allMatch(n -> n > 0); // true

// noneMatch() - true if no elements match predicate
boolean noneNegative = numbers.stream()
    .noneMatch(n -> n < 0); // true

// findFirst() - get first element (ordered streams)
Optional<Integer> firstEven = numbers.stream()
    .filter(n -> n % 2 == 0)
    .findFirst(); // Optional[2]

// findAny() - get any element (better for parallel streams)
Optional<Integer> anyEven = numbers.stream()
    .filter(n -> n % 2 == 0)
    .findAny(); // Could be 2 or 4 (non-deterministic in parallel)

// Note: findAny() can be more efficient than findFirst() for parallel streams
```

### **forEach & forEachOrdered**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// forEach() - process each element (order not guaranteed in parallel)
names.stream()
    .forEach(System.out::println);

// forEachOrdered() - process in encounter order (even in parallel)
names.parallelStream()
    .forEachOrdered(System.out::println); // Maintains order

// Note: Prefer collect() over forEach() for side-effects
List<String> processed = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList()); // Better than forEach with side effects
```

### **Reduction Operations**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// reduce() - with identity and accumulator
Integer sum1 = numbers.stream()
    .reduce(0, (a, b) -> a + b); // 15

// reduce() - with accumulator only (returns Optional)
Optional<Integer> sum2 = numbers.stream()
    .reduce((a, b) -> a + b); // Optional[15]

// reduce() - with identity, accumulator, and combiner (for parallel streams)
Integer sum3 = numbers.parallelStream()
    .reduce(0, (a, b) -> a + b, (a, b) -> a + b); // 15

// String concatenation with reduce
String concatenated = names.stream()
    .reduce("", (a, b) -> a + ", " + b); // ", Alice, Bob, Charlie"

// More efficient string concatenation
String efficient = names.stream()
    .collect(Collectors.joining(", ")); // "Alice, Bob, Charlie"
```

---

## 📊 Collectors Utility Class

### **Basic Collectors**
```java
List<Person> people = getPeople();

// toList()
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

// toSet()
Set<String> uniqueNames = people.stream()
    .map(Person::getName)
    .collect(Collectors.toSet());

// toCollection() - specify collection type
LinkedList<String> linkedNames = people.stream()
    .map(Person::getName)
    .collect(Collectors.toCollection(LinkedList::new));

// toMap() - with key and value mappers
Map<String, Integer> nameToAge = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge
    ));

// toMap() - with duplicate key handler
Map<String, Person> personByName = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Function.identity(),
        (existing, replacement) -> existing // keep first on duplicate
    ));

// toMap() - with specific map type
TreeMap<String, Integer> sortedMap = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge,
        (oldVal, newVal) -> oldVal,
        TreeMap::new
    ));
```

### **Grouping & Partitioning**
```java
List<Person> people = getPeople();

// groupingBy() - group by a classifier
Map<String, List<Person>> peopleByCity = people.stream()
    .collect(Collectors.groupingBy(Person::getCity));

// groupingBy() - with downstream collector
Map<String, Long> countByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.counting()
    ));

Map<String, Double> avgAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.averagingInt(Person::getAge)
    ));

Map<String, Set<String>> namesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(Person::getName, Collectors.toSet())
    ));

// groupingBy() - with map type specification
TreeMap<String, List<Person>> sortedByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        TreeMap::new,
        Collectors.toList()
    ));

// partitioningBy() - split into two groups based on predicate
Map<Boolean, List<Person>> partitionedByAge = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));

// partitioningBy() - with downstream collector
Map<Boolean, Long> countByAgeGroup = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.getAge() >= 18,
        Collectors.counting()
    ));
```

### **Advanced Collectors**
```java
List<Person> people = getPeople();

// averaging collectors
Double avgAge = people.stream()
    .collect(Collectors.averagingInt(Person::getAge));

// summing collectors
Integer totalAge = people.stream()
    .collect(Collectors.summingInt(Person::getAge));

// summarizing collectors
IntSummaryStatistics ageStats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));

// maxBy/minBy collectors
Optional<Person> oldest = people.stream()
    .collect(Collectors.maxBy(Comparator.comparingInt(Person::getAge)));

Optional<Person> youngest = people.stream()
    .collect(Collectors.minBy(Comparator.comparingInt(Person::getAge)));

// mapping collector (transform before collecting)
List<String> upperCaseNames = people.stream()
    .collect(Collectors.mapping(
        p -> p.getName().toUpperCase(),
        Collectors.toList()
    ));

// filtering collector (filter before collecting)
List<Person> adults = people.stream()
    .collect(Collectors.filtering(
        p -> p.getAge() >= 18,
        Collectors.toList()
    ));

// flatMapping collector (Java 9+)
Map<String, Set<String>> hobbiesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.flatMapping(
            p -> p.getHobbies().stream(),
            Collectors.toSet()
        )
    ));

// teeing collector (Java 12+) - combine two collectors
record Stats(long count, double average) {}

Stats stats = people.stream()
    .collect(Collectors.teeing(
        Collectors.counting(),
        Collectors.averagingInt(Person::getAge),
        Stats::new
    ));
```

### **Custom Collector**
```java
// Create custom collector
Collector<Person, ?, Map<String, Double>> avgAgeByGender = Collector.of(
    // Supplier: create accumulator
    () -> new HashMap<String, IntSummaryStatistics>(),
    
    // Accumulator: add element to accumulator
    (map, person) -> {
        IntSummaryStatistics stats = map.computeIfAbsent(
            person.getGender(),
            k -> new IntSummaryStatistics()
        );
        stats.accept(person.getAge());
    },
    
    // Combiner: merge two accumulators (for parallel streams)
    (map1, map2) -> {
        map2.forEach((gender, stats2) -> {
            IntSummaryStatistics stats1 = map1.merge(
                gender, stats2,
                (s1, s2) -> { s1.combine(s2); return s1; }
            );
        });
        return map1;
    },
    
    // Finisher: transform accumulator to final result
    map -> map.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> e.getValue().getAverage()
        ))
);

// Usage
Map<String, Double> result = people.stream()
    .collect(avgAgeByGender);
```

---

## ⚡ Parallel Streams

### **Creating Parallel Streams**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

// From collection
Stream<String> parallelStream1 = names.parallelStream();

// Convert existing stream to parallel
Stream<String> parallelStream2 = names.stream().parallel();

// From array
Stream<String> parallelStream3 = Arrays.stream(new String[]{"A", "B", "C"}).parallel();

// IntStream parallel
IntStream parallelIntStream = IntStream.range(1, 100).parallel();
```

### **Parallel Stream Considerations**
```java
// When to use parallel streams:
// - Large datasets (> 10,000 elements)
// - Computationally intensive operations
// - No stateful operations or side effects
// - Order doesn't matter (or can be reordered)

// When NOT to use parallel streams:
// - Small datasets
// - I/O bound operations
// - Order-sensitive operations
// - Operations with side effects
// - Shared mutable state

// Example: Parallel processing with ordering
List<Integer> numbers = IntStream.rangeClosed(1, 1_000_000)
    .boxed()
    .collect(Collectors.toList());

// Unordered parallel stream (faster)
long countUnordered = numbers.parallelStream()
    .unordered() // Hint that order doesn't matter
    .filter(n -> n % 2 == 0)
    .count();

// Ordered parallel stream
List<Integer> evenNumbersOrdered = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList()); // Maintains order

// Performance comparison
long start = System.currentTimeMillis();
long sequentialCount = numbers.stream()
    .filter(n -> n % 2 == 0)
    .count();
long sequentialTime = System.currentTimeMillis() - start;

start = System.currentTimeMillis();
long parallelCount = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .count();
long parallelTime = System.currentTimeMillis() - start;

System.out.printf("Sequential: %dms, Parallel: %dms%n", 
    sequentialTime, parallelTime);
```

### **Parallel Stream Pitfalls**
```java
// 1. Shared mutable state (race condition)
List<Integer> sharedList = Collections.synchronizedList(new ArrayList<>());
IntStream.range(0, 10000).parallel()
    .forEach(sharedList::add); // Thread-safe but inefficient

// Better: Use collect() instead
List<Integer> collected = IntStream.range(0, 10000).parallel()
    .boxed()
    .collect(Collectors.toList());

// 2. Non-associative operations in reduce()
// Wrong: Subtraction is not associative
int wrong = IntStream.range(1, 10).parallel()
    .reduce(0, (a, b) -> a - b); // Non-deterministic result!

// Correct: Use associative operations
int sum = IntStream.range(1, 10).parallel()
    .reduce(0, Integer::sum); // Correct

// 3. Stateful lambda expressions
List<Integer> stateful = new ArrayList<>();
IntStream.range(0, 100).parallel()
    .map(i -> {
        stateful.add(i); // MUTATION! Race condition!
        return i * 2;
    })
    .sum();

// 4. Using forEach with side effects
Map<Integer, Integer> map = new ConcurrentHashMap<>();
IntStream.range(0, 100).parallel()
    .forEach(i -> map.put(i % 10, i)); // Concurrent, but order lost

// Better: Use groupingBy
Map<Integer, List<Integer>> grouped = IntStream.range(0, 100).parallel()
    .boxed()
    .collect(Collectors.groupingBy(i -> i % 10));
```

### **Controlling Parallelism**
```java
// Custom ForkJoinPool for parallel streams
ForkJoinPool customPool = new ForkJoinPool(4); // 4 threads

List<Integer> numbers = IntStream.range(1, 100).boxed()
    .collect(Collectors.toList());

// Run parallel stream in custom pool
Long result = customPool.submit(() -> 
    numbers.parallelStream()
        .filter(n -> n % 2 == 0)
        .count()
).join();

// Note: This affects all parallel streams in the pool
// Use with caution - can lead to thread pool exhaustion

// Alternative: Use CompletableFuture for better control
CompletableFuture<Long> future = CompletableFuture.supplyAsync(() ->
    numbers.parallelStream()
        .filter(n -> n % 2 == 0)
        .count(),
    customPool
);

Long count = future.get();
```

---

## 🔄 Streams with Collections API

### **Converting Between Collections and Streams**
```java
// List ↔ Stream
List<String> list = Arrays.asList("A", "B", "C");
Stream<String> streamFromList = list.stream();
List<String> listFromStream = streamFromList.collect(Collectors.toList());

// Set ↔ Stream
Set<Integer> set = Set.of(1, 2, 3);
Stream<Integer> streamFromSet = set.stream();
Set<Integer> setFromStream = streamFromSet.collect(Collectors.toSet());

// Map ↔ Stream
Map<String, Integer> map = Map.of("A", 1, "B", 2);
Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();

// Map from Stream
Map<String, Integer> mapFromStream = entryStream
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

// Array ↔ Stream
String[] array = {"A", "B", "C"};
Stream<String> streamFromArray = Arrays.stream(array);
String[] arrayFromStream = streamFromArray.toArray(String[]::new);

// Primitive array ↔ Stream
int[] intArray = {1, 2, 3};
IntStream intStream = Arrays.stream(intArray);
int[] newIntArray = intStream.toArray();
```

### **Common Collection Operations with Streams**
```java
List<Person> people = getPeople();

// Filtering and collecting
List<Person> adults = people.stream()
    .filter(p -> p.getAge() >= 18)
    .collect(Collectors.toList());

// Transformation
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

// Sorting
List<Person> sortedByAge = people.stream()
    .sorted(Comparator.comparingInt(Person::getAge))
    .collect(Collectors.toList());

// Removing duplicates
List<String> uniqueNames = people.stream()
    .map(Person::getName)
    .distinct()
    .collect(Collectors.toList());

// Checking conditions
boolean allAdults = people.stream()
    .allMatch(p -> p.getAge() >= 18);

boolean anySenior = people.stream()
    .anyMatch(p -> p.getAge() >= 65);

// Finding elements
Optional<Person> firstAdult = people.stream()
    .filter(p -> p.getAge() >= 18)
    .findFirst();

// Aggregation
int totalAge = people.stream()
    .mapToInt(Person::getAge)
    .sum();

double averageAge = people.stream()
    .collect(Collectors.averagingInt(Person::getAge));
```

### **Stream Operations vs Traditional Loops**
```java
// Traditional approach
List<String> traditional = new ArrayList<>();
for (Person person : people) {
    if (person.getAge() >= 18) {
        traditional.add(person.getName().toUpperCase());
    }
}
Collections.sort(traditional);

// Stream approach
List<String> streamBased = people.stream()
    .filter(p -> p.getAge() >= 18)
    .map(p -> p.getName().toUpperCase())
    .sorted()
    .collect(Collectors.toList());

// Performance comparison:
// - Streams: More readable, declarative, parallelizable
// - Loops: More control, sometimes faster for simple operations
// - Choose based on readability and performance needs
```

### **Streams with Optional**
```java
// Working with Optional in streams
List<Optional<String>> optionalNames = Arrays.asList(
    Optional.of("Alice"),
    Optional.empty(),
    Optional.of("Bob"),
    Optional.of("Charlie")
);

// Filter out empty Optionals and get values
List<String> names = optionalNames.stream()
    .filter(Optional::isPresent)
    .map(Optional::get)
    .collect(Collectors.toList());

// Using flatMap to handle Optionals
List<String> namesFlatMap = optionalNames.stream()
    .flatMap(opt -> opt.stream())
    .collect(Collectors.toList());

// Or using Java 9+ Optional.stream()
List<String> namesJava9 = optionalNames.stream()
    .flatMap(Optional::stream)
    .collect(Collectors.toList());

// Chaining Optional with Stream
Optional<Person> person = findPersonById(123);
List<String> hobbies = person.stream()
    .flatMap(p -> p.getHobbies().stream())
    .collect(Collectors.toList());
```

---

## 📊 Advanced Stream Patterns

### **Stream Pagination**
```java
public <T> List<T> getPage(List<T> source, int page, int pageSize) {
    return source.stream()
        .skip((long) page * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
}

// Usage
List<String> allItems = getAllItems(); // 1000 items
List<String> page1 = getPage(allItems, 0, 20); // Items 0-19
List<String> page2 = getPage(allItems, 1, 20); // Items 20-39
```

### **Batch Processing**
```java
public <T> Stream<List<T>> batch(List<T> source, int batchSize) {
    return IntStream.range(0, (source.size() + batchSize - 1) / batchSize)
        .mapToObj(i -> source.subList(
            i * batchSize,
            Math.min(source.size(), (i + 1) * batchSize)
        ));
}

// Usage
List<Integer> numbers = IntStream.rangeClosed(1, 100)
    .boxed()
    .collect(Collectors.toList());

batch(numbers, 10).forEach(batch -> {
    System.out.println("Processing batch: " + batch);
    // Process batch
});
```

### **Window/Sliding Window**
```java
public <T> Stream<List<T>> slidingWindow(List<T> source, int windowSize) {
    return IntStream.range(0, source.size() - windowSize + 1)
        .mapToObj(i -> source.subList(i, i + windowSize));
}

// Usage
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
slidingWindow(numbers, 3).forEach(window -> {
    System.out.println("Window: " + window);
    // Windows: [1,2,3], [2,3,4], [3,4,5], [4,5,6]
});
```

### **Stream Zip (Combine two streams)**
```java
public static <A, B, C> Stream<C> zip(
        Stream<A> streamA,
        Stream<B> streamB,
        BiFunction<A, B, C> zipper) {
    
    Spliterator<A> splitA = streamA.spliterator();
    Spliterator<B> splitB = streamB.spliterator();
    
    int characteristics = splitA.characteristics() & 
                        splitB.characteristics() & 
                        ~(Spliterator.DISTINCT | Spliterator.SORTED);
    
    long zipSize = Math.min(splitA.estimateSize(), splitB.estimateSize());
    
    Iterator<A> itrA = Spliterators.iterator(splitA);
    Iterator<B> itrB = Spliterators.iterator(splitB);
    
    Iterator<C> itrC = new Iterator<C>() {
        @Override
        public boolean hasNext() {
            return itrA.hasNext() && itrB.hasNext();
        }
        
        @Override
        public C next() {
            return zipper.apply(itrA.next(), itrB.next());
        }
    };
    
    Spliterator<C> splitC = Spliterators.spliterator(
        itrC, zipSize, characteristics);
    
    return StreamSupport.stream(splitC, false);
}

// Usage
Stream<String> names = Stream.of("Alice", "Bob", "Charlie");
Stream<Integer> ages = Stream.of(25, 30, 35);

Stream<String> zipped = zip(names, ages, (name, age) -> name + " is " + age);
zipped.forEach(System.out::println);
// Alice is 25, Bob is 30, Charlie is 35
```

### **Stream Peek with State**
```java
// Process stream with progress tracking
List<String> items = getLargeList();

List<String> processed = items.stream()
    .peek(new Consumer<String>() {
        private int count = 0;
        private final int total = items.size();
        
        @Override
        public void accept(String item) {
            count++;
            if (count % 1000 == 0) {
                System.out.printf("Processed %d/%d (%.1f%%)%n",
                    count, total, (count * 100.0 / total));
            }
        }
    })
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

## 🎯 Real-World Examples

### **Example 1: Data Analysis Pipeline**
```java
public class SalesAnalysis {
    
    public record Sale(String product, String region, double amount, LocalDate date) {}
    
    public AnalysisResult analyze(List<Sale> sales) {
        // 1. Filter recent sales (last 30 days)
        LocalDate cutoff = LocalDate.now().minusDays(30);
        
        // 2. Group by product and region
        Map<String, Map<String, DoubleSummaryStatistics>> stats = sales.stream()
            .filter(sale -> !sale.date().isBefore(cutoff))
            .collect(Collectors.groupingBy(
                Sale::product,
                Collectors.groupingBy(
                    Sale::region,
                    Collectors.summarizingDouble(Sale::amount)
                )
            ));
        
        // 3. Find top products
        List<ProductSummary> topProducts = stats.entrySet().stream()
            .map(entry -> {
                String product = entry.getKey();
                Map<String, DoubleSummaryStatistics> regionStats = entry.getValue();
                
                double total = regionStats.values().stream()
                    .mapToDouble(DoubleSummaryStatistics::getSum)
                    .sum();
                
                double average = regionStats.values().stream()
                    .mapToDouble(DoubleSummaryStatistics::getAverage)
                    .average()
                    .orElse(0);
                
                return new ProductSummary(product, total, average);
            })
            .sorted(Comparator.comparing(ProductSummary::total).reversed())
            .limit(10)
            .collect(Collectors.toList());
        
        // 4. Find best performing regions
        Map<String, Double> regionPerformance = sales.stream()
            .filter(sale -> !sale.date().isBefore(cutoff))
            .collect(Collectors.groupingBy(
                Sale::region,
                Collectors.summingDouble(Sale::amount)
            ));
        
        String bestRegion = regionPerformance.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
        
        return new AnalysisResult(topProducts, bestRegion, stats);
    }
    
    public static class ProductSummary {
        private final String product;
        private final double total;
        private final double average;
        // Constructor, getters
    }
    
    public static class AnalysisResult {
        private final List<ProductSummary> topProducts;
        private final String bestRegion;
        private final Map<String, Map<String, DoubleSummaryStatistics>> detailedStats;
        // Constructor, getters
    }
}
```

### **Example 2: File Processing Pipeline**
```java
public class LogAnalyzer {
    
    public AnalysisResult analyzeLogs(Path logDir) throws IOException {
        // 1. Read all log files
        Map<String, Long> errorCounts = Files.walk(logDir)
            .filter(Files::isRegularFile)
            .filter(path -> path.toString().endsWith(".log"))
            .flatMap(this::readLines)
            .filter(line -> line.contains("ERROR"))
            .map(this::extractErrorType)
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
        
        // 2. Find most frequent errors
        List<Map.Entry<String, Long>> topErrors = errorCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());
        
        // 3. Analyze error patterns over time
        Map<LocalDate, Long> errorsByDate = Files.walk(logDir)
            .filter(Files::isRegularFile)
            .filter(path -> path.toString().endsWith(".log"))
            .flatMap(this::readLines)
            .filter(line -> line.contains("ERROR"))
            .collect(Collectors.groupingBy(
                line -> extractDate(line).toLocalDate(),
                Collectors.counting()
            ));
        
        // 4. Find peak error day
        Optional<LocalDate> peakDay = errorsByDate.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
        
        return new AnalysisResult(topErrors, errorsByDate, peakDay.orElse(null));
    }
    
    private Stream<String> readLines(Path file) {
        try {
            return Files.lines(file);
        } catch (IOException e) {
            return Stream.empty();
        }
    }
    
    private String extractErrorType(String line) {
        // Extract error type from log line
        Pattern pattern = Pattern.compile("ERROR\\s+\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(line);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }
    
    private LocalDateTime extractDate(String line) {
        // Extract date from log line
        try {
            return LocalDateTime.parse(
                line.substring(0, 19),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
```

### **Example 3: E-commerce Recommendation Engine**
```java
public class RecommendationEngine {
    
    public List<Product> recommendProducts(User user, List<PurchaseHistory> allPurchases) {
        // 1. Find users with similar purchase history
        Map<User, Double> similarUsers = findSimilarUsers(user, allPurchases);
        
        // 2. Get products purchased by similar users
        List<Product> recommended = similarUsers.entrySet().stream()
            .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
            .limit(5) // Top 5 similar users
            .flatMap(entry -> {
                User similarUser = entry.getKey();
                return getPurchasedProducts(similarUser, allPurchases).stream();
            })
            .filter(product -> !hasPurchased(user, product, allPurchases))
            .distinct()
            .limit(10) // Top 10 recommendations
            .collect(Collectors.toList());
        
        // 3. Boost recommendations based on user preferences
        return boostRecommendations(user, recommended);
    }
    
    private Map<User, Double> findSimilarUsers(User user, List<PurchaseHistory> allPurchases) {
        // Calculate similarity score based on purchase history
        return allPurchases.stream()
            .collect(Collectors.groupingBy(PurchaseHistory::getUser))
            .entrySet().stream()
            .filter(entry -> !entry.getKey().equals(user))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> calculateSimilarity(user, entry.getKey(), entry.getValue())
            ));
    }
    
    private double calculateSimilarity(User u1, User u2, List<PurchaseHistory> history) {
        Set<Product> products1 = getPurchasedProducts(u1, history);
        Set<Product> products2 = getPurchasedProducts(u2, history);
        
        // Jaccard similarity
        Set<Product> intersection = new HashSet<>(products1);
        intersection.retainAll(products2);
        
        Set<Product> union = new HashSet<>(products1);
        union.addAll(products2);
        
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }
    
    private Set<Product> getPurchasedProducts(User user, List<PurchaseHistory> allPurchases) {
        return allPurchases.stream()
            .filter(ph -> ph.getUser().equals(user))
            .flatMap(ph -> ph.getProducts().stream())
            .collect(Collectors.toSet());
    }
    
    private boolean hasPurchased(User user, Product product, List<PurchaseHistory> allPurchases) {
        return allPurchases.stream()
            .anyMatch(ph -> ph.getUser().equals(user) && 
                           ph.getProducts().contains(product));
    }
    
    private List<Product> boostRecommendations(User user, List<Product> products) {
        // Boost products based on user preferences, trending, etc.
        return products.stream()
            .sorted(Comparator
                .comparing((Product p) -> user.getPreferredCategories().contains(p.getCategory()) ? 1 : 0)
                .thenComparing(Product::getRating).reversed()
                .thenComparing(Product::getPopularity).reversed())
            .collect(Collectors.toList());
    }
}
```

### **Example 4: Real-time Data Processing**
```java
public class RealTimeProcessor {
    private final BlockingQueue<DataEvent> eventQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    
    public void startProcessing() {
        // Process events in real-time using streams
        new Thread(() -> {
            while (running || !eventQueue.isEmpty()) {
                try {
                    // Batch process events (non-blocking poll)
                    List<DataEvent> batch = new ArrayList<>();
                    eventQueue.drainTo(batch, 100); // Get up to 100 events
                    
                    if (!batch.isEmpty()) {
                        processBatch(batch);
                    } else {
                        Thread.sleep(10); // Small sleep if no events
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
    
    private void processBatch(List<DataEvent> batch) {
        // 1. Filter valid events
        Map<Boolean, List<DataEvent>> partitioned = batch.stream()
            .collect(Collectors.partitioningBy(DataEvent::isValid));
        
        List<DataEvent> validEvents = partitioned.get(true);
        List<DataEvent> invalidEvents = partitioned.get(false);
        
        // Log invalid events
        invalidEvents.forEach(event -> 
            System.err.println("Invalid event: " + event));
        
        // 2. Group by type and process
        Map<EventType, List<DataEvent>> byType = validEvents.stream()
            .collect(Collectors.groupingBy(DataEvent::getType));
        
        byType.forEach((type, events) -> {
            switch (type) {
                case METRIC -> processMetrics(events);
                case LOG -> processLogs(events);
                case ALERT -> processAlerts(events);
            }
        });
        
        // 3. Aggregate statistics
        EventStatistics stats = validEvents.stream()
            .collect(Collectors.teeing(
                Collectors.counting(),
                Collectors.averagingInt(DataEvent::getSize),
                Collectors.filtering(
                    DataEvent::isHighPriority,
                    Collectors.toList()
                ),
                (count, avgSize, highPriority) -> 
                    new EventStatistics(count, avgSize, highPriority.size())
            ));
        
        // Update dashboard
        updateDashboard(stats);
    }
    
    private void processMetrics(List<DataEvent> metrics) {
        // Calculate percentiles, averages, etc.
        DoubleSummaryStatistics stats = metrics.stream()
            .mapToDouble(DataEvent::getValue)
            .summaryStatistics();
        
        // Detect anomalies
        List<DataEvent> anomalies = metrics.stream()
            .filter(event -> Math.abs(event.getValue() - stats.getAverage()) > 
                           2 * calculateStdDev(metrics))
            .collect(Collectors.toList());
        
        if (!anomalies.isEmpty()) {
            triggerAlert(anomalies);
        }
    }
    
    private double calculateStdDev(List<DataEvent> events) {
        double avg = events.stream()
            .mapToDouble(DataEvent::getValue)
            .average()
            .orElse(0);
        
        double variance = events.stream()
            .mapToDouble(e -> Math.pow(e.getValue() - avg, 2))
            .average()
            .orElse(0);
        
        return Math.sqrt(variance);
    }
    
    // Other processing methods...
    
    public void addEvent(DataEvent event) {
        eventQueue.offer(event);
    }
    
    public void stop() {
        running = false;
    }
}
```

---

## ⚡ Performance & Best Practices

### **Performance Optimization Tips**
```java
// 1. Use primitive streams for numeric operations
// SLOWER (boxing overhead)
List<Integer> numbers = getNumbers();
int sum = numbers.stream()
    .mapToInt(Integer::intValue) // Still boxing in list
    .sum();

// FASTER (use primitive arrays)
int[] primitiveNumbers = getPrimitiveNumbers();
int sumFast = Arrays.stream(primitiveNumbers).sum();

// 2. Avoid stateful lambda expressions
// BAD (stateful, not thread-safe)
List<Integer> result = new ArrayList<>();
stream.forEach(result::add); // Stateful side-effect

// GOOD (stateless)
List<Integer> goodResult = stream.collect(Collectors.toList());

// 3. Use parallel streams wisely
// TEST performance - parallel isn't always faster
List<Integer> largeList = createLargeList(1_000_000);

long start = System.currentTimeMillis();
long sequential = largeList.stream()
    .filter(n -> n % 2 == 0)
    .count();
long seqTime = System.currentTimeMillis() - start;

start = System.currentTimeMillis();
long parallel = largeList.parallelStream()
    .filter(n -> n % 2 == 0)
    .count();
long parTime = System.currentTimeMillis() - start;

System.out.printf("Seq: %dms, Par: %dms%n", seqTime, parTime);

// 4. Order intermediate operations efficiently
// LESS EFFICIENT
List<String> result1 = list.stream()
    .sorted()                    // Early sort - processes all elements
    .filter(s -> s.length() > 3) // Then filters
    .collect(Collectors.toList());

// MORE EFFICIENT
List<String> result2 = list.stream()
    .filter(s -> s.length() > 3) // Filter first (reduces elements)
    .sorted()                    // Sort fewer elements
    .collect(Collectors.toList());

// 5. Reuse streams? NO - streams are single-use
Stream<String> stream = list.stream();
stream.filter(s -> s.startsWith("A")); // OK
// stream.filter(s -> s.startsWith("B")); // ERROR: stream already used

// 6. Use collectors for complex reductions
// Instead of multiple terminal operations
List<Person> people = getPeople();

// INEFFICIENT (multiple passes)
int maxAge = people.stream()
    .mapToInt(Person::getAge)
    .max().orElse(0);
int minAge = people.stream()
    .mapToInt(Person::getAge)
    .min().orElse(0);

// EFFICIENT (single pass)
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));
int maxAge2 = stats.getMax();
int minAge2 = stats.getMin();
```

### **Common Pitfalls & Solutions**
```java
// 1. Infinite streams without limit
Stream.iterate(0, i -> i + 1)
    .forEach(System.out::println); // Infinite! Never terminates

// Solution: Add limit()
Stream.iterate(0, i -> i + 1)
    .limit(100)
    .forEach(System.out::println);

// 2. Modifying source during stream processing
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
list.stream()
    .forEach(s -> list.add("D")); // ConcurrentModificationException

// Solution: Don't modify source during streaming

// 3. Using streams for simple loops
// OVERKILL for simple operations
list.stream().forEach(System.out::println);

// BETTER for simple loops
for (String s : list) {
    System.out.println(s);
}

// 4. Ignoring Optional results
String first = list.stream()
    .filter(s -> s.length() > 10)
    .findFirst()
    .get(); // NoSuchElementException if no match

// Solution: Handle Optional properly
String firstSafe = list.stream()
    .filter(s -> s.length() > 10)
    .findFirst()
    .orElse("default");

// 5. Performance with boxed primitives
List<Integer> boxed = Arrays.asList(1, 2, 3, 4, 5);
int sum = boxed.stream()
    .reduce(0, Integer::sum); // Boxing in reduce

// Better: Use IntStream
int sumBetter = boxed.stream()
    .mapToInt(Integer::intValue)
    .sum();

// 6. Memory issues with large streams
// Creating large intermediate collections
List<String> huge = getHugeList();
List<String> processed = huge.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList()); // Creates another huge list

// Consider: Process in batches or use lazy evaluation
```

### **When to Use Streams vs Traditional Loops**
```java
// ✅ USE STREAMS WHEN:
// 1. Processing collections with multiple operations
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());

// 2. Parallel processing needed
long count = largeList.parallelStream()
    .filter(this::expensiveOperation)
    .count();

// 3. Complex data transformations
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// 4. Declarative code is clearer
double avg = numbers.stream()
    .mapToInt(Integer::intValue)
    .average()
    .orElse(0);

// ❌ USE TRADITIONAL LOOPS WHEN:
// 1. Simple iterations
for (String s : list) {
    System.out.println(s);
}

// 2. Need to modify collection during iteration
for (Iterator<String> it = list.iterator(); it.hasNext();) {
    String s = it.next();
    if (s.length() < 3) {
        it.remove();
    }
}

// 3. Need early break or continue
for (String s : list) {
    if (s == null) break; // Can't do this with streams
    System.out.println(s);
}

// 4. Performance-critical simple operations
int sum = 0;
for (int n : numbers) {
    sum += n; // Often faster than stream for simple ops
}

// 5. Multiple unrelated operations
// Streams force sequential logic, loops allow arbitrary flow
```

---

## 📚 Quick Reference Cheatsheet

### **Stream Creation**
```java
collection.stream()           // Sequential stream
collection.parallelStream()   // Parallel stream
Arrays.stream(array)          // From array
Stream.of(values...)          // From values
Stream.iterate(seed, fn)      // Infinite sequence
Stream.generate(supplier)     // Infinite generated
Files.lines(path)             // File lines
IntStream.range(start, end)   // Primitive range
```

### **Intermediate Operations**
```java
.filter(Predicate)            // Keep matching elements
.map(Function)                // Transform elements
.flatMap(Function)            // Flatten nested streams
.distinct()                   // Remove duplicates
.sorted() / .sorted(Comparator) // Sort elements
.limit(n)                     // First n elements
.skip(n)                      // Skip first n elements
.peek(Consumer)               // Debug/observe elements
.takeWhile(Predicate)         // Take while true (Java 9+)
.dropWhile(Predicate)         // Drop while true (Java 9+)
```

### **Terminal Operations**
```java
.collect(Collector)           // Collect to collection
.forEach(Consumer)            // Process each element
.toArray()                    // Convert to array
.reduce(identity, accumulator) // Reduce to single value
.min(Comparator) / .max(Comparator) // Find min/max
.count()                      // Count elements
.anyMatch(Predicate)          // Any element matches
.allMatch(Predicate)          // All elements match
.noneMatch(Predicate)         // No elements match
.findFirst() / .findAny()     // Find element
```

### **Common Collectors**
```java
Collectors.toList()           // Collect to List
Collectors.toSet()            // Collect to Set
Collectors.toMap(key, value)  // Collect to Map
Collectors.joining(delimiter) // Join strings
Collectors.groupingBy(classifier) // Group by key
Collectors.partitioningBy(predicate) // Split into two groups
Collectors.counting()         // Count elements
Collectors.summingInt()       // Sum of ints
Collectors.averagingInt()     // Average of ints
Collectors.summarizingInt()   // Full statistics
```

### **Primitive Stream Specializations**
```java
IntStream, LongStream, DoubleStream
.mapToInt(ToIntFunction)      // Convert to IntStream
.mapToLong(ToLongFunction)    // Convert to LongStream
.mapToDouble(ToDoubleFunction)// Convert to DoubleStream
.sum(), .average(), .min(), .max() // Specialized operations
.boxed()                      // Convert to object stream
```

### **Parallel Stream Tips**
```java
.parallel()                   // Convert to parallel
.sequential()                 // Convert to sequential
.isParallel()                 // Check if parallel
.forEachOrdered(Consumer)     // Ordered forEach
.unordered()                  // Hint that order doesn't matter
```

---

## 📖 Summary

**Java Streams** revolutionize collection processing by providing:
1. **Declarative syntax** for cleaner, more readable code
2. **Parallel processing** with minimal code changes
3. **Lazy evaluation** for performance optimization
4. **Functional programming** patterns for better abstraction

**Key Takeaways:**
1. **Use streams for complex data transformations** involving multiple operations
2. **Prefer `collect()` over `forEach()`** for side-effect-free code
3. **Use primitive streams** (`IntStream`, `LongStream`, `DoubleStream`) for numeric operations
4. **Consider parallel streams for CPU-intensive operations** on large datasets
5. **Chain operations efficiently** (filter before map/sort when possible)
6. **Handle `Optional` results properly** to avoid `NoSuchElementException`
7. **Test performance** - streams aren't always faster than loops
8. **Use appropriate collectors** for grouping, partitioning, and aggregation

**Remember**: Streams are a powerful tool, but not always the right tool. Choose the approach that makes your code more readable and maintainable while meeting performance requirements.

This comprehensive guide covers everything from basic stream operations to advanced patterns. Bookmark this cheatsheet for quick reference during your Java development work!