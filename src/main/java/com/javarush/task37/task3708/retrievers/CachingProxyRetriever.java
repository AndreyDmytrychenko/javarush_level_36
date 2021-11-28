package com.javarush.task37.task3708.retrievers;

import com.javarush.task37.task3708.cache.LRUCache;
import com.javarush.task37.task3708.storage.Storage;

public class CachingProxyRetriever implements Retriever{

    OriginalRetriever originalRetriever;
    LRUCache<Long, Object> cache = new LRUCache<Long, Object>(10);;

    public CachingProxyRetriever(Storage storage) {
        this.originalRetriever = new OriginalRetriever(storage);
    }

    @Override
    public Object retrieve(long id) {
        Object valueFromCache = cache.find(id);
        if (valueFromCache != null) {
            System.out.println("returning cached object");
            return valueFromCache;
        } else {
            Object valueFromStorage = originalRetriever.retrieve(id);
            cache.set(id, valueFromStorage);
            System.out.println("returning object from storage");
            return valueFromStorage;
        }
    }
}
