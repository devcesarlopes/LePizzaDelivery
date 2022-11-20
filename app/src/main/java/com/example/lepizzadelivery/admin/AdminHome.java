package com.example.lepizzadelivery.admin;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.lepizzadelivery.R;
import com.example.lepizzadelivery.models.Users.User;
import com.example.lepizzadelivery.session.SharedPrefsDatabase;

public class AdminHome extends AppCompatActivity {

    ConstraintLayout business, newBusiness, worker, loadingPage;
    ImageView businessIcon, newBusinessIcon, workerIcon;
    TextView businessText, newBusinessText, workerText;

    AdminBusinessFragment adminBusiness;
    AdminNewBusinessFragment adminNewBusiness;
    AdminWorkerFragment adminWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        setViews();
        setOnClickListeners();
        selectBottomMenu(businessIcon, businessText);
        openBusinessFragment();
    }

    private void setViews(){
        //Bottom Menu Views
        business = findViewById(R.id.business);
        newBusiness = findViewById(R.id.newBusiness);
        worker = findViewById(R.id.worker);
        businessIcon = findViewById(R.id.businessIcon);
        newBusinessIcon = findViewById(R.id.newBusinessIcon);
        workerIcon = findViewById(R.id.workerIcon);
        businessText = findViewById(R.id.businessText);
        newBusinessText = findViewById(R.id.newBusinessText);
        workerText = findViewById(R.id.workerText);
        loadingPage = findViewById(R.id.loadingPage);
    }

    private void setOnClickListeners(){
        business.setOnClickListener(view -> openBusinessFragment());
        newBusiness.setOnClickListener(view -> openNewBusinessFragment());
        worker.setOnClickListener(view -> openWorkersFragment());
    }

    public void openBusinessFragment(){
        selectBottomMenu(businessIcon, businessText);
        if(adminBusiness == null) adminBusiness = new AdminBusinessFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activityFrame, adminBusiness).commit();
    }
    private void openNewBusinessFragment(){
        selectBottomMenu(newBusinessIcon, newBusinessText);
        if(adminNewBusiness == null) adminNewBusiness = new AdminNewBusinessFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activityFrame, adminNewBusiness).commit();
    }
    private void openWorkersFragment(){
        selectBottomMenu(workerIcon, workerText);
        if(adminWorker == null) adminWorker = new AdminWorkerFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activityFrame, adminWorker).commit();
    }
    private void selectBottomMenu(ImageView image, TextView text){
        deselectAll();
        image.setColorFilter(getResources().getColor(R.color.bold_yellow_background), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.bold_yellow_background));
    }

    private void deselectAll(){
        deselectBottomMenu(businessIcon, businessText);
        deselectBottomMenu(newBusinessIcon, newBusinessText);
        deselectBottomMenu(workerIcon, workerText);
    }

    private void deselectBottomMenu(ImageView image, TextView text){
        image.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        text.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onBackPressed() {
        if(!getSupportFragmentManager().getFragments().get(0).getClass().getSimpleName().equals("ClientSearchFragment")) openBusinessFragment();
        else super.onBackPressed();
    }
}