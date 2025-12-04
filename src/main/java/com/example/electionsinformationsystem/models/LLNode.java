package com.example.electionsinformationsystem.models;

public class LLNode<T> {
    // fields
    private final T data; // the data stored in this node
    public LLNode<T> next; // reference to next node

    // constructor
    public LLNode(T data) {
        this.data = data;
        this.next = null;
    }

    // getters and setters
    public T getData() {
        return data;
    }

    public LLNode<T> getNext() {
        return next;
    }

    public void setNext(LLNode<T> next) {
        this.next = next;
    }
}
