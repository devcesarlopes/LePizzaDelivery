package com.example.lepizzadelivery.models.coupon;

import android.annotation.SuppressLint;

import com.example.lepizzadelivery.R;

import java.util.Date;

public class CouponPercentage extends Coupon{

    double percentage;

    public CouponPercentage(String uuid, Date deadline, double percentage) {
        super(uuid, deadline);
        this.type = typePercentage;
        this.percentage = percentage;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getName() {
        return String.format("%.2f", percentage) + "% de Desconto";
    }

    @Override
    public int getImageResource() {
        return R.drawable.ic_action_percentage;
    }
}
