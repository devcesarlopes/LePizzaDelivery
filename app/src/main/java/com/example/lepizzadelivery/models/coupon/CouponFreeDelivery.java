package com.example.lepizzadelivery.models.coupon;

import com.example.lepizzadelivery.R;

import java.util.Date;

public class CouponFreeDelivery extends Coupon{


    public CouponFreeDelivery(String uuid, Date deadline) {
        super(uuid, deadline);
        this.type = typeFreeDelivery;
    }

    @Override
    public String getName() {
        return this.type;
    }

    @Override
    public int getImageResource() {
        return R.drawable.ic_action_delivery;
    }


}
