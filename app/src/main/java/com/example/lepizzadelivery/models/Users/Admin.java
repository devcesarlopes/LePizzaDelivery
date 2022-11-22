package com.example.lepizzadelivery.models.Users;

import java.io.Serializable;
import java.util.Map;

public class Admin extends User implements Serializable {

    Admin(String uid, String email, String name){
        super(email);
        this.uid = uid;
        this.name = name;
        this.type = UserTypes.ADMIN.toString();
    }

    @Override
    public Map<String, Object> getMap() {
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\n' +
                ", name='" + name + '\n' +
                ", email='" + email + '\n' +
                ", type='" + type + '\n' +
                '}';
    }
}
