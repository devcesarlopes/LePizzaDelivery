package com.example.lepizzadelivery.client;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lepizzadelivery.controllers.Login;
import com.example.lepizzadelivery.R;

public class ClientBottomMenu extends Fragment {

    private static final String clientHome = "ClientHome";
    private static final String clientCart = "ClientCart";
    private static final String clientOrders = "ClientOrders";
    private static final String clientProfile = "ClientProfile";

    ConstraintLayout search, bag, orders, profile;
    ImageView searchIcon, bagIcon, ordersIcon, profileIcon;
    TextView searchText, bagText, ordersText, profileText;

    public ClientBottomMenu() {
        // Required empty public constructor
    }

    public static ClientBottomMenu newInstance() {
        return new ClientBottomMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.client_bottom_menu, container, false);
    }

    private void setViews(){
//        search  = requireView().findViewById(R.id.search);
//        bag = requireView().findViewById(R.id.bag);
//        orders = requireView().findViewById(R.id.orders);
//        profile = requireView().findViewById(R.id.profile);
//        searchIcon = requireView().findViewById(R.id.searchIcon);
//        bagIcon = requireView().findViewById(R.id.bagIcon);
//        ordersIcon = requireView().findViewById(R.id.ordersIcon);
//        profileIcon = requireView().findViewById(R.id.profileIcon);
//        searchText = requireView().findViewById(R.id.searchText);
//        bagText = requireView().findViewById(R.id.bagText);
//        ordersText = requireView().findViewById(R.id.ordersText);
//        profileText = requireView().findViewById(R.id.profileText);
    }

    private void paintSelected(){
        switch (requireActivity().getClass().getSimpleName()){
            case clientHome:
                selectBottomMenu(searchIcon, searchText);break;
            case clientCart:
                selectBottomMenu(bagIcon, bagText);break;
            case clientOrders:
                selectBottomMenu(ordersIcon, ordersText);break;
            case clientProfile:
                selectBottomMenu(profileIcon, profileText);break;
        }
    }

    private void setOnClickListeners(){
//        search.setOnClickListener(view -> {
//            Intent intent = new Intent(getActivity(), ClientSearchFragment.class);
//
//        });
//        bag.setOnClickListener(view -> {
//            Intent intent = new Intent(getActivity(), ClientBag.class);
//
//        });
//        orders.setOnClickListener(view -> {
//            Intent intent = new Intent(getActivity(), ClientOrders.class);
//
//        });
//        profile.setOnClickListener(view -> {
//            Intent intent = new Intent(getActivity(), ClientProfile.class);
//
//        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        paintSelected();
        setOnClickListeners();

        searchIcon.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void selectBottomMenu(ImageView image, TextView text){
        image.setColorFilter(getResources().getColor(R.color.bold_yellow_background), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.bold_yellow_background));
    }

    private void deselectBottomMenu(ImageView image, TextView text){
        image.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.black));
    }
}