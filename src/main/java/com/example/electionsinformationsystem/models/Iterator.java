package com.example.electionsinformationsystem.models;

public class Iterator<T> implements java.util.Iterator<T> {

    // keeps track of position in the linked list
    private LLNode<T> pos;

    // starts the iterator at the given node
    public Iterator(LLNode<T> node) {
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
        LLNode<T> temp = pos;
        pos = pos.next;
        return temp.getData();
    }
}
