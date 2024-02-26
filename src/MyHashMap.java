
// Custom HashMap implementation. It uses seperate chaining.
import java.util.Iterator;

public class MyHashMap<K, V> implements Iterable<K> {
  private LinkedList<Item<K, V>>[] container;
  private int size = 1009;
  private int totalItems = 0;

  private static class Item<K, V> {
    private V data;
    private K key;

    private Item(K key, V value) {
      this.data = value;
      this.key = key;
    }

    public V getData() {
      return data;
    }

    public K getKey() {
      return key;
    }
  }

  public MyHashMap() {
    container = new LinkedList[size];
    for (int i = 0; i < size; i++) {
      container[i] = new LinkedList<Item<K, V>>();
    }
  }

  public int getSize() {
    return totalItems;
  }

  public void add(K key, V item) {
    add(key, item, container);
  }

  // Calculate the position of the item inside the hashmap and put it inside the
  // linked list at this position. If an item with the same key is given than it
  // is just omitted.
  private void add(K key, V value, LinkedList<Item<K, V>>[] container) {
    int position = key.hashCode() % size;
    position = position >= 0 ? position : position + size;
    Item<K, V> tempItem = new Item<K, V>(key, value);
    for (Item<K, V> item : container[position]) {
      if (item.getKey().equals(tempItem.getKey())) {
        return;
      }
    }
    container[position].addLast(new Item<K, V>(key, value));
    totalItems++;
    if (totalItems > size) {
      rehash(MathUtils.findPrime(size * 2));
    }
  }

  // Create an array at least 2 times bigger and has a prime capacity. Recalculate
  // the positions of old items.
  private void rehash(int size) {
    LinkedList<Item<K, V>>[] tempContainer = new LinkedList[size];
    for (int i = 0; i < size; i++) {
      tempContainer[i] = new LinkedList<Item<K, V>>();
    }
    for (LinkedList<Item<K, V>> list : container) {
      for (Item<K, V> item : list) {
        add(item.getKey(), item.getData(), tempContainer);
      }
    }
    container = tempContainer;
  }

  // Calculate the hash from the given key. Check the linked list at the
  // calculated position. If the item is there return true else return false.
  public boolean contains(K key) {
    int position = key.hashCode() % size;
    position = position >= 0 ? position : position + size;
    for (Item<K, V> item : container[position]) {
      if (item.getKey().equals(key)) {
        return true;
      }
    }
    return false;
  }

  // Calculate the has from the given key. If item is in there return the value of
  // the item. If it is not in there throw an exception. Which is caught by
  // project2 class.
  public V get(K key) {
    int position = key.hashCode() % size;
    position = position >= 0 ? position : position + size;
    for (Item<K, V> item : container[position]) {
      if (item.getKey().equals(key)) {
        return item.getData();
      }
    }
    throw new RuntimeException("The item with key " + key.toString() + " is not in the HashMap.");
  }

  // Calculate the hash from the given key. Change the value of this key.
  public void set(K key, V value) {
    int position = key.hashCode() % size;
    position = position >= 0 ? position : position + size;
    int i = 0;
    for (Item<K, V> item : container[position]) {
      if (item.getKey().equals(key)) {
        break;
      }
      i++;
    }
    if (i >= container[position].size()) {
      return;
    }
    Item<K, V> temp = new Item<K, V>(key, value);
    container[position].set(i, temp);
  }

  // Calcualte the hash from the given key. Remove it from the linked list. If the
  // item is not in the hashmap throw an exception.
  public V remove(K key) {
    int position = key.hashCode() % size;
    position = position >= 0 ? position : position + size;
    int i = 0;
    for (Item<K, V> item : container[position]) {
      if (item.getKey().equals(key)) {
        break;
      }
      i++;
    }
    if (i >= container[position].size()) {
      throw new RuntimeException("Item is not in the HashMap");
    }
    totalItems--;
    return container[position].remove(i).getData();
  }

  public Iterator<K> iterator() {
    return new HashMapIterator();
  }

  // Used to iterate the keys of the hashmap. It is mainly loops through the
  // hashmap container. If the linked list at current position has items start
  // iterating this linked list. When the linked list is iterated move to the next
  // linked list which has items in it.
  class HashMapIterator implements Iterator<K> {
    LinkedList.Node<Item<K, V>> current;
    int currentIndex;
    int passedElements = 0;

    public HashMapIterator() {
      if (totalItems == 0) {
        current = null;
        return;
      }
      for (int i = 0; i < size; i++) {
        if (container[i].size() > 0) {
          current = container[i].getFirst();
          currentIndex = i;
          passedElements++;
          break;
        }
      }
    }

    public boolean hasNext() {
      return current != null;
    }

    public K next() {
      K key = current.getData().getKey();
      if (passedElements < totalItems) {
        if (current.getNext() == null || current.getNext().getData() == null) {
          for (int i = currentIndex + 1; i < size; i++) {
            if (container[i].size() > 0) {
              current = container[i].getFirst();
              currentIndex = i;
              passedElements++;
              break;
            }
          }
          return key;
        }
        current = current.getNext();
        passedElements++;
        return key;
      }
      current = null;
      return key;
    }
  }
}
