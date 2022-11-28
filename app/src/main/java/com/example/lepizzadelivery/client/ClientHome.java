package com.example.lepizzadelivery.client;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.Objects;

public class ClientHome extends AppCompatActivity {

    DatabaseReference reference;
    ValueEventListener numberOfOrdersEventListener;

    LinearLayout bottomLayout;
    ConstraintLayout loadingPage, orderConstraint, search, orders, profile;
    ImageView searchIcon , ordersIcon, profileIcon;
    TextView searchText, ordersText, profileText;
    TextView totalPrice, numberOfOrders;
    CardView finishOrder;

    ClientSearchFragment clientSearch;
    ClientOrdersFragment clientOrders;
    ClientProfileFragment clientProfile;
    ClientRestaurantMenuFragment clientRestaurant;

//    Order order = new Order();
    Client user;
    Restaurant selectedRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_home);

        setViews();
        user = (Client) SharedPrefsDatabase.getUser(this);
        setOnClickListeners();
        selectBottomMenu(searchIcon, searchText);
        openSearchFragment();

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(User.UserTypes.CLIENT.name()).child(user.getUid());
        numberOfOrdersEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot = snapshot.child("orders");
                System.out.println("On NUMBER OF ORDER DATA CHANGE");
                if(!snapshot.exists()){
                    numberOfOrders.setText("0");
                    return;
                }
                int openedOrders = 0;
                for (DataSnapshot order : snapshot.getChildren()){
                    String orderStatus = order.child("status").getValue(String.class);
                    if(Objects.equals(orderStatus, Order.OrderStatus.RESTAURANT_ACCEPTED.name())){
                        openedOrders ++;
                    }
                }
                numberOfOrders.setText(String.valueOf(openedOrders));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ClientHome", error.getMessage());
            }
        };
        reference.addValueEventListener(numberOfOrdersEventListener);
    }

    private void setViews(){
        //LoadingPage
        loadingPage = findViewById(R.id.loadingPage);

        //Bottom Menu Views
        numberOfOrders = findViewById(R.id.numberOfOrders);
        search = findViewById(R.id.search);
        orders = findViewById(R.id.orders);
        profile = findViewById(R.id.profile);
        searchIcon = findViewById(R.id.searchIcon);
        ordersIcon = findViewById(R.id.ordersIcon);
        profileIcon = findViewById(R.id.profileIcon);
        searchText = findViewById(R.id.searchText);
        ordersText = findViewById(R.id.ordersText);
        profileText = findViewById(R.id.profileText);
        bottomLayout = findViewById(R.id.bottomLayout);
        orderConstraint = findViewById(R.id.orderConstraint);
        finishOrder = findViewById(R.id.finishOrder);
        totalPrice = findViewById(R.id.totalPrice);
    }

    private void setOnClickListeners(){
        search.setOnClickListener(view -> openSearchFragment());
        orders.setOnClickListener(view -> openOrdersFragment());
        profile.setOnClickListener(view -> openProfileFragment());
    }

    public void openSearchFragment(){
        selectBottomMenu(searchIcon, searchText);
        if(clientSearch == null) clientSearch = new ClientSearchFragment();
        orderConstraint.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activityFrame, clientSearch).commit();
    }

    private void openOrdersFragment(){
        selectBottomMenu(ordersIcon, ordersText);
        if(clientOrders == null) clientOrders = new ClientOrdersFragment();
        orderConstraint.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("user", user);
        clientOrders.setArguments(data);
        fragmentTransaction.replace(R.id.activityFrame, clientOrders).commit();
    }
    private void openProfileFragment(){
        selectBottomMenu(profileIcon, profileText);
        if(clientProfile == null) clientProfile = new ClientProfileFragment();
        orderConstraint.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("user", user);
        clientProfile.setArguments(data);
        fragmentTransaction.replace(R.id.activityFrame, clientProfile).commit();
    }
    public void openRestaurantFragment(Restaurant restaurant){
        if(selectedRestaurant == null) {
            selectedRestaurant = restaurant;
            clientRestaurant = new ClientRestaurantMenuFragment();
        }
        else if(!selectedRestaurant.equals(restaurant)) {
            selectedRestaurant = restaurant;
            clientRestaurant = new ClientRestaurantMenuFragment();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("restaurant", selectedRestaurant);
        clientRestaurant.setArguments(data);
        fragmentTransaction.replace(R.id.activityFrame, clientRestaurant).commit();
    }

    private void selectBottomMenu(ImageView image, TextView text){
        deselectAll();
        image.setColorFilter(getResources().getColor(R.color.bold_yellow_background), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.bold_yellow_background));
    }

    private void deselectAll(){
        deselectBottomMenu(searchIcon, searchText);
        deselectBottomMenu(ordersIcon, ordersText);
        deselectBottomMenu(profileIcon, profileText);
    }

    private void deselectBottomMenu(ImageView image, TextView text){
        image.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onBackPressed() {
        if(!getSupportFragmentManager().getFragments().get(0).getClass().getSimpleName().equals("ClientSearchFragment")) openSearchFragment();
        else {
            reference.removeEventListener(numberOfOrdersEventListener);
            super.onBackPressed();
        }
    }
}