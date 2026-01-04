package com.example.electionsinformationsystem.models;

// separate chaining hash table
public class HashTableSC<K, V> {
    // an array of linked lists
    private final LinkedList<HashNode<K, V>>[] hashTable;
    private final int numBuckets;

    // sets up hash table
    @SuppressWarnings("unchecked")
    public HashTableSC(int size) {
        this.numBuckets = size;

        // creates LL of given size
        hashTable = new LinkedList[size];

        // place empty LL in each bucket
        for (int i = 0; i < size; i++) {
            hashTable[i] = new LinkedList<>();
        }
    }

    // assigns bucket to a key
    private int hashFunction(K key) {
        int hashCode = key.hashCode();

        // mode it by total buckets
        int index = hashCode % numBuckets;

        // remove negatives
        index = index < 0 ? -index : index;
        return index;
    }

    // adds KV pair to hash table
    public void put(K key, V value) {
        // find bucket assigned to key
        int index = hashFunction(key);
        LinkedList<HashNode<K, V>> bucket = hashTable[index];

        // check if key already exists
        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        // if new, add to bucket
        bucket.add(new HashNode<>(key, value));
    }

    // removes KV pair from hash table
    public void remove(K key) {
        int index = hashFunction(key);
        LinkedList<HashNode<K, V>> bucket = hashTable[index];

        // find key in bucket and remove
        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                return;
            }
        }
    }

    // returns value associated with key
    public V get(K key) {
        int index = hashFunction(key);
        LinkedList<HashNode<K, V>> bucket = hashTable[index];

        // loop through bucket and find key
        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                // if found, return the value
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
    // clears the hash table
    public void clear() {
        for (LinkedList<HashNode<K, V>> bucket : hashTable) {
            bucket.clear();
        }
    }
}
