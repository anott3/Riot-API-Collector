
/**
 * Class used to keep track of the number of each case of a given type
 * Backed by a HashMap that is maintained to increase the current value by one
 * if a duplicate is being added.
 * @param <T>   Type being counted
 */
public class CountTable<T> {
    private java.util.HashMap<T, Integer> hashMap;

    /**
     * No-args constructor for CountTable
     */
    public CountTable() {
        hashMap = new java.util.HashMap<T, java.lang.Integer>();
    }

    /**
     * Constructor that takes in an initialCapacity parameter to predetermine
     * the initial size of the HashMap
     * @param initialCapacity   initial size for HashMap
     */
    public CountTable(int initialCapacity) {
        hashMap = new java.util.HashMap<T, Integer>(initialCapacity);
    }

    /**
     * Constructor that takes in an initialCapacity parameter to predetermine
     * the initial size of the HashMap and a loadFactor parameter to determine
     * how often the HashMap resizes.
     * @param initialCapacity   the initial size for HashMap
     * @param loadFactor    load factor for the HashMap
     */
    public CountTable(int initialCapacity, float loadFactor) {
        hashMap = new java.util.HashMap<T, Integer>(initialCapacity);
    }

    /**
     * Removes all of the mappings from the HashMap
     */
    public void clear() {
        hashMap.clear();
    }

    /**
     * Calls the containsKey method for the HashMap
     * @param key   key being checked
     * @return   true if the HashMap contains a mapping for the specified key

     */
    public boolean containsKey(Object key) {
        return hashMap.containsKey(key);
    }

    /**
     * Calls the entrySet method for the HashMap
     * @return  a Set view of the mappings contained in the HashMap
     */
    public java.util.Set<java.util.Map.Entry<T, Integer>> entrySet() {
        return hashMap.entrySet();
    }

    /**
     * Returns the integer to which the specified key is mapped,
     * or null if the HashMap contains no mapping for the key.
     * @param key   the key whose associated value is to be returned
     * @return the integer mapped to the key, null if the key has no mapping
     */
    public int get(Object key) {
        return hashMap.get(key);
    }

    /**
     * Puts the key into the HashMap, incrementing the value it contains
     * @param key   key being included
     */
    public void put(T key) {
        java.lang.Integer check = hashMap.get(key);
        if (check == null) {
            hashMap.put(key, 1);
        } else {
            hashMap.put(key, ++check);
        }
    }

    /**
     * Puts all the keys into the HashMap, incrementing the values as necessary
     * for each one
     * @param keys  group of keys being added at once
     */
    @java.lang.SuppressWarnings("unchecked")
    public void putAll(T ... keys) {
        for (T key : keys) {
            put(key);
        }
    }

    /**
     * Removes the count for the specified key from the HashMap if present.
     * @param key   key being removed from HashMap
     * @return how many times the key was counted, null otherwise
     */
    public int remove(Object key) {
        return hashMap.remove(key);
    }

    /**
     * Returns the current number of mappings in the HashMap
     * @return  the number of key-value mappings in the HashMap.
     */
    public int size() {
        return hashMap.size();
    }
}
