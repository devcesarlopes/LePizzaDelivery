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
    Address address;

    public User(){}

    public User(String email){
        this.email = email;
    }

//    public User(String name, String email, String password, String phone){
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.phone = phone;
//        this.type = UserTypes.CLIENT.toString();
//    }

//    public User(String email, String password, String restaurantUid){
//        this.email = email;
//        this.password = password;
//        this.restaurantUid = restaurantUid;
//        this.type = UserTypes.WORKER.toString();
//    }

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
            String phone = snapshot.child("phone").getValue(String.class);
            Address address = new Address(snapshot.child("address").getValue(String.class), snapshot.child("lat").getValue(Double.class), snapshot.child("lng").getValue(Double.class));
            return new Client(name, email, phone, address);
        }else if(type.equals(UserTypes.WORKER.toString())){
            String restaurantUid = snapshot.child("restaurant").getValue(String.class);
            assert restaurantUid != null;
            Restaurant restaurant = new Restaurant(dataSnapshot.child("restaurants").child(restaurantUid));
            return new Worker(name, email, restaurant);
        }else if(type.equals(UserTypes.ADMIN.toString())){
            return new Admin(name, email);
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\n' +
                ", name='" + name + '\n' +
                ", email='" + email + '\n' +
                ", phone='" + phone + '\n' +
                ", type='" + type + '\n' +
                ", address=" + address + '\n' +
                '}';
    }
}

