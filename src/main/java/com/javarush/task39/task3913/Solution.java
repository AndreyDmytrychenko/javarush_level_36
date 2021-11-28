package com.javarush.task39.task3913;

import java.nio.file.Paths;
import java.util.Date;

public class Solution {
    public static void main(String[] args) {
       LogParser logParser = new LogParser(Paths.get("c:/Users/Андрей/IdeaProjects/level36/src/main/java/com/javarush/task39/task3913/logs/"));

       System.out.println(logParser.execute("get user for event = \"LOGIN\" and date between \"11.12.2013 0:00:00\" and \"03.01.2014 23:59:59\"."));







    }
}
