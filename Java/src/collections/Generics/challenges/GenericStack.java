package challenges;
import java.util.EmptyStackException;

public class GenericStack<T> {
    private T[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    
    @SuppressWarnings("unchecked")
    public GenericStack() {
        // Workaround: Create Object array and cast to T[]
        array = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }
    
    @SuppressWarnings("unchecked")
    public GenericStack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        array = (T[]) new Object[initialCapacity];
        size = 0;
    }
    
    public void push(T item) {
        if (size == array.length) {
            resizeArray();
        }
        array[size++] = item;
    }
    
    @SuppressWarnings("unchecked")
    private void resizeArray() {
        int newCapacity = array.length * 2;
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }
    
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T item = array[--size];
        array[size] = null; // Prevent memory leak
        return item;
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array[size - 1];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
      return size;
    }
    public static void main(String[] args) {
      GenericStack<Integer> stack = new GenericStack<>();
    }
}