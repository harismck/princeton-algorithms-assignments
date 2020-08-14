import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int n;

    public Deque() {
        n = 0;
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;

        if (oldfirst == null)
            last = first;
        else
            oldfirst.prev = first;

        n++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;

        if (oldlast == null)
            first = last;
        else
            oldlast.next = last;

        n++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item tmp = first.item;
        first = first.next;
        n--;
        return tmp;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item tmp = last.item;
        last = last.prev;
        n--;
        return tmp;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node pos = first;

        public boolean hasNext() {
            return pos != null;
        }

        public Item next() {
            if (isEmpty())
                throw new NoSuchElementException();

            Item tmp = pos.item;
            pos = pos.next;
            return tmp;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.isEmpty();
        deque.addFirst(2);
        deque.removeLast();
        deque.addFirst(4);
        deque.removeLast();

    }
}

