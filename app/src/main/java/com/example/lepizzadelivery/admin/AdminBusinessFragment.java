package com.example.lepizzadelivery.admin;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.Router;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminBusinessFragment extends Fragment {

    User user;
    List<Restaurant> listaRestaurantes = new ArrayList<>();
    LinearLayout list;
    ImageView logout;

    ConstraintLayout restaurant, coupon;
    ImageView restaurantIcon, couponIcon;
    TextView restaurantText, couponText;

    DoubleClick doubleClick = new DoubleClick();

    Api Api = new Api();

    ProgressBar progressBar;

    public AdminBusinessFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharedPrefsDatabase.getUser(requireActivity());
        System.out.println("user" + user);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews(){
        //Top Menu Views
        logout = requireView().findViewById(R.id.logout);
        restaurant = requireView().findViewById(R.id.restaurant);
        coupon = requireView().findViewById(R.id.coupon);
        restaurantIcon = requireView().findViewById(R.id.restaurantIcon);
        couponIcon = requireView().findViewById(R.id.couponIcon);
        restaurantText = requireView().findViewById(R.id.restaurantText);
        couponText = requireView().findViewById(R.id.couponText);
        progressBar = requireView().findViewById(R.id.progressBar);

        //Others
        list = requireView().findViewById(R.id.list);
    }

    private void setOnClickListeners(){
        restaurant.setOnClickListener(view -> { selectTopMenu(restaurantIcon, restaurantText); openRestaurantList(); });
        coupon.setOnClickListener(view -> { selectTopMenu(couponIcon, couponText); openCouponList(); });
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
        return inflater.inflate(R.layout.admin_business_fragment, container, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews();
        setOnClickListeners();

        Api.getRestaurants()
        .then(((action, data) -> {
            List<Restaurant> restaurants = (ArrayList<Restaurant>) data;
            listaRestaurantes.clear();
            listaRestaurantes.addAll(restaurants);
            System.out.println(listaRestaurantes);
            openRestaurantList();
        })).start();

        selectTopMenu(restaurantIcon, restaurantText);
    }

    private void openRestaurantList(){

//        if(restaurant.getRestaurantView().getParent() == null)
        list.removeAllViews();
        for(Restaurant restaurant : listaRestaurantes) {
            restaurant.generateView(getActivity(), list, user);
            list.addView(restaurant.getRestaurantView());
        }
        progressBar.setVisibility(View.GONE);
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


    public final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
    result -> {
        if(result.getContents() == null) {
            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }
    });
}










