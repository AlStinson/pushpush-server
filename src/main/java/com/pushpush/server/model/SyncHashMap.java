package com.pushpush.server.model;

import lombok.Locked;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SyncHashMap<K, V> {
    private final HashMap<K, V> hashMap = new HashMap<>();

    @Locked.Read
    public V get(K key) {
        return hashMap.get(key);
    }

    @Locked.Write
    public V getOrPut(K key, V value) {
        return hashMap.putIfAbsent(key, value);
    }

    @Locked.Write
    public V put(K key, V value) {
        return hashMap.put(key, value);
    }

    @Locked.Write
    public V remove(K key) {
        return hashMap.remove(key);
    }

    @Locked.Write
    public void removeIf(Predicate<Map.Entry<K, V>> filter) {
        hashMap.entrySet().removeIf(filter);
    }
}
