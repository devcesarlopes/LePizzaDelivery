package com.example.lepizzadelivery.models;

import com.example.lepizzadelivery.models.food.Food;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
    List<Food> menu;

    public Menu(List<Food> menu) {
        this.menu = menu;
    }
}
