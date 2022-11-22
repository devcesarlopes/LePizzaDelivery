package com.example.lepizzadelivery.models.menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.food.Food;
import com.example.lepizzadelivery.models.food.OnFoodSelectedCallback;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Menu implements Serializable {
    HashMap<String, List<Food>> menu = new HashMap<>();
    Restaurant restaurant;
    OnMenuUpdatedCallback onMenuUpdatedCallback;
    Activity activity;
    LinearLayout list;
    Order order;

    public Menu(Activity activity, Restaurant restaurant, OnMenuUpdatedCallback onMenuUpdatedCallback, LinearLayout lista, Double deliveryPrice) {
        this.activity = activity;
        this.list = lista;
        this.restaurant = restaurant;
        this.onMenuUpdatedCallback = onMenuUpdatedCallback;
        this.order = new Order(restaurant, deliveryPrice);

        OnFoodSelectedCallback onFoodSelectedCallback = (food, multiplier) -> onMenuUpdatedCallback.onCallback(order.updateOrder(food, multiplier));
        
        if (restaurant.foodTypes.contains("Lanches")) {
            List<Food> list = new ArrayList<>();
            list.add(new Food("ed3bfd4a-8afc-4dde-8ab7-f1b1352a7a87", "Coxinha de Camarão", "300g", "Coxinha de Camarão delicioa!", 17.50, R.drawable.coxinhacamarao, onFoodSelectedCallback));
            list.add(new Food("a79f84ae-e9b4-438e-819e-18f1cf4a9d68", "Coxinha Oriental", "300g", "Pra você que gosta de sushi", 17.50, R.drawable.coxinhaoriental, onFoodSelectedCallback));
            list.add(new Food("4a1515dd-baab-4f79-8130-c54795abab1b", "Coxinha 4 Quejos", "350g", "4 queijos, parmesão, gorgonzola, cheddar e muzzarella!", 17.50, R.drawable.coxinhaqueijos, onFoodSelectedCallback));
            list.add(new Food("c64fa6a0-1480-4cba-b778-19131e0569b2", "Coxinha Tradicional", "200g", "Frago com catupiry!", 17.50, R.drawable.coxinhatradicional, onFoodSelectedCallback));
            list.add(new Food("54afb23c-ddb2-4379-850a-3bc8b0b56dbc", "Kibe", "250g", "Para você que gosta de Picante!", 17.50, R.drawable.coxinhakibe, onFoodSelectedCallback));
            menu.put("Lanches", list);
        }
        if (restaurant.foodTypes.contains("Pizza")) {
            List<Food> list = new ArrayList<>();
            list.add(new Food("a35e55a3-1a12-40e2-b37b-391f7ade5a69", "Pizza Média", "8 Fatias", "Pizza Média até 2 sabores!", 30.50, R.drawable.pizzamedia, onFoodSelectedCallback));
            list.add(new Food("a502172c-32a3-46c4-a968-204177173b4b", "Pizza Grande", "10 Fatias", "Pizza Grande até 2 sabores!", 35.50, R.drawable.pizzagrande, onFoodSelectedCallback));
            list.add(new Food("564ba882-5ba0-4521-8807-9ae146f40f90", "Pizza Família", "12 Fatias", "Pizza Família até 3 sabores!", 39.50, R.drawable.pizzafamilia, onFoodSelectedCallback));
            menu.put("Pizza", list);
        }
        if (restaurant.foodTypes.contains("Hambúrguer")) {
            List<Food> list = new ArrayList<>();
            list.add(new Food("1d73d692-826d-425a-a509-771e444b16bf", "Angus", "400g", "Hambúrguer de angus, já provou?", 22.90, R.drawable.angus, onFoodSelectedCallback));
            list.add(new Food("67b35c5d-1d71-40c6-ab6f-c9c989439d1c", "Double Burguer", "500g", "Pra você que gosta de sushi", 25.90, R.drawable.doubleburguer, onFoodSelectedCallback));
            menu.put("Hambúrguer", list);
        }
        if (restaurant.foodTypes.contains("Bebidas")) {
            List<Food> list = new ArrayList<>();
            list.add(new Food("d97b359b-3aaf-4668-ba2a-b94ecaecf677", "Coca Cola", "350ml", "Coca Cola em lata 350ml", 6.50, R.drawable.coca, onFoodSelectedCallback));
            list.add(new Food("badc45cc-31ae-4ad0-89b5-ea88e787bf89", "Fanta Laranja", "350ml", "Fanta Laranja em lata 350ml", 5.50, R.drawable.fantalaranja, onFoodSelectedCallback));
            list.add(new Food("119db1c8-2825-492d-96e0-d90906e26dc1", "Fanta Uva", "350ml", "Fanta Uva em lata 350ml", 5.50, R.drawable.fantauva, onFoodSelectedCallback));
            list.add(new Food("06b7bbaa-08d5-4531-a726-c0fa4a1480a8", "H2OH", "500ml", "H2Oh 500ml", 6.50, R.drawable.hdoisoh, onFoodSelectedCallback));
            list.add(new Food("d73cc777-5df2-49d7-a106-c23f740840c3", "Heineken", "390ml", "Final de semestre vai rolar né?", 8.50, R.drawable.heineken, onFoodSelectedCallback));
            menu.put("Bebidas", list);
        }
        if (restaurant.foodTypes.contains("MilkShake")) {
            List<Food> list = new ArrayList<>();
            list.add(new Food("87a09400-da40-48fa-980d-c9dd8072ad6d", "MilkShake Chocolate", "500ml", "MilkShake de Chocolate com calda", 13.90, R.drawable.milkchoco, onFoodSelectedCallback));
            list.add(new Food("15af14bd-e72e-40a9-96aa-aa43c22240f3", "MilkShake Morango", "500ml", "MilkShake de Morango com pedaços de fruta", 13.90, R.drawable.milkmorango, onFoodSelectedCallback));
            list.add(new Food("e10e9d12-4745-4b26-98bd-241aeec79bf0", "MilkShake Ovomaltine", "500ml", "MilkShake de Ovomaltine com pedaços de chocolate", 13.90, R.drawable.milkovomaltine, onFoodSelectedCallback));
            menu.put("MilkShake", list);
        }

        for (Entry<String, List<Food>> entry : menu.entrySet()){
            generateMenuSectionTitle(activity, lista, entry.getKey());
            entry.getValue().forEach(food -> {
                food.generateView(activity, lista);
                lista.addView(food.getView());
            });
        }


        System.out.println(menu);
    }

    private void generateMenuSectionTitle(Activity activity, LinearLayout lista, String text){
        View v = LayoutInflater.from(activity).inflate(R.layout.menu_section_title, lista, false);
        TextView vText = v.findViewById(R.id.text);
        vText.setText(text);
        lista.addView(v);
    }
}



















