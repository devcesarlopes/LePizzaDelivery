package com.example.lepizzadelivery.worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lepizzadelivery.Api;
import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.Router;
import com.example.lepizzadelivery.models.Order;
import com.example.lepizzadelivery.models.Restaurant;
import com.example.lepizzadelivery.models.Users.Client;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.models.Users.Worker;
import com.example.lepizzadelivery.models.food.Food;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class WorkerHome extends AppCompatActivity {

    Api Api = new Api();
    LinearLayout list;
    ProgressBar progressBar;
    ImageView logout;
    TextView notFoundOrders, title, name;

    Worker user;
    DoubleClick doubleClick = new DoubleClick(3000);

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_home);

        progressBar = findViewById(R.id.progressBar);
        logout = findViewById(R.id.logout);
        title = findViewById(R.id.title);
        notFoundOrders = findViewById(R.id.notFoundOrders);
        name = findViewById(R.id.name);
        list = findViewById(R.id.list);

        logout.setOnClickListener(view -> {
            if(doubleClick.detected()) return;
            FirebaseAuth.getInstance().signOut();
            Router.goToLogin(this);
        });

        user = (Worker) SharedPrefsDatabase.getUser(this);

        assert user != null;
        title.setText("LePizza " + user.getRestaurant().getDistrict());
        name.setText(
            (user.getName().split(" ").length <= 2) ?
                user.getName() :
                user.getName().split(" ")[0] + user.getName().split(" ")[1]
        );


        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("restaurants")
                .child(user.getRestaurant().getUid())
                .child("orders");
        reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 progressBar.setVisibility(View.VISIBLE);
                 if(!snapshot.exists()) {
                     notFoundOrders.setVisibility(View.VISIBLE);
                     progressBar.setVisibility(View.GONE);
                     return;
                 }
                 list.removeAllViews();
                 boolean notFound = true;
                 for (DataSnapshot order : snapshot.getChildren()) {
                     String status = Objects.requireNonNull(order.child("status").getValue(String.class));
                     if(status.equals(Order.OrderStatus.CLIENT_ORDERED.name()) ||  status.equals(Order.OrderStatus.RESTAURANT_ACCEPTED.name())){
                         notFound = false;
                         getUserOrder(order.child("userUid").getValue(String.class), order.child("uid").getValue(String.class), status);
                     }
                 }
                 if(notFound){
                     notFoundOrders.setVisibility(View.VISIBLE);
                     progressBar.setVisibility(View.GONE);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {  }
         });
    }

    private void getUserOrder(String userUid, String orderUid, String status){
        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(User.UserTypes.CLIENT.name())
                .child(userUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) return;
                Client client = new Client(snapshot);
                Order orderObject = new Order(user.getRestaurant(), snapshot.child("orders").child(orderUid));
                list.addView(orderObject.generateView(WorkerHome.this, list, client, status, doubleClick));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
    }
}