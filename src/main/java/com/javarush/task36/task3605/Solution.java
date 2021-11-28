package com.javarush.task36.task3605;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Solution {
    public static void main(String[] args) {

       // args = new String[] {"c:\\Users\\Андрей\\Desktop\\text.txt"};
        int i = 0;
        for (Character ch : sortText(readFile(args[0]))) {
            if (i == 5) break;
            System.out.print(ch);
            i++;
        }

    }
    public static Set<Character> sortText(List<String> list) {
        Set<Character> set = new TreeSet<Character>();
        list.forEach( text -> {
            for (Character ch : text.replaceAll("\\W","").toLowerCase().toCharArray()) {
               set.add(ch);
            }
        });
        return set;
    }
    public static List<String> readFile(String fileName) {
        List<String> list = new ArrayList<String>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.ready()) {
                list.add(reader.readLine());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
