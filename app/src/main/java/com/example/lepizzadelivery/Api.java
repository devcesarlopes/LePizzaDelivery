package com.example.lepizzadelivery;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lepizzadelivery.models.Order;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
                    SharedPrefsDatabase.SaveUserOnSharedPreferences(activity, user, Context.MODE_PRIVATE);
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
        loadinPage.setVisibility(View.VISIBLE);
        Promise.resolve()
        .then(new Promise((action, data) -> mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            System.out.println("Promise1");
            if (task.isSuccessful()) {
                action.resolve(FirebaseAuth.getInstance().getUid());
            } else {
                Toast.makeText(activity, "O Email ou a Senha Estão incorretos.", Toast.LENGTH_SHORT).show();
                loadinPage.setVisibility(View.GONE);
                action.reject();
            }
        })))
        .then(new Promise((action, data) -> {
            System.out.println("Promise2");
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
                    System.out.println("PROMISE2 REJECT");
                    loadinPage.setVisibility(View.GONE);
                    Toast.makeText(activity, "Usuário não encontrado no banco de dados. Contate o Suporte!", Toast.LENGTH_SHORT).show();
                    action.reject();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loadinPage.setVisibility(View.GONE);
                    Toast.makeText(activity, "Falha ao identificar o Usuário no banco de dados.", Toast.LENGTH_SHORT).show();
                    action.reject();
                }
            });
        }))
        .then(new Promise((action, data) -> {
            System.out.println("PROMISE3");
            User user = (User) data;
            System.out.println(user);
            SharedPrefsDatabase.SaveUserOnSharedPreferences(activity, user, Context.MODE_PRIVATE);
            System.out.println("SharedPrefsDatabase");

            SessionManagement session = new SessionManagement(activity);
            System.out.println("SessionManagement");

            session.saveSession(user);
            System.out.println("saveSession");
            System.out.println(user);
            if(user instanceof Client) Router.goToClientHomePage(activity);
            if(user instanceof Admin) Router.goToAdminHomePage(activity);
            if(user instanceof Worker) Router.goToWorkerHomePage(activity);
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

    public Promise registerOrder(Activity activity, Order order, ConstraintLayout loadingPage){
        loadingPage.setVisibility(View.VISIBLE);
        System.out.println("registerOrder");
        return Promise.resolve()
        .then(new Promise((action, data) -> {
            System.out.println("Promise");
            reference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("users")
                    .child(User.UserTypes.CLIENT.name())
                    .child(order.getUser().getUid())
                    .child("orders")
                    .child(order.getUid());
            reference.updateChildren(order.getUserMap())
            .addOnSuccessListener(unused -> {
                System.out.println("success");
                action.resolve();
            })
            .addOnFailureListener(e -> {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Falha ao registrar o Pedido no banco de dados.", Toast.LENGTH_SHORT).show();
                action.reject();
            });
        }))
        .then(new Promise((action, data) -> {
            reference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("restaurants")
                    .child(order.getRestaurant().getUid())
                    .child("orders")
                    .child(order.getUid());
            reference.updateChildren(order.getRestaurantMap())
            .addOnSuccessListener(unused -> {
                System.out.println("success");
                action.resolve();
            })
            .addOnFailureListener(e -> {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(activity, "Falha ao registrar o Pedido no banco de dados.", Toast.LENGTH_SHORT).show();
                action.reject();
            });
        }));
    }

    @SuppressWarnings("unchecked")
    public Promise getAcceptedOrders(Client user){
    return getRestaurants()
        .then(new Promise((action, data) -> {
            List<Restaurant> restaurants = (ArrayList<Restaurant>) data;
            List<Order> orders = new ArrayList<>();
            reference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("users")
                    .child(User.UserTypes.CLIENT.name())
                    .child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot user) {
                    if(!user.child("orders").exists()){
                        action.resolve(orders);
                        return;
                    }
                    for (DataSnapshot order : user.child("orders").getChildren()){
                        if(Objects.equals(order.child("status").getValue(String.class), Order.OrderStatus.RESTAURANT_ACCEPTED.name())){
                            Restaurant restaurant = restaurants.stream()
                                    .filter(restaurant1 -> restaurant1.getUid().equals(order.child("restaurantUid").getValue(String.class)))
                                    .findFirst()
                                    .orElse(null);
                            orders.add(new Order(restaurant, order));
                        }
                    }
                    action.resolve(orders);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    action.resolve(orders);
                }
            });
        }));
    }

    public void acceptOrder(Activity activity, Order order, Client user, String orderStatus){
        String newOrderStatus = (orderStatus.equals(Order.OrderStatus.CLIENT_ORDERED.name())) ? Order.OrderStatus.RESTAURANT_ACCEPTED.name() : Order.OrderStatus.DELIVERED.name();
        Promise.resolve()
        .then(new Promise((action, data) -> {
            reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(User.UserTypes.CLIENT.name())
                .child(user.getUid())
                .child("orders")
                .child(order.getUid())
                .child("status");
            reference.setValue(newOrderStatus)
                .addOnSuccessListener(unused -> {
                    System.out.println("success1");
                    action.resolve();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Falha ao registrar o Pedido no banco de dados.", Toast.LENGTH_SHORT).show();
                    action.reject();
                });
        }))
        .then(new Promise((action, data) -> {
            reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("restaurants")
                .child(order.getRestaurant().getUid())
                .child("orders")
                .child(order.getUid())
                .child("status");
            reference.setValue(newOrderStatus)
                .addOnSuccessListener(unused -> {
                    String text = (orderStatus.equals(Order.OrderStatus.CLIENT_ORDERED.name())) ? "Pedido Aceito com Sucesso!" : "Pedido Finalizado com sucesso!";
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                    action.resolve();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Falha ao registrar o Pedido no banco de dados.", Toast.LENGTH_SHORT).show();
                    action.reject();
                });
        }))
        .then(new Promise((action, data) -> {
            if(orderStatus.equals(Order.OrderStatus.CLIENT_ORDERED.name())){
                action.resolve();
                return;
            }
            reference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("restaurants")
                    .child(order.getRestaurant().getUid());
            reference.updateChildren(order.getRestaurant().getOrderMap(order))
                    .addOnSuccessListener(unused -> {
                        action.resolve();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(activity, "Falha ao registrar o Pedido no banco de dados.", Toast.LENGTH_SHORT).show();
                        action.reject();
                    });
        })).start();
    }
}
