package com.javarush.task37.task3701;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;


/*
Круговой итератор
*/

public class Solution2<T> extends ArrayList<T> {


    @Override
    public Iterator<T> iterator() {
        return new RoundIterator();
    }

    public static void main(String[] args) {


        Solution2<Integer> list = new Solution2<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        int count = 0;

        for (Integer i : list) {
            //1 2 3 1 2 3 1 2 3 1
            System.out.print(i + " ");
            count++;
            if (count == 16) {
                break;

            }
        }
    }

    public class RoundIterator implements Iterator<T> {

        Object[] elementData;

        Field elementDataField;

        int count;

        {
            try {
                elementDataField = ArrayList.class.getDeclaredField("elementData");
                elementDataField.setAccessible(true);
                elementData = (Object[]) elementDataField.get(Solution2.this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        @Override
        public boolean hasNext() {
            return elementData.length != 0;
        }

        @Override
        public T next() {
            if(!hasNext() || elementData[count] == null)
                count = 0;
            if (elementData[count] == (Object) 5) {
                count++;
                if(!hasNext() || elementData[count] == null)
                    count = 0;
            }

            return (T)elementData[count++];
        }

    }
}
