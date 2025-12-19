# Chapter 17: 🧩 **Erasure of Generic Types**

## What Happens to Classes and Interfaces After Type Erasure

Type erasure transforms generic types into raw types, but the process has important nuances for classes and interfaces. This chapter dives deep into how generic types are erased, how bridge methods preserve polymorphism, and the limitations this imposes at runtime.

## 🏗️ **Class/Interface Erasure in Detail**

### How Different Generic Types are Erased

```java
import java.io.Serializable;
import java.lang.reflect.*;

public class ClassInterfaceErasure {
    
    // Example 1: Simple generic class
    class Box<T> {
        private T value;
        
        public Box(T value) {
            this.value = value;
        }
        
        public T getValue() {
            return value;
        }
        
        public void setValue(T value) {
            this.value = value;
        }
    }
    
    // After erasure of Box<String>:
    // class Box {
    //     private Object value;   // T -> Object
    //     
    //     public Box(Object value) {
    //         this.value = value;
    //     }
    //     
    //     public Object getValue() {
    //         return value;
    //     }
    //     
    //     public void setValue(Object value) {
    //         this.value = value;
    //     }
    // }
    
    // Example 2: Generic class with bounded type parameter
    class NumberContainer<T extends Number> {
        private T number;
        
        public double doubleValue() {
            return number.doubleValue();  // Can call Number methods
        }
        
        public T getNumber() {
            return number;
        }
    }
    
    // After erasure:
    // class NumberContainer {
    //     private Number number;   // T -> Number (the bound)
    //     
    //     public double doubleValue() {
    //         return number.doubleValue();
    //     }
    //     
    //     public Number getNumber() {
    //         return number;
    //     }
    // }
    
    // Example 3: Multiple type parameters
    class Pair<K, V> {
        private K key;
        private V value;
        
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() { return key; }
        public V getValue() { return value; }
    }
    
    // After erasure:
    // class Pair {
    //     private Object key;    // K -> Object
    //     private Object value;  // V -> Object
    //     
    //     public Pair(Object key, Object value) {
    //         this.key = key;
    //         this.value = value;
    //     }
    //     
    //     public Object getKey() { return key; }
    //     public Object getValue() { return value; }
    // }
    
    // Example 4: Generic interface
    interface Repository<T> {
        T findById(Long id);
        void save(T entity);
    }
    
    // After erasure:
    // interface Repository {
    //     Object findById(Long id);  // T -> Object
    //     void save(Object entity);
    // }
    
    // Example 5: Class implementing generic interface
    class UserRepository implements Repository<String> {
        @Override
        public String findById(Long id) {
            return "User" + id;
        }
        
        @Override
        public void save(String entity) {
            System.out.println("Saving: " + entity);
        }
    }
    
    // After erasure of UserRepository:
    // class UserRepository implements Repository {
    //     // These are the methods WE wrote:
    //     public String findById(Long id) {
    //         return "User" + id;
    //     }
    //     
    //     public void save(String entity) {
    //         System.out.println("Saving: " + entity);
    //     }
    //     
    //     // But Repository interface expects:
    //     // Object findById(Long id)
    //     // void save(Object entity)
    //     
    //     // Solution: COMPILER GENERATES BRIDGE METHODS!
    // }
    
    // Using reflection to examine erasure
    public static void examineErasure() throws Exception {
        System.out.println("=== Examining Erasure with Reflection ===\n");
        
        // Get the Box class
        Class<?> boxClass = Class.forName("ClassInterfaceErasure$Box");
        System.out.println("Box class: " + boxClass.getName());
        
        // Check if it's generic
        TypeVariable<?>[] typeParams = boxClass.getTypeParameters();
        System.out.println("Type parameters: " + typeParams.length);
        for (TypeVariable<?> param : typeParams) {
            System.out.println("  " + param.getName() + 
                " extends " + Arrays.toString(param.getBounds()));
        }
        
        // Get generic superclass
        Type genericSuperclass = boxClass.getGenericSuperclass();
        System.out.println("Generic superclass: " + genericSuperclass);
        
        // Get erased superclass
        Class<?> erasedSuperclass = boxClass.getSuperclass();
        System.out.println("Erased superclass: " + erasedSuperclass);
        
        // Examine methods
        System.out.println("\nMethods of Box class:");
        Method[] methods = boxClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  " + method.getName() + 
                " -> " + method.getReturnType().getSimpleName() +
                "(" + Arrays.toString(method.getParameterTypes()) + ")" +
                " [Generic: " + method.toGenericString() + "]");
        }
        
        // Examine UserRepository
        System.out.println("\n=== Examining UserRepository ===");
        Class<?> userRepoClass = Class.forName("ClassInterfaceErasure$UserRepository");
        
        System.out.println("UserRepository methods:");
        Method[] userRepoMethods = userRepoClass.getDeclaredMethods();
        for (Method method : userRepoMethods) {
            System.out.println("  " + method.getName() + 
                " [Bridge: " + method.isBridge() + 
                ", Synthetic: " + method.isSynthetic() + "]");
        }
    }
    
    // Example 6: Nested generics
    class Container<T> {
        class Inner<S> {
            T outerField;
            S innerField;
            
            Inner(T outer, S inner) {
                this.outerField = outer;
                this.innerField = inner;
            }
        }
        
        Inner<String> createInner(T outer, String inner) {
            return new Inner<>(outer, inner);
        }
    }
    
    // After erasure:
    // class Container {
    //     class Inner {
    //         Object outerField;  // T -> Object
    //         Object innerField;  // S -> Object
    //         
    //         Inner(Object outer, Object inner) {
    //             this.outerField = outer;
    //             this.innerField = inner;
    //         }
    //     }
    //     
    //     Inner createInner(Object outer, String inner) {
    //         return new Inner(outer, inner);
    //     }
    // }
    
    // Example 7: Static nested generic class
    static class StaticPair<A, B> {
        A first;
        B second;
        
        StaticPair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
    
    // Static nested classes erase independently
    // After erasure:
    // static class StaticPair {
    //     Object first;
    //     Object second;
    //     
    //     StaticPair(Object first, Object second) {
    //         this.first = first;
    //         this.second = second;
    //     }
    // }
    
    public static void main(String[] args) throws Exception {
        examineErasure();
    }
}
```

## 🌉 **Bridge Method Generation**

### How Polymorphism is Preserved After Erasure

```java
import java.lang.reflect.*;
import java.util.*;

public class BridgeMethodGeneration {
    
    // The Bridge Method Problem:
    // After erasure, method signatures don't match between
    // generic interfaces/classes and their implementations
    
    // Example 1: Comparable interface
    static class MyComparable implements Comparable<MyComparable> {
        private int value;
        
        public MyComparable(int value) {
            this.value = value;
        }
        
        // This is the method we implement
        @Override
        public int compareTo(MyComparable other) {
            return Integer.compare(this.value, other.value);
        }
        
        // The compiler generates this bridge method:
        // public int compareTo(Object other) {
        //     return compareTo((MyComparable) other);
        // }
    }
    
    // Example 2: Generic interface with implementation
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
    
    // Example 3: Generic superclass
    static abstract class AbstractProcessor<T> {
        public abstract T process(T input);
    }
    
    static class IntegerProcessor extends AbstractProcessor<Integer> {
        @Override
        public Integer process(Integer input) {
            return input * 2;
        }
        
        // Bridge method generated:
        // public Object process(Object input) {
        //     return process((Integer) input);
        // }
    }
    
    // Example 4: Covariant return types with generics
    static class Animal {
        public Animal reproduce() {
            return new Animal();
        }
    }
    
    static class Dog extends Animal {
        @Override
        public Dog reproduce() {  // Covariant return
            return new Dog();
        }
    }
    
    // Generic version:
    static class GenericAnimal<T extends Animal> {
        public T reproduce() {
            return null;  // Simplified
        }
    }
    
    static class GenericDog extends GenericAnimal<Dog> {
        @Override
        public Dog reproduce() {
            return new Dog();
        }
        
        // Bridge method generated:
        // public Animal reproduce() {
        //     return reproduce();  // Calls Dog reproduce()
        // }
        // Actually, it's: return (Animal) reproduce();
    }
    
    // Example 5: Multiple bridge methods
    interface Converter<S, T> {
        T convert(S source);
    }
    
    static class StringToIntConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(String source) {
            return source.length();
        }
        
        // Bridge method generated:
        // public Object convert(Object source) {
        //     return convert((String) source);
        // }
    }
    
    // Using reflection to find bridge methods
    public static void examineBridgeMethods() throws Exception {
        System.out.println("=== Examining Bridge Methods ===\n");
        
        // Examine MyComparable
        Class<?> myComparableClass = MyComparable.class;
        System.out.println("MyComparable methods:");
        Method[] methods = myComparableClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  " + method.getName() + 
                "(" + Arrays.toString(method.getParameterTypes()) + ")" +
                " -> " + method.getReturnType().getSimpleName() +
                " [Bridge: " + method.isBridge() + 
                ", Synthetic: " + method.isSynthetic() + "]");
        }
        
        // Examine StringProcessor
        System.out.println("\nStringProcessor methods:");
        Class<?> stringProcessorClass = StringProcessor.class;
        Method[] spMethods = stringProcessorClass.getDeclaredMethods();
        for (Method method : spMethods) {
            System.out.println("  " + method.getName() + 
                "(" + Arrays.toString(method.getParameterTypes()) + ")" +
                " -> " + method.getReturnType().getSimpleName() +
                " [Bridge: " + method.isBridge() + "]");
            
            if (method.isBridge()) {
                // Show the actual bytecode of bridge method
                System.out.println("    Bytecode: " + method);
            }
        }
        
        // Test that bridge methods work
        System.out.println("\n=== Testing Bridge Methods ===");
        
        // Through interface reference
        Processor<String> processor = new StringProcessor();
        String result = processor.process("hello");
        System.out.println("Processor result: " + result);
        
        // What really happens:
        // 1. processor is Processor<String> (erased to Processor)
        // 2. Processor interface has: Object process(Object)
        // 3. Bridge method is called, which casts and delegates
        // 4. Actual StringProcessor.process(String) is called
        
        // Using raw type (demonstrates bridge method)
        Processor rawProcessor = new StringProcessor();
        Object rawResult = rawProcessor.process("world");
        System.out.println("Raw processor result: " + rawResult);
    }
    
    // Example 6: Bridge methods for multiple inheritance
    interface A<T> {
        T methodA(T input);
    }
    
    interface B<T> {
        T methodB(T input);
    }
    
    static class MultipleInterfaces implements A<String>, B<String> {
        @Override
        public String methodA(String input) {
            return "A: " + input;
        }
        
        @Override
        public String methodB(String input) {
            return "B: " + input;
        }
        
        // Two bridge methods generated:
        // public Object methodA(Object input) {
        //     return methodA((String) input);
        // }
        // public Object methodB(Object input) {
        //     return methodB((String) input);
        // }
    }
    
    // Example 7: Complex inheritance hierarchy
    static abstract class Base<T> {
        abstract T process(T input);
    }
    
    static abstract class Middle<S> extends Base<S> {
        // No implementation here
    }
    
    static class Concrete extends Middle<String> {
        @Override
        public String process(String input) {
            return "Processed: " + input;
        }
        
        // Bridge method chain:
        // Base class expects: Object process(Object)
        // Middle class doesn't implement
        // Concrete generates: Object process(Object) bridge
    }
    
    // The importance of bridge methods
    public static void demonstrateImportance() {
        System.out.println("\n=== Importance of Bridge Methods ===");
        
        // Without bridge methods, this wouldn't work:
        List<MyComparable> list = new ArrayList<>();
        list.add(new MyComparable(5));
        list.add(new MyComparable(3));
        list.add(new MyComparable(8));
        
        // Collections.sort() calls compareTo(Object) through raw Comparable interface
        Collections.sort(list);
        System.out.println("Sorted list: " + list.stream()
            .map(mc -> mc.value)
            .toList());
        
        // The bridge method enables:
        // 1. Polymorphic calls through erased interface
        // 2. Type safety with casts
        // 3. Backward compatibility
    }
    
    public static void main(String[] args) throws Exception {
        examineBridgeMethods();
        demonstrateImportance();
    }
    
    // Helper method to get bridge methods
    static List<Method> getBridgeMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(Method::isBridge)
            .collect(java.util.stream.Collectors.toList());
    }
}
```

## ⚠️ **Runtime Type Information (RTTI) Limitations**

### What You Can and Cannot Do at Runtime

```java
import java.util.*;
import java.lang.reflect.*;

public class RTTILimitations {
    
    // LIMITATION 1: instanceof with generic types doesn't work
    
    public static void instanceofLimitation() {
        List<String> stringList = new ArrayList<>();
        
        // ❌ This doesn't compile
        // if (stringList instanceof List<String>) { }
        
        // ✅ Only works with raw types
        if (stringList instanceof List) {
            System.out.println("It's a List (raw)");
        }
        
        // ✅ Workaround: Check element types manually
        if (!stringList.isEmpty()) {
            Object first = stringList.get(0);
            if (first instanceof String) {
                System.out.println("First element is String");
            }
        }
        
        // ✅ Using Class objects
        Class<?> listClass = stringList.getClass();
        System.out.println("List class: " + listClass);
        
        // But this doesn't tell us it's List<String>
        System.out.println("Is ArrayList? " + 
            (listClass == ArrayList.class));  // true
    }
    
    // LIMITATION 2: Cannot create arrays of parameterized types
    
    public static void arrayCreationLimitation() {
        // ❌ This doesn't compile
        // List<String>[] array = new List<String>[10];
        
        // ✅ Only raw or wildcard arrays
        List[] rawArray = new List[10];  // Raw - warning
        List<?>[] wildcardArray = new List<?>[10];  // Wildcard - warning
        
        // ⚠️ But be careful - can lead to heap pollution
        rawArray[0] = new ArrayList<String>();
        rawArray[1] = new ArrayList<Integer>();
        
        // This compiles but is dangerous
        List<String> strings = (List<String>) rawArray[1];  // Unchecked cast
        // strings.add("hello");  // Would cause problems
        
        // ✅ Safe alternative: Use List of Lists
        List<List<String>> listOfLists = new ArrayList<>();
        listOfLists.add(new ArrayList<>());
        listOfLists.get(0).add("Safe");
    }
    
    // LIMITATION 3: Type parameters cannot be used in instanceof checks
    
    public static <T> void checkType(T obj) {
        // ❌ Can't do this
        // if (obj instanceof T) { }
        
        // ✅ Workaround: Pass Class<T> as parameter
    }
    
    public static <T> boolean isInstance(T obj, Class<T> clazz) {
        return clazz.isInstance(obj);  // ✅ Works
    }
    
    // LIMITATION 4: Cannot catch exceptions of generic type
    
    public static void exceptionLimitation() {
        // ❌ This doesn't compile
        // try {
        //     throw new RuntimeException();
        // } catch (Exception<String> e) { }
        
        // Generic exceptions are not allowed
        // class GenericException<T> extends Exception { }  // ❌ Compile error
    }
    
    // LIMITATION 5: Reflection loses generic information
    
    public static void reflectionLimitations() throws Exception {
        List<String> stringList = new ArrayList<>();
        stringList.add("Hello");
        stringList.add("World");
        
        // Get the class
        Class<?> listClass = stringList.getClass();
        
        // Get generic superclass/interfaces
        Type genericType = listClass.getGenericSuperclass();
        System.out.println("Generic superclass: " + genericType);
        
        // But for actual type arguments of the instance, we need more work
        Field field = RTTILimitations.class.getDeclaredField("stringList");
        Type fieldType = field.getGenericType();
        
        if (fieldType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fieldType;
            Type[] typeArgs = pt.getActualTypeArguments();
            System.out.println("Type arguments: " + Arrays.toString(typeArgs));
            // Output: [class java.lang.String]
        }
        
        // However, this only works for fields, not for local variables
        // Local variable type information is not available via reflection
    }
    
    static List<String> stringList;  // Field for reflection example
    
    // LIMITATION 6: Cannot overload methods with same erasure
    
    public static void overloadingLimitation() {
        // These methods would have the same erasure (List)
        // ❌ Not allowed
        // public static void process(List<String> list) { }
        // public static void process(List<Integer> list) { }
    }
    
    // Workaround for RTTI limitations
    
    public static class TypeSafeContainer {
        // Store type information explicitly
        private final Class<?> type;
        private final List<Object> items = new ArrayList<>();
        
        public TypeSafeContainer(Class<?> type) {
            this.type = type;
        }
        
        public void add(Object item) {
            if (!type.isInstance(item)) {
                throw new ClassCastException(
                    "Expected " + type + ", got " + item.getClass());
            }
            items.add(item);
        }
        
        @SuppressWarnings("unchecked")
        public <T> T get(int index, Class<T> clazz) {
            Object item = items.get(index);
            if (clazz.isInstance(item)) {
                return (T) item;
            }
            throw new ClassCastException(
                "Expected " + clazz + ", got " + item.getClass());
        }
        
        // For the declared type
        @SuppressWarnings("unchecked")
        public <T> T getTyped(int index) {
            return (T) items.get(index);  // Unsafe, but we validated on add
        }
    }
    
    // Using Super Type Tokens (Neil Gafter's pattern)
    public abstract static class TypeReference<T> {
        private final Type type;
        
        protected TypeReference() {
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter");
            }
            this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        }
        
        public Type getType() {
            return type;
        }
    }
    
    public static void superTypeTokenDemo() {
        // Capture generic type at runtime
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        System.out.println("Captured type: " + typeRef.getType());
        // Output: java.util.List<java.lang.String>
        
        // This works because anonymous class inherits type parameter
    }
    
    // LIMITATION 7: Cannot instantiate type parameters
    
    public static <T> T createInstance(Class<T> clazz) throws Exception {
        // ❌ Can't do: return new T();
        // ✅ Must pass Class object
        return clazz.getDeclaredConstructor().newInstance();
    }
    
    // Testing the limitations
    public static void main(String[] args) throws Exception {
        System.out.println("=== RTTI Limitations Demo ===\n");
        
        instanceofLimitation();
        System.out.println();
        
        arrayCreationLimitation();
        System.out.println();
        
        // Test TypeSafeContainer
        TypeSafeContainer stringContainer = new TypeSafeContainer(String.class);
        stringContainer.add("Hello");
        stringContainer.add("World");
        // stringContainer.add(123);  // Throws ClassCastException
        
        String first = stringContainer.get(0, String.class);
        System.out.println("First string: " + first);
        
        // Test Super Type Token
        superTypeTokenDemo();
        
        // Test reflection
        reflectionLimitations();
    }
}
```

## 🔍 **Debugging Erasure Issues**

### Common Problems and Solutions

```java
import java.util.*;
import java.lang.reflect.*;

public class ErasureDebugging {
    
    // PROBLEM 1: ClassCastException from raw types
    
    public static void problem1RawTypes() {
        System.out.println("=== Problem 1: Raw Types Causing ClassCastException ===\n");
        
        List rawList = new ArrayList();
        rawList.add("String");
        rawList.add(123);  // Mixing types - problematic!
        
        // Assign to typed list (unchecked warning)
        @SuppressWarnings("unchecked")
        List<String> stringList = rawList;
        
        try {
            // This will fail when we encounter the Integer
            for (String s : stringList) {
                System.out.println(s.toUpperCase());
            }
        } catch (ClassCastException e) {
            System.out.println("Caught ClassCastException: " + e.getMessage());
            System.out.println("Cause: Raw list contained mixed types");
        }
        
        // SOLUTION: Use generic types consistently
        List<String> safeList = new ArrayList<>();
        safeList.add("String");
        // safeList.add(123);  // Compile error - prevents the problem
    }
    
    // PROBLEM 2: Bridge method confusion in stack traces
    
    public static void problem2BridgeMethods() {
        System.out.println("\n=== Problem 2: Bridge Methods in Stack Traces ===\n");
        
        // Create a processor
        Processor<String> processor = new StringProcessor();
        
        try {
            // This will call the bridge method
            Object result = ((Processor)processor).process(123);  // Wrong type
            System.out.println("Result: " + result);
        } catch (ClassCastException e) {
            System.out.println("Stack trace shows bridge method:");
            e.printStackTrace(System.out);
            System.out.println("\nNote the bridge method in stack trace");
        }
        
        // SOLUTION: Ensure type safety at compile time
        // Don't use raw types, and the compiler will catch errors
    }
    
    interface Processor<T> {
        T process(T input);
    }
    
    static class StringProcessor implements Processor<String> {
        @Override
        public String process(String input) {
            return input.toUpperCase();
        }
    }
    
    // PROBLEM 3: Reflection doesn't see generic methods as overridden
    
    public static void problem3ReflectionOverride() throws Exception {
        System.out.println("\n=== Problem 3: Reflection and Generic Overrides ===\n");
        
        Class<?> clazz = MyComparable.class;
        Method[] methods = clazz.getDeclaredMethods();
        
        System.out.println("Methods in MyComparable:");
        for (Method method : methods) {
            System.out.println("  " + method.getName() + 
                " [Bridge: " + method.isBridge() + "]");
            
            // Find the compareTo methods
            if (method.getName().equals("compareTo")) {
                System.out.println("    Parameters: " + 
                    Arrays.toString(method.getParameterTypes()));
                System.out.println("    Generic: " + method.toGenericString());
            }
        }
        
        // The bridge method hides the fact that we're overriding
        // Comparable<String>.compareTo(String)
        
        // SOLUTION: Check for bridge methods and their actual implementations
        for (Method method : methods) {
            if (method.isBridge()) {
                // Find the actual method it bridges to
                System.out.println("Bridge method " + method + 
                    " delegates to actual method");
            }
        }
    }
    
    static class MyComparable implements Comparable<String> {
        @Override
        public int compareTo(String other) {
            return 0;
        }
    }
    
    // PROBLEM 4: Type information lost in collections
    
    public static void problem4LostTypeInfo() {
        System.out.println("\n=== Problem 4: Lost Type Information in Collections ===\n");
        
        // Create a typed collection
        List<List<String>> nested = new ArrayList<>();
        nested.add(Arrays.asList("A", "B"));
        nested.add(Arrays.asList("C", "D"));
        
        // After erasure, the inner list type is lost
        Object obj = nested.get(0);
        System.out.println("Type of first element: " + obj.getClass());
        System.out.println("Is List? " + (obj instanceof List));  // true
        System.out.println("Is List<String>? Can't check at runtime!");
        
        // This is why this doesn't work:
        // List<String> first = (List<String>) nested.get(0);  // Warning
        
        // SOLUTION: Use checked collections
        List<List<String>> checked = Collections.checkedList(
            new ArrayList<>(), (Class<List<String>>)(Class<?>)List.class);
        // Note: This only checks outer list, not inner lists
        
        // Better: Validate when extracting
        Object first = nested.get(0);
        if (first instanceof List) {
            List<?> list = (List<?>) first;
            // Check each element
            for (Object item : list) {
                if (!(item instanceof String)) {
                    throw new ClassCastException("Not a string: " + item);
                }
            }
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) list;
            System.out.println("Safe to use: " + stringList);
        }
    }
    
    // PROBLEM 5: Generic signatures in bytecode
    
    public static void problem5BytecodeSignatures() throws Exception {
        System.out.println("\n=== Problem 5: Generic Signatures in Bytecode ===\n");
        
        // Generic information IS preserved in bytecode as metadata
        // But it's not used by the JVM for type checking
        
        Field field = ErasureDebugging.class.getDeclaredField("genericList");
        Type genericType = field.getGenericType();
        
        System.out.println("Field type: " + field.getType());
        System.out.println("Generic type: " + genericType);
        
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            System.out.println("Raw type: " + pt.getRawType());
            System.out.println("Type args: " + Arrays.toString(pt.getActualTypeArguments()));
        }
        
        // However, this metadata can be lost in some scenarios:
        // - Through serialization/deserialization
        // - When using reflection to create instances
        // - When mixing compiled code from different Java versions
    }
    
    static List<String> genericList;  // For reflection example
    
    // Debugging tools and techniques
    
    public static class DebugTools {
        // Tool 1: Print generic information
        public static void printGenericInfo(Class<?> clazz) {
            System.out.println("Class: " + clazz.getName());
            
            // Type parameters
            TypeVariable<?>[] typeParams = clazz.getTypeParameters();
            if (typeParams.length > 0) {
                System.out.println("Type parameters:");
                for (TypeVariable<?> param : typeParams) {
                    System.out.println("  " + param.getName() + 
                        " extends " + Arrays.toString(param.getBounds()));
                }
            }
            
            // Generic superclass
            Type genericSuperclass = clazz.getGenericSuperclass();
            if (genericSuperclass != null && 
                !genericSuperclass.equals(Object.class)) {
                System.out.println("Generic superclass: " + genericSuperclass);
            }
            
            // Generic interfaces
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            if (genericInterfaces.length > 0) {
                System.out.println("Generic interfaces:");
                for (Type iface : genericInterfaces) {
                    System.out.println("  " + iface);
                }
            }
            
            // Methods with generic signatures
            System.out.println("Generic methods:");
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.getTypeParameters().isEmpty() ||
                    !method.toGenericString().equals(method.toString())) {
                    System.out.println("  " + method.toGenericString());
                }
            }
        }
        
        // Tool 2: Check for bridge methods
        public static void printBridgeMethods(Class<?> clazz) {
            List<Method> bridgeMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(Method::isBridge)
                .collect(java.util.stream.Collectors.toList());
            
            if (!bridgeMethods.isEmpty()) {
                System.out.println("Bridge methods in " + clazz.getName() + ":");
                for (Method method : bridgeMethods) {
                    System.out.println("  " + method + 
                        " -> " + method.getReturnType() +
                        "(" + Arrays.toString(method.getParameterTypes()) + ")");
                    
                    // Try to find what it bridges to
                    try {
                        // Bridge methods usually delegate to a method with 
                        // the same name but different parameter types
                        Method[] allMethods = clazz.getDeclaredMethods();
                        for (Method m : allMethods) {
                            if (!m.isBridge() && 
                                m.getName().equals(method.getName()) &&
                                !Arrays.equals(m.getParameterTypes(), 
                                              method.getParameterTypes())) {
                                System.out.println("    Bridges to: " + m);
                            }
                        }
                    } catch (Exception e) {
                        // Ignore
                    }
                }
            }
        }
        
        // Tool 3: Validate type safety
        public static <T> void validateType(Object obj, Class<T> expectedType) {
            if (!expectedType.isInstance(obj)) {
                throw new ClassCastException(
                    "Expected " + expectedType + ", got " + 
                    (obj == null ? "null" : obj.getClass()));
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        problem1RawTypes();
        problem2BridgeMethods();
        problem3ReflectionOverride();
        problem4LostTypeInfo();
        problem5BytecodeSignatures();
        
        System.out.println("\n=== Debugging Tools Demo ===");
        DebugTools.printGenericInfo(StringProcessor.class);
        DebugTools.printBridgeMethods(StringProcessor.class);
    }
}
```

## 📊 **Erasion Rules Summary Table**

| Generic Construct | After Erasure | Notes |
|------------------|---------------|-------|
| `List<T>` | `List` | `T` becomes `Object` |
| `List<? extends T>` | `List` | Wildcard information lost |
| `List<? super T>` | `List` | Wildcard information lost |
| `List<String>` | `List` | Type argument `String` lost |
| `T extends Number` | `Number` | Uses first bound |
| `T extends Number & Serializable` | `Number` | Only first bound remains |
| `Class<T>` parameter | `Class` | Type parameter lost |
| Generic method `<T> T method()` | `Object method()` | Return type becomes `Object` |
| Bridge methods | Added by compiler | Preserve polymorphism |

## 🎓 **Key Takeaways**

1. **All generic type information is removed** during compilation (type erasure)
2. **Type parameters become their bounds** (`Object` if unbounded)
3. **Bridge methods are generated** to preserve polymorphism after erasure
4. **Runtime type information is limited** - can't use `instanceof` with generics
5. **Reflection can access generic metadata** but it's not used by JVM
6. **Raw types bypass type safety** - avoid them in new code
7. **Arrays of parameterized types are not allowed** - use collections instead
8. **Understanding erasure helps debug** `ClassCastException` and bridge method issues

## 🚀 **Next Steps**

Now that you understand how generic types are erased, let's explore how generic methods specifically are affected. In [Chapter 18](#18-erasure-of-generic-methods), we'll dive into **Erasure of Generic Methods**, covering method-level type parameter erasure, differences from class erasure, and the implications for overloading and overriding.

> 💡 **Practice Exercise**: 
> 1. Write a generic class and use `javap -c -v` to examine the bridge methods
> 2. Create a situation that causes a `ClassCastException` due to type erasure
> 3. Use reflection to find and invoke a bridge method
> 4. Implement a type-safe heterogeneous container using `Class` tokens
> 5. Write a utility that prints all generic information about a class using reflection