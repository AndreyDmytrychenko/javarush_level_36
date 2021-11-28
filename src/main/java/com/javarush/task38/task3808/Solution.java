package com.javarush.task38.task3808;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

/*
Неверные аннотации
*/

public class Solution {
    @Main
    public static void main(String[] args) {
        Solution solution = new Solution().new SubSolution();
        solution.overriddenMethod();
    }

    public void overriddenMethod() {
    }
    @SuppressWarnings("all")
    public class SubSolution extends Solution {
        @Override
        public void overriddenMethod() {
            System.out.println(uncheckedCall());
        }



        @SuppressWarnings("unchecked")
        List uncheckedCall() {
            List list = new ArrayList();
            list.add("hello");
            return list;
        }
    }
}
