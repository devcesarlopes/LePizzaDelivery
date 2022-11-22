package com.example.lepizzadelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.session.SessionManagement;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.d("INSTTESTE", "firebaseUser NULL");
            Router.goToLogin(this);
            return;
        }

        //if user is logged in --> Check User Type
        SessionManagement session = new SessionManagement(this);
        String userUid = session.getSession();
        Log.d("INSTTESTE", userUid);
        if(userUid == null || userUid.equals("-1")) {
            Router.goToLogin(this);
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String type = Objects.requireNonNull(SharedPrefsDatabase.getUser(this)).getType();
            if(type.equals(User.UserTypes.CLIENT.toString())) Router.goToClientHomePage(this);
            if(type.equals(User.UserTypes.ADMIN.toString())) Router.goToAdminHomePage(this);
            if(type.equals(User.UserTypes.WORKER.toString())) Router.goToWorkerHomePage(this);
        }, 1000);
    }
}