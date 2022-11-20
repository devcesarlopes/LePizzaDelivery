package com.example.lepizzadelivery.models.food;

import java.util.HashMap;

public class Drinks extends Food{

    public Drinks(String name, String servesNumber, String description, double price, String size) {
        super(name, servesNumber, description, price, size);
        this.classIdentifier = "38b67f72-b4ca-4377-a3cb-23e6b4e46759";
    }

    public Drinks(String name){
        this.name = name;
    }

    @Override
    public String getSize() {
        return size + "ml";
    }

    @Override
    public HashMap<String, String> getOptions() {
        return null;
    }

}
