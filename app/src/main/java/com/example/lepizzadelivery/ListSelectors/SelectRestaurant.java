package com.example.lepizzadelivery.ListSelectors;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class SelectRestaurant extends AppCompatActivity {

    Intent intent;
    LinearLayout header;
    LinearLayout listaLayout;
    ImageView goBack;
    Api Api = new Api();
    DoubleClick doubleClick = new DoubleClick();

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_restaurant);

        intent = getIntent();

        header = findViewById(R.id.header);
        listaLayout = findViewById(R.id.listaLayout);
        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(view -> {
            if (doubleClick.detected()) return;
            onBackPressed();
        });

        Api.getRestaurants()
        .then(((action, data) -> {
            System.out.println("Api.getRestaurants()");
            List<Restaurant> restaurants = (ArrayList<Restaurant>) data;
            for (Restaurant restaurant : restaurants){
                View obj = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_select_restaurant, listaLayout, false);
                TextView vDistrict = obj.findViewById(R.id.district);
                vDistrict.setText(restaurant.getDistrict());
                obj.setTag(restaurant);
                obj.setOnClickListener(view -> {
                    if(doubleClick.detected()) return;
                    Intent intent = new Intent();
                    intent.putExtra("result", (Restaurant) obj.getTag());
                    setResult (SelectRestaurant.RESULT_OK, intent);
                    finish();
                });
                System.out.println(restaurant.getDistrict());
                listaLayout.addView(obj);
            }
        })).start();

    }
}