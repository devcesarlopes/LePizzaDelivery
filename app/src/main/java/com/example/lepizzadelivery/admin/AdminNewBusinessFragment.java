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
import com.example.lepizzadelivery.models.Address;
import com.example.lepizzadelivery.models.Restaurant;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdminNewBusinessFragment extends Fragment {

    ConstraintLayout loadingPage;
    EditText district, location, foodTypes;
    CardView register;

    ArrayList<String> foodTypesArray;
    Address address;

    Restaurant restaurant;

    Api Api = new Api();
    DoubleClick doubleClick = new DoubleClick();

    public AdminNewBusinessFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews(){
        //Top Menu Views
        loadingPage = ((AdminHome) requireActivity()).loadingPage;
        district = requireView().findViewById(R.id.district);
        location = requireView().findViewById(R.id.location);
        foodTypes = requireView().findViewById(R.id.foodTypes);
        register = requireView().findViewById(R.id.register);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_new_business_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews();

        location.setOnClickListener(view12 -> {
            if(doubleClick.detected()) return;
            getLocation.launch(Router.goToGoogleMapsApi(requireActivity()));
        });

        foodTypes.setOnClickListener(view12 -> {
            if(doubleClick.detected()) return;
            getMenuItems.launch(Router.goToSelectMenuItems(requireActivity()));
        });

        register.setOnClickListener(view1 -> {
            if(doubleClick.detected()) return;
            try{
                restaurant = validateFields();
                Api.registerRestaurant(requireActivity(), restaurant, loadingPage)
                .then((action, data) -> {
                    loadingPage.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "Restaurante Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    clearFields();
                }).start();
            }catch (Exception e){
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private Restaurant validateFields() throws Exception{
        if(district.getText().toString().trim().isEmpty()) throw new Exception("O Bairro não foi preenchido.");
        if(address == null) throw new Exception("A localização não foi Selecionada");
        if(foodTypes == null) throw new Exception("O Cardápio não foi Selecionado");
        return new Restaurant(district.getText().toString().trim(), address, foodTypesArray);
    }

    private void clearFields(){
        address = null;
        restaurant = null;
        foodTypesArray = null;
        district.setText("");
        location.setText("");
        foodTypes.setText("");
    }

    ActivityResultLauncher<Intent> getLocation = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if(data == null) Toast.makeText(requireActivity(), "É necessário registrar a localização para registrar a franquia.", Toast.LENGTH_LONG).show();
            else {
                address = (Address) data.getSerializableExtra("result");
                location.setText(address.getName());
            }
        } else{
            Toast.makeText(requireActivity(), "É necessário registrar a localização para registrar a franquia.", Toast.LENGTH_LONG).show();
        }
    });

    ActivityResultLauncher<Intent> getMenuItems = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if(data == null) Toast.makeText(requireActivity(), "É necessário registrar a localização para registrar a franquia.", Toast.LENGTH_LONG).show();
            else {
                foodTypesArray = data.getStringArrayListExtra("result");
                foodTypes.setText(String.join(", ", foodTypesArray));
            }
        } else{
            Toast.makeText(requireActivity(), "É necessário registrar a localização para registrar a franquia.", Toast.LENGTH_LONG).show();
        }
    });
}