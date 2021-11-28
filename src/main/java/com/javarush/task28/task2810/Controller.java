package com.javarush.task28.task2810;
import com.javarush.task28.task2810.model.Model;
import com.javarush.task28.task2810.model.Provider;


public class Controller {

    private Model model;

    public Controller(Model model) {
        if (model == null) throw new IllegalArgumentException();
        this.model = model;
    }

    public void onCitySelect(String cityName) {
        model.selectCity(cityName);
    }
}

