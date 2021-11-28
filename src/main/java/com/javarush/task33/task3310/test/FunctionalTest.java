package com.javarush.task33.task3310.test;

import com.javarush.task33.task3310.Shortener;
import com.javarush.task33.task3310.strategy.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionalTest {

    public void testStorage(Shortener shortener) {

        String test1 = "Test";
        String test2 = "Test0";
        String test3 = "Test";

        Long id1 = shortener.getId(test1);
        Long id2 = shortener.getId(test2);
        Long id3 = shortener.getId(test3);

        Assert.assertNotEquals(test1, test2);
        Assert.assertNotEquals(test2, test3);

        Assert.assertEquals(test1, test3);

        Assert.assertEquals(shortener.getString(id1), test1);
        Assert.assertEquals(shortener.getString(id2), test2);
        Assert.assertEquals(shortener.getString(id3), test3);
    }
    @Test
    public void testHashMapStorageStrategy() {
        testStorage(new Shortener(new HashMapStorageStrategy()));
    }

    @Test
    public void testOurHashMapStorageStrategy() {
        testStorage(new Shortener(new OurHashMapStorageStrategy()));
    }


    @Test
    public void testFileStorageStrategy() {
        testStorage(new Shortener(new FileStorageStrategy()));
    }

    @Test
    public void testHashBiMapStorageStrategy() {
        testStorage(new Shortener(new HashBiMapStorageStrategy()));
    }


    @Test
    public void testDualHashBidiMapStorageStrategy() {
        testStorage(new Shortener(new DualHashBidiMapStorageStrategy()));
    }


    @Test
    public void testOurHashBiMapStorageStrategy() {
        testStorage(new Shortener(new OurHashBiMapStorageStrategy()));
    }
}
