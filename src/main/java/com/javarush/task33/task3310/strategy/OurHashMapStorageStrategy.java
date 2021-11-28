package com.javarush.task33.task3310.strategy;

public class OurHashMapStorageStrategy implements StorageStrategy {

    static final int DEFAULT_INITIAL_CAPACITY = 16;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];

    int size;

    int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

    float loadFactor = DEFAULT_LOAD_FACTOR;

    static int hash(Long key) {

        return key.hashCode();
    }

    static int indexFor(int h, int length) {
        return h & (length-1);
    }

    final Entry getEntry(Long key) {
        if (size == 0) return null;

        int hash = hash(key);
        int index = indexFor(hash, table.length);
             for (Entry entry = table[index]; entry != null; entry = entry.next) {
                 if (key.equals(entry.getKey()))
                     return entry;
             }
        return null;
    }

    void resize(int newCapacity) {

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }

    void transfer(Entry[] newTable) {

        int newCapacity = newTable.length;
        for (Entry e : table) {
            while (e != null) {
                Entry next = e.next;
                int indexInNewTable = indexFor(hash(e.getKey()), newCapacity);
                e.next = newTable[indexInNewTable];
                newTable[indexInNewTable] = e;
                e = next;
            }
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex) {

        if (size >= threshold) {
            resize(2 * table.length);
            hash = hash(key);
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    void createEntry(int hash, Long key, String value, int bucketIndex) {
        Entry e = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, e);
        size++;
    }



    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }



    @Override
    public boolean containsValue(String value) {

        Entry[] tab = table;
        for (int i = 0; i < tab.length ; i++)
            for (Entry e = tab[i] ; e != null ; e = e.next)
                if (value.equals(e.value))
                    return true;
        return false;
    }

    @Override
    public void put(Long key, String value) {

        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Entry entry = table[index]; entry != null; entry = entry.next) {
            if (key.equals(entry.getKey())) {
                entry.value = value;
                return;
            }
        }


        addEntry(hash, key, value, index);
    }


    @Override
    public Long getKey(String value) {
        for (Entry tableElement : table) {
            for (Entry e = tableElement; e != null; e = e.next)
            if (value.equals(e.value))
                return e.getKey();
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        Entry entry = getEntry(key);
        if (entry != null)
            return entry.getValue();

        return null;

    }
}
