package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mohith.delicious.Adapter.CartListAdapter;
import com.mohith.delicious.Helper.ManagementCart;
import com.mohith.delicious.Interface.ChangeNumberItemsListener;

public class cart extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalPriceTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private double tax;
    private ScrollView scrollView;

    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();



        checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), payment.class));

                Intent intent = new Intent(cart.this, payment.class);
                intent.putExtra("total", totalTxt.getText());
                startActivity(intent);
            }
        });

        managementCart = new ManagementCart(this);

        initView();
        initList();
        calculateCard();

        BottomNavigationView bottomNavigationView = findViewById(R.id.footer);
        bottomNavigationView.setSelectedItemId(R.id.cart);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cart:
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

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCard(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCard();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if (managementCart.getListCard().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCard() {
        double percentTax = 0.01;
        double delivery = 10;

        tax = 30;
        double total = Math.round((managementCart.getTotalPrice() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round(managementCart.getTotalPrice() * 100.0) / 100.0;

        totalPriceTxt.setText("₹" + itemTotal);
        taxTxt.setText("₹" + tax);
        deliveryTxt.setText("₹" + delivery);
        totalTxt.setText("₹" + total);
    }

    private void initView() {
        recyclerViewList = findViewById(R.id.recyclerview);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView);
    }
}