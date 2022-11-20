package com.example.lepizzadelivery.models;

import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.models.food.Food;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    Restaurant restaurant;
    User user;
    Date orderTime;
    List<Food> food;

    @Override
    public String toString() {
        return "Order{" +
                '}';
    }
}
