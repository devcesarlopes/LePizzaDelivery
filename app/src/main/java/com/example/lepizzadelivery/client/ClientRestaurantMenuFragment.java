package com.example.lepizzadelivery.client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Restaurant;

public class ClientRestaurantMenuFragment extends Fragment {

    CardView goBack;

    Restaurant restaurant;
    LinearLayout ratingStars;
    TextView ratingNum;

    public ClientRestaurantMenuFragment() {
        // Required empty public constructor
    }

    public static ClientRestaurantMenuFragment newInstance(String param1, String param2) {
        return new ClientRestaurantMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle data = getArguments();
        if(data != null) restaurant = (Restaurant) data.getSerializable("restaurant");
        return inflater.inflate(R.layout.client_restaurant_menu_fragment, container, false);
    }

    private void setViews(){
        goBack = requireView().findViewById(R.id.goBack);

        //rating
        ratingNum = requireView().findViewById(R.id.rating);
        ratingStars = requireView().findViewById(R.id.ratingStars);

        ratingNum.setText(restaurant.getrating());
        ViewGroup.LayoutParams params = ratingStars.getLayoutParams();
        params.width = ratingToPixels(restaurant.getratingNum());
        ratingStars.setLayoutParams(params);
    }

    private void setOnClickListeners(){
        goBack.setOnClickListener(view -> requireActivity().onBackPressed());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
    }

    private int ratingToPixels(double rating){
        int totalDps = 15*5;
        double percentage = rating/5;
        return (int) (totalDps * percentage * requireActivity().getResources().getDisplayMetrics().density + 0.5f);
    }
}