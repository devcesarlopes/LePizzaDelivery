package com.example.lepizzadelivery;

import android.os.SystemClock;

public class DoubleClick {
    long lastClickTime = 0;
    long doubleClickInterval = 1000;

    public DoubleClick(){}

    public DoubleClick(long doubleClickInterval){
        this.doubleClickInterval = doubleClickInterval;
    }

    public boolean detected(){
        if (SystemClock.elapsedRealtime() - lastClickTime < doubleClickInterval){
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
}
