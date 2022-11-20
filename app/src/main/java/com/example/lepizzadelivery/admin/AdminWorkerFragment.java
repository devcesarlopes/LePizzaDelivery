package com.example.lepizzadelivery.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.Router;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.Worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminWorkerFragment extends Fragment {

    ConstraintLayout loadingPage;
    EditText name, email, password, restaurant;
    CardView register;
    Api Api = new Api();

    Restaurant restaurantObject;
    Worker worker;
    DoubleClick doubleClick = new DoubleClick();

    public AdminWorkerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews(){
        //Top Menu Views
        loadingPage = ((AdminHome) requireActivity()).loadingPage;
        name = requireView().findViewById(R.id.name);
        email = requireView().findViewById(R.id.email);
        password = requireView().findViewById(R.id.password);
        restaurant = requireView().findViewById(R.id.restaurant);
        register = requireView().findViewById(R.id.register);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_worker_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();

        restaurant.setOnClickListener(view12 -> {
            if(doubleClick.detected()) return;
            getRestaurant.launch(Router.goToSelectRestaurant(requireActivity()));
        });

        register.setOnClickListener(view1 -> {
            if(doubleClick.detected()) return;
            try{
                worker = validateFields();
                Api.registerWorker(requireActivity(), worker, loadingPage)
                .then((action, data) -> {
                    loadingPage.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "Funcionário Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    clearFields();
                }).start();
            }catch (Exception e){
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private Worker validateFields() throws Exception{
        Map<String,String> fields = new HashMap<String, String>(){{
            put("Nome", name.getText().toString().trim());
            put("Email", email.getText().toString().trim());
            put("Senha", password.getText().toString().trim());
        }};
        for(Map.Entry<String, String> field : fields.entrySet()) if(field.getValue().isEmpty()) throw new Exception("O Campo " + field.getKey() + " Não foi Preenchido");
        if(!Objects.requireNonNull(fields.get("Email")).matches("^[_a-zA-Z0-9-]+@[a-zA-Z0-9]+.[a-zA-z0-9]+$")) throw new Exception("O Email não é válido");
        if(Objects.requireNonNull(fields.get("Senha")).length() < 7) throw new Exception("A senha deve conter no mínimo 7 caracteres");
        if(restaurantObject == null) throw new Exception("É necessário selecionar um Restaurante");

        return new Worker(
            name.getText().toString().trim(),
            email.getText().toString().trim(),
            password.getText().toString().trim(),
            restaurantObject
        );
    }

    private void clearFields(){
        name.setText("");
        email.setText("");
        password.setText("");
        restaurant.setText("");
        restaurantObject = null;
    }

    ActivityResultLauncher<Intent> getRestaurant = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if(data == null) Toast.makeText(requireActivity(), "É necessário escolher um restaurante para registrar um empregado.", Toast.LENGTH_LONG).show();
            else {
                restaurantObject = (Restaurant) data.getSerializableExtra("result");
                restaurant.setText(restaurantObject.getDistrict());
            }
        } else{
            Toast.makeText(requireActivity(), "É necessário escolher um restaurante para registrar um empregado.", Toast.LENGTH_LONG).show();
        }
    });
}










