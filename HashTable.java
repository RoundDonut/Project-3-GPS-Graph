package hashtable;

import java.util.ArrayList;

//... Author Information ...
//Name: Leo Liang
//Contact: lliang11@u.rochester.edu
//Class: CSC 172, University of Rochester
//Assignment: Lab 5

public class HashTable<Key, Value> extends UR_HashTable<Key, Value>{

    public HashTable() {
        super();
    }

    public HashTable(int capacity) {
        super(capacity);
    }

    /**
     * Inserts a key-value pair to the hash table. If key already exists, new value with replace the old one.
     *
     * @param key the key for your insert
     * @param val the value associated to the key
     * @exception IllegalArgumentException is thrown if either key or value is null
     */
    @Override
    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (val == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }

        if (n >= m * 75/100) {
            resize();
        }

        int bucket = Math.abs(hash(key)) % m;

        if (keys[bucket] == null) {
            keys[bucket] = key;
            vals[bucket] = val;
            tombstone[bucket] = false;
            n++;
        }
        // if there are two keys that are the same, the value gets replaced
        else if (keys[bucket].equals(key)) {
            vals[bucket] = val;
        }
        else {
            int bucketsProbed = 0;

            collisions++;
            while (bucketsProbed < m) {
                bucket = (bucket + 1) % m;

                if (keys[bucket] == null) {
                    vals[bucket] = val;
                    keys[bucket] = key;
                    tombstone[bucket] = false;

                    break;
                }

                bucketsProbed++;
            }

            n++;
        }

        inserts++;
    }

    /**
     * Calculate the hash of the key. Implements basic hash function for string and numbers.
     *
     * @param key the key to hash
     * @return a hash value
     */
    private int hash(Key key) {
        if (key instanceof String) {

            int hashValue = 0;
            String str = (String) key;

            for (int i = 0; i < str.length(); i++) {
                hashValue += str.charAt(i) * 1572869;
            }

            return hashValue;
        }
        else if (key instanceof Number) {
            return (Integer) key;
        }
        else {
            throw new IllegalArgumentException("Invalid key. Must be number of string");
        }
    }

    /**
     * Gets the value associated with a key.
     *
     * @param key the lookup key for the value
     * @return the value associated with the key or null if the key doesn't exist
     * @exception IllegalArgumentException is thrown if key is null
     */
    @Override
    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (isEmpty()) {
            return null;
        }

        int bucket = Math.abs(hash(key)) % m;

        if (keys[bucket] == null && tombstone[bucket] == false) {
            return null;
        }
        else if (keys[bucket] != null && keys[bucket].equals(key)) {
            return vals[bucket];
        }
        else {
            // When keys[bucket]==null && tombstone[bucket]==true
            // (or) keys[bucket]!=null && keys[bucket] does not equal key

            int bucketsProbed = 0;

            while (bucketsProbed < m) {
                bucket = (bucket + 1) % m;

                if (keys[bucket] == null && tombstone[bucket] == false) {
                    // Must check if tombstone is false, if so, can stop at that point
                    return null;
                }
                else if (keys[bucket] != null && keys[bucket].equals(key)) {
                    return vals[bucket];
                }

                // When keys[bucket]==null && tombstone[bucket]==true
                // (or) keys[bucket]!=null && keys[bucket] does not equal key
                bucketsProbed++;
            }
        }

        return null;
    }

    /**
     * Deletes a key-value pair. No operation if key doesn't exist
     *
     * @param key the key you wish to delete and its associated value
     * @exception IllegalArgumentException is thrown if the key is null
     */
    @Override
    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

        int bucket = Math.abs(hash(key)) % m;

        if (keys[bucket] == null && tombstone[bucket] == false) {
            return;
        }

        if (keys[bucket] != null && keys[bucket].equals(key)) {
            keys[bucket] = null;
            vals[bucket] = null;
            tombstone[bucket] = true;
            n--;
        }
        else {
            // keys[bucket] == null (or) keys[bucket] does not equal key

            int bucketsProbed = 0;
            while (bucketsProbed < m) {
                bucket = (bucket + 1) % m;

                if (keys[bucket] == null && tombstone[bucket] == false) {
                    return;
                }

                if (keys[bucket] != null && keys[bucket].equals(key)) {
                    keys[bucket] = null;
                    vals[bucket] = null;
                    tombstone[bucket] = true;
                    n--;

                    return;
                }
                // keys[bucket] == null (or) keys[bucket] does not equal key

                bucketsProbed++;
            }
        }
    }

    /**
     * Returns the size (non-null elements) in the hash table
     *
     * @return the size of the hash table
     */
    @Override
    public int size() {
        return n;
    }

    /**
     * Returns the capacity of the hash table
     *
     * @return the capacity of the hash table
     */
    public int capacity() {
        return m;
    }

    /**
     * Determines whether the hash table is empty or not.
     *
     * @return true or false
     */
    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Determines whether the hash table contains a specific key
     *
     * @param key the key that you wish to determine if it is in the hash table
     * @return true or false
     */
    @Override
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     * An iterable function that returns an iterable key object to use in a for-each loop
     *
     * @return an iterable key function
     */
    @Override
    public Iterable<Key> keys() {
        ArrayList<Key> keyList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                keyList.add(keys[i]);
            }
        }
        return keyList;
    }

    /**
     * An iterable function that returns an iterable value object to use in a for-each loop
     *
     * @return an iterable value function
     */
    public Iterable<Value> values() {
        ArrayList<Value> valueList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (vals[i] != null) {
                valueList.add(vals[i]);
            }
        }

        return valueList;
    }

    /**
     * Resizes the hash table based on a load factor of .75. The hash table's capacity is duplicated.
     */
    private void resize() {
        HashTable<Key, Value> tempTable = new HashTable<>(m * 2);

        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                tempTable.put(keys[i], vals[i]);
            }
        }

        keys = tempTable.keys;
        vals = tempTable.vals;
        tombstone = tempTable.tombstone;
        m = tempTable.m;
    }
}
