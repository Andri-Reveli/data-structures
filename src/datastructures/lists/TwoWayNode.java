package datastructures.lists;

class TwoWayNode<T> {
    T element;
    TwoWayNode<T> pre;
    TwoWayNode<T> next;

    public TwoWayNode(T element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return String.format("Pre: %s\nNext: %s", pre, next);
    }
}
