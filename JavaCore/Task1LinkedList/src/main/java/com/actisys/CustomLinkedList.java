package com.actisys;

public class CustomLinkedList<T> {

  private static class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data) {
      this.data = data;
      this.next = null;
    }
  }


  private Node<T> head;
  private Node<T> tail;
  private int size;

  public CustomLinkedList() {
    head = null;
    tail = null;
    size = 0;
  }

  public void addFirst(T data) {
    Node<T> newNode = new Node<>(data);
    if (head == null) {
      head = newNode;
      tail = newNode;
    } else {
      newNode.next = head;
      head = newNode;
    }
    size++;
  }

  public void addLast(T el) {
    Node<T> node = new Node<>(el);
    if (head == null) {
      head = node;
      tail = node;
    } else {
      tail.next = node;
      tail = node;
    }
    size++;
  }

  public T getFirst() {
    if (head == null) {
      return null;
    }
    return head.data;
  }

  public T getLast() {
    if (tail == null) {
      return null;
    }
    return tail.data;
  }

  public T get(int index) {
    if (index < 0 || index >= size) {
      return null;
    }
    Node<T> current = head;
    for (int i = 0; i < index; i++) {
      current = current.next;
    }
    return current.data;
  }

  public void add(int index, T el) {
    if (index < 0 || index > size) {
      return;
    }
    if (index == 0) {
      addFirst(el);
    } else if (index == size) {
      addLast(el);
    } else {
      Node<T> newNode = new Node<>(el);
      Node<T> current = head;
      for (int i = 0; i < index - 1; i++) {
        current = current.next;
      }
      newNode.next = current.next;
      current.next = newNode;
      size++;
    }
  }

  public T removeFirst() {
    if (head == null) {
      return null;
    }
    T data = head.data;
    head = head.next;
    if (head == null) {
      tail = null;
    }
    size--;
    return data;
  }

  public T removeLast() {
    if (tail == null) {
      return null;
    }
    T data = tail.data;
    if (head.next == null) {
      tail = head = null;
      size--;
      return data;
    }
    Node<T> current = head;
    for (int i = 0; i < size - 2; i++) {
      current = current.next;
    }
    current.next = null;
    tail = current;
    size--;
    return data;
  }

  public T remove(int index) {
    if (index < 0 || index >= size) {
      return null;
    } else if (index == 0) {
      return removeFirst();
    } else if (index == size - 1) {
      return removeLast();
    }
    Node<T> current = head;
    for (int i = 0; i < index - 1; i++) {
      current = current.next;
    }
    T data = current.next.data;
    current.next = current.next.next;
    size--;
    return data;
  }

  public int getSize() {
    return size;
  }

}