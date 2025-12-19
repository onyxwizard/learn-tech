# Chapter 10: 🔼 **Upper Bounded Wildcards**

## The "Producer" Wildcard: Reading with Confidence

Upper bounded wildcards (`? extends T`) are used when you need to read from a structure and you want to accept not just a specific type `T`, but any type that is a subtype of `T`. This is the foundation of the **producer** side of the PECS principle.

## 📈 **Understanding `? extends T`**

### Basic Concept

```java
import java.util.*;

public class UpperBoundedBasics {
    
    // Method that can process any list of numbers
    public static double sumOfList(List<? extends Number> list) {
        double sum = 0.0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }
    
    // Without wildcard - less flexible
    public static double sumOfNumbers(List<Number> list) {
        double sum = 0.0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }
    
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        List<Long> longs = Arrays.asList(100L, 200L, 300L);
        
        // ✅ Works with upper bounded wildcard
        System.out.println("Sum of integers: " + sumOfList(integers));  // 15.0
        System.out.println("Sum of doubles: " + sumOfList(doubles));   // 6.6
        System.out.println("Sum of longs: " + sumOfList(longs));       // 600.0
        
        // ❌ Does NOT work without wildcard
        // sumOfNumbers(integers);  // Compile error!
        // sumOfNumbers(doubles);   // Compile error!
        
        // Only works with exact List<Number>
        List<Number> numbers = Arrays.asList(1, 2.5, 3L);
        System.out.println("Sum of numbers: " + sumOfNumbers(numbers)); // 6.5
    }
}
```

## 🛠️ **Accessing Common Methods**

### Using Methods from the Bound

```java
public class CommonMethodAccess {
    
    static class Animal {
        protected String name;
        
        public Animal(String name) {
            this.name = name;
        }
        
        public void eat() {
            System.out.println(name + " is eating");
        }
        
        public String getName() {
            return name;
        }
    }
    
    static class Dog extends Animal {
        public Dog(String name) {
            super(name);
        }
        
        public void bark() {
            System.out.println(name + " says: Woof!");
        }
    }
    
    static class Cat extends Animal {
        public Cat(String name) {
            super(name);
        }
        
        public void meow() {
            System.out.println(name + " says: Meow!");
        }
    }
    
    // Can call any Animal method on elements
    public static void feedAnimals(List<? extends Animal> animals) {
        for (Animal animal : animals) {
            animal.eat();  // Can call Animal methods
            System.out.println("Fed: " + animal.getName());
            
            // ❌ Cannot call subtype-specific methods
            // if (animal instanceof Dog) {
            //     ((Dog) animal).bark();  // Would require cast
            // }
        }
    }
    
    // Generic method alternative (more flexible)
    public static <T extends Animal> void feedAnimalsGeneric(List<T> animals) {
        for (T animal : animals) {
            animal.eat();
            System.out.println("Fed: " + animal.getName());
            
            // Can call subtype methods with casting or instanceof
        }
    }
    
    public static void main(String[] args) {
        List<Dog> dogs = Arrays.asList(new Dog("Buddy"), new Dog("Max"));
        List<Cat> cats = Arrays.asList(new Cat("Whiskers"), new Cat("Mittens"));
        
        feedAnimals(dogs);
        feedAnimals(cats);
        
        // Also works with mixed lists through common supertype
        List<Animal> mixed = Arrays.asList(new Dog("Rex"), new Cat("Felix"));
        feedAnimals(mixed);
    }
}
```

## 🧠 **Use Case: Processing Collections**

### Real-World Number Processing

```java
import java.util.*;
import java.util.function.*;

public class NumberProcessing {
    
    // 1. Basic statistics
    public static DoubleStats calculateStats(List<? extends Number> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return new DoubleStats(0.0, 0.0, 0.0, 0.0);
        }
        
        double sum = 0.0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        
        for (Number num : numbers) {
            double value = num.doubleValue();
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        
        double avg = sum / numbers.size();
        return new DoubleStats(sum, avg, min, max);
    }
    
    static class DoubleStats {
        final double sum, avg, min, max;
        
        DoubleStats(double sum, double avg, double min, double max) {
            this.sum = sum;
            this.avg = avg;
            this.min = min;
            this.max = max;
        }
        
        @Override
        public String toString() {
            return String.format("Sum: %.2f, Avg: %.2f, Min: %.2f, Max: %.2f", 
                                sum, avg, min, max);
        }
    }
    
    // 2. Filtering numbers
    public static List<Number> filterNumbers(List<? extends Number> numbers, 
                                            Predicate<Number> predicate) {
        List<Number> result = new ArrayList<>();
        for (Number num : numbers) {
            if (predicate.test(num)) {
                result.add(num);
            }
        }
        return result;
    }
    
    // 3. Converting number types
    public static List<Double> toDoubles(List<? extends Number> numbers) {
        List<Double> doubles = new ArrayList<>();
        for (Number num : numbers) {
            doubles.add(num.doubleValue());
        }
        return doubles;
    }
    
    // 4. Finding maximum (needs Comparable)
    public static <T extends Number & Comparable<T>> T findMax(List<T> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("List is empty");
        
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
    
    // Alternative with wildcard
    public static Number findMaxNumber(List<? extends Number> list, 
                                      Comparator<Number> comparator) {
        if (list.isEmpty()) throw new IllegalArgumentException("List is empty");
        
        Number max = list.get(0);
        for (Number num : list) {
            if (comparator.compare(num, max) > 0) {
                max = num;
            }
        }
        return max;
    }
    
    public static void main(String[] args) {
        // Mixed number list
        List<Number> mixed = Arrays.asList(1, 2.5, 3L, 4.7f);
        System.out.println("Mixed stats: " + calculateStats(mixed));
        
        // Integer list
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Integer stats: " + calculateStats(integers));
        
        // Filter even numbers
        List<Number> evens = filterNumbers(integers, n -> n.intValue() % 2 == 0);
        System.out.println("Even numbers: " + evens);
        
        // Convert to doubles
        List<Double> doubles = toDoubles(integers);
        System.out.println("As doubles: " + doubles);
        
        // Find max with comparator
        Number max = findMaxNumber(integers, 
            Comparator.comparingDouble(Number::doubleValue));
        System.out.println("Max: " + max);
    }
}
```

## ⚠️ **Limitations and Workarounds**

### The Write Restriction

```java
public class WriteRestriction {
    
    public static void demonstrateLimitation() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        
        // Upper bounded reference
        List<? extends Number> numbers = integers;
        
        // ✅ CAN READ
        Number first = numbers.get(0);  // OK
        for (Number n : numbers) {      // OK
            System.out.println(n);
        }
        
        // ❌ CANNOT WRITE (except null)
        // numbers.add(4);            // Compile error
        // numbers.add(4.5);          // Compile error
        // numbers.add(new Integer(5)); // Compile error
        numbers.add(null);            // Only null allowed
        
        // Why? Because we don't know the exact type.
        // Could be List<Integer>, List<Double>, etc.
        // Adding Integer to potential List<Double> would be unsafe.
    }
    
    // Workaround 1: Use lower bounded wildcard for writing
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
    
    // Workaround 2: Use generic method with type parameter
    public static <T extends Number> void addToList(List<T> list, T element) {
        list.add(element);  // OK - we know the exact type
    }
    
    // Workaround 3: Copy to new list
    public static List<Number> copyAndAdd(List<? extends Number> source, 
                                         Number toAdd) {
        List<Number> result = new ArrayList<>(source);
        result.add(toAdd);
        return result;
    }
    
    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        
        // Using workarounds
        addNumbers(integers);  // Workaround 1
        System.out.println("After addNumbers: " + integers);
        
        addToList(integers, 4);  // Workaround 2
        System.out.println("After addToList: " + integers);
        
        List<Number> combined = copyAndAdd(integers, 5.5);  // Workaround 3
        System.out.println("Combined: " + combined);
    }
}
```

## 🔄 **Covariance in Action**

### Understanding the Type Relationships

```java
public class CovarianceDemo {
    
    static class Shape {
        public void draw() {
            System.out.println("Drawing shape");
        }
    }
    
    static class Circle extends Shape {
        @Override
        public void draw() {
            System.out.println("Drawing circle");
        }
        
        public void getRadius() {
            System.out.println("Radius: 5");
        }
    }
    
    static class Rectangle extends Shape {
        @Override
        public void draw() {
            System.out.println("Drawing rectangle");
        }
        
        public void getDimensions() {
            System.out.println("Width: 10, Height: 20");
        }
    }
    
    // Covariant array (for comparison)
    public static void arrayCovariance() {
        Circle[] circles = new Circle[3];
        Shape[] shapes = circles;  // Arrays are covariant
        
        // Runtime problem:
        shapes[0] = new Circle();  // OK
        // shapes[1] = new Rectangle();  // ❌ Runtime ArrayStoreException!
    }
    
    // Generic covariance with wildcards
    public static void genericCovariance() {
        List<Circle> circles = new ArrayList<>();
        circles.add(new Circle());
        
        // Covariant reference
        List<? extends Shape> shapes = circles;
        
        // Can read as Shape
        Shape first = shapes.get(0);
        first.draw();  // Polymorphism works
        
        // Cannot write (compile-time safety!)
        // shapes.add(new Circle());    // ❌ Compile error
        // shapes.add(new Rectangle()); // ❌ Compile error
        // shapes.add(new Shape());     // ❌ Compile error
        
        // This is SAFER than arrays!
    }
    
    // Processing shapes without knowing exact type
    public static void drawAll(List<? extends Shape> shapes) {
        for (Shape shape : shapes) {
            shape.draw();  // Can call Shape methods
            
            // If we need subtype methods, we must check and cast
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                circle.getRadius();
            }
        }
    }
    
    public static void main(String[] args) {
        List<Circle> circles = Arrays.asList(new Circle(), new Circle());
        List<Rectangle> rectangles = Arrays.asList(new Rectangle(), new Rectangle());
        
        drawAll(circles);
        drawAll(rectangles);
        
        // Also works with mixed list
        List<Shape> mixed = Arrays.asList(new Circle(), new Rectangle());
        drawAll(mixed);
    }
}
```

## 🏗️ **Advanced Patterns**

### 1. **Builder Pattern with Upper Bounds**

```java
public class BuilderPattern {
    
    abstract static class Vehicle {
        protected final String make;
        protected final String model;
        
        protected Vehicle(String make, String model) {
            this.make = make;
            this.model = model;
        }
        
        public abstract void start();
    }
    
    static class Car extends Vehicle {
        private final int doors;
        
        private Car(String make, String model, int doors) {
            super(make, model);
            this.doors = doors;
        }
        
        @Override
        public void start() {
            System.out.println("Car starting: " + make + " " + model);
        }
        
        public int getDoors() { return doors; }
    }
    
    static class Truck extends Vehicle {
        private final double payload;
        
        private Truck(String make, String model, double payload) {
            super(make, model);
            this.payload = payload;
        }
        
        @Override
        public void start() {
            System.out.println("Truck starting: " + make + " " + model);
        }
        
        public double getPayload() { return payload; }
    }
    
    // Generic vehicle processor using upper bounded wildcards
    public static class VehicleProcessor {
        
        // Process any collection of vehicles
        public static void processAll(List<? extends Vehicle> vehicles) {
            for (Vehicle vehicle : vehicles) {
                vehicle.start();
                
                // Can check specific types
                if (vehicle instanceof Car) {
                    Car car = (Car) vehicle;
                    System.out.println("Doors: " + car.getDoors());
                } else if (vehicle instanceof Truck) {
                    Truck truck = (Truck) vehicle;
                    System.out.println("Payload: " + truck.getPayload());
                }
            }
        }
        
        // Filter vehicles by make
        public static List<Vehicle> filterByMake(List<? extends Vehicle> vehicles, 
                                                String make) {
            List<Vehicle> result = new ArrayList<>();
            for (Vehicle vehicle : vehicles) {
                if (vehicle.make.equals(make)) {
                    result.add(vehicle);
                }
            }
            return result;
        }
        
        // Count vehicles by type
        public static <T extends Vehicle> long countByType(List<T> vehicles, 
                                                          Class<T> type) {
            return vehicles.stream()
                .filter(type::isInstance)
                .count();
        }
    }
    
    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(
            new Car("Toyota", "Camry", 4),
            new Car("Honda", "Accord", 4)
        );
        
        List<Truck> trucks = Arrays.asList(
            new Truck("Ford", "F-150", 1500.0),
            new Truck("Chevrolet", "Silverado", 2000.0)
        );
        
        VehicleProcessor.processAll(cars);
        VehicleProcessor.processAll(trucks);
        
        List<Vehicle> filtered = VehicleProcessor.filterByMake(cars, "Toyota");
        System.out.println("Filtered count: " + filtered.size());
        
        long carCount = VehicleProcessor.countByType(cars, Car.class);
        System.out.println("Car count: " + carCount);
    }
}
```

### 2. **Repository Pattern with Upper Bounds**

```java
public class RepositoryPattern {
    
    interface Entity {
        Long getId();
    }
    
    static class User implements Entity {
        private final Long id;
        private final String username;
        
        public User(Long id, String username) {
            this.id = id;
            this.username = username;
        }
        
        @Override public Long getId() { return id; }
        public String getUsername() { return username; }
    }
    
    static class Product implements Entity {
        private final Long id;
        private final String name;
        private final Double price;
        
        public Product(Long id, String name, Double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
        
        @Override public Long getId() { return id; }
        public String getName() { return name; }
        public Double getPrice() { return price; }
    }
    
    // Generic repository with upper bounds
    public static class EntityRepository<T extends Entity> {
        
        public List<T> findAll() {
            // Implementation would query database
            return new ArrayList<>();
        }
        
        public T findById(Long id) {
            // Implementation
            return null;
        }
        
        // Method that works with any entity repository
        public static void printAllIds(List<? extends EntityRepository<?>> repositories) {
            for (EntityRepository<?> repo : repositories) {
                List<? extends Entity> entities = repo.findAll();
                for (Entity entity : entities) {
                    System.out.println("ID: " + entity.getId());
                }
            }
        }
        
        // Process entities from multiple repositories
        public static <T extends Entity> List<T> mergeEntities(
                List<? extends EntityRepository<T>> repositories) {
            List<T> allEntities = new ArrayList<>();
            for (EntityRepository<T> repo : repositories) {
                allEntities.addAll(repo.findAll());
            }
            return allEntities;
        }
    }
    
    public static void main(String[] args) {
        EntityRepository<User> userRepo = new EntityRepository<>();
        EntityRepository<Product> productRepo = new EntityRepository<>();
        
        List<EntityRepository<?>> repos = Arrays.asList(userRepo, productRepo);
        EntityRepository.printAllIds(repos);
    }
}
```

## 🧪 **Performance Considerations**

### Boxing vs Primitive Specialization

```java
public class PerformanceConsiderations {
    
    // Upper bounded wildcards with boxed types
    public static double sumWithWildcard(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number num : numbers) {
            sum += num.doubleValue();  // Boxing/unboxing overhead
        }
        return sum;
    }
    
    // Specialized methods for performance
    public static int sumInts(int[] ints) {
        int sum = 0;
        for (int num : ints) {
            sum += num;  // No boxing
        }
        return sum;
    }
    
    public static double sumDoubles(double[] doubles) {
        double sum = 0.0;
        for (double num : doubles) {
            sum += num;  // No boxing
        }
        return sum;
    }
    
    // Using streams for flexibility
    public static double sumWithStream(List<? extends Number> numbers) {
        return numbers.stream()
            .mapToDouble(Number::doubleValue)
            .sum();
    }
    
    // Benchmark comparison
    public static void benchmark() {
        int size = 1_000_000;
        
        // List of Integers
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            integerList.add(i);
        }
        
        // Array of ints
        int[] intArray = new int[size];
        for (int i = 0; i < size; i++) {
            intArray[i] = i;
        }
        
        // Measure performance
        long start = System.nanoTime();
        double sum1 = sumWithWildcard(integerList);
        long time1 = System.nanoTime() - start;
        
        start = System.nanoTime();
        int sum2 = sumInts(intArray);
        long time2 = System.nanoTime() - start;
        
        System.out.println("Wildcard sum: " + sum1 + " in " + time1 + " ns");
        System.out.println("Primitive sum: " + sum2 + " in " + time2 + " ns");
        System.out.println("Speedup: " + (double) time1 / time2 + "x");
    }
    
    public static void main(String[] args) {
        benchmark();
    }
}
```

## 📐 **Guidelines for Upper Bounded Wildcards**

### When to Use `? extends T`

1. **Input parameters that are "producers"** - you only read from them
2. **Return types when you want covariance** - but be careful about write restrictions
3. **APIs that should accept subtypes** for maximum flexibility
4. **Method parameters in utility classes** where you process collections

### When NOT to Use `? extends T`

1. **When you need to add elements** to the collection
2. **When the exact type matters** for return values
3. **In class fields** (usually better to use type parameters)
4. **When you need to use the type parameter multiple times** in method signature

## 🎓 **Best Practices**

1. **Use for read-only operations** - perfect for processing data
2. **Combine with lower bounds (`? super T`)** for complete PECS implementation
3. **Document the producer role** in method javadoc
4. **Consider performance** - boxing overhead can be significant for primitives
5. **Use streams for complex operations** - they handle wildcards well
6. **Test with different subtype collections** to ensure flexibility

## 🚀 **Next Steps**

Upper bounded wildcards give us the ability to read from generic structures flexibly. But what about when we don't care about the type at all? In [Chapter 11](#11-unbounded-wildcards), we'll explore **Unbounded Wildcards** (`?`) - when you truly don't need to know anything about the type, just that it's a type.

> 💡 **Practice Exercise**: 
> 1. Create a `StatisticsCalculator` class with methods that work on `List<? extends Number>`
> 2. Implement methods for mean, median, mode, and standard deviation
> 3. Create a `ShapeRenderer` that can draw any `List<? extends Shape>`
> 4. Write a method that takes two `List<? extends Number>` and returns a list of their pairwise sums
> 5. Benchmark the performance difference between using `List<Integer>` and `int[]` for large datasets