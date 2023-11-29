package hashtable;

//... Author Information ...
//Name: Leo Liang
//Contact: lliang11@u.rochester.edu
//Class: CSC 172, University of Rochester
//Assignment: Lab 5
abstract public class UR_HashTable<Key,Value> {
    private static final int INIT_CAPACITY = 5 ;
    protected int n; // size of the data set
    protected int m ; // size of the hash table
    protected Key[] keys;
    protected Value[] vals;
    protected boolean[] tombstone; //false means empty-before-removal, true means empty-after-removal
    int inserts, collisions;
    // Constuctors

    public UR_HashTable() {
        this(INIT_CAPACITY);
    }
    public UR_HashTable(int cap) {
        if (cap <= 0) {
            throw new IllegalArgumentException("Capacity can not be less than or equal to 0");
        }

        m = cap;
        n = 0;
        keys = (Key[]) new Object[m];
        vals = (Value[]) new Object[m];
        tombstone = new boolean[m];
        for (int i = 0; i < m; i++) {
            vals[i] = null;
            keys[i] = null;
            tombstone[i] = false;
        }
    }

    abstract public void put (Key key, Value val) ;
    abstract public Value get (Key key) ;
    abstract public void delete(Key key) ;
    abstract public int size() ;
    abstract public boolean isEmpty() ;
    abstract public boolean contains(Key key);
    abstract public Iterable<Key> keys() ;
    // Useful helpers
    // private int hash(Key key) ;
    // private void resize(int capacity) ;
}