package com.example.lepizzadelivery.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lepizzadelivery.models.Users.Admin;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.models.Users.Worker;
import com.google.gson.Gson;

public class SharedPrefsDatabase {

    public static void SaveUserOnSharedPreferences(Context context, User user, int MODE_PRIVATE) {
        Gson gson = new Gson();
        System.out.println("GSON");
        String json = null;
        if(user instanceof Client) {
            System.out.println("GSON CLIENT");
            try{
                gson.toJson(user);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            json = gson.toJson(user);
            System.out.println(json);
        }
        if(user instanceof Worker) json = gson.toJson(user, Worker.class);
        if(user instanceof Admin) json = gson.toJson(user, Admin.class);

        System.out.println("toJson");
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        System.out.println("getSharedPreferences");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Editor");

        System.out.println(user.getType());
        editor.putString("type", user.getType());
        editor.putString("user", json);
        editor.apply();

        System.out.println("SaveUserOnSharedPreferences");
    }

    public static User getUser(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", 0);
        String type = sharedPreferences.getString("type", "");
        String json = sharedPreferences.getString("user", "");
        System.out.println(type);
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
