package com.example.lepizzadelivery.models.Users;

import com.example.lepizzadelivery.models.Address;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Client extends User{

    Address address;

    public Client(String name, String email, String password, String phone){
        super(email);
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.type = UserTypes.CLIENT.toString();
    }

    public Client(String name, String email, String phone, Address address){
        super(email);
        this.name = name;
        this.phone = phone;
        this.address = address;
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

}
