package com.example.lepizzadelivery.models.menu;

import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.food.Food;

public interface OnMenuUpdatedCallback {
    void onCallback(Order order);
}
