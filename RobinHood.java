import java.util.function.Function;

public class RobinHood<Key, Value> implements Hashing<Key, Value>, Remove<Key, Value>{

    public static class Entry<Key, Value>{
        public Key key;
        public Value value;
        public int probingLength;
        public Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
        public String toString() {
            return "[Key] "+key+" [Value] "+value;
        }

    }
    private final int m; // how many buckets
    private int n; // how many elements in table
    private Entry<Key, Value>[] table; // actual table
    private Function<Key, Integer> hashFunction;

    public RobinHood(int size, Function<Key,Integer> hash) {
        this.m = size;
        this.n = 0;
        this.table = (Entry<Key, Value>[]) new Entry[size];
        this.hashFunction = hash;
    }

    @Override
    public Value put(Key key, Value value) throws IllegalStateException {
        if (n >= m) throw new IllegalStateException("Table is full");

        int home = hashFunction.apply(key) % m;
        int index = home;
        int disposition = 0;

        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                // Key already exists, replace value and reset probing length
                table[index].value = value;
                return value;
            }

            if (table[index].probingLength < disposition) {
                break;
            }

            disposition++;
            index = (index + 1) % m;
        }

        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            table[index].probingLength = disposition;
            this.n++;
        } else {
            Entry<Key, Value> prevEntry = table[index];
            table[index] = new Entry<>(key, value);
            table[index].probingLength = disposition;
            put(prevEntry.key, prevEntry.value);
        }
        return null;
    }

    @Override
    public Value get(Key key) {
        int home = hashFunction.apply(key) % m;
        int index = home;

        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                return table[index].value;
            }
            index = (index + 1) % m;
            if (index == home) {
                break;
            }
        }

        return null;
    }

    @Override
    public Value remove(Key key) {
        int home = hashFunction.apply(key) % m;
        int index = home;
        int originalIndex = -1;

        // Find the element to remove
        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                originalIndex = index;
                break;
            }
            index = (index + 1) % m;
            if (index == home) {
                return null; // Element not found
            }
        }

        if (originalIndex == -1) {
            return null; // Element not found
        }

        // Save the value to return later
        Value removedValue = table[originalIndex].value;

        // Shift elements
        index = (originalIndex + 1) % m;
        while (table[index] != null && table[index].probingLength > 0) {
            int newIndex = (index - 1 + m) % m;
            table[newIndex] = table[index];
            table[newIndex].probingLength--;
            index = (index + 1) % m;
        }

        // Mark the last shifted position as empty
        table[(index - 1 + m) % m] = null;
        n--;

        return removedValue;
    }

    public static void main(String[] args) {
        RobinHood<Integer, Integer> rh = new RobinHood<>(5,(x) -> x%5);
        rh.put(50,50);
        rh.put(50,1);
        rh.put(10,10);
        rh.put(2,2);
        rh.put(2,3);
        for (int i = 0; i<rh.m; i++){
            System.out.print("<Place " + i + ": "+ rh.get(i)+"> ");
        }
    }

}
