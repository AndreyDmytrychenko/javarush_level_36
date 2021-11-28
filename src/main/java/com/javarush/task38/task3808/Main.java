package com.javarush.task38.task3808;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Inherited
public @interface Main {
}
