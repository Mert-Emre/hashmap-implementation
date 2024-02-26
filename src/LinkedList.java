
// Basic linked list implementation. It doesn't check for errors 
// because hashmap already does this and doesn't send input to the linked list class.
import java.util.Iterator;

public class LinkedList<E> implements Iterable<E> {
  private Node<E> head;
  private Node<E> tail;
  private int size;

  public static class Node<E> {
    private Node<E> prev;
    private Node<E> next;
    private E data;

    public Node(Node<E> prev, Node<E> next, E data) {
      this.prev = prev;
      this.next = next;
      this.data = data;
    }

    public Node<E> getNext() {
      return next;
    }

    public Node<E> getPrev() {
      return prev;
    }

    public E getData() {
      return data;
    }

    public void setNext(Node<E> next) {
      this.next = next;
    }

    public void setPrev(Node<E> prev) {
      this.prev = prev;
    }

    public void setData(E data) {
      this.data = data;
    }
  }

  public LinkedList() {
    head = new Node<E>(null, null, null);
    tail = new Node<E>(head, null, null);
    head.setNext(tail);
    size = 0;
  }

  public Node<E> getFirst() {
    return head.getNext();
  }

  public Node<E> getLast() {
    return tail.getPrev();
  }

  public void addLast(E data) {
    Node<E> temp = new Node<E>(tail.prev, tail, data);
    tail.getPrev().setNext(temp);
    tail.setPrev(temp);
    size++;
  }

  public void addFirst(E data) {
    Node<E> temp = new Node<E>(head, head.next, data);
    head.getNext().setPrev(temp);
    head.setNext(temp);
    size++;
  }

  public void set(int index, E data) {
    Node<E> temp = head;
    for (int i = 0; i <= index; i++) {
      temp = temp.getNext();
    }
    temp.setData(data);
  }

  public E remove(int index) {
    Node<E> temp = head;
    for (int i = 0; i <= index; i++) {
      temp = temp.getNext();
    }
    temp.getPrev().setNext(temp.getNext());
    temp.getNext().setPrev(temp.getPrev());
    return temp.getData();
  }

  public int size() {
    return size;
  }

  public Iterator<E> iterator() {
    return new LinkedListIterator();
  }

  private class LinkedListIterator implements Iterator<E> {
    Node<E> current;

    public LinkedListIterator() {
      current = getFirst();
    }

    @Override
    public boolean hasNext() {
      return current != tail;
    }

    @Override
    public E next() {
      E data = current.getData();
      current = current.getNext();
      return data;
    }
  }
}
