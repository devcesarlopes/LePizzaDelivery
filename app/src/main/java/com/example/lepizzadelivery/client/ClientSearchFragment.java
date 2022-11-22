package com.example.lepizzadelivery.client;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.Router;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientSearchFragment extends Fragment {

    User user;
    Order order;
    List<Restaurant> listaRestaurantes = new ArrayList<>();
    LinearLayout list;
    ImageView logout;

    ConstraintLayout restaurant, coupon, qrCode;
    ImageView restaurantIcon, couponIcon, qrCodeIcon;
    TextView restaurantText, couponText, qrCodeText;

    DoubleClick doubleClick = new DoubleClick();
    Api Api = new Api();

    public ClientSearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews(){
        //Top Menu Views
        logout = requireView().findViewById(R.id.logout);
        restaurant = requireView().findViewById(R.id.restaurant);
        coupon = requireView().findViewById(R.id.coupon);
        qrCode = requireView().findViewById(R.id.qrCode);
        restaurantIcon = requireView().findViewById(R.id.restaurantIcon);
        couponIcon = requireView().findViewById(R.id.couponIcon);
        qrCodeIcon = requireView().findViewById(R.id.qrCodeIcon);
        restaurantText = requireView().findViewById(R.id.restaurantText);
        couponText = requireView().findViewById(R.id.couponText);
        qrCodeText = requireView().findViewById(R.id.qrCodeText);

        //Others
        list = requireView().findViewById(R.id.list);
    }

    private void setOnClickListeners(){
        restaurant.setOnClickListener(view -> { selectTopMenu(restaurantIcon, restaurantText); openRestaurantList(); });
        coupon.setOnClickListener(view -> { selectTopMenu(couponIcon, couponText); openCouponList(); });
        qrCode.setOnClickListener(view -> { if(doubleClick.detected()) return; readQrCode(); });
        logout.setOnClickListener(view -> {
            if(doubleClick.detected()) return;
            FirebaseAuth.getInstance().signOut();
            Router.goToLogin(requireActivity());
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.client_search_fragment, container, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews();
        setOnClickListeners();
        user = SharedPrefsDatabase.getUser(requireActivity());

        Api.getRestaurants()
        .then((action, data) -> {
            List<Restaurant> restaurants = (ArrayList<Restaurant>) data;
            listaRestaurantes.clear();
            listaRestaurantes.addAll(restaurants);
            System.out.println(listaRestaurantes);
            openRestaurantList();
        }).start();

        selectTopMenu(restaurantIcon, restaurantText);
    }

    private void openRestaurantList(){

        list.removeAllViews();
        for(Restaurant restaurant : listaRestaurantes) {
            System.out.println(user);
            restaurant.generateView(getActivity(), list, user);
            System.out.println("restaurant.getRestaurantView() " + restaurant.getRestaurantView());
            list.addView(restaurant.getRestaurantView());
        }
    }

    private void openCouponList(){
        list.removeAllViews();
        for(Restaurant restaurant :
            listaRestaurantes
            .stream()
            .filter(r -> r.getCoupon() != null)
            .collect(Collectors.toList()))
        if(restaurant.getRestaurantView().getParent() == null) list.addView(restaurant.getCouponView());
    }

    private void readQrCode(){
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("QR Code Scan");
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        barcodeLauncher.launch(options);
    }

    private void selectTopMenu(ImageView image, TextView text){
        deselectAll();
        image.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.white));
    }

    private void deselectAll(){
        deselectTopMenu(restaurantIcon, restaurantText);
        deselectTopMenu(couponIcon, couponText);
    }

    private void deselectTopMenu(ImageView image, TextView text){
        image.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.black));
    }

    public ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
    result -> {
        if(result.getContents() == null) {
            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }
    });
}










