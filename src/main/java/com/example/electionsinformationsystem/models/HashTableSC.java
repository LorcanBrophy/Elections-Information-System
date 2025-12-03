package com.example.electionsinformationsystem.models;

public class HashTableSC<T> {
    private final LinkedList<T>[] hashTable;
    private final int numBuckets;

    @SuppressWarnings("unchecked")
    public HashTableSC(int size) {
        this.numBuckets = size;
        hashTable = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            hashTable[i] = new LinkedList<>();
        }
    }

    private int hashFunction(T key) {
        int hashCode = key.hashCode();
        int index = hashCode % numBuckets;
        index = index < 0 ? -index : index;
        return index;
    }

    public void add(T data) {
        int index = hashFunction(data);
        hashTable[index].add(data);
    }

    public void remove(T data) {
        int index = hashFunction(data);
        hashTable[index].remove(data);
    }

    public T get(T data) {
        int index = hashFunction(data);
        for (T element : hashTable[index]) {
            if (element.equals(data)) return element;
        }
        return null;
    }
}
