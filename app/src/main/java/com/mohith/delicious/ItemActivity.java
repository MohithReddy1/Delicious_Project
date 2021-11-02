package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mohith.delicious.Domain.FoodDomain;
import com.mohith.delicious.Helper.ManagementCart;

public class ItemActivity extends AppCompatActivity {

    private Button addToCardBtn;
    private TextView titleTxt, priceTxt;
    private ImageView picFood, backBtn;
    private FoodDomain object;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().hide();

        managementCart = new ManagementCart(this);

        initView();
        getBundle();

        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemActivity.this, Home.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.footer);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), cart.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), orders.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

    private void getBundle() {
        object = (FoodDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId = this.getResources().getIdentifier(object.getPic(), "drawable", this.getPackageName());
        this.picFood.setImageResource(drawableResourceId);

        titleTxt.setText(object.getTitle());
        priceTxt.setText(""+object.getPrice());

        addToCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(numberOrder);
                managementCart.insertFood(object);
            }
        });
    }

    private void initView() {
        addToCardBtn = findViewById(R.id.addToCardBtn);
        titleTxt = findViewById(R.id.titleTxt);
        priceTxt = findViewById(R.id.priceTxt);
        picFood = findViewById(R.id.foodPic);
    }


}