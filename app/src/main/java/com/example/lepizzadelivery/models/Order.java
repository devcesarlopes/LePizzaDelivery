package com.example.lepizzadelivery.models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.TouchListener;
import com.example.lepizzadelivery.client.ClientHome;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.food.Food;
import com.example.lepizzadelivery.models.menu.DefaultMenu;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Order implements Serializable, DefaultMenu {

    public enum OrderStatus{
        CLIENT_PICKING,
        CLIENT_ORDERED,
        RESTAURANT_ACCEPTED,
        RESTAURANT_REJECTED,
        DELIVERED
    };
    String uid;
    Restaurant restaurant;
    Client user;
    Date clientOrderedTime;
    HashMap<Food, Integer> orderFoodList = new HashMap<>();
    Double deliveryPrice;
    Double foodPrice;
    String status;

    public Order(Restaurant restaurant, Double deliveryPrice) {
        this.uid = UUID.randomUUID().toString();
        this.restaurant = restaurant;
        this.deliveryPrice = deliveryPrice;
        this.status = OrderStatus.CLIENT_PICKING.name();
    }

    public Order(Restaurant restaurant, DataSnapshot order) {
        this.uid = order.child("uid").getValue(String.class);
        this.restaurant = restaurant;
        this.deliveryPrice = order.child("deliveryPrice").getValue(Double.class);
        this.foodPrice = order.child("foodPrice").getValue(Double.class);
        this.status = order.child("status").getValue(String.class);
        for(DataSnapshot foodList : order.child("orderedFood").getChildren()){
            Food food = Objects.requireNonNull(defaultMenu.get(foodList.getKey()));
            orderFoodList.put(food, Objects.requireNonNull(foodList.getValue(Integer.class)));
        }
    }

    public Order updateOrder(Food food, int multiplier){
        orderFoodList.putIfAbsent(food, 0);
        if(multiplier > 0) orderFoodList.put(food, Objects.requireNonNull(orderFoodList.get(food)) + 1);
        else orderFoodList.put(food, Objects.requireNonNull(orderFoodList.get(food)) - 1);
        return this;
    }

    public boolean isEmpty(){
        if(orderFoodList.values().isEmpty()) return true;
        for(Integer quantity : orderFoodList.values()) if(quantity > 0) return false;
        return true;
    }

    private Double getTotalPriceDouble() {
        return orderFoodList.
                entrySet().
                stream().
                mapToDouble(entry->entry.getKey().getPrice()*entry.getValue()).sum()
                + deliveryPrice;
    }

    private Double getFoodPriceDouble(){
        return orderFoodList.
                entrySet().
                stream().
                mapToDouble(entry->entry.getKey().getPrice()*entry.getValue()).sum();
    }

    public String getTotalPrice(){
        if(isEmpty()) return "0,00";
        return new DecimalFormat("0.00").format(getTotalPriceDouble()).replace(".", ",");
    }

    public void finishOrder(Client user){
        this.status = OrderStatus.CLIENT_ORDERED.name();
        this.clientOrderedTime = new Date();
        this.user = user;
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public View generateView(Activity activity, LinearLayout lista){
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_client_orders, lista, false);
        TextView vDistrict = v.findViewById(R.id.district);
        TextView vFoodType = v.findViewById(R.id.foodType);
        TextView vrating = v.findViewById(R.id.rating);
        TextView vDeliveryPrice = v.findViewById(R.id.deliveryPrice);
        TextView vTotalPrice = v.findViewById(R.id.totalPice);
        LinearLayout vOrderedFood = v.findViewById(R.id.list);
        vDistrict.setText(restaurant.getDistrict());
        vFoodType.setText(restaurant.getFoodTypes());
        vrating.setText(restaurant.getRating());
        vDeliveryPrice.setText("R$" + new DecimalFormat("0.00").format(deliveryPrice).replace('.',','));
        vTotalPrice.setText("R$" + getTotalPrice());
        for (Map.Entry<Food, Integer> entry : orderFoodList.entrySet()){
            View foodView = LayoutInflater.from(activity).inflate(R.layout.custom_client_orders_item, vOrderedFood, false);
            TextView vName = foodView.findViewById(R.id.name);
            TextView vQuantity = foodView.findViewById(R.id.quantity);
            TextView vPrice = foodView.findViewById(R.id.price);
            vName.setText(entry.getKey().getName());
            vQuantity.setText(String.valueOf(entry.getValue()));
            vPrice.setText("R$" + new DecimalFormat("0.00").format(entry.getKey().getPrice()).replace('.',','));
            vOrderedFood.addView(foodView);
        }
        return v;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public View generateView(Activity activity, LinearLayout lista, Client client, String orderStatus, DoubleClick doubleClick){
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_worker_orders, lista, false);
        ImageFilterView imageLogo = v.findViewById(R.id.logo);
        CardView orderButton = v.findViewById(R.id.orderButton);
        TextView orderButtonText = v.findViewById(R.id.buttonText);
        TextView vUserName = v.findViewById(R.id.userName);
        TextView vUserPhone = v.findViewById(R.id.userPhone);
        TextView vUserAddress = v.findViewById(R.id.userAddress);
        TextView vDeliveryPrice = v.findViewById(R.id.deliveryPrice);
        TextView vTotalPrice = v.findViewById(R.id.totalPice);
        LinearLayout vOrderedFood = v.findViewById(R.id.list);
        vUserName.setText(client.getName());
        vUserPhone.setText(client.getPhone());
        vUserAddress.setText(client.getAddress().getName());
        vDeliveryPrice.setText("R$" + new DecimalFormat("0.00").format(deliveryPrice).replace('.',','));
        vTotalPrice.setText("R$" + getTotalPrice());
        for (Map.Entry<Food, Integer> entry : orderFoodList.entrySet()){
            View foodView = LayoutInflater.from(activity).inflate(R.layout.custom_client_orders_item, vOrderedFood, false);
            TextView vName = foodView.findViewById(R.id.name);
            TextView vQuantity = foodView.findViewById(R.id.quantity);
            TextView vPrice = foodView.findViewById(R.id.price);
            vName.setText(entry.getKey().getName());
            vQuantity.setText(String.valueOf(entry.getValue()));
            vPrice.setText("R$" + new DecimalFormat("0.00").format(entry.getKey().getPrice()).replace('.',','));
            vOrderedFood.addView(foodView);
        }
        if(orderStatus.equals(OrderStatus.CLIENT_ORDERED.name())){
            orderButtonText.setText("Aceitar Pedido");
            imageLogo.setImageResource(R.drawable.ic_action_notes);
        }else if(orderStatus.equals(OrderStatus.RESTAURANT_ACCEPTED.name())){
            orderButtonText.setText("Pedido Entregue");
            imageLogo.setImageResource(R.drawable.ic_action_delivered);
        }
        orderButton.setOnTouchListener(TouchListener.getTouch("#D7B807"));
        orderButton.setOnClickListener(view -> {
            if(doubleClick.detected()) return;
            Api api = new Api();
            api.acceptOrder(activity, Order.this, client, orderStatus);
        });
        return v;
    }


    public String getUid() {return uid;}
    public Client getUser() {return user;}
    public Restaurant getRestaurant() {return restaurant;}

    @SuppressLint("SimpleDateFormat")
    public Map<String, Object> getUserMap(){
        Map<String, Object> map = new HashMap<>();
        HashMap<String, Object> orderedFood = new HashMap<>();
        orderFoodList.forEach((key, value) -> {
            if (value == 0) return;
            orderedFood.put(key.getUid(), value);
        });
        map.put("uid", uid);
        map.put("orderedFood", orderedFood);
        map.put("status", OrderStatus.CLIENT_ORDERED.name());
        map.put("foodPrice", getFoodPriceDouble());
        map.put("deliveryPrice", deliveryPrice);
        map.put("totalPrice", getTotalPriceDouble());
        map.put("restaurantUid", restaurant.getUid());
        map.put("clientOrderedTime", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(clientOrderedTime));
        System.out.println(map);
        return map;
    }

    @SuppressLint("SimpleDateFormat")
    public Map<String, Object> getRestaurantMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("status", OrderStatus.CLIENT_ORDERED.name());
        map.put("userUid", user.getUid());
        return map;
    }
}
