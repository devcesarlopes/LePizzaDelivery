package com.example.lepizzadelivery.client;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;

import java.util.List;

public class ClientProfileFragment extends Fragment {

    private Client user;
    private EditText name, email, address, phone;

    public ClientProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (Client) getArguments().getSerializable("user");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews(){
        name = requireView().findViewById(R.id.name);
        email = requireView().findViewById(R.id.email);
        address = requireView().findViewById(R.id.address);
        phone = requireView().findViewById(R.id.phone);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.client_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        if(user == null) user = (Client) SharedPrefsDatabase.getUser(requireActivity());
        assert user != null;
        name.setText(user.getName());
        email.setText(user.getEmail());
        address.setText(user.getAddress().getName());
        phone.setText(user.getPhone());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}