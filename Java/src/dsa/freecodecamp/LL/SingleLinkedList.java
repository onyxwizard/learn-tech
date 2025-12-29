package LL;

/**
 * @author onyxwizard
 * @date 28-12-2025
 */

class Node<T> {
  public T data;
  public Node<T> next;

  Node(T data, Node<T> next) {
    this.data = data;
    this.next = next;
  }
}

public class SingleLinkedList<T> {
  private Node<T> head;
  private int size;

  public SingleLinkedList() {
    this.head = null;
    size = 0;
  }

  public SingleLinkedList(T data) {
    this.head = new Node<>(data, null);
    size = 1;
  }

  public void add(T data) {
    Node<T> temp = new Node<>(data, null);
    if (head == null) {
      head = temp;
    } else {
      Node<T> curr = head;
      while (curr.next != null) {
        curr = curr.next;
      }
      curr.next = temp;
    }
    size++;
  }

  public void add(T data, int idx) {
    if (idx < 0 || idx > size) {
      throw new IndexOutOfBoundsException("Index not suitable");
    }

    Node<T> newNode = new Node<>(data, null);
    if (idx == 0) {
      newNode.next = head;
      head = newNode;
    } else {
      Node<T> cur = head;
      for (int i = 0; i < idx - 1; i++) {
        cur = cur.next;
      }
      newNode.next = cur.next;
      cur.next = newNode;
    }
    size++;
  }

  // Get element at index
  public T get(int idx) {
    if (idx < 0 || idx >= size) {
      throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);
    }

    Node<T> current = head;
    for (int i = 0; i < idx; i++) {
      current = current.next;
    }
    return current.data;
  }

  // Remove element at index
  public T remove(int idx) {
    if (idx < 0 || idx >= size) {
      throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);
    }

    T removedData;
    if (idx == 0) {
      // Remove head
      removedData = head.data;
      head = head.next;
    } else {
      // Remove from middle or end
      Node<T> current = head;
      for (int i = 0; i < idx - 1; i++) {
        current = current.next;
      }
      removedData = current.next.data;
      current.next = current.next.next;
    }
    size--;
    return removedData;
  }

  // Remove element at index
  public T remove() {
    if (size == 0) {
      throw new IndexOutOfBoundsException("Size: " + size);
    }

    T removedData;
    Node<T> current = head;
    while (current.next.next != null) {
      current = current.next;
    }
    removedData = current.next.data;
    current.next = current.next.next;

    size--;
    return removedData;

  }

  // Get size
  public int size() {
    return size;
  }

  // Check if list is empty
  public boolean isEmpty() {
    return size == 0;
  }

  // Display list
  public void display() {
    Node<T> current = head;
    System.out.print("List: ");
    while (current != null) {
      System.out.print(current.data + " -> ");
      current = current.next;
    }
    System.out.println("null");
  }

  // Clear list
  public void clear() {
    head = null;
    size = 0;
  }

  // Find index of element
  public int indexOf(T data) {
    Node<T> current = head;
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

  // Check if contains element
  public boolean contains(T data) {
    return indexOf(data) != -1;
  }

}
