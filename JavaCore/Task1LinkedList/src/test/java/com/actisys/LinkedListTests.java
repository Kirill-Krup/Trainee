package com.actisys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTests {
    private CustomLinkedList <String> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test void testAddFirst() {
        list.addFirst("firstElement");
        list.addFirst("secondElement");
        assertEquals(2, list.getSize());
        assertEquals("secondElement", list.getFirst());
    }

    @Test void testAddLast() {
        list.addLast("preLastElement");
        list.addLast("lastElement");
        assertEquals(2, list.getSize());
        assertEquals("lastElement", list.getLast());
    }

    @Test void testAdd() {
        list.addFirst("firstElement");
        list.addLast("lastElement");
        list.add(1, "middleElement");

        assertEquals(3, list.getSize());
        assertEquals("firstElement", list.get(0));
        assertEquals("middleElement", list.get(1));
        assertEquals("lastElement", list.get(2));
    }


    @Test void testRemoveFirst() {
        list.addFirst("firstElement");
        list.addLast("lastElement");
        list.removeFirst();
        assertEquals(1, list.getSize());
        assertEquals("lastElement", list.getFirst());
    }

    @Test void testRemoveLast() {
        list.addFirst("firstElement");
        list.addLast("lastElement");
        list.removeLast();
        assertEquals(1, list.getSize());
        assertEquals("firstElement", list.getLast());
    }

    @Test void testRemove() {
        list.addFirst("firstElement");
        list.addLast("secondElement");
        list.addLast("thirdElement");

        String removed = list.remove(1);
        assertEquals("secondElement", removed);
        assertEquals(2, list.getSize());
        assertEquals("firstElement", list.get(0));
        assertEquals("thirdElement", list.get(1));
    }

    @Test void testRemoveByInvalidIndex() {
        list.addFirst("firstElement");
        assertNull(list.remove(-1));
        assertNull(list.remove(1));
        assertEquals(1, list.getSize());
    }

    @Test void testGet() {
        list.addFirst("firstElement");
        list.addLast("secondElement");
        list.addLast("thirdElement");

        assertEquals("firstElement", list.get(0));
        assertEquals("secondElement", list.get(1));
        assertEquals("thirdElement", list.get(2));
    }

    @Test void testGetByInvalidIndex() {
        list.addFirst("firstElement");
        assertNull(list.get(-1));
        assertNull(list.get(1));
    }

    @Test void testGetFirstEmpty() {
        assertNull(list.getFirst());
    }

    @Test void testGetFirst() {
        list.addFirst("firstElement");
        list.addLast("secondElement");
        assertEquals("firstElement", list.getFirst());
    }

    @Test void testGetLastEmpty() {
        assertNull(list.getLast());
    }

    @Test void testGetLast() {
        list.addFirst("firstElement");
        list.addLast("secondElement");
        assertEquals("secondElement", list.getLast());
    }

//    add(index, el) - adds the element in the list by index

}
