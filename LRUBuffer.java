import java.util.HashMap;

/**
 * A buffer based on the least recently used strategy.
 * The buffer keeps a configurable number of elements.
 * Once full, it evicts the element that has not been accessed longest,
 * i.e. the put or get operation is furthest away from the present.
 */
public class LRUBuffer<Key, Value> implements Remove<Key, Value>, Hashing<Key,Value>{
    int capacity;
    HashMap<Key, DoublyLinkedList.Node<Value>> ht;
    DoublyLinkedList<Value> list;

    public LRUBuffer(int capacity) {
        this.capacity = capacity;
        this.ht = new HashMap<>(capacity);
        this.list = new DoublyLinkedList<>();
    }

    /**
     * Inserts the given (key, value) pair into the hash data structure.
     *
     * @param key  The key of the (key, value) pair.
     * @param value The value of the (key, value) pair.
     * @return A value previously associated with the key; null if key did not exist before.
     * @throws IllegalStateException if the element cannot be added due to capacity restrictions.
     */
    @Override
    public Value putHashing(Key key, Value value) throws IllegalStateException {
        if (ht.size()<capacity){
            if (ht.containsKey(key)){
                DoublyLinkedList.Node<Value> prevValue = ht.get(key);
                remove(key);
                list.addFirst(value);
                ht.put(key,list.head);
                return prevValue.element;
            }
            else {
                list.addFirst(value);
                ht.put(key, list.head);
                return null;
            }
        }
        throw new IllegalStateException();
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key The key to search for.
     * @return The value associated with the key; null if key is not in the data structure.
     */
    @Override
    public Value getHashing(Key key) {
        if (this.ht.containsKey(key)) {
            return ht.get(key).element;
        }
        else {
            return null;
        }
    }
    /**
     * Remove the (key, value) pair from this data structure, returning the value.
     *
     * @param key The key of the (key, value) pair.
     * @return The value associated with the given key; null if key is not present.
     */
    @Override
    public Value remove(Key key) {
        if(ht.containsKey(key)){
            DoublyLinkedList.Node<Value> node = ht.get(key);
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                list.head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                list.tail = node.prev;
            }
            ht.remove(key,node);
            return node.element;
        }
        return null;
    }
    /**
     * Puts the given key and value into the buffer possibly replacing and returning a value previously
     * associated with the key. If the buffer is full, the least recently used element is evicted.
     * The operation has an expected runtime of O(1).
     *
     * @param key   The search key of the element.
     * @param value The value associated with the key.
     * @return The value previously associated with key; null if the key is not present in the buffer.
     */
    public Value put(Key key, Value value) {
        if (ht.size() >= capacity) {
            list.removeLast();
        }
        return putHashing(key, value);
    }

    /**
     * Gets the value associated with the given key.
     * The operation has an expected runtime of O(1).
     *
     * @param key The search key of the element.
     * @return The value associated with the key; null if the key is not present in the buffer.
     */
    public Value get(Key key) {
        if (ht.containsKey(key)){
            Value value = ht.get(key).element;
            remove(key);
            list.addFirst(value);
            ht.put(key,list.head);
            return getHashing(key);
        }
        else{
            return null;
        }
    }

    public static void main(String[] args) {
        LRUBuffer<Integer, String> buffer = new LRUBuffer<>(5);
        buffer.put(1,"A");
        System.out.println(buffer.list);
        buffer.put(2,"B");
        System.out.println(buffer.list);
        buffer.get(1);
        System.out.println(buffer.list);
        buffer.put(3,"C");
        System.out.println(buffer.list);
        buffer.remove(2);
        System.out.println(buffer.list);
        buffer.put(4, "D");
        System.out.println(buffer.list);
        buffer.put(2,"B");
        System.out.println(buffer.list);
        buffer.get(3);
        System.out.println(buffer.list);
    }
}
