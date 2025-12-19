# Chapter 4: 🧠 **Generic Methods**

## Beyond Classes: Type-Safe Methods for Any Type

Generic methods allow you to introduce type parameters at the method level, independent of whether the class itself is generic. This provides incredible flexibility for writing type-safe utility methods and algorithms.

## ✨ **Declaring Generic Methods**

### Basic Syntax

```java
// Generic method in a non-generic class
public class ArrayUtils {
    
    // <T> is the type parameter declaration
    // T[] is the return type (array of T)
    public static <T> T[] swap(T[] array, int i, int j) {
        if (array == null || i < 0 || j < 0 || 
            i >= array.length || j >= array.length) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        
        return array;
    }
}
```

### Key Components of Generic Method Declaration

```java
public class MethodSyntaxDemo {
    
    // 1. Type parameter section before return type
    //    <T> - declares a type parameter T
    //    T - return type
    public <T> T identity(T input) {
        return input;
    }
    
    // 2. Multiple type parameters
    public <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        return inverted;
    }
    
    // 3. Generic method with varargs
    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }
}
```

## 🎯 **Type Parameter Scoping: Method vs. Class**

### Method-Level Type Parameters

```java
public class ScopeDemo<T> {  // Class-level type parameter T
    
    private T classLevelField;
    
    // Method-level type parameter T SHADOWS class-level T
    // These are DIFFERENT T's!
    public <T> T methodWithSameName(T param) {
        // Here, T refers to method type parameter
        // Cannot access class-level T directly
        System.out.println("Method T: " + param.getClass());
        return param;
    }
    
    // Better practice: Use different names
    public <U> U methodWithDifferentName(U param) {
        System.out.println("Class T field: " + classLevelField);
        System.out.println("Method U param: " + param);
        return param;
    }
    
    // Mixing class and method type parameters
    public <U> Map<T, U> createMap(U value) {
        Map<T, U> map = new HashMap<>();
        map.put(classLevelField, value);
        return map;
    }
}
```

### Understanding Shadowing

```java
public class ShadowingExample {
    public static void main(String[] args) {
        ScopeDemo<String> demo = new ScopeDemo<>();
        demo.setClassLevelField("Class Level");
        
        // Method T is Integer, different from class T (String)
        Integer result = demo.<Integer>methodWithSameName(123);
        System.out.println(result);  // 123
        
        // Explicit type witness shows the difference
        String stringResult = demo.<String>methodWithSameName("Method Level");
        System.out.println(stringResult);  // "Method Level"
    }
}
```

## ⚡ **Static Generic Methods**

### Static Methods Cannot Use Class Type Parameters

```java
public class StaticMethods<T> {
    private T instanceField;
    
    // ❌ CANNOT do this - static method cannot use class type parameter
    // public static T staticMethod() { return instanceField; }
    
    // ✅ Static method with its own type parameter
    public static <S> S firstElement(List<S> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    // ✅ Static method can use class type parameter if it's in the signature
    public static <S> boolean contains(List<S> list, S element) {
        return list.contains(element);
    }
}
```

### Practical Static Generic Utility Methods

```java
public class CollectionUtils {
    
    // Find maximum element in a collection
    public static <T extends Comparable<T>> T max(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        
        Iterator<T> iterator = collection.iterator();
        T max = iterator.next();
        
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        
        return max;
    }
    
    // Convert array to set
    public static <T> Set<T> arrayToSet(T[] array) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, array);
        return set;
    }
    
    // Merge two maps
    public static <K, V> Map<K, V> mergeMaps(
            Map<K, V> map1, 
            Map<K, V> map2, 
            BinaryOperator<V> mergeFunction) {
        
        Map<K, V> result = new HashMap<>(map1);
        map2.forEach((key, value) -> 
            result.merge(key, value, mergeFunction));
        
        return result;
    }
}
```

## 🔄 **Generic Methods with Return Type Inference**

### Type Inference in Action

```java
public class InferenceDemo {
    
    // Generic method with complex return type
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    // Chaining generic methods
    public static <T, R> List<R> transform(
            List<T> list, 
            Function<T, R> transformer) {
        
        List<R> result = new ArrayList<>();
        for (T item : list) {
            result.add(transformer.apply(item));
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        
        // Type inference at work - compiler figures out types
        List<String> longNames = filter(names, s -> s.length() > 4);
        List<Integer> nameLengths = transform(names, String::length);
        
        // Explicit type witness (rarely needed)
        List<String> explicit = InferenceDemo.<String>filter(names, s -> s.startsWith("A"));
        
        System.out.println("Long names: " + longNames);
        System.out.println("Name lengths: " + nameLengths);
    }
}
```

## 🏗️ **Advanced Generic Method Patterns**

### Factory Pattern with Generic Methods

```java
public class ObjectFactory {
    
    // Generic factory method
    public static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance", e);
        }
    }
    
    // Factory with supplier
    public static <T> T create(Supplier<T> supplier) {
        return supplier.get();
    }
    
    // Factory with constructor arguments
    public static <T> T createWithArgs(
            Class<T> clazz, 
            Object... args) throws Exception {
        
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        
        return clazz.getConstructor(paramTypes).newInstance(args);
    }
}

// Usage
String stringInstance = ObjectFactory.createInstance(String.class);
List<String> listInstance = ObjectFactory.create(ArrayList::new);
```

### Builder Pattern with Generic Methods

```java
public class QueryBuilder {
    
    // Generic fluent interface
    public static <T> Query<T> selectFrom(Class<T> entityClass) {
        return new Query<>(entityClass);
    }
    
    public static class Query<T> {
        private Class<T> entityClass;
        private List<String> fields = new ArrayList<>();
        private List<Predicate<T>> predicates = new ArrayList<>();
        
        private Query(Class<T> entityClass) {
            this.entityClass = entityClass;
        }
        
        public Query<T> select(String... fields) {
            this.fields.addAll(Arrays.asList(fields));
            return this;
        }
        
        public Query<T> where(Predicate<T> predicate) {
            this.predicates.add(predicate);
            return this;
        }
        
        public List<T> execute() {
            // Execute query and return typed results
            return new ArrayList<>();
        }
    }
}

// Usage
List<User> users = QueryBuilder.selectFrom(User.class)
    .select("id", "name", "email")
    .where(user -> user.isActive())
    .execute();
```

## 🧩 **Generic Methods in Collections Framework**

### Real Examples from Java API

```java
public class CollectionsFrameworkExamples {
    
    // Collections.sort() - generic method
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        // Implementation
    }
    
    // Collections.binarySearch() 
    public static <T> int binarySearch(
            List<? extends Comparable<? super T>> list, 
            T key) {
        // Implementation
        return 0;
    }
    
    // Arrays.asList() - generic varargs
    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        return new ArrayList<>(a);
    }
    
    // Stream.map() - generic method in Stream interface
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        // Implementation
        return null;
    }
}
```

## ⚠️ **Common Pitfalls and Solutions**

### 1. **Type Erasure Limitations**

```java
public class ErasurePitfall {
    
    // ❌ Can't create array of generic type
    public static <T> T[] toArray(List<T> list) {
        // T[] array = new T[list.size()];  // Compile error!
        
        // ✅ Workaround 1: Use Array.newInstance with Class<T>
        public static <T> T[] toArray(List<T> list, Class<T> clazz) {
            @SuppressWarnings("unchecked")
            T[] array = (T[]) Array.newInstance(clazz, list.size());
            return list.toArray(array);
        }
        
        // ✅ Workaround 2: Let caller provide array
        public static <T> T[] toArray(List<T> list, T[] array) {
            return list.toArray(array);
        }
    }
}
```

### 2. **Overloading Generic Methods**

```java
public class OverloadingIssues {
    
    // ❌ These conflict after type erasure
    // public void process(List<String> list) {}
    // public void process(List<Integer> list) {}  // Compile error!
    
    // ✅ Different type parameters to avoid conflict
    public <T> void processList(List<T> list) {
        // Process any list
    }
    
    public void processStringList(List<String> list) {
        // String-specific processing
    }
}
```

### 3. **Bridge Methods Consideration**

```java
public interface Processor<T> {
    void process(T item);
}

public class StringProcessor implements Processor<String> {
    
    // This generates a bridge method:
    // public void process(Object item) {
    //     process((String) item);
    // }
    
    @Override
    public void process(String item) {
        System.out.println("Processing: " + item);
    }
}
```

## 🧪 **Practical Exercise: Generic Validation Framework**

```java
public class Validator {
    
    // Generic validation method
    public static <T> ValidationResult<T> validate(
            T object, 
            Predicate<T> validator, 
            String errorMessage) {
        
        if (validator.test(object)) {
            return ValidationResult.success(object);
        } else {
            return ValidationResult.failure(errorMessage);
        }
    }
    
    // Chain multiple validators
    @SafeVarargs
    public static <T> ValidationResult<T> validateAll(
            T object, 
            ValidatorRule<T>... rules) {
        
        ValidationResult<T> result = ValidationResult.success(object);
        
        for (ValidatorRule<T> rule : rules) {
            result = rule.validate(object);
            if (!result.isValid()) {
                break;
            }
        }
        
        return result;
    }
    
    public static class ValidationResult<T> {
        private final boolean valid;
        private final T value;
        private final String error;
        
        private ValidationResult(boolean valid, T value, String error) {
            this.valid = valid;
            this.value = value;
            this.error = error;
        }
        
        public static <T> ValidationResult<T> success(T value) {
            return new ValidationResult<>(true, value, null);
        }
        
        public static <T> ValidationResult<T> failure(String error) {
            return new ValidationResult<>(false, null, error);
        }
        
        public boolean isValid() { return valid; }
        public T getValue() { return value; }
        public String getError() { return error; }
    }
    
    @FunctionalInterface
    public interface ValidatorRule<T> {
        ValidationResult<T> validate(T object);
    }
}

// Usage
Validator.ValidationResult<String> emailResult = 
    Validator.validate("test@example.com", 
        email -> email.contains("@"), 
        "Invalid email format");

Validator.ValidationResult<Integer> ageResult = 
    Validator.validateAll(25,
        age -> Validator.validate(age, a -> a >= 0, "Age cannot be negative"),
        age -> Validator.validate(age, a -> a <= 150, "Age cannot exceed 150"));
```

## 📊 **Comparison: Generic Methods vs Generic Classes**

| Aspect | Generic Methods | Generic Classes |
|--------|----------------|-----------------|
| **Scope** | Method-level only | Class-level (all instances) |
| **Flexibility** | Each call can have different type | Same type for all instances |
| **Static Methods** | Can be static | Cannot use class type parameters in static methods |
| **Use Case** | Utility methods, algorithms | Data structures, containers |
| **Type Inference** | Compiler infers from arguments | Must specify at instantiation |

## 🎓 **Best Practices**

1. **Use descriptive type parameter names** when they differ from class parameters
2. **Keep generic methods small and focused** on a single responsibility
3. **Use wildcards appropriately** for maximum flexibility (covered in later chapters)
4. **Document constraints** on type parameters with Javadoc
5. **Consider using bounded type parameters** (next chapter) for constraints
6. **Test generic methods with multiple type arguments**

## 🚀 **Next Steps**

Generic methods become even more powerful when combined with constraints. In [Chapter 5](#5-bounded-type-parameters), we'll explore **Bounded Type Parameters** - how to restrict type parameters to specific hierarchies, enabling you to call methods on generic types with confidence.

> 💡 **Practice Exercise**: Create a `MathUtils` class with generic methods for `min()`, `max()`, and `average()` that work with any `Number` type. Then create a generic `Pair` class with a method to swap its elements, and a static factory method to create pairs.
