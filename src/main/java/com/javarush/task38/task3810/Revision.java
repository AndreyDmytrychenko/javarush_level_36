package com.javarush.task38.task3810;


public @interface Revision {
    int revision();

    String comment() default "";

    Date date();

    Author[] authors() default {};
}
