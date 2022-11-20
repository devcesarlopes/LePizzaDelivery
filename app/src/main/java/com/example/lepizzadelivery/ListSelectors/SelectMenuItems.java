package com.example.lepizzadelivery.ListSelectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepizzadelivery.DoubleClick;
import com.example.lepizzadelivery.R;

import java.util.ArrayList;
import java.util.List;

public class SelectMenuItems extends AppCompatActivity {

    Intent intent;
    LinearLayout header;
    LinearLayout listaLayout;
    ImageView goBack;
    DoubleClick doubleClick = new DoubleClick();
    List<String> items = new ArrayList<String>(){{
        add("Lanches");
        add("Sobremesas");
        add("Pizza");
        add("Hambúrguer");
    }};
    ArrayList<String> result = new ArrayList<>();
    CardView submitForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu_items);

        intent = getIntent();

        submitForm = findViewById(R.id.submitForm);
        header = findViewById(R.id.header);
        listaLayout = findViewById(R.id.listaLayout);
        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(view -> {
            if (doubleClick.detected()) return;
            onBackPressed();
        });

        for (String item : items){
            View obj = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_menu_items, listaLayout, false);
            TextView objItem1 = obj.findViewById(R.id.text);
            objItem1.setText(item);
            CheckBox item2 = obj.findViewById(R.id.checkbox);
            item2.setOnClickListener(view -> {
                if(item2.isChecked()) obj.setTag(item);
                else obj.setTag(null);
            });
            listaLayout.addView(obj);
        }

        submitForm.setOnClickListener(view -> {
            if(doubleClick.detected()) return;
            for(int i = 0 ; i < listaLayout.getChildCount(); i++){
                String tag = (listaLayout.getChildAt(i).getTag() == null) ? null : String.valueOf(listaLayout.getChildAt(i).getTag());
                if(tag != null) result.add(tag);
            }
            if(result.isEmpty()) {
                Toast.makeText(this, "Selecione ao menos um Item da Lista!", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra("result", result);
            setResult (SelectMenuItems.RESULT_OK, intent);
            finish();
        });

    }

}