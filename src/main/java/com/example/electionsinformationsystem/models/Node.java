package com.example.electionsinformationsystem.models;

public class Node<T> {
    // fields
    private final T data; // the data stored in this node
    public Node<T> next; // reference to next node

    // constructor
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    // getters and setters
    public T getData() {
        return data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
