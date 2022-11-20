package com.example.lepizzadelivery.models.Users;

import java.util.Map;

public class Admin extends User{

    Admin(String email, String name){
        super(email);
        this.name = name;
        this.type = UserTypes.ADMIN.toString();
    }

    @Override
    public Map<String, Object> getMap() {
        return null;
    }
}
