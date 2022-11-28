package com.example.lepizzadelivery.models.Users;

import com.example.lepizzadelivery.models.Address;
import com.example.lepizzadelivery.models.Restaurant;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class User implements Serializable {

    public enum UserTypes{
        CLIENT,
        ADMIN,
        WORKER
    };
    String uid;
    String name;
    String email;
    String phone;
    String password;
    String type;

    public User(){}

    public User(String email){ this.email = email; }
    public String getUid() {return uid;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getPhone() {return phone;}
    public String getType() {return type;}
    public abstract Map<String, Object> getMap();
    public void setUid(String uid) { this.uid = uid; }

    public static User setValuesFromFirebase(String uid, String type, DataSnapshot dataSnapshot){
        DataSnapshot snapshot = dataSnapshot.child("users").child(type).child(uid);
        String email = snapshot.child("email").getValue(String.class);
        String name = snapshot.child("name").getValue(String.class);
        if(type.equals(UserTypes.CLIENT.toString())){
            System.out.println(type);
            String phone = snapshot.child("phone").getValue(String.class);
            Address address = new Address(snapshot.child("address").getValue(String.class), snapshot.child("lat").getValue(Double.class), snapshot.child("lng").getValue(Double.class));
            return new Client(uid, name, email, phone, address);
        }else if(type.equals(UserTypes.WORKER.toString())){
            System.out.println(type);
            String restaurantUid = snapshot.child("restaurant").getValue(String.class);
            assert restaurantUid != null;
            Restaurant restaurant = new Restaurant(dataSnapshot.child("restaurants").child(restaurantUid));
            return new Worker(uid, name, email, restaurant);
        }else if(type.equals(UserTypes.ADMIN.toString())){
            System.out.println(type);
            return new Admin(uid, name, email);
        }
        return null;
    }
}

