package com.example.lepizzadelivery.models.Users;

import com.example.lepizzadelivery.models.Address;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Client extends User implements Serializable {

    private Address address;

    public Client(String name, String email, String password, String phone){
        super(email);
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.type = UserTypes.CLIENT.toString();
    }

    public Client(String uid, String name, String email, String phone, Address address){
        super(email);
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type = UserTypes.CLIENT.toString();
    }

    public Client(DataSnapshot snapshot){
        super(snapshot.child("email").getValue(String.class));
        this.uid = snapshot.child("uid").getValue(String.class);
        this.name = snapshot.child("name").getValue(String.class);
        this.phone = snapshot.child("phone").getValue(String.class);
        this.address = new Address(snapshot.child("address").getValue(String.class), snapshot.child("lat").getValue(Double.class), snapshot.child("lng").getValue(Double.class));
        this.type = UserTypes.CLIENT.toString();
    }

    @Override
    public Map<String, Object> getMap(){
        return new HashMap<String, Object>(){{
            put("uid", uid);
            put("name", name);
            put("email", email);
            put("phone", phone);
            put("address", address.getName());
            put("lat", address.getLat());
            put("lng", address.getLng());
        }};
    }

    public Address getAddress() {return address;}
    public void setAddress(Address address) { this.address = address; }

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
