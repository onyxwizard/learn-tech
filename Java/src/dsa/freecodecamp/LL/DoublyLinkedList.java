package LL;

/**
 * @author onyxwizard
 * @date 28-12-2025
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

class DNode<T> {
  
    public T data;
    public DNode<T> prev;
    public DNode<T> next;

    DNode(T data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }

    DNode(T data, DNode<T> prev, DNode<T> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
}

public class DoublyLinkedList<T> {
    private DNode<T> head;
    private DNode<T> tail;
    private int size;

    // Constructors
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public DoublyLinkedList(T data) {
        DNode<T> newNode = new DNode<>(data);
        this.head = newNode;
        this.tail = newNode;
        this.size = 1;
    }

    // Add at end (append)
    public void add(T data) {
        addLast(data);
    }

    // Add at end (append)
    public void addLast(T data) {
        DNode<T> newNode = new DNode<>(data);
        
        if (head == null) {
            // Empty list
            head = newNode;
            tail = newNode;
        } else {
            // Non-empty list
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    // Add at beginning (prepend)
    public void addFirst(T data) {
        DNode<T> newNode = new DNode<>(data);
        
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Add at specific index
    public void add(T data, int idx) {
        if (idx < 0 || idx > size) {
            throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);
        }

        if (idx == 0) {
            addFirst(data);
        } else if (idx == size) {
            addLast(data);
        } else {
            // Find the node before insertion point
            DNode<T> current = getNode(idx - 1);
            DNode<T> newNode = new DNode<>(data, current, current.next);
            
            current.next.prev = newNode;
            current.next = newNode;
            size++;
        }
    }

    // Get element at index
    public T get(int idx) {
        return getNode(idx).data;
    }

    // Get first element
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    // Get last element
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    // Get node at index (helper method)
    private DNode<T> getNode(int idx) {
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);
        }

        // Optimize: start from head or tail based on index position
        if (idx < size / 2) {
            // Start from head
            DNode<T> current = head;
            for (int i = 0; i < idx; i++) {
                current = current.next;
            }
            return current;
        } else {
            // Start from tail
            DNode<T> current = tail;
            for (int i = size - 1; i > idx; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    // Remove element at index
    public T remove(int idx) {
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);
        }

        if (idx == 0) {
            return removeFirst();
        } else if (idx == size - 1) {
            return removeLast();
        } else {
            DNode<T> toRemove = getNode(idx);
            return removeNode(toRemove);
        }
    }

    // Remove first element
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }

        T data = head.data;
        
        if (head == tail) { // Only one element
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    // Remove last element
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }

        T data = tail.data;
        
        if (head == tail) { // Only one element
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    // Remove specific node (helper)
    private T removeNode(DNode<T> node) {
        if (node == head) return removeFirst();
        if (node == tail) return removeLast();
        
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        
        return node.data;
    }

    // Remove first occurrence of data
    public boolean remove(T data) {
        DNode<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Remove all occurrences of data
    public void removeAll(T data) {
        DNode<T> current = head;
        while (current != null) {
            DNode<T> next = current.next;
            if (current.data.equals(data)) {
                removeNode(current);
            }
            current = next;
        }
    }

    // Get size
    public int size() {
        return size;
    }

    // Check if empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Check if contains element
    public boolean contains(T data) {
        return indexOf(data) != -1;
    }

    // Find index of first occurrence
    public int indexOf(T data) {
        DNode<T> current = head;
        int idx = 0;
        while (current != null) {
            if (current.data.equals(data)) {
                return idx;
            }
            current = current.next;
            idx++;
        }
        return -1;
    }

    // Find index of last occurrence
    public int lastIndexOf(T data) {
        DNode<T> current = tail;
        int idx = size - 1;
        while (current != null) {
            if (current.data.equals(data)) {
                return idx;
            }
            current = current.prev;
            idx--;
        }
        return -1;
    }

    // Clear list
    public void clear() {
        // Help garbage collection by breaking links
        DNode<T> current = head;
        while (current != null) {
            DNode<T> next = current.next;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    // Display forward
    public void displayForward() {
        DNode<T> current = head;
        System.out.print("Forward: ");
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" ⇄ ");
            }
            current = current.next;
        }
        System.out.println();
    }

    // Display backward
    public void displayBackward() {
        DNode<T> current = tail;
        System.out.print("Backward: ");
        while (current != null) {
            System.out.print(current.data);
            if (current.prev != null) {
                System.out.print(" ⇄ ");
            }
            current = current.prev;
        }
        System.out.println();
    }

    // Update element at index
    public void set(int idx, T data) {
        DNode<T> node = getNode(idx);
        node.data = data;
    }

    // Convert to array
    public Object[] toArray() {
        Object[] array = new Object[size];
        DNode<T> current = head;
        int i = 0;
        while (current != null) {
            array[i++] = current.data;
            current = current.next;
        }
        return array;
    }

    // Reverse the list
    public void reverse() {
        DNode<T> current = head;
        DNode<T> temp = null;
        
        while (current != null) {
            // Swap prev and next pointers
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            
            // Move to next node (which is actually previous due to swap)
            current = current.prev;
        }
        
        // Swap head and tail
        temp = head;
        head = tail;
        tail = temp;
    }

    // Check if list is palindrome (requires Comparable)
    public boolean isPalindrome() {
        if (isEmpty() || size == 1) {
            return true;
        }
        
        DNode<T> front = head;
        DNode<T> back = tail;
        
        while (front != back && front.prev != back) {
            if (!front.data.equals(back.data)) {
                return false;
            }
            front = front.next;
            back = back.prev;
        }
        return true;
    }

    // Iterator implementation
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private DNode<T> current = head;
            
            @Override
            public boolean hasNext() {
                return current != null;
            }
            
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Descending iterator (from tail to head)
    public Iterator<T> descendingIterator() {
        return new Iterator<T>() {
            private DNode<T> current = tail;
            
            @Override
            public boolean hasNext() {
                return current != null;
            }
            
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                T data = current.data;
                current = current.prev;
                return data;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // For testing
    public static void main(String[] args) {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>();
        
        // Test basic operations
        dll.add(10);
        dll.add(20);
        dll.addFirst(5);
        dll.addLast(30);
        dll.add(15, 2); // Add at index 2
        
        dll.displayForward();  // Forward: 5 ⇄ 10 ⇄ 15 ⇄ 20 ⇄ 30
        dll.displayBackward(); // Backward: 30 ⇄ 20 ⇄ 15 ⇄ 10 ⇄ 5
        
        System.out.println("Size: " + dll.size()); // 5
        System.out.println("First: " + dll.getFirst()); // 5
        System.out.println("Last: " + dll.getLast()); // 30
        System.out.println("Element at index 2: " + dll.get(2)); // 15
        
        // Test removal
        dll.removeFirst();
        dll.removeLast();
        dll.remove(1); // Remove at index 1
        
        dll.displayForward(); // Forward: 10 ⇄ 20
        
        // Test reverse
        dll.addFirst(5);
        dll.addLast(25);
        dll.displayForward(); // Forward: 5 ⇄ 10 ⇄ 20 ⇄ 25
        dll.reverse();
        dll.displayForward(); // Forward: 25 ⇄ 20 ⇄ 10 ⇄ 5
        
        // Test iterator
        System.out.print("Using iterator: ");
        Iterator<Integer> it = dll.iterator();
        
        while (it.hasNext()) {
          System.out.print(it.next() + " ");
        }
        
        System.out.println();
        
        // Clear list
        dll.clear();
        System.out.println("Is empty after clear: " + dll.isEmpty()); // true
    }
}
