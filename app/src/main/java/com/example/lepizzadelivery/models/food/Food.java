package com.example.lepizzadelivery.models.food;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lepizzadelivery.Encoding;
import com.example.lepizzadelivery.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

public class Food extends Encoding{

    View view;
    LinearLayout lista;
    Activity activity;
    String uid;
    String name;
    String size;
    String description;
    int imgSrc;
    double price;
    int quantity;
    OnFoodSelectedCallback callback;

    public Food(String uid, String name, String size, String description, double price, int imgSrc, OnFoodSelectedCallback callback) {
        this.uid = uid;
        this.name = name;
        this.size = size;
        this.description = description;
        this.price = price;
        this.imgSrc = imgSrc;
        this.quantity = 0;
        this.callback = callback;
    }

    public Food(String uid, String name, String size, String description, double price) {
        this.uid = uid;
        this.name = name;
        this.size = size;
        this.description = description;
        this.price = price;
    }

    public void generateView(Activity activity, LinearLayout lista){
        this.lista = lista;
        this.activity = activity;
        View v = LayoutInflater.from(activity).inflate(R.layout.menu_item, lista, false);
        TextView vName = v.findViewById(R.id.name);
        TextView vSize = v.findViewById(R.id.size);
        TextView vDescription = v.findViewById(R.id.description);
        TextView vPrice = v.findViewById(R.id.price);
        ImageView vImg = v.findViewById(R.id.image);
        TextView vQuantity = v.findViewById(R.id.quantity);
        ImageView plus = v.findViewById(R.id.plus);
        plus.setOnClickListener(view -> {
            quantity++;
            vQuantity.setText(String.valueOf(quantity));
            callback.onCallback(this, 1);
        });
        ImageView minus = v.findViewById(R.id.minus);
        minus.setOnClickListener(view -> {
            if(quantity > 0) {
                quantity--;
                vQuantity.setText(String.valueOf(quantity));
                callback.onCallback(this, -1);
            }
        });

        vName.setText(name);
        vSize.setText(size);
        vDescription.setText(description);
        vPrice.setText(new DecimalFormat("0.00").format(price).replace('.', ','));
        vImg.setImageResource(imgSrc);
        vQuantity.setText(String.valueOf(quantity));
        v.setTag(this);
//        constraintLayout.setOnTouchListener(TouchListener.getTouch("#FFFFFF"));
//        constraintLayout.setOnClickListener(view -> {
//        });
        this.view = v;

    }

    public String getUid() {return uid;}
    public String getName() {return name;}
    public double getPrice() {return price;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return uid.equals(food.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                '}';
    }

    public View getView() {return view;}
}