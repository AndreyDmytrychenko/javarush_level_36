package com.javarush.task38.task3804;

/*
Фабрика исключений
*/


public class Solution {
    public static Class getFactoryClass() {

        return ExceptionFactory.class;
       }


    public static void main(String[] args) {
        System.out.println(getFactoryClass());
        System.out.println(ExceptionFactory.getException(ApplicationExceptionMessage.SOCKET_IS_CLOSED));

    }
}
