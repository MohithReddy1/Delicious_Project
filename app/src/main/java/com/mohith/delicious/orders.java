package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class orders extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    String text;
    TextView track1, track2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        track1 = findViewById(R.id.track1);
        track2 = findViewById(R.id.track2);

        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), order_tracking.class);
                startActivity(intent);
            }
        });

        track2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), order_tracking.class);
                startActivity(intent);
            }
        });

        FloatingActionButton support = findViewById(R.id.support);

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp(view);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.footer);

        bottomNavigationView.setSelectedItemId(R.id.orders);

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

    private void openWhatsApp(View view){

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = reference.orderByChild("email").equalTo(user.getEmail());
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        text = snapshot.child(user.getUid()).child("email").getValue(String.class);
                        String toNumber = "916363764993";

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}