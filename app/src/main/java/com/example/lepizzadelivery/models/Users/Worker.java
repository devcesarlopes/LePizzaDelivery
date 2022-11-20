package com.example.lepizzadelivery.models.Users;

import com.example.lepizzadelivery.models.Restaurant;

import java.util.HashMap;
import java.util.Map;

public class Worker extends User{

    Restaurant restaurant;

    public Worker(String name, String email, String password, Restaurant restaurant){
        super(email);
        this.name = name;
        this.password = password;
        this.restaurant = restaurant;
        this.type = UserTypes.WORKER.toString();
    }

    public Worker(String name, String email, Restaurant restaurant){
        super(email);
        this.name = name;
        this.restaurant = restaurant;
        this.type = UserTypes.WORKER.toString();
    }

    public Restaurant getRestaurant() {return restaurant;}
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
    @Override
    public Map<String, Object> getMap(){
        return new HashMap<String, Object>(){{
            put("uid", uid);
            put("name", name);
            put("email", email);
            put("restaurant", restaurant.getUid());
        }};
    }

}
