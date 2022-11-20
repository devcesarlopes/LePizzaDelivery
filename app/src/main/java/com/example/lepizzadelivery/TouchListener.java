package com.example.lepizzadelivery;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TouchListener {
    public static View.OnTouchListener getTouch(String color){
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9A9A9")));
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                        v.invalidate();
                        break;
                    }
                }

                return false;
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    public static View.OnTouchListener getSinlgeImageInActivityTouch(){
        return (v, event) -> {
            ImageView view = (ImageView) v;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    //overlay is black with transparency of 0x77 (119)
                    view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:
                    //clear the overlay
                    view.getDrawable().clearColorFilter();
                    view.invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL: {
                    //clear the overlay
                    view.getDrawable().clearColorFilter();
                    view.invalidate();
                    break;
                }
            }
            return false;
        };
    }
}
