import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <pre class="doc_header">
 * <p>
 * </pre>
 *
 * @author kelmore5
 * @custom.date 3/19/17
 */
public class Queue <E>
{
    private static class Node<E>
    {
        private Node<E> next;
        private E cargo;
    }

    private Node<E> front = null;
    private Node<E> back = null;

    public boolean isEmpty()
    {
        return front == null;
    }

    public void push(E obj) {
        Node<E> newNode = new Node<>();
        newNode.cargo = obj;

        if(isEmpty()) {
            newNode.next = back;
            front = newNode;
            return;
        }
        else {
            if(back != null)
                back.next = newNode;
        }
        back = newNode;
        if(front.next == null)
            front.next = back;
    }

    public E peek() throws NoSuchElementException {
        if(back == null)
            return front.cargo;
        return back.cargo;
    }

    public E pop() throws NoSuchElementException {
        E temp = front.cargo;
        front = front.next;
        return temp;
    }

    public int size() {
        if(isEmpty())
            return 0;
        Node<E> temp = front;
        int k = 0;
        while(temp != null) {
            k++;
            temp = temp.next;
        }
        return k;
    }

    public boolean contains(E obj) {
        Iterator<E> iter = getIterator();

        while(iter.hasNext()) {
            E temp = iter.next();
            if(temp.equals(obj))
                return true;
        }
        return false;
    }

    public Iterator<E> getIterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<E>  {
        private Node<E> node;

        public QueueIterator() {
            node = front;
        }

        public boolean hasNext() {
            return !(node == null);
        }

        public E next() {
            E temp = node.cargo;
            node = node.next;
            return temp;
        }

        @Override
        public void remove() {}

    }
}