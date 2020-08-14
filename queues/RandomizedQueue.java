import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;

    public RandomizedQueue() {
        a = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (n == a.length)
            resize(2 * a.length);
        a[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int i = StdRandom.uniform(0, n);

        Item item = a[i];
        a[i] = a[--n]; // Replace the nth item with the last item
        a[n] = null;

        if (n > 0 && n == a.length / 4)
            resize(a.length / 2);

        return item;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int i = StdRandom.uniform(0, n);
        return a[i];
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++)
            temp[i] = a[i];
        a = temp;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        RandomizedQueue<Item> copy = createCopy();

        private RandomizedQueue<Item> createCopy() {
            RandomizedQueue<Item> rq = new RandomizedQueue<>();
            for (int j = 0; j < n; j++)
                rq.enqueue(a[j]);
            return rq;
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return copy.dequeue();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        for (String s : a) rq.enqueue(s);

        StdOut.println(rq.size());

        for (String s : rq) StdOut.print(s);
        StdOut.println();

        StdOut.println(rq.sample());

        while (!rq.isEmpty())
            StdOut.print(rq.dequeue());
        StdOut.println();
    }
}
