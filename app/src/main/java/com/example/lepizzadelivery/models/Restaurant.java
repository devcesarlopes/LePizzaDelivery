package com.example.lepizzadelivery.models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.TouchListener;
import com.example.lepizzadelivery.models.Users.Admin;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.models.coupon.Coupon;
import com.example.lepizzadelivery.client.ClientHome;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Restaurant implements Serializable {

    //Layout Visualization
    transient LinearLayout lista;
    transient Activity activity;
    transient View restaurantView;
    transient View couponView;
    transient int height;

    //Attributes
    String uid;
    Address address;
    String district;
    List<String> foodTypes;
    transient Double distance;
    transient int minTime;
    transient int maxTime;
    double rating;
    int ratingVotes;
    Coupon coupon;
    Menu menu;
    int deliveries;
    double income;

    public Restaurant(String district, Address address, List<String> foodTypes) {
        this.uid = UUID.randomUUID().toString();
        this.address = address;
        this.district = district;
        this.foodTypes = foodTypes;
        this.rating = 5.0;
        this.ratingVotes = 1;
        this.deliveries = 0;
        this.income = 0.00;
    }

    public Restaurant(DataSnapshot restaurant) {
        this.uid = restaurant.getKey();
        this.deliveries = Objects.requireNonNull(restaurant.child("deliveries").getValue(Integer.class));
        this.district = restaurant.child("district").getValue(String.class);
        this.address = new Address(restaurant.child("address").getValue(String.class), restaurant.child("lat").getValue(Double.class), restaurant.child("lng").getValue(Double.class));
        List<String> foodtypesList = new ArrayList<>();
        restaurant.child("foodTypes").getChildren().iterator().forEachRemaining(dataSnapshot -> foodtypesList.add(dataSnapshot.getValue(String.class)));
        this.foodTypes = foodtypesList;
        this.rating = Objects.requireNonNull(restaurant.child("rating").getValue(Double.class));
        this.ratingVotes = Objects.requireNonNull(restaurant.child("rating").getValue(Integer.class));
        this.income = Objects.requireNonNull(restaurant.child("income").getValue(Double.class));
//        this.coupon = restaurant.child("coupon").getValue(String.class);
    }


//    public Restaurant(String district, )

    public void generateView(Activity activity, LinearLayout lista, User user){
        this.lista = lista;
        this.activity = activity;
        if(user instanceof Client) {
            Client client = (Client) user;
            Double distance = client.getAddress().getDistance(this.address);
            this.distance = Double.parseDouble(Address.format(distance, 1));
            this.minTime = Integer.parseInt(String.valueOf(distance*5));
            this.maxTime = Integer.parseInt(String.valueOf(distance*10));
            generateClientRestaurantView();
            generateClientCouponView();
        } else if(user instanceof Admin){
            generateAdminRestaurantView();
            generateAdminCouponView();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void generateClientRestaurantView(){
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_client_restaurant, lista, false);
        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintLayout);
        TextView vDistrict = v.findViewById(R.id.district);
        TextView vFoodType = v.findViewById(R.id.foodType);
        TextView vDistance = v.findViewById(R.id.distance);
        TextView vMinTime = v.findViewById(R.id.minTime);
        TextView vMaxTime = v.findViewById(R.id.maxTime);
        TextView vrating = v.findViewById(R.id.rating);
        vDistrict.setText(getDistrict());
        vFoodType.setText(getFoodTypes());
        vDistance.setText(getDistance());
        vMinTime.setText(getMinTime());
        vMaxTime.setText(getMaxTime());
        vrating.setText(getrating());
        v.setTag(this);
        constraintLayout.setOnTouchListener(TouchListener.getTouch("#FFFFFF"));
        constraintLayout.setOnClickListener(view -> {
            ((ClientHome)activity).openRestaurantFragment(this);
        });
        this.restaurantView = v;
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void generateClientCouponView(){
        if(coupon == null) return;
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_coupon, lista, false);
        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintLayout);
        TextView vDistrict = v.findViewById(R.id.district);
        TextView vCouponType = v.findViewById(R.id.couponType);
        TextView vDistance = v.findViewById(R.id.distance);
        TextView vMinTime = v.findViewById(R.id.minTime);
        TextView vMaxTime = v.findViewById(R.id.maxTime);
        TextView vUsed = v.findViewById(R.id.used);
        TextView vDeadline = v.findViewById(R.id.deadline);
        ImageView vLogo = v.findViewById(R.id.couponLogo);
        vDistrict.setText(getDistrict());
        vCouponType.setText(coupon.getName());
        vDeadline.setText("Até " + coupon.getDeadLine());
        vDistance.setText(getDistance());
        vMinTime.setText(getMinTime());
        vMaxTime.setText(getMaxTime());
        vUsed.setText(Coupon.available);
        vLogo.setImageResource(coupon.getImageResource());
        v.setTag(this);
        constraintLayout.setOnTouchListener(TouchListener.getTouch("#FFFFFF"));
        constraintLayout.setOnClickListener(view -> {
        });
        this.couponView = v;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void generateAdminRestaurantView(){
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_admin_restaurant, lista, false);
        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintLayout);
        TextView vDistrict = v.findViewById(R.id.district);
        TextView vFoodType = v.findViewById(R.id.foodType);
        TextView vDeliveries = v.findViewById(R.id.deliveries);
        TextView vIncome = v.findViewById(R.id.income);
        TextView vrating = v.findViewById(R.id.rating);
        vDistrict.setText(getDistrict());
        vFoodType.setText(getFoodTypes());
        vDeliveries.setText(getDeliveries());
        vIncome.setText(getIncome());
        vrating.setText(getrating());
        v.setTag(this);
        constraintLayout.setOnTouchListener(TouchListener.getTouch("#FFFFFF"));
        constraintLayout.setOnClickListener(view -> {
        });
        this.restaurantView = v;
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void generateAdminCouponView(){
        if(coupon == null) return;
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_coupon, lista, false);
        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintLayout);
        TextView vDistrict = v.findViewById(R.id.district);
        TextView vCouponType = v.findViewById(R.id.couponType);
        TextView vDistance = v.findViewById(R.id.distance);
        TextView vMinTime = v.findViewById(R.id.minTime);
        TextView vMaxTime = v.findViewById(R.id.maxTime);
        TextView vUsed = v.findViewById(R.id.used);
        TextView vDeadline = v.findViewById(R.id.deadline);
        ImageView vLogo = v.findViewById(R.id.couponLogo);
        vDistrict.setText(getDistrict());
        vCouponType.setText(coupon.getName());
        vDeadline.setText("Até " + coupon.getDeadLine());
        vDistance.setText(getDistance());
        vMinTime.setText(getMinTime());
        vMaxTime.setText(getMaxTime());
        vUsed.setText(Coupon.available);
        vLogo.setImageResource(coupon.getImageResource());
        v.setTag(this);
        constraintLayout.setOnTouchListener(TouchListener.getTouch("#FFFFFF"));
        constraintLayout.setOnClickListener(view -> {
        });
        this.couponView = v;
    }

    public String getUid() {return uid;}
    public String getDistrict() {return district;}
    public String getFoodTypes() {return String.join(", ", foodTypes);}
    public String getDistance() {return String.valueOf(distance);}
    public String getMinTime() {return String.valueOf(minTime);}
    public String getMaxTime() {return String.valueOf(maxTime);}
    public String getrating() {return new DecimalFormat("##.0").format(rating);}
    public double getratingNum() {return rating;}
    public Coupon getCoupon() {return coupon;}
    public double getRating() {return rating;}
    public int getRatingVotes() {return ratingVotes;}
    public String getDeliveries() {return String.valueOf(deliveries);}
    public String getIncome() {return new DecimalFormat("#.##").format(income);}
    public View getRestaurantView() {return restaurantView;}
    public View getCouponView() {return couponView;}
    public Map<String, Object> getMap(){
        return new HashMap<String, Object>(){{
            put("uid", uid);
            put("deliveries", deliveries);
            put("district", district);
            put("foodTypes", IntStream.range(0, foodTypes.size()).boxed().collect(Collectors.toMap(String::valueOf, foodTypes::get)));
            put("income", income);
            put("rating", rating);
            put("ratingVotes", ratingVotes);
            put("address", address.getName());
            put("lat", address.getLat());
            put("lng", address.getLng());
        }};
    }
    public int getHeight() {return height;}
    public void setHeight(int height) {
        this.height = height;
    }
}
