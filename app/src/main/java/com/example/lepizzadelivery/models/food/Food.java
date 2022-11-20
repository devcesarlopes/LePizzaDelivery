package com.example.lepizzadelivery.models.food;

import android.annotation.SuppressLint;

import com.example.lepizzadelivery.Encoding;

import java.util.HashMap;
import java.util.Objects;

public abstract class Food extends Encoding {

    protected String uuid;
    protected String classIdentifier;
    protected String name;
    protected String servesNumber;
    protected String description;
    protected double price;
    protected String size;
    protected HashMap<String, String> options;

    public Food(String name, String servesNumber, String description, double price, String size) {
        this.name = name;
        this.servesNumber = servesNumber;
        this.description = description;
        this.price = price;
        this.size = size;
    }

    public Food(){

    }

    public String getName() {return name;}
    public String getDecription() {return description;}
    @SuppressLint("DefaultLocale")
    public String getPrice() {return String.format("%.2f", price);}
    public String getUuid(){
        return String.join(";",
            classIdentifier,
            name,
            description,
            String.valueOf(hashCode(getServesNumber())),
            String.valueOf(hashCode(String.valueOf(getPrice()))),
            String.valueOf(hashCode(getSize())),
            String.valueOf(hashCode(getOptions().toString()))
        );
    }
    public String getServesNumber() {return servesNumber;}
    public abstract String getSize();
    public abstract HashMap<String, String> getOptions();
    protected int hashCode(String str) {
        return Objects.hash(str);
    }
}
