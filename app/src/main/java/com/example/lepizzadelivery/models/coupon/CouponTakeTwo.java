package com.example.lepizzadelivery.models.coupon;

import android.annotation.SuppressLint;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.food.Food;

import java.util.Date;

public class CouponTakeTwo extends Coupon{

    Food food;

    public CouponTakeTwo(String uuid, Date deadline, Food food) {
        super(uuid, deadline);
        this.type = typePercentage;
        this.food = food;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getName() {
//        return "Compre 1 " + food.getName() + " e Leve 2";
        return "Compre 1 e Leve 2";

    }

    @Override
    public int getImageResource() {
        return R.drawable.ic_action_food;
    }
}
