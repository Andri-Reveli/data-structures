package datastructures.lists;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A double linked list datastructure
 *
 * @param <T> type of object that will be inserted in the list
 */
public class DoubleLinkedList<T> {
    /**
     * Size of the list
     */
    private int size;
    /**
     * Pointer to list's head
     */
    private TwoWayNode<T> head;
    /**
     * Pointer to list's tail
     */
    private TwoWayNode<T> tail;

    public DoubleLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    /**
     * @return list's size
     */
    public int size() {
        return size;
    }

    /**
     * @return <code>true</code> if list is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds an element to the end (head) of the list
     *
     * @param element the element to be added
     */
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

    /**
     * Adds an element to the beginning (tail) of the list
     *
     * @param element the element to be added
     */
    public void addToBeginning(T element) {
        TwoWayNode<T> node = new TwoWayNode<>(element);

        node.next = tail;
        tail.pre = node;
        tail = node;

        ++size;
    }

    /**
     * @see #append(T element)
     */
    public void add(T element) {
        append(element);
    }

    /**
     * Adds an element to a specific index inside the list
     *
     * @param element the element to be added
     * @param index   index where element will be added
     * @throws IndexOutOfBoundsException if the <code>index</code> is outside the boundaries
     */
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

    /**
     * Gets the element in a specific index inside the list
     *
     * @param index element's index
     * @return the element
     * @throws IndexOutOfBoundsException if the <code>index</code> is outside the boundaries
     */
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

    /**
     * Controls if this element is inside the list
     *
     * @param element the element to be looked for
     * @return <code>true</code> if the element is inside the list
     */
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

    /**
     * Gets the index of a specific element inside the list
     *
     * @param element the element to be looked for
     * @return -1 if there is no such element in the list, otherwise returns its index
     */
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

    /**
     * Does a full iteration of the list from tail (index = 0) to head (index = <code>size</code>
     * - 1)
     */
    public void forEach(Consumer<T> consumer) {
        for (TwoWayNode<T> current = tail; current != null; current = current.next) {
            consumer.accept(current.element);
        }
    }

    /**
     * Does a full iteration of the list from head (index = <code>size</code> - 1) to head (index =
     * 0
     */
    public void reverseForEach(Consumer<T> consumer) {
        for (TwoWayNode<T> current = head; current != null; current = current.pre) {
            consumer.accept(current.element);
        }
    }

    /**
     * Applies function: T -> R, to each element of this list
     *
     * @param function the function to be applied
     * @param <R>      the return value of <code>function</code>
     * @return a new list where each element is the result of mapping each existing element using
     * <code>function</code>
     */
    public <R> DoubleLinkedList<R> map(Function<T, R> function) {
        DoubleLinkedList<R> list = new DoubleLinkedList<>();

        this.forEach(el -> list.add(function.apply(el)));
        return list;
    }

    /**
     * Filters is each element of the list based on the condition
     *
     * @param predicate the condition each element has to fulfill
     * @return a new list where each element has fulfill the <code>predicate</code>
     */
    public DoubleLinkedList<T> filter(Predicate<T> predicate) {
        DoubleLinkedList<T> list = new DoubleLinkedList<>();
        this.forEach(el -> {
            if (predicate.test(el)) {
                list.add(el);
            }
        });

        return list;
    }

    /**
     * Removes the first element, counting from tail to had, that fulfills a predicate
     *
     * @param predicate the condition element has to fulfill to be removed from the list
     * @return a new list where the first element that has fulfilled the
     * <code>predicate</code> has been removed
     */
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

    /**
     * Removes all elements that fulfill a predicate
     *
     * @param predicate the condition element has to fulfill to be removed from the list
     * @return a new list where each element that has fulfilled the
     * <code>predicate</code> has been removed
     */
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

    /**
     * Prints the list from tail to head.
     */
    public void printList() {
        TwoWayNode<T> current = tail;

        for (int i = 0; i < size; ++i) {
            System.out.printf("%2d: %s\n", i, current.element.toString());
            current = current.next;
        }
    }

    /**
     * @return the element <code>this.tail</code> points to
     */
    public T tail() {
        return tail.element;
    }

    /**
     * @return the element <code>this.head</code> points to
     */
    public T head() {
        return head.element;
    }

    /**
     * Removes from he element <code>this.tail</code> points to
     *
     * @return the removed element
     */
    public T deleteTail() {
        TwoWayNode<T> out = tail;

        tail = tail.next;
        tail.pre = null;
        out.next = null;

        --size;
        return out.element;
    }

    /**
     * Removes from he element <code>this.head</code> points to
     *
     * @return the removed element
     */
    public T deleteHead() {
        TwoWayNode<T> out = head;

        head = head.pre;
        head.next = null;
        out.pre = null;

        --size;
        return out.element;
    }

    /**
     * Removes the element at a specific index inside the list
     *
     * @param index the index to be removed
     * @return the removed element
     * @throws IndexOutOfBoundsException if index is outside the boundaries
     */
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

    /**
     * Removes the first value that equals <code>value</code>
     *
     * @param value the value that has to be matched
     * @throws NoSuchElementException if there is no element that equals <code>value</code>
     */
    public void removeValue(T value) {
        int index = find(value);
        if (index == -1) {
            throw new NoSuchElementException(String.format(
                    "List does not contain %s", value
            ));
        }

        remove(index);
    }


    /**
     * Does a fast iteration over the list by checking which iteration is faster, from tail
     * to head or from head to tail
     *
     * @param index of desired element
     * @return a pair of element's index and a pointer to its node
     */
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

    /**
     * Traverses the list to a specific element and deletes it
     *
     * @param index of desired element
     * @return dhe deleted element
     */
    private T traversAndDelete(int index) {
        Pair<Integer, TwoWayNode<T>> pair = fastIterator(index);
        return removeNode(pair._2, pair._1);
    }

    /**
     * Removes a node
     *
     * @param out   the node to be removed
     * @param index of the node. It is needed only if the node to be removed is the
     *              tail or the head of the list
     * @return the deleted element
     */
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

    /**
     * Adds a new node to an empty list
     *
     * @param node the node to be added to the list
     */
    private void addToEmptyList(TwoWayNode<T> node) {
        head = tail = node;
    }


    /**
     * Traverses to a specific index and adds a new node
     *
     * @param index where the new node will be added
     * @param node  the node that will be added
     */
    private void traversAndAdd(int index, TwoWayNode<T> node) {
        if (index < size - index) {
            traverseFromTail(index, node);
        }
        else {
            traverseFromHead(index, node);
        }
    }

    /**
     * Traverses the list up to a specific index starting from tail and adds a new node
     *
     * @param index index where the node will be added
     * @param node  that will be added
     */
    private void traverseFromTail(int index, TwoWayNode<T> node) {
        TwoWayNode<T> current = tail;

        for (int i = 0; i < index; ++i) {
            current = current.next;
        }

        insert(node, current);
    }

    /**
     * Traverses the list up to a specific index starting from head and adds a new node
     *
     * @param index index where the node will be added
     * @param node  that will be added
     */
    private void traverseFromHead(int index, TwoWayNode<T> node) {
        TwoWayNode<T> current = head;

        for (int i = size - 1; i > index; --i) {
            current = current.pre;
        }

        insert(node, current);
    }

    /**
     * Inserts a new node before an existing one
     *
     * @param node    that will be added
     * @param current node
     */
    private void insert(TwoWayNode<T> node, TwoWayNode<T> current) {
        node.next = current;
        node.pre = current.pre;

        node.pre.next = node;
        current.pre = node;

        ++size;
    }

}
