package com.actisys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTests {

  private CustomLinkedList<String> list;

  @BeforeEach
  void setUp() {
    list = new CustomLinkedList<>();
  }

  @Test
  @DisplayName("Add element to the beginning of the list")
  void testAddFirst() {
    list.addFirst("firstElement");
    list.addFirst("secondElement");
    assertEquals(2, list.getSize());
    assertEquals("secondElement", list.getFirst());
  }

  @Test
  @DisplayName("Add element to the end of the list")
  void testAddLast() {
    list.addLast("preLastElement");
    list.addLast("lastElement");
    assertEquals(2, list.getSize());
    assertEquals("lastElement", list.getLast());
  }

  @Test
  @DisplayName("Add element at specific index in the middle")
  void testAdd() {
    list.addFirst("firstElement");
    list.addLast("lastElement");
    list.add(1, "middleElement");

    assertEquals(3, list.getSize());
    assertEquals("firstElement", list.get(0));
    assertEquals("middleElement", list.get(1));
    assertEquals("lastElement", list.get(2));
  }

  @Test
  @DisplayName("Remove first element from the list")
  void testRemoveFirst() {
    list.addFirst("firstElement");
    list.addLast("lastElement");
    list.removeFirst();
    assertEquals(1, list.getSize());
    assertEquals("lastElement", list.getFirst());
  }

  @Test
  @DisplayName("Remove last element from the list")
  void testRemoveLast() {
    list.addFirst("firstElement");
    list.addLast("lastElement");
    list.removeLast();
    assertEquals(1, list.getSize());
    assertEquals("firstElement", list.getLast());
  }

  @Test
  @DisplayName("Remove element at specific index")
  void testRemove() {
    list.addFirst("firstElement");
    list.addLast("secondElement");
    list.addLast("thirdElement");

    String removed = list.remove(1);
    assertEquals("secondElement", removed);
    assertEquals(2, list.getSize());
    assertEquals("firstElement", list.get(0));
    assertEquals("thirdElement", list.get(1));
  }

  @Test
  @DisplayName("Attempt to remove element with invalid index")
  void testRemoveByInvalidIndex() {
    list.addFirst("firstElement");
    assertNull(list.remove(-1));
    assertNull(list.remove(1));
    assertEquals(1, list.getSize());
  }

  @Test
  @DisplayName("Get elements by valid indices")
  void testGet() {
    list.addFirst("firstElement");
    list.addLast("secondElement");
    list.addLast("thirdElement");

    assertEquals("firstElement", list.get(0));
    assertEquals("secondElement", list.get(1));
    assertEquals("thirdElement", list.get(2));
  }

  @Test
  @DisplayName("Attempt to get elements with invalid indices")
  void testGetByInvalidIndex() {
    list.addFirst("firstElement");
    assertNull(list.get(-1));
    assertNull(list.get(1));
  }

  @Test
  @DisplayName("Get first element from empty list")
  void testGetFirstEmpty() {
    assertNull(list.getFirst());
  }

  @Test
  @DisplayName("Get first element from non-empty list")
  void testGetFirst() {
    list.addFirst("firstElement");
    list.addLast("secondElement");
    assertEquals("firstElement", list.getFirst());
  }

  @Test
  @DisplayName("Get last element from empty list")
  void testGetLastEmpty() {
    assertNull(list.getLast());
  }

  @Test
  @DisplayName("Get last element from non-empty list")
  void testGetLast() {
    list.addFirst("firstElement");
    list.addLast("secondElement");
    assertEquals("secondElement", list.getLast());
  }
}