package com.example.lepizzadelivery.client;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.TouchListener;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.menu.Menu;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.menu.OnMenuUpdatedCallback;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;

import java.util.Arrays;

public class ClientRestaurantMenuFragment extends Fragment {

    Client user;
    Order order;
    Api Api = new Api();

    //ClientHome views
    ConstraintLayout orderConstraint, loadingPage;
    TextView totalPrice, numberOfOrders;
    LinearLayout bottomLayout, menuFoodTypes;
    CardView finishOrder;

    //RestaurantMenuViews
    CardView goBack;

    Restaurant restaurant;
    LinearLayout ratingStars, menuListView;
    TextView ratingNum, restaurantAddress;

    Double deliveryPrice;

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
        //ClientHome views
        loadingPage = ((ClientHome) requireActivity()).loadingPage;
        orderConstraint = ((ClientHome) requireActivity()).orderConstraint;
        totalPrice = ((ClientHome) requireActivity()).totalPrice;
        numberOfOrders = ((ClientHome) requireActivity()).numberOfOrders;
        bottomLayout = ((ClientHome) requireActivity()).bottomLayout;
        finishOrder = ((ClientHome) requireActivity()).finishOrder;

        //rating
        ratingNum = requireView().findViewById(R.id.rating);
        ratingStars = requireView().findViewById(R.id.ratingStars);
        restaurantAddress= requireView().findViewById(R.id.restaurantAddress);

        restaurantAddress.setText(restaurant.getAddress().getName());
        ratingNum.setText(restaurant.getRating());
        ViewGroup.LayoutParams params = ratingStars.getLayoutParams();
        params.width = ratingToPixels(restaurant.getratingNum());
        ratingStars.setLayoutParams(params);

        //menu
        menuListView = requireView().findViewById(R.id.list);
        menuFoodTypes = requireView().findViewById(R.id.menuFoodTypes);

        Arrays.stream(restaurant.getFoodTypes().split(",")).forEach(food -> {
            View v = LayoutInflater.from(requireActivity()).inflate(R.layout.menu_horizontal_scrolling_item, menuFoodTypes, false);
            TextView vText = v.findViewById(R.id.text);
            vText.setText(food.trim());
            menuFoodTypes.addView(v);
        });
        goBack = requireView().findViewById(R.id.goBack);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners(){
        goBack.setOnClickListener(view -> requireActivity().onBackPressed());
        finishOrder.setOnTouchListener(TouchListener.getTouch("#D7B807"));
        finishOrder.setOnClickListener(view -> {
            if(order.isEmpty()) {
                Toast.makeText(requireActivity(), "Adicione um item para completar o pedido!", Toast.LENGTH_LONG).show();
                return;
            }
            order.finishOrder(user);
            Api.registerOrder(requireActivity(), order, loadingPage)
            .then((action, data) -> {
                loadingPage.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), "Pedido Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }).start();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();

        user = (Client) SharedPrefsDatabase.getUser(requireContext());
        assert user != null;
        deliveryPrice = restaurant.calculateDeliveryPrice(user);

        OnMenuUpdatedCallback callback = (order) -> {
            this.order = order;
            totalPrice.setText(order.getTotalPrice());
            if(order.isEmpty()){
                orderConstraint.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.VISIBLE);
            }else{
                orderConstraint.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.GONE);
            }
        };
        new Menu(requireActivity(), restaurant, callback, menuListView, deliveryPrice);
    }

    private int ratingToPixels(double rating){
        int totalDps = 15*5;
        double percentage = rating/5;
        return (int) (totalDps * percentage * requireActivity().getResources().getDisplayMetrics().density + 0.5f);
    }
}