package datastructures.lists;

class OneWayNode<T> {
    public T element;
    public OneWayNode<T> next;

    public OneWayNode(T element) {
        this.element = element;
        this.next = null;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
