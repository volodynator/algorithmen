import com.sun.jdi.Value;

import java.security.Key;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class DoublyLinkedList<E> {
    static class Node<E> {
        final E element;
        Node<E> next;
        Node<E> prev;

        Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }
    }

    Node<E> head;
    Node<E> tail;

    void addLast(E element) {
        this.tail = new Node<>(element, this.tail, null);
        if (this.head == null)
            this.head = this.tail;

        if (this.tail.prev != null)
            this.tail.prev.next = this.tail;
    }

    void addFirst(E element) {
        if (this.isEmpty()) {
            this.addLast(element);
            return;
        }

        this.head = new Node<>(element, null, this.head);
        if (this.head.next != null)
            this.head.next.prev = this.head;
    }

    E removeLast() {
        if (this.tail == null)
            throw new IllegalStateException("Empty list");

        if (this.tail.prev != null)
            this.tail.prev.next = null;

        E last = this.tail.element;
        this.tail = this.tail.prev;
        return last;
    }

    E get(int n) {
        if (this.isEmpty())
            throw new NoSuchElementException();

        Node<E> curr;
        for (curr = this.head; curr.next != null && n != 0; curr = curr.next, n--) ;

        if (n == 0)
            return curr.element;

        throw new NoSuchElementException();
    }

    boolean isEmpty() {
        return this.head == null;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("(Head) ");
        for (Node<?> curr = this.head; curr != null; curr = curr.next)
            str.append(curr.element).append(" <--> ");

        str.setLength(str.length() - " <--> ".length());
        return str.append(" (Tail)").toString();
    }
}
