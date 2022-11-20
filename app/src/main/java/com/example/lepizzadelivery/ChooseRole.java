package com.example.lepizzadelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseRole extends AppCompatActivity {

    CardView restaurantButton, clientButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        restaurantButton = findViewById(R.id.restaurant);
        clientButton = findViewById(R.id.client);

        restaurantButton.setOnTouchListener(TouchListener.getTouch("#FFFFFF"));
        clientButton.setOnTouchListener(TouchListener.getTouch("#000000"));

        restaurantButton.setOnClickListener(view -> {
            System.out.println("restaurante");
        });

        clientButton.setOnClickListener(view -> {
            startActivity(new Intent(this, Register.class));
            finish();
        });
    }
}