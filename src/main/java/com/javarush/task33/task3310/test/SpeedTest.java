package com.javarush.task33.task3310.test;

import com.javarush.task33.task3310.Helper;
import com.javarush.task33.task3310.Shortener;
import com.javarush.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {

    public long getTimeToGetId(Shortener shortener, Set<String> strings, Set<Long> ids) {
        Date data1 = new Date();
        for (String id : strings)
            ids.add(shortener.getId(id));
        Date data2 = new Date();

        return data2.getTime() - data1.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener,Set<Long> ids, Set<String> strings) {

        Date data1 = new Date();
        for (Long string : ids)
            strings.add(shortener.getString(string));
        Date data2 = new Date();

        return data2.getTime() - data1.getTime();
    }

    @Test
    public void testHashMapStorage() {

        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());

        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());

        Set<String> origStrings = new HashSet<>();


        for (int i = 0; i < 1000; i++) {
            origStrings.add(Helper.generateRandomString());
        }
        Set<Long> ids1 = new HashSet<>();
        Set<String> strings1 = new HashSet<>();
        long timeGetIdsForSh1 = getTimeToGetId(shortener1, origStrings, ids1);
        long timeGetStringsForSh1 = getTimeToGetStrings(shortener1, ids1, strings1);

        Set<Long> ids2 = new HashSet<>();
        Set<String> strings2 = new HashSet<>();
        long timeGetIdsForSh2 = getTimeToGetId(shortener2, origStrings, ids2);
        long timeGetStringsForSh2 = getTimeToGetStrings(shortener2, ids2, strings2);

        Assert.assertTrue(timeGetIdsForSh1 > timeGetIdsForSh2);

        Assert.assertEquals(timeGetStringsForSh1, timeGetStringsForSh2, 30);

    }
}
