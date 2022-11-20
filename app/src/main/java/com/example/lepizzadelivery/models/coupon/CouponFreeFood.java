package com.example.lepizzadelivery.models.coupon;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.food.Food;

import java.util.Date;

public class CouponFreeFood extends Coupon{

    Food food;

    public CouponFreeFood(String uuid, Date deadline, Food food) {
        super(uuid, deadline);
        this.type = typefreeFood;
        this.food = food;
    }

    @Override
    public String getName() {
        return food.getName() + " Grátis";
    }

    @Override
    public int getImageResource() {
        return R.drawable.ic_action_food;
    }
}
