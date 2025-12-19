# Chapter 7: 👨‍👦 **Generics, Inheritance, and Subtypes**

## Understanding Type Relationships in Generic Systems

One of the most common points of confusion in Java generics is understanding how inheritance and subtyping work with parameterized types. This chapter will clarify these relationships and introduce you to the concepts of covariance, contravariance, and invariance.

## 🔄 **The Fundamental Question**

### Is `List<String>` a Subtype of `List<Object>`?

```java
import java.util.*;

public class SubtypeQuestion {
    public static void main(String[] args) {
        // Common misconception:
        // Since String is a subtype of Object, 
        // people think List<String> is a subtype of List<Object>
        
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        
        // ❌ This does NOT compile!
        // List<Object> objects = strings;  // Compile error
        
        // Why not? Let's see what would happen if it were allowed:
        // objects.add(123);  // Adding Integer to List<String>!
        // String s = strings.get(2);  // ClassCastException at runtime!
        
        // ✅ This works: using wildcards (we'll cover later)
        List<? extends Object> wildcardList = strings;  // OK
    }
}
```

**Answer: NO!** `List<String>` is **NOT** a subtype of `List<Object>`. They are unrelated types.

## 📈 **Covariance, Contravariance, and Invariance**

### Definitions

| Concept | Meaning | Java Example |
|---------|---------|--------------|
| **Covariance** | Preserves the ordering of types (subtype → subtype) | `String[]` is covariant with `Object[]` |
| **Contravariance** | Reverses the ordering of types (supertype → subtype) | `Comparator<Object>` is contravariant with `Comparator<String>` |
| **Invariance** | Neither covariant nor contravariant | `List<String>` is invariant with `List<Object>` |

### Visualizing the Concepts

```
Covariance:      A ≤ B  ⇒  Container[A] ≤ Container[B]
Contravariance:  A ≤ B  ⇒  Container[B] ≤ Container[A]
Invariance:      No relationship based on type parameters
```

## 🏗️ **Java's Approach to Variance**

### 1. **Arrays are Covariant (Pre-Generics Legacy)**

```java
public class ArrayVariance {
    public static void main(String[] args) {
        // Arrays are covariant in Java (for historical reasons)
        String[] strings = {"A", "B", "C"};
        Object[] objects = strings;  // ✅ Allowed - covariance
        
        // But this is UNSAFE!
        objects[0] = 123;  // ❌ Runtime ArrayStoreException!
        // Java arrays check at runtime, generics check at compile time
        
        printArray(objects);  // Works because arrays are covariant
    }
    
    public static void printArray(Object[] array) {
        for (Object obj : array) {
            System.out.println(obj);
        }
    }
}
```

### 2. **Generics are Invariant (By Default)**

```java
public class GenericInvariance {
    static class Animal {}
    static class Dog extends Animal {}
    static class Cat extends Animal {}
    
    public static void main(String[] args) {
        // Invariance example
        List<Dog> dogs = new ArrayList<>();
        // List<Animal> animals = dogs;  // ❌ Compile error - invariance
        
        // Both lines below are compile errors
        // List<Animal> animals1 = new ArrayList<Dog>();
        // List<Dog> dogs1 = new ArrayList<Animal>();
        
        // No relationship between generic types with different parameters
    }
}
```

### 3. **Wildcards Introduce Variance (Covariance & Contravariance)**

```java
public class WildcardVariance {
    static class Animal {
        public void eat() {
            System.out.println("Animal eating");
        }
    }
    
    static class Dog extends Animal {
        @Override
        public void eat() {
            System.out.println("Dog eating");
        }
        
        public void bark() {
            System.out.println("Woof!");
        }
    }
    
    static class Cat extends Animal {
        @Override
        public void eat() {
            System.out.println("Cat eating");
        }
    }
    
    public static void main(String[] args) {
        // Covariance with ? extends (producer/read-only)
        List<Dog> dogs = Arrays.asList(new Dog(), new Dog());
        List<? extends Animal> animals = dogs;  // ✅ Covariance
        
        // Can read as Animal
        Animal first = animals.get(0);
        first.eat();
        
        // Cannot add (except null)
        // animals.add(new Dog());  // ❌ Compile error
        // animals.add(new Animal()); // ❌ Compile error
        
        // Contravariance with ? super (consumer/write-only)
        List<Animal> animalList = new ArrayList<>();
        List<? super Dog> dogSuper = animalList;  // ✅ Contravariance
        
        // Can add Dogs
        dogSuper.add(new Dog());
        // dogSuper.add(new Animal());  // ❌ Compile error
        // dogSuper.add(new Cat());     // ❌ Compile error
        
        // Can only read as Object
        Object obj = dogSuper.get(0);
        // Dog dog = dogSuper.get(0);  // ❌ Compile error
    }
}
```

## 🧬 **Type Hierarchy Examples**

### Understanding the Relationships

```java
public class TypeHierarchy {
    
    // Basic class hierarchy
    static class Entity {
        private Long id;
        public Long getId() { return id; }
    }
    
    static class User extends Entity {
        private String username;
        public String getUsername() { return username; }
    }
    
    static class Admin extends User {
        private String role;
        public String getRole() { return role; }
    }
    
    // Generic container
    static class Box<T> {
        private T content;
        public void set(T content) { this.content = content; }
        public T get() { return content; }
    }
    
    public static void main(String[] args) {
        // Regular inheritance (covariant)
        Admin admin = new Admin();
        User user = admin;        // ✅ OK - Admin is subtype of User
        Entity entity = admin;    // ✅ OK - Admin is subtype of Entity
        Object obj = admin;       // ✅ OK - Admin is subtype of Object
        
        // Generic invariance
        Box<Admin> adminBox = new Box<>();
        // Box<User> userBox = adminBox;     // ❌ Compile error
        // Box<Entity> entityBox = adminBox; // ❌ Compile error
        
        // Wildcard covariance (? extends)
        Box<? extends User> userBox = adminBox;  // ✅ OK
        Box<? extends Entity> entityBox = adminBox; // ✅ OK
        
        // Wildcard contravariance (? super)
        Box<Object> objectBox = new Box<>();
        Box<? super Admin> adminSuperBox = objectBox;  // ✅ OK
        Box<? super User> userSuperBox = objectBox;    // ✅ OK
        Box<? super Entity> entitySuperBox = objectBox; // ✅ OK
    }
}
```

## 🔧 **Practical Implications**

### 1. **Method Parameter Acceptance**

```java
public class MethodParameterAcceptance {
    static class Animal {}
    static class Dog extends Animal {}
    
    // Method accepting List<Animal>
    public static void processAnimals(List<Animal> animals) {
        for (Animal animal : animals) {
            System.out.println("Processing animal");
        }
    }
    
    // Method accepting List<? extends Animal> (more flexible)
    public static void processAnyAnimal(List<? extends Animal> animals) {
        for (Animal animal : animals) {
            System.out.println("Processing " + animal.getClass().getSimpleName());
        }
    }
    
    // Method accepting List<? super Dog> (for adding dogs)
    public static void addDogs(List<? super Dog> dogs) {
        dogs.add(new Dog());
        dogs.add(new Dog());
    }
    
    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        List<Dog> dogs = new ArrayList<>();
        
        // processAnimals(dogs);  // ❌ Compile error - invariance
        processAnimals(animals);  // ✅ OK
        
        processAnyAnimal(animals);  // ✅ OK
        processAnyAnimal(dogs);     // ✅ OK - covariance
        
        addDogs(animals);  // ✅ OK - contravariance
        addDogs(dogs);     // ✅ OK
        // addDogs(new ArrayList<Object>());  // ✅ OK
    }
}
```

### 2. **Return Type Considerations**

```java
public class ReturnTypeConsiderations {
    
    // Factory method returning specific type
    public static List<String> createStringList() {
        return Arrays.asList("A", "B", "C");
    }
    
    // More flexible return type using wildcards
    public static List<? extends CharSequence> createCharSequenceList() {
        // Could return List<String>, List<StringBuilder>, etc.
        return Arrays.asList("A", "B", "C");
    }
    
    // Even more flexible - use generic type parameter
    public static <T extends CharSequence> List<T> createList(T... items) {
        return Arrays.asList(items);
    }
    
    public static void main(String[] args) {
        List<String> strings1 = createStringList();
        
        // Can't assign directly due to wildcard
        // List<String> strings2 = createCharSequenceList();  // ❌
        
        List<? extends CharSequence> sequences = createCharSequenceList();
        
        // Generic method provides most flexibility
        List<String> strings3 = createList("X", "Y", "Z");
        List<StringBuilder> builders = createList(
            new StringBuilder("A"), 
            new StringBuilder("B")
        );
    }
}
```

## 🎯 **The Get-Put Principle (PECS Preview)**

### Understanding Producer and Consumer Roles

```java
public class GetPutPrinciple {
    
    static class Fruit {}
    static class Apple extends Fruit {}
    static class Orange extends Fruit {}
    static class RedApple extends Apple {}
    
    // Producer - provides items (use extends)
    public static void printFruits(List<? extends Fruit> fruits) {
        // Can GET fruits as Fruit objects
        for (Fruit fruit : fruits) {
            System.out.println(fruit);
        }
        
        // Cannot PUT/ADD to this list (except null)
        // fruits.add(new Apple());  // ❌ Compile error
    }
    
    // Consumer - consumes items (use super)
    public static void addApples(List<? super Apple> apples) {
        // Can PUT/ADD Apples (or subtypes)
        apples.add(new Apple());
        apples.add(new RedApple());
        
        // Cannot GET specific type (only Object)
        Object obj = apples.get(0);
        // Apple apple = apples.get(0);  // ❌ Compile error
    }
    
    // Both producer and consumer (use exact type)
    public static void processApples(List<Apple> apples) {
        // Can GET as Apple
        for (Apple apple : apples) {
            System.out.println(apple);
        }
        
        // Can PUT/ADD Apples
        apples.add(new Apple());
        apples.add(new RedApple());
    }
    
    public static void main(String[] args) {
        List<Fruit> fruits = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();
        List<RedApple> redApples = new ArrayList<>();
        
        printFruits(fruits);     // ✅ OK
        printFruits(apples);     // ✅ OK
        printFruits(redApples);  // ✅ OK
        
        addApples(fruits);       // ✅ OK
        addApples(apples);       // ✅ OK
        // addApples(redApples);  // ❌ Compile error (RedApple is not super of Apple)
        
        // processApples(fruits);     // ❌ Compile error
        processApples(apples);       // ✅ OK
        // processApples(redApples);  // ❌ Compile error
    }
}
```

## 🏗️ **Designing Type Hierarchies with Generics**

### 1. **Generic Interfaces and Implementation**

```java
public interface Repository<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T entity);
}

// Base implementation
public abstract class BaseRepository<T> implements Repository<T> {
    protected final Class<T> entityClass;
    
    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public T findById(Long id) {
        // Implementation
        return null;
    }
    
    @Override
    public abstract List<T> findAll();
    
    @Override
    public abstract void save(T entity);
}

// Specialized repository
public interface UserRepository extends Repository<User> {
    User findByUsername(String username);
    List<User> findActiveUsers();
}

// Implementation with additional bounds
public class AuditableRepository<T extends Entity & Auditable> 
       extends BaseRepository<T> {
    
    public AuditableRepository(Class<T> entityClass) {
        super(entityClass);
    }
    
    @Override
    public void save(T entity) {
        entity.setLastModified(new Date());
        // Save implementation
    }
    
    // Additional methods specific to auditable entities
    public List<T> findModifiedAfter(Date date) {
        // Implementation
        return new ArrayList<>();
    }
}
```

### 2. **Builder Pattern with Inheritance**

```java
public abstract class Animal {
    private final String name;
    
    protected Animal(String name) {
        this.name = name;
    }
    
    public String getName() { return name; }
    
    // Generic builder with recursive type parameter
    public abstract static class Builder<T extends Animal, B extends Builder<T, B>> {
        protected String name;
        
        public B name(String name) {
            this.name = name;
            return self();
        }
        
        protected abstract B self();
        public abstract T build();
    }
}

public class Dog extends Animal {
    private final String breed;
    
    private Dog(DogBuilder builder) {
        super(builder.name);
        this.breed = builder.breed;
    }
    
    public String getBreed() { return breed; }
    
    public static DogBuilder builder() {
        return new DogBuilder();
    }
    
    public static class DogBuilder extends Builder<Dog, DogBuilder> {
        private String breed;
        
        public DogBuilder breed(String breed) {
            this.breed = breed;
            return this;
        }
        
        @Override
        protected DogBuilder self() {
            return this;
        }
        
        @Override
        public Dog build() {
            return new Dog(this);
        }
    }
}

// Usage
Dog dog = Dog.builder()
    .name("Buddy")
    .breed("Golden Retriever")
    .build();
```

## ⚠️ **Common Mistakes and Pitfalls**

### 1. **Assuming Covariance Where It Doesn't Exist**

```java
public class CommonMistakes {
    public static void main(String[] args) {
        // Mistake 1: Assuming List<String> can be assigned to List<Object>
        List<String> strings = new ArrayList<>();
        // List<Object> objects = strings;  // ❌ Compile error
        
        // Mistake 2: Trying to create array of generic type
        // List<String>[] array = new List<String>[10];  // ❌ Compile error
        
        // Mistake 3: Using raw types to bypass invariance
        List rawList = new ArrayList<String>();
        List<Object> objects = rawList;  // ❌ Unsafe, but compiles with warning
        
        // Mistake 4: Forgetting wildcard capture
        List<?> unknownList = new ArrayList<String>();
        // unknownList.add("test");  // ❌ Compile error
    }
}
```

### 2. **Overcomplicating Type Parameters**

```java
// ❌ Overcomplicated
public class Overcomplicated<T extends Comparable<? super T> & Serializable> {
    // Hard to use and understand
}

// ✅ Better: Split into separate concerns
public class ComparableSerializable<T extends Comparable<T>> {
    private T value;
    // Comparable operations
}

public class SerializableWrapper<T> {
    private T value;
    // Serializable operations
}
```

## 🧪 **Type Safety Exercise**

```java
public class TypeSafetyExercise {
    
    static class Container<T> {
        private T item;
        
        public void set(T item) {
            this.item = item;
        }
        
        public T get() {
            return item;
        }
    }
    
    public static void main(String[] args) {
        // Test invariance
        Container<String> stringContainer = new Container<>();
        stringContainer.set("Hello");
        
        // ❌ This should fail at compile time
        // Container<Object> objectContainer = stringContainer;
        
        // Test wildcard covariance
        Container<? extends CharSequence> charSequenceContainer = stringContainer;
        CharSequence cs = charSequenceContainer.get();  // ✅ OK - reading
        // charSequenceContainer.set("World");  // ❌ Compile error - writing
        
        // Test wildcard contravariance
        Container<? super String> superStringContainer = new Container<Object>();
        superStringContainer.set("Hello");  // ✅ OK - writing
        Object obj = superStringContainer.get();  // ✅ OK - reading as Object
        // String s = superStringContainer.get();  // ❌ Compile error
    }
}
```

## 📊 **Summary Table: Type Relationships**

| Scenario | Allowed? | Reason |
|----------|----------|---------|
| `String s = new Object()` | ❌ | Object is not subtype of String |
| `Object o = new String()` | ✅ | String is subtype of Object |
| `List<Object> lo = new ArrayList<String>()` | ❌ | Generics are invariant |
| `List<?> lw = new ArrayList<String>()` | ✅ | Unbounded wildcard |
| `List<? extends Object> le = new ArrayList<String>()` | ✅ | Covariant wildcard |
| `List<? super String> ls = new ArrayList<Object>()` | ✅ | Contravariant wildcard |
| `List<String>[] array = new List<String>[10]` | ❌ | Cannot create generic array |
| `List<?>[] array = new List<?>[10]` | ✅ | Wildcard array allowed |

## 🎓 **Key Takeaways**

1. **Generics are invariant by default** - `List<String>` is NOT a subtype of `List<Object>`
2. **Arrays are covariant** (for historical reasons) but unsafe
3. **Wildcards introduce variance**:
   - `? extends T` for covariance (read-only, producer)
   - `? super T` for contravariance (write-only, consumer)
4. **Use the right variance for your use case**:
   - Producer (source of data) → `extends`
   - Consumer (destination for data) → `super`
5. **Design hierarchies carefully** considering variance constraints
6. **Avoid raw types** even when dealing with invariance - use wildcards instead

## 🚀 **Next Steps**

Now that you understand how generics interact with inheritance, you're ready to learn about **Type Inference** - how the Java compiler can often figure out type parameters without you explicitly specifying them. In [Chapter 8](#8-type-inference), we'll explore the diamond operator, target typing, and how to write code that leverages Java's powerful type inference capabilities.

> 💡 **Practice Exercise**: Create a class hierarchy: `Vehicle` ← `Car` ← `SportsCar`. Create generic methods that demonstrate:
> 1. A method that accepts any `List<? extends Vehicle>` and prints each vehicle
> 2. A method that accepts any `List<? super Car>` and adds cars to it
> 3. A generic `Garage<T extends Vehicle>` class that can store vehicles
> Test all three with different type arguments to understand the variance rules.