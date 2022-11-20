package com.example.lepizzadelivery;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.Admin;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.models.Users.Worker;
import com.example.lepizzadelivery.session.SessionManagement;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.riversun.promise.Promise;

import java.util.ArrayList;
import java.util.List;


public class Api {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference reference;

    public Api(){

    }

    public void registerClient(Activity activity, Client user, ConstraintLayout loadingPage){
        loadingPage.setVisibility(View.VISIBLE);
        Promise.resolve(user)
        .then(new Promise((action, data) -> mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser == null){
                    Toast.makeText(activity, "Falha ao registrar o Login.", Toast.LENGTH_SHORT).show();
                    loadingPage.setVisibility(View.GONE);
                    action.reject();
                }else{
                    user.setUid(firebaseUser.getUid());
                    action.resolve(user);
                }
            } else {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Usuário já registrado.", Toast.LENGTH_SHORT).show();
            }
        })))
        .then(new Promise((action, data) -> {
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(User.UserTypes.CLIENT.toString()).child(user.getUid());
            reference.updateChildren(user.getMap())
                .addOnSuccessListener(unused -> {
                    SharedPrefsDatabase.SaveUserOnSharedPreferences(activity, user);
                    SessionManagement session = new SessionManagement(activity);
                    session.saveSession(user);
                    Router.goToClientHomePage(activity);
                })
            .addOnFailureListener(e -> {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Falha ao registrar o Usuário no banco de dados.", Toast.LENGTH_SHORT).show();
                action.reject();
            });
        })).start();
    }

    public Promise registerWorker(Activity activity, Worker worker, ConstraintLayout loadingPage){
        loadingPage.setVisibility(View.VISIBLE);
        return Promise.resolve(worker)
        .then(new Promise((action, data) -> mAuth.createUserWithEmailAndPassword(worker.getEmail(), worker.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser == null){
                    Toast.makeText(activity, "Falha ao registrar o Login.", Toast.LENGTH_SHORT).show();
                    loadingPage.setVisibility(View.GONE);
                    action.reject();
                }else{
                    worker.setUid(firebaseUser.getUid());
                    action.resolve(worker);
                }
            } else {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Usuário já registrado.", Toast.LENGTH_SHORT).show();
            }
        })))
        .then(new Promise((action, data) -> {
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(User.UserTypes.WORKER.toString()).child(worker.getUid());
            reference.updateChildren(worker.getMap())
            .addOnSuccessListener(unused -> action.resolve(worker))
            .addOnFailureListener(e -> {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Falha ao registrar o Usuário no banco de dados.", Toast.LENGTH_SHORT).show();
                action.reject();
            });
        }));
    }

    public void login(Activity activity, String email, String password, ConstraintLayout loadinPage){
        loadinPage.setVisibility(View.GONE);
        Promise.resolve()
        .then(new Promise((action, data) -> mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                action.resolve(FirebaseAuth.getInstance().getUid());
            } else {
                Toast.makeText(activity, "O Email ou a Senha Estão incorretos.", Toast.LENGTH_SHORT).show();
                loadinPage.setVisibility(View.VISIBLE);
                action.reject();
            }
        })))
        .then(new Promise((action, data) -> {
            String uid = (String) data;
            reference = FirebaseDatabase.getInstance().getReference();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot userType : snapshot.child("users").getChildren())
                        if(userType.child(uid).exists()) {
                            action.resolve(User.setValuesFromFirebase(uid, userType.getKey(), snapshot));
                            return;
                        }
                    loadinPage.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "Usuário não encontrado no banco de dados. Contate o Suporte!", Toast.LENGTH_SHORT).show();
                    action.reject();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loadinPage.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "Falha ao identificar o Usuário no banco de dados.", Toast.LENGTH_SHORT).show();
                    action.reject();
                }
            });
        }))
        .then(new Promise((action, data) -> {
            User user = (User) data;
            SharedPrefsDatabase.SaveUserOnSharedPreferences(activity, user);
            SessionManagement session = new SessionManagement(activity);
            session.saveSession(user);
            if(user instanceof Client) Router.goToClientHomePage(activity);
            if(user instanceof Admin) Router.goToAdminHomePage(activity);
        })).start();
    }

    public Promise getRestaurants(){
        return Promise.resolve()
        .then(new Promise((action, data) -> {
            List<Restaurant> restaurants = new ArrayList<>();
            reference = FirebaseDatabase.getInstance().getReference().child("restaurants");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot restaurant : snapshot.getChildren()) restaurants.add(new Restaurant(restaurant));
                    action.resolve(restaurants);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    action.reject();
                }
            });
        }));
    }

    public Promise registerRestaurant(Activity activity, Restaurant restaurant, ConstraintLayout loadingPage){
        loadingPage.setVisibility(View.VISIBLE);
        System.out.println("registerRestaurant");
        return Promise.resolve()
        .then(new Promise((action, data) -> {
            System.out.println("Promise");
            reference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurant.getUid());
            reference.updateChildren(restaurant.getMap())
            .addOnSuccessListener(unused -> {
                System.out.println("success");
                action.resolve(restaurant);
            })
            .addOnFailureListener(e -> {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Falha ao registrar o Restaurante no banco de dados.", Toast.LENGTH_SHORT).show();
                action.reject();
            });
        }));
    }
}
