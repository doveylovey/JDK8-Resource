package com.resource.test;

import org.junit.Test;

import java.util.LinkedList;

public class LinkedListTests {
    @Test
    public void test01() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.addFirst("first");
        linkedList.addLast("last");
        linkedList.forEach(System.out::println);
    }
}
