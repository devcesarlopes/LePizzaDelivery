package com.example.lepizzadelivery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lepizzadelivery.models.Address;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class Register extends AppCompatActivity {

    ConstraintLayout loadingPage;
    CardView register;
    TextView login;
    EditText name, email, phone, password;
    DoubleClick doubleClick = new DoubleClick();
    Api Api = new Api();
    Client user;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        loadingPage = findViewById(R.id.loadingPage);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        register.setOnTouchListener(TouchListener.getTouch("#FBD603"));
        login.setOnTouchListener(TouchListener.getTouch("#D7B807"));

        register.setOnClickListener(view -> {
            if(doubleClick.detected()) return;
            try{
                user = validateFields();
                getLocation.launch(Router.goToGoogleMapsApi(this));
                loadingPage.setVisibility(View.VISIBLE);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(view -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });
    }

    private Client validateFields() throws Exception{
        Map<String,String> fields = new HashMap<String, String>(){{
            put("Nome", name.getText().toString().trim());
            put("Email", email.getText().toString().trim());
            put("Senha", password.getText().toString().trim());
            put("Telefone", phone.getText().toString().trim());
        }};
        for(Entry<String, String> field : fields.entrySet()) if(field.getValue().isEmpty()) throw new Exception("O Campo " + field.getKey() + " Não foi Preenchido");
        if(!Objects.requireNonNull(fields.get("Email")).matches("^[_a-zA-Z0-9-]+@[a-zA-Z0-9]+.[a-zA-z0-9]+$")) throw new Exception("O Email não é válido");
        if(Objects.requireNonNull(fields.get("Senha")).length() < 7) throw new Exception("A senha deve conter no mínimo 7 caracteres");
        if(!Objects.requireNonNull(fields.get("Telefone")).matches("\\d+")) throw new Exception("O Telefone só pode conter números");
        if(Objects.requireNonNull(fields.get("Telefone")).length() <= 9) throw new Exception("O Telefone deve conter o DDD ex: 71993253792");

        return new Client(
            name.getText().toString().trim(),
            email.getText().toString().trim(),
            password.getText().toString().trim(),
            phone.getText().toString().trim()
        );
    }

    ActivityResultLauncher<Intent> getLocation = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if(data == null) Toast.makeText(this, "É necessário registrar a localização para se registrar", Toast.LENGTH_LONG).show();
            else {
                System.out.println(data.getSerializableExtra("result"));
                user.setAddress((Address) data.getSerializableExtra("result"));
                Api.registerClient(this, user, loadingPage);
            }
        } else{
            loadingPage.setVisibility(View.GONE);
            Toast.makeText(this, "É necessário registrar a localização para se registrar", Toast.LENGTH_LONG).show();
        }
    });
}