package datastructures.lists;


public class Stack<T> {
    private int size;
    private OneWayNode<T> head;

    public Stack() {
        size = 0;
        head = null;
    }

    public void push(T element) {
        OneWayNode<T> node = new OneWayNode<>(element);

        node.next = head;
        head = node;
        ++size;
    }

    public T peek() {
        if (size > 0) {
            return head.element;
        }

        return null;
    }

    public T pop() {
        if (size == 0) {
            return null;
        }

        OneWayNode<T> top = head;
        T out = top.element;

        head = head.next;
        top = null;
        --size;
        return out;
    }

    public void printStack() {
        OneWayNode<T> currentNode = head;

        while (currentNode != null) {
            System.out.println(currentNode);
            currentNode = currentNode.next;
        }
    }

    public void empty() {
        while (size > 0) {
            pop();
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
