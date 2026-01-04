package com.example.electionsinformationsystem.models;

// single node of hash table
public class HashNode<K, V> {
    // key used to find data
    K key;

    // actual data being stored
    V value;

    // constructor
    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }

}
