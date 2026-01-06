package com.example.electionsinformationsystem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTableSCTest {
    private HashTableSC<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTableSC<>(5);
    }

    @Test
    void testPutAndGetSingleEntry() {
        hashTable.put("Alice", 100);
        Integer value = hashTable.get("Alice");
        assertNotNull(value);
        assertEquals(100, value);
    }

    @Test
    void testPutOverwriteExistingKey() {
        hashTable.put("Alice", 100);
        hashTable.put("Alice", 200);
        Integer value = hashTable.get("Alice");
        assertEquals(200, value);
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(hashTable.get("Bob"));
    }

    @Test
    void testRemoveExistingKey() {
        hashTable.put("Alice", 100);
        hashTable.remove("Alice");
        assertNull(hashTable.get("Alice"));
    }

    @Test
    void testRemoveNonExistentKey() {
        hashTable.remove("Bob");
        assertNull(hashTable.get("Bob"));
    }

    @Test
    void testClear() {
        hashTable.put("Alice", 100);
        hashTable.put("Bob", 200);

        hashTable.clear();

        assertNull(hashTable.get("Alice"));
        assertNull(hashTable.get("Bob"));
    }
}
