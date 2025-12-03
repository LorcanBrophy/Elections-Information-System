package com.example.electionsinformationsystem.models;

public class Iterator<T> implements java.util.Iterator<T> {

    // keeps track of position in the linked list
    private Node<T> pos;

    // starts the iterator at the given node
    public Iterator(Node<T> node) {
        this.pos = node;
    }

    // checks there is another element in the list
    @Override
    public boolean hasNext() {
        return pos != null;
    }

    @Override
    public T next() {
        // store the current node, move to the next one, then return the data
        Node<T> temp = pos;
        pos = pos.next;
        return temp.getData();
    }
}
