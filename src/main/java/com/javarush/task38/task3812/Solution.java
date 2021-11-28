package com.javarush.task38.task3812;

/*
Обработка аннотаций
*/

import java.util.Arrays;

public class Solution {
    public static void main(String[] args) {
        printFullyQualifiedNames(Solution.class);
        printFullyQualifiedNames(SomeTest.class);

        printValues(Solution.class);
        printValues(SomeTest.class);
    }
    @SuppressWarnings("all")
    public static boolean printFullyQualifiedNames(Class c) {
        if (c.isAnnotationPresent(PrepareMyTest.class)) {
            PrepareMyTest prepareMyTest = (PrepareMyTest) c.getAnnotation(PrepareMyTest.class);
            Arrays
                    .stream(prepareMyTest.fullyQualifiedNames())
                    .forEach(System.out::println);
            return true;
        }

        return false;
    }
    @SuppressWarnings("all")
    public static boolean printValues(Class c) {
        if (c.isAnnotationPresent(PrepareMyTest.class)) {
            PrepareMyTest prepareMyTest = (PrepareMyTest) c.getAnnotation(PrepareMyTest.class);
            Arrays
                    .stream(prepareMyTest.value())
                    .map(Class::getSimpleName)
                    .forEach(System.out::println);
            return true;
        }

        return false;
    }
}


