package com.example.lepizzadelivery.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.TouchListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    ConstraintLayout loadingPage;
    CardView login;
    TextView register;
    EditText email, password;
    DoubleClick doubleClick = new DoubleClick();
    com.example.lepizzadelivery.Api Api = new Api();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loadingPage = findViewById(R.id.loadingPage);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login.setOnTouchListener(TouchListener.getTouch("#FBD603"));
        register.setOnTouchListener(TouchListener.getTouch("#D7B807"));

        login.setOnClickListener(view -> {
            if(doubleClick.detected()) return;
            try{
                validateFields();
                Api.login(this, email.getText().toString().trim(), password.getText().toString().trim(), loadingPage);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        register.setOnClickListener(view -> {
            startActivity(new Intent(this, Register.class));
            finish();
        });
    }

    private void validateFields() throws Exception{
        Map<String,String> fields = new HashMap<String, String>(){{
            put("Email", email.getText().toString().trim());
            put("Senha", password.getText().toString().trim());
        }};
        for(Map.Entry<String, String> field : fields.entrySet()) if(field.getValue().isEmpty()) throw new Exception("O Campo " + field.getKey() + " Não foi Preenchido");
        if(!Objects.requireNonNull(fields.get("Email")).matches("^[_a-zA-Z0-9-]+@[a-zA-Z0-9]+.[a-zA-z0-9]+$")) throw new Exception("O Email não é válido");
        if(Objects.requireNonNull(fields.get("Senha")).length() < 7) throw new Exception("A senha deve conter no mínimo 7 caracteres");
    }
}