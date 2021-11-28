package com.javarush.task38.task3810;

import java.lang.annotation.*;

@Target(ElementType.TYPE)

@Retention(RetentionPolicy.RUNTIME)

public @interface Changelog {


    Revision[] value();
}
