package com.example.lepizzadelivery.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lepizzadelivery.models.Users.Admin;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.models.Users.Worker;
import com.google.gson.Gson;

public class SharedPrefsDatabase {

    public static void SaveUserOnSharedPreferences(Context context, User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);

        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println(user.getType());
        editor.putString("type", user.getType());
        editor.putString("user", json);
        editor.apply();
    }

    public static User getUser(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", 0);
        String type = sharedPreferences.getString("type", "");
        String json = sharedPreferences.getString("user", "");
        if(type.equals(User.UserTypes.ADMIN.toString())){
            return gson.fromJson(json, Admin.class);
        }else if(type.equals(User.UserTypes.CLIENT.toString())){
            return gson.fromJson(json, Client.class);
        }else if(type.equals(User.UserTypes.WORKER.toString())){
            return gson.fromJson(json, Worker.class);
        }
        return null;
    }
}
