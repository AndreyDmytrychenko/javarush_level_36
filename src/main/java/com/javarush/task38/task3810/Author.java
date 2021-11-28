package com.javarush.task38.task3810;

public @interface Author {
    String value();

    Position position() default Position.OTHER;
}
