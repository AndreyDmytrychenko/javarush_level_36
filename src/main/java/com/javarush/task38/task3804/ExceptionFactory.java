package com.javarush.task38.task3804;

public class ExceptionFactory {

    public static Throwable getException(Enum enumParam) {

        if (enumParam == null) return new IllegalArgumentException();

        String firstLetter = enumParam.toString().substring(0,1);
        String anotherLetters = enumParam.toString().toLowerCase().replaceAll("[_]", " ").substring(1);
        String massage = firstLetter + anotherLetters;



        if (enumParam instanceof ApplicationExceptionMessage) {

            return new Exception(massage);

        } else if (enumParam instanceof DatabaseExceptionMessage) {

            return new RuntimeException(massage);


        } else if (enumParam instanceof UserExceptionMessage) {

            return new Error(massage);

        } else {
            return new IllegalArgumentException();
        }

    }
}
