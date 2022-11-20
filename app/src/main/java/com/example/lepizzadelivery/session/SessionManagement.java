package com.example.lepizzadelivery.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lepizzadelivery.models.Users.User;


public class SessionManagement {
    public static final String USER_NOT_FOUND = "-1";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        //save session of user whenever user is logged in
        String uid = user.getUid();
        editor.putString(SESSION_KEY,uid).commit();
    }

    public String getSession(){
        //return user whose session is saved
        return sharedPreferences.getString(SESSION_KEY, USER_NOT_FOUND);
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,USER_NOT_FOUND).commit();
    }
}
