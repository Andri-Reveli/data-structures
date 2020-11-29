package datastructures.lists;


public class Queue<T> {
    private int size;
    private OneWayNode<T> tail;
    private OneWayNode<T> head;

    public Queue() {
        size = 0;
        tail = null;
        head = null;
    }

    public void enqueue(T element) {
        OneWayNode<T> node = new OneWayNode<>(element);
        node.next = null;

        if (size == 0) {
            head = node;
        }
        else {
            tail.next = node;
        }

        tail = node;
        ++size;
    }

    public T dequeue() {
        if (size == 0) {
            throw new UnsupportedOperationException("Cannot dequeue from an empty queue");
        }

        OneWayNode<T> out = head;
        if (size == 1) {
            tail = head = null;
        }
        else {
            head = head.next;
        }

        out.next = null;
        --size;
        return out.element;
    }

    public int size() {
        return size;
    }

    public T head() {
        return head.element;
    }

    public T tail() {
        return tail.element;
    }

    public void empty() {
        while (size > 0) {
            dequeue();
        }
    }

    public void printQueue() {
        OneWayNode<T> cur = head;

        System.out.printf("\nSize: %d\nQueue:", size);
        while (cur != null) {
            System.out.print(" " + cur.element);
            cur = cur.next;
        }
        System.out.println();
    }
}
