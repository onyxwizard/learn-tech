package Iterator.challenges;

import java.util.Iterator;

public class StackIterator<T> implements Iterator {
    private final T[] data;
    private int topIndex;

    public StackIterator(T[] data, int size) {
        this.data = data;
        this.topIndex = size - 1; // points to top element
    }

    @Override
    public boolean hasNext() {
        return topIndex > -1; // ❌ suspicious
    }

    @Override
    public T next() {
        return data[topIndex--];
    }
    public static void main(String[] args) {
        String[] stack = {"first", "second", "third"}; // "third" is top
        int size = 3;
        Iterator<String> it = new StackIterator<>(stack, size);

        while (it.hasNext()) {
        System.out.println(it.next());
}
    }
}
