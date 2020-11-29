package datastructures.lists;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class DoubleLinkedList<T> {
    private int size;
    private TwoWayNode<T> head;
    private TwoWayNode<T> tail;

    public DoubleLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void append(T element) {
        TwoWayNode<T> node = new TwoWayNode<>(element);

        // add to 0
        if (size == 0) {
            addToEmptyList(node);
        }
        else {
            node.pre = head;
            head.next = node;
            head = node;
        }
        ++size;
    }

    public void addToBeginning(T element) {
        TwoWayNode<T> node = new TwoWayNode<>(element);

        node.next = tail;
        tail.pre = node;
        tail = node;

        ++size;
    }

    public void add(T element) {
        append(element);
    }

    public void add(T element, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(String.format(
                    "Attempting adding an element outside the list's borders\nList: [%d, %d)\nAttempting at %d", 0, size, index
            ));
        }

        if (index == 0) {
            addToBeginning(element);
        }
        else if (index == size) {
            append(element);
        }
        else {
            TwoWayNode<T> node = new TwoWayNode<>(element);
            traversAndAdd(index, node);
        }
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format(
                    "\nAttempting retrieving an element outside the list's borders\n\tList: " +
                            "[%d, " +
                            "%d)\n\tAttempting at %d", 0, size, index
            ));
        }

        return fastIterator(index)._2.element;
    }

    public boolean contains(T element) {
        TwoWayNode<T> current = head;

        while (current != null) {
            if (current.element.equals(element)) {
                return true;
            }
            current = current.pre;
        }
        return false;
    }

    public int find(T element) {
        TwoWayNode<T> current = tail;

        for (int i = 0; i < size; ++i) {
            if (current.element.equals(element)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    public void forEach(Consumer<T> consumer) {
        for (TwoWayNode<T> current = tail; current != null; current = current.next) {
            consumer.accept(current.element);
        }
    }

    public void reverseForEach(Consumer<T> consumer) {
        for (TwoWayNode<T> current = head; current != null; current = current.pre) {
            consumer.accept(current.element);
        }
    }

    public <R> DoubleLinkedList<R> map(Function<T, R> function) {
        DoubleLinkedList<R> list = new DoubleLinkedList<>();

        this.forEach(el -> list.add(function.apply(el)));
        return list;
    }

    public DoubleLinkedList<T> filter(Predicate<T> predicate) {
        DoubleLinkedList<T> list = new DoubleLinkedList<>();
        this.forEach(el -> {
            if (predicate.test(el)) {
                list.add(el);
            }
        });

        return list;
    }

    public T removeIf(Predicate<T> predicate) {
        int i = 0;
        for (TwoWayNode<T> current = tail; current != null; current = current.next,
                ++i) {
            if (predicate.test(current.element)) {
                return removeNode(current, i);
            }
        }
        return null;
    }

    public void removeAll(Predicate<T> predicate) {
        TwoWayNode<T> helper;
        int i = 0;
        for (TwoWayNode<T> current = tail; current != null; ) {
            if (predicate.test(current.element)) {
                helper = current.next;
                removeNode(current, i);
                current = helper;
            }
            else {
                current = current.next;
                ++i;
            }
        }
    }

    public void printList() {
        TwoWayNode<T> current = tail;

        for (int i = 0; i < size; ++i) {
            System.out.printf("%2d: %s\n", i, current.element.toString());
            current = current.next;
        }
    }

    public T tail() {
        return tail.element;
    }

    public T head() {
        return head.element;
    }

    public T deleteTail() {
        TwoWayNode<T> out = tail;

        tail = tail.next;
        tail.pre = null;
        out.next = null;

        --size;
        return out.element;
    }

    public T deleteHead() {
        TwoWayNode<T> out = head;

        head = head.pre;
        head.next = null;
        out.pre = null;

        --size;
        return out.element;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format(
                    "Attempting removing an element outside the list's borders\nList: [%d, %d)" +
                            "\nAttempting at %d", 0, size, index
            ));
        }

        if (index == 0) {
            return deleteTail();
        }
        else if (index == size - 1) {
            return deleteHead();
        }
        else {
            return traversAndDelete(index);
        }
    }

    public T removeValue(T value) {
        int index = find(value);
        if (index == -1) {
            throw new NoSuchElementException(String.format(
                    "List does not contain %s", value
            ));
        }

        return remove(index);
    }

    private Pair<Integer, TwoWayNode<T>> fastIterator(int index) {
        TwoWayNode<T> out;
        int i;

        if (index < size - index) {
            out = tail;
            for (i = 0; i < index; ++i) {
                out = out.next;
            }
        }
        else {
            out = head;
            for (i = size - 1; i > index; --i) {
                out = out.pre;
            }
        }
        return new Pair<>(i, out);
    }

    private T traversAndDelete(int index) {
        Pair<Integer, TwoWayNode<T>> pair = fastIterator(index);
        return removeNode(pair._2, pair._1);
    }

    private T removeNode(TwoWayNode<T> out, int index) {
        if (index == 0) {
            return deleteTail();
        }
        else if (index == size - 1) {
            return deleteHead();
        }

        out.pre.next = out.next;
        out.next.pre = out.pre;

        out.pre = out.next = null;
        --size;
        return out.element;
    }

    private void addToEmptyList(TwoWayNode<T> node) {
        head = tail = node;
    }

    private void traversAndAdd(int index, TwoWayNode<T> node) {
        if (index < size - index) {
            traverseFromTail(index, node);
        }
        else {
            traverseFromHead(index, node);
        }
    }

    private void traverseFromTail(int index, TwoWayNode<T> node) {
        TwoWayNode<T> current = tail;

        for (int i = 0; i < index; ++i) {
            current = current.next;
        }

        insert(node, current);
    }

    private void traverseFromHead(int index, TwoWayNode<T> node) {
        TwoWayNode<T> current = head;

        for (int i = size - 1; i > index; --i) {
            current = current.pre;
        }

        insert(node, current);
    }

    private void insert(TwoWayNode<T> node, TwoWayNode<T> current) {
        node.next = current;
        node.pre = current.pre;

        node.pre.next = node;
        current.pre = node;

        ++size;
    }

}
