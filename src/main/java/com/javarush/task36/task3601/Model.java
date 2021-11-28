package com.javarush.task36.task3601;


import java.util.List;

public class Model {
    private DataBase data = new DataBase();
    public List<String> getStringDataList() {
        return data.getData();
    }
}
