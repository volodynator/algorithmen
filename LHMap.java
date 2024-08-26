import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * This class implements a map that uses linear hashing to expand the underlying hash table
 *
 * @param <Key>
 * @param <Value>
 */
public class LHMap<Key, Value> implements Map<Key, Value> {
    private final List<List<MapEntry<Key, Value>>> hashTable;
    private final int m0;
    private final double alphaMax;

    private Function<Key, Integer> hashFunction_level;
    private int n;
    private int level;
    private int expansionPointer;

    public LHMap(int m0, double alphaMax, Function<Key, Integer> hashFunction) {
        assert m0 > 0;
        assert alphaMax > 0d;

        this.m0 = m0;
        this.alphaMax = alphaMax;
        this.hashFunction_level = hashFunction;

        this.level = 0;
        this.expansionPointer = 0;
        this.hashTable = new ArrayList<>();

        for (int i = 0; i < m0; i++)
            this.hashTable.add(new LinkedList<>());
    }

    /**
     * get the address for the given key with respect to current level
     *
     * @param key
     * @return the address for the given key with respect to current level
     */
    public int getAddress(Key key) {
        int adr = hashFunction_level.apply(key);
        if (adr<expansionPointer){
            adr = nextLevelHash(key);
        }
        return adr;
    }

    public int nextLevelHash(Key key){
        return (int) key % ((int) Math.pow(2,level+1) * m0);
    }

    /**
     * get the current alpha value
     *
     * @return the current alpha value
     */
    public double getAlpha() {
        return (double) this.n/this.hashTable.size();
    }

    /**
     * check if number of elements in hash table exceeds threshold
     *
     * @return true if the hash table needs to be extended
     */
    public boolean checkOverflow() {
        return this.getAlpha() > this.alphaMax;
    }

    /**
     * expands the hash table
     */
    protected void split() {
        while (checkOverflow()){
            List<MapEntry<Key, Value>> bucket = this.hashTable.get(expansionPointer);
            List<MapEntry<Key, Value>> bucket1 = new LinkedList<>();
            hashTable.add(new LinkedList<>());
            for (MapEntry<Key, Value> tuple: bucket) {
                if (nextLevelHash(tuple.getKey())==expansionPointer){
                    bucket1.add(tuple);
                }
                else {
                    hashTable.get(expansionPointer+ m0* (int)Math.pow(2, level)).add(tuple);
                }
            }
            hashTable.set(expansionPointer, bucket1);
            expansionPointer++;
            if (expansionPointer == Math.pow(2, level) * m0) {
                level++;
                expansionPointer = 0;
            }
        }
    }

    @Override
    public Value get(Key key) {
        List<MapEntry<Key, Value>> bucket = this.hashTable.get(this.getAddress(key));
        for (MapEntry<Key, Value> tuple:
             bucket) {
            return tuple.getValue();
        }
        return null;
    }

    @Override
    public void put(Key key, Value value) {
        List<MapEntry<Key, Value>> bucket = this.hashTable.get(this.getAddress(key));
        for (MapEntry<Key, Value> tuple : bucket) {
            if (tuple.getKey().equals(key)) {
                tuple.setValue(value);
                return;
            }
        }

        bucket.add(new MapEntry<>(key, value));
        this.n++;

        if (this.checkOverflow())
            this.split();
    }

    @Override
    public void remove(Key key) {
        List<MapEntry<Key, Value>> bucket = this.hashTable.get(this.getAddress(key));
        for (Iterator<MapEntry<Key, Value>> it = bucket.iterator(); it.hasNext(); ) {
            if (it.next().getKey().equals(key)) {
                it.remove();
                this.n--;
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Iterable<?> bucket : this.hashTable) {
            str.append('[');
            for (Object entry : bucket)
                str.append(entry);

            str.append(']');
        }

        return str.toString();
    }

    public static void main(String[] args) {
        LHMap<Integer, String> lhMap = new LHMap<>(5,0.8d, (x)->x%5);
        lhMap.put(125, "125");
        lhMap.put(402, "402");
        lhMap.put(507, "507");
        lhMap.put(613, "613");
        lhMap.put(211, "211");
        lhMap.put(135, "136");
        lhMap.put(120, "120");
        System.out.println(lhMap);
    }
}