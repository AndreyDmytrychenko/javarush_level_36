package com.javarush.task33.task3310;


import com.javarush.task33.task3310.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        testStrategy(new HashMapStorageStrategy(), 1000);

        testStrategy(new OurHashMapStorageStrategy(), 1000);

        //testStrategy(new FileStorageStrategy(), 100);

        testStrategy(new HashBiMapStorageStrategy(), 1000);

        testStrategy(new OurHashBiMapStorageStrategy(), 1000);

        testStrategy(new DualHashBidiMapStorageStrategy(), 1000);

    }
    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        Set<Long> set = new HashSet<Long>();

        for (String str : strings) {
            set.add(shortener.getId(str));
        }
        return set;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> set = new HashSet<String>();

        for (Long key : keys) {
            set.add(shortener.getString(key));
        }
        return set;
    }

    public static void testStrategy(StorageStrategy strategy, long elementNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());

        Set<String> stringsTest = new HashSet<String>();
        for (long l = 0; l < elementNumber; l++)
            stringsTest.add(Helper.generateRandomString());

        Shortener shortener = new Shortener(strategy);

        Date start = new Date();
        Set<Long> keys = getIds(shortener, stringsTest);
        Date end = new Date();
        Helper.printMessage("Время получения ключей для " + elementNumber + " элементов составляет: " + String.valueOf(end.getTime() - start.getTime()) + " мс.");

        Date start1 = new Date();
        Set<String> strings = getStrings(shortener, keys);
        Date end1 = new Date();
        Helper.printMessage("Время получения значений для " + elementNumber + " элементов составляет: " + String.valueOf(end1.getTime() - start1.getTime()) + " мс.");

        if (strings.equals(stringsTest))
            Helper.printMessage("Тест пройден.");
         else
            Helper.printMessage("Тест не пройден.");

    }
}
