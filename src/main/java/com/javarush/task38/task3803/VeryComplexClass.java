package com.javarush.task38.task3803;

/*
Runtime исключения (unchecked exception)
*/

public class VeryComplexClass {
    public void methodThrowsClassCastException() {
        method1("");

    }

    public void methodThrowsNullPointerException() {
        VeryComplexClass veryComplexClass = null;
        method2(veryComplexClass);

    }

    public static Integer method1(Object o) {
            return (Integer) o;
    }
    public static void method2(VeryComplexClass veryComplexClass) {
            veryComplexClass.methodThrowsClassCastException();
    }

    public static void main(String[] args) {
        VeryComplexClass veryComplexClass = new VeryComplexClass();
        try {
            veryComplexClass.methodThrowsClassCastException();
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName());
        }

        try {
            veryComplexClass.methodThrowsNullPointerException();
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName());
        }


    }
}


