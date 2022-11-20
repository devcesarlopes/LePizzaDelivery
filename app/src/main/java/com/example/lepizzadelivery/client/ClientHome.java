package com.example.lepizzadelivery.client;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;

public class ClientHome extends AppCompatActivity {

    ConstraintLayout search, bag, orders, profile;
    ImageView searchIcon, bagIcon, ordersIcon, profileIcon;
    TextView searchText, bagText, ordersText, profileText;
    TextView foodInBag;

    ClientSearchFragment clientSearch;
    ClientBagFragment clientBag;
    ClientOrdersFragment clientOrders;
    ClientProfileFragment clientProfile;
    ClientRestaurantMenuFragment clientRestaurant;

    Order order = new Order();
    User user;
    Restaurant selectedRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_home);

        setViews();
        user = SharedPrefsDatabase.getUser(this);
        setOnClickListeners();
        selectBottomMenu(searchIcon, searchText);
        openSearchFragment();
    }

    private void setViews(){
        //Bottom Menu Views
        foodInBag = findViewById(R.id.foodInBag);
        search = findViewById(R.id.search);
        bag = findViewById(R.id.bag);
        orders = findViewById(R.id.orders);
        profile = findViewById(R.id.profile);
        searchIcon = findViewById(R.id.searchIcon);
        bagIcon = findViewById(R.id.bagIcon);
        ordersIcon = findViewById(R.id.ordersIcon);
        profileIcon = findViewById(R.id.profileIcon);
        searchText = findViewById(R.id.searchText);
        bagText = findViewById(R.id.bagText);
        ordersText = findViewById(R.id.ordersText);
        profileText = findViewById(R.id.profileText);
    }

    private void setOnClickListeners(){
        search.setOnClickListener(view -> openSearchFragment());
        bag.setOnClickListener(view -> openBagFragment());
        orders.setOnClickListener(view -> openOrdersFragment());
        profile.setOnClickListener(view -> openProfileFragment());
    }

    public void openSearchFragment(){
        selectBottomMenu(searchIcon, searchText);
        if(clientSearch == null) clientSearch = new ClientSearchFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("order", order);
        clientSearch.setArguments(data);
        fragmentTransaction.replace(R.id.activityFrame, clientSearch).commit();
    }
    private void openBagFragment(){
        selectBottomMenu(bagIcon, bagText);
        if(clientBag == null) clientBag = new ClientBagFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("order", order);
        clientBag.setArguments(data);
        fragmentTransaction.replace(R.id.activityFrame, clientBag).commit();
    }
    private void openOrdersFragment(){
        selectBottomMenu(ordersIcon, ordersText);
        if(clientOrders == null) clientOrders = new ClientOrdersFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("user", user);
        clientOrders.setArguments(data);
        fragmentTransaction.replace(R.id.activityFrame, clientOrders).commit();
    }
    private void openProfileFragment(){
        selectBottomMenu(profileIcon, profileText);
        if(clientProfile == null) clientProfile = new ClientProfileFragment();

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
        deselectBottomMenu(bagIcon, bagText);
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
        else super.onBackPressed();
    }
}