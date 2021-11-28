package com.javarush.task36.task3610;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        int count = 0;
        for (List<V> value : map.values()) {
            count += value.size();
        }
        return count;
    }

    @Override
    public V put(K key, V value) {

        if (map.containsKey(key)) {
            List<V> values = map.get(key);
            if (values.size() < repeatCount) {
                values.add(value);
                map.put(key, values);
                return values.get(values.size() - 2);
            } else if (values.size() == repeatCount) {
                values.remove(0);
                values.add(value);
                map.put(key, values);
                return values.get(values.size() - 2);
            }
        } else {
            List<V> newValues = new ArrayList<>();
            newValues.add(value);
            map.put(key, newValues);

        }
        return null;
    }

    @Override
    public V remove(Object key) {

            List<V> values = map.get(key);
            if (values != null) {
                if (values.size() >= 1) {
                    V value = values.remove(0);

                    if (values.size() == 0) map.remove(key);

                    else map.put((K) key, values);

                    return value;

                } else {
                    map.remove(key);
                }
            }

        return null;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        List<V> list = new ArrayList<V>();
        for (List<V> value : map.values()) {
            list.addAll(value);
        }
        return list;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}

