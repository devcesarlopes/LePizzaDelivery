package com.example.lepizzadelivery.models.coupon;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Coupon {

    public static final String available = "Disponível";
    public static final String unavailable = "Já Utilizado";

    String typeFreeDelivery = "Entrega Grátis";
    String typePercentage = "Desconto";
    String typefreeFood = "1 Item Grátis";
    String typePay1Take2 = "Compre 1 Leve 2";

    protected String uuid;
    protected String type;
    protected Date deadline;

    public Coupon(String uuid, Date deadline) {
        this.uuid = uuid;
        this.deadline = deadline;
    }

    public String getUuid() {return uuid;}
    public String getType() {return type;}
    public abstract String getName();
    public abstract int getImageResource();
    @SuppressLint("SimpleDateFormat")
    public String getDeadLine() {return new SimpleDateFormat("dd/MMM/yyyy").format(deadline);}
//    public abstract void applyCoupon();
}
