package com.example.electionsinformationsystem.models;

public class HashTableSC<K, V> {
    private final LinkedList<HashNode<K, V>>[] hashTable;
    private final int numBuckets;

    @SuppressWarnings("unchecked")
    public HashTableSC(int size) {
        this.numBuckets = size;
        hashTable = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            hashTable[i] = new LinkedList<HashNode<K, V>>();
        }
    }

    private int hashFunction(K key) {
        int hashCode = key.hashCode();
        int index = hashCode % numBuckets;
        index = index < 0 ? -index : index;
        return index;
    }

    public void put(K key, V value) {
        int index = hashFunction(key);
        LinkedList<HashNode<K, V>> bucket = hashTable[index];

        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        bucket.add(new HashNode<>(key, value));
    }

    public void remove(K key) {
        int index = hashFunction(key);
        LinkedList<HashNode<K, V>> bucket = hashTable[index];

        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                return;
            }
        }
    }

    public V get(K key) {
        int index = hashFunction(key);
        LinkedList<HashNode<K, V>> bucket = hashTable[index];

        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    public void printTable() {
        for (int i = 0; i < numBuckets; i++) {
            System.out.println("Bucket " + i + ":");
            for (HashNode<K, V> node : hashTable[i]) {
                if (node.value instanceof LinkedList<?> list) {
                    System.out.println(node.key + " -> \n" + list.display());
                } else {
                    System.out.println(node.key + " -> " + node.value);
                }
            }
        }
    }
}
