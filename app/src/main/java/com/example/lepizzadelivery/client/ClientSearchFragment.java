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

import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Restaurant;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientSearchFragment extends Fragment {

    Order order;
    List<Restaurant> listaRestaurantes = new ArrayList<>();
    LinearLayout list;

    ConstraintLayout restaurant, coupon, qrCode;
    ImageView restaurantIcon, couponIcon, qrCodeIcon;
    TextView restaurantText, couponText, qrCodeText;

    DoubleClick doubleClick = new DoubleClick();

    public ClientSearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if(data != null) order = (Order) data.getSerializable("order");
        System.out.println(order);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews(){
        //Top Menu Views
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.client_search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews();
        setOnClickListeners();

//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Pituba", Arrays.asList("Lanches".split(",")), 2.0F, 10, 20, 4.0F, new CouponFreeDelivery("1", new Date())));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Calabar", Arrays.asList("Lanches,Sobremesas".split(",")), 2.3F, 12, 24, 4.3F, null));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Paralela", Arrays.asList("Lanches,Pizza".split(",")), 4.5F, 25, 45, 4.5F, new CouponTakeTwo("1", new Date(), new Drinks("coca"))));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Rio Vermelho", Arrays.asList("Lanches,Hambúrguer".split(",")), 1.6F, 8, 16, 4.6F, null));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Marechal Rondom", Arrays.asList("Lanches".split(",")), 2.0F, 10, 20, 4.0F, new CouponFreeFood("1", new Date(), new Drinks("sprite"))));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Sussuarana", Arrays.asList("Lanches,Sobremesas".split(",")), 2.3F, 12, 24, 4.3F, null));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Federação", Arrays.asList("Lanches,Pizza".split(",")), 4.5F, 25, 45, 4.5F, null));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Cajazeiras", Arrays.asList("Lanches,Hambúrguer".split(",")),1.6F, 8, 16, 4.6F, new CouponPercentage("1", new Date(), 20)));
//        listaRestaurantes.add(new Restaurant(getActivity(), list, "Cajazeiras", Arrays.asList("Lanches,Hambúrguer".split(",")),1.6F, 8, 16, 4.6F, new CouponPercentage("1", new Date(), 20)));

        selectTopMenu(restaurantIcon, restaurantText);
//        if(list.getChildCount() == 0) openRestaurantList();
        openRestaurantList();
    }

    private void openRestaurantList(){

        list.removeAllViews();
        for(Restaurant restaurant : listaRestaurantes) if(restaurant.getRestaurantView().getParent() == null) list.addView(restaurant.getRestaurantView());


//        ScrollView scroll = requireView().findViewById(R.id.scroll);
//        scroll.post(() -> scroll.smoothScrollTo(0, 300));
//
//        scroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                scroll.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int height = scroll.getHeight();
////                System.out.println(height);
//                int h = 0;
//                System.out.println(height);
//                for(Restaurant restaurant : listaRestaurantes) {
//                    h += restaurant.getRestaurantView().getHeight();
//                    restaurant.setHeight((h - height < 0) ? 0 : h);
//                    System.out.println(restaurant.getHeight());
//                }
//
//                System.out.println();
//
//                ScrollView scroll = requireView().findViewById(R.id.scroll);
//                scroll.post(() -> scroll.smoothScrollTo(0, listaRestaurantes.get(5).getHeight()));
//            }
//        });
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










