package KeyValueRemove;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LinearProbingRemove<Key, Value> implements Hashing<Key, Value>, Remove<Key,Value> {
    public enum Status{
        FREI, BELEGT, GELOESCHT;
    }
    public class KeyValuePair {
        Key key;
        Value value;

        public KeyValuePair(Key key, Value value){
            this.key = key;
            this.value = value;
        }
    }
    public class KeyValuePairStatus{
        public Status status;
        public KeyValuePair pair;
        public KeyValuePairStatus(KeyValuePair pair, Status status){
            this.pair = pair;
            this.status = status;
        }

        @Override
        public String toString() {
            if (pair!=null){
                return "[KEY] " + pair.key +" [STATUS] " + status;
            }
            else return null;
        }
    }
    private final int maxSize;
    private final Function<Key, Integer> hashFunction;
    private final List<KeyValuePairStatus> hashTable;

    public LinearProbingRemove(int size, Function<Key,Integer> hash){
        this.maxSize = size;
        this.hashFunction = hash;
        this.hashTable = new ArrayList<>();
        for (int i = 0; i<size; i++){
            hashTable.add(new KeyValuePairStatus(null, Status.FREI));
        }
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
    public Value put(Key key, Value value) throws IllegalStateException {
        int home = hashFunction.apply(key);
        KeyValuePairStatus homeEntry = hashTable.get(home);
        if (!homeEntry.status.equals(Status.BELEGT)){
            KeyValuePair newPair = new KeyValuePair(key, value);
            hashTable.set(home, new KeyValuePairStatus(newPair, Status.BELEGT));
            return null;
        }
        else {
            KeyValuePairStatus result = this.search(key);
            if (result!=null && result.status!=Status.GELOESCHT){
                Value prevValue = result.pair.value;
                result.pair.value = value;
                return prevValue;
            }
            else {
                int i = home+1;
                while (hashTable.get(i).status==Status.BELEGT && i!=home){
                    i++;
                    if (i==maxSize){
                        i=0;
                    }
                }
                if(i==home){
                    throw new IllegalStateException();
                }
                else {
                    KeyValuePair pair = new KeyValuePair(key, value);
                    hashTable.set(i, new KeyValuePairStatus(pair, Status.BELEGT));
                    return null;
                }
            }
        }
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key The key to search for.
     * @return The value associated with the key; null if key is not in the data structure.
     */
    @Override
    public Value get(Key key) {
        KeyValuePairStatus result = this.search(key);
        if (result!=null){
            return result.pair.value;
        }
        return null;
    }
    /**
     * Remove the (key, value) pair from this data structure, returning the value.
     *
     * @param key The key of the (key, value) pair.
     * @return The value associated with the given key; null if key is not present.
     */
    @Override
    public Value remove(Key key) {
        KeyValuePairStatus result = this.search(key);
        if(result!=null && result.status!=Status.FREI){
            result.status=Status.GELOESCHT;
            return result.pair.value;
        }
        else {
            return null;
        }
    }

    public KeyValuePairStatus search(Key key) {
        int home = hashFunction.apply(key);
        KeyValuePairStatus homeEntry = hashTable.get(home);
        if (homeEntry.status==Status.BELEGT && homeEntry.pair.key.equals(key)){
            return hashTable.get(home);
        }
        else {
            int i = home+1;
            while (hashTable.get(i).status != Status.FREI && i!=home){
                KeyValuePairStatus result = hashTable.get(i);
                if (result.pair.key.equals(key) && result.status==Status.BELEGT){
                    return result;
                }
                i++;
                if (i==maxSize){
                    i=0;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        LinearProbingRemove<Integer, String> linearProbing = new LinearProbingRemove<>(5,(x) -> x%5);
        System.out.println("Put 17:");
        linearProbing.put(17,"");
        for (int i = 0; i<linearProbing.hashTable.size(); i++){
            System.out.print("<Place " + i + ": "+ linearProbing.hashTable.get(i)+"> ");
        }
        System.out.println();
        System.out.println("Put 27:");
        linearProbing.put(27,"");
        for (int i = 0; i<linearProbing.hashTable.size(); i++){
            System.out.print("<Place " + i + ": "+ linearProbing.hashTable.get(i)+"> ");
        }
        System.out.println();
        System.out.println("Put 17:");
        linearProbing.put(17,"");
        for (int i = 0; i<linearProbing.hashTable.size(); i++){
            System.out.print("<Place " + i + ": "+ linearProbing.hashTable.get(i)+"> ");
        }
        System.out.println();
        System.out.println("Put 37:");
        linearProbing.put(37,"");
        for (int i = 0; i<linearProbing.hashTable.size(); i++){
            System.out.print("<Place " + i + ": "+ linearProbing.hashTable.get(i)+"> ");
        }
        System.out.println();
        System.out.println("Put 50:");
        linearProbing.put(50,"");
        for (int i = 0; i<linearProbing.hashTable.size(); i++){
            System.out.print("<Place " + i + ": "+ linearProbing.hashTable.get(i)+"> ");
        }
        System.out.println();
        System.out.println("Put 60:");
        linearProbing.put(60,"");
        for (int i = 0; i<linearProbing.hashTable.size(); i++){
            System.out.print("<Place " + i + ": "+ linearProbing.hashTable.get(i)+"> ");
        }
    }
}
