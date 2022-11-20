package com.example.lepizzadelivery;

import android.app.Activity;
import android.content.Intent;

import com.example.lepizzadelivery.ListSelectors.SelectMenuItems;
import com.example.lepizzadelivery.ListSelectors.SelectRestaurant;
import com.example.lepizzadelivery.admin.AdminHome;
import com.example.lepizzadelivery.client.ClientHome;

public class Router {

    public static void goToClientHomePage(Activity activity){
        activity.startActivity(new Intent(activity, ClientHome.class));
        activity.finish();
    }

    public static void goToAdminHomePage(Activity activity){
        activity.startActivity(new Intent(activity, AdminHome.class));
        activity.finish();
    }

    public static void goToLogin(Activity activity){
        activity.startActivity(new Intent(activity, Login.class));
        activity.finish();
    }

    public static Intent goToGoogleMapsApi(Activity activity){
        return new Intent(activity, GoogleMapsApi.class);
    }

    public static Intent goToSelectRestaurant(Activity activity) {
        return new Intent(activity, SelectRestaurant.class);
    }

    public static Intent goToSelectMenuItems(Activity activity){
        return new Intent(activity, SelectMenuItems.class);
    }
}
