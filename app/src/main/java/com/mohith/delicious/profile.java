package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {
    ImageView photo;
    Button update;
    EditText email,password,mobile,address,name;
    DatabaseReference reference;
    String dbEmail,dbPassword,dbMobile,dbAddress,dbName;

    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        reference = FirebaseDatabase.getInstance().getReference("users");

        email = findViewById(R.id.update_email);
        password = findViewById(R.id.update_password);
        mobile = findViewById(R.id.update_mobile);
        address = findViewById(R.id.update_address);
        name = findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setData();
        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateData(view);
            }
        });

        photo=findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hello();
            }
        });

        ImageView logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(profile.this, login_activity.class));

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.footer);

        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        return true;

                }
                return false;
            }
        });
    }

    public void updateData(View view){
        String currentName = name.getText().toString().trim();
        String currentPassword = password.getText().toString().trim();
        String currentMobile = mobile.getText().toString().trim();
        String currentAddress = address.getText().toString().trim();
        String currentEmail = email.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        if(!currentName.equals(dbName)){
            reference.child(user.getUid()).child("name").setValue(currentName);
        }
//        if(!currentPassword)

        if(!currentMobile.equals(dbMobile)){
            reference.child(user.getUid()).child("mobile").setValue(currentMobile);
        }

        if(!currentAddress.equals(dbAddress)){
            reference.child(user.getUid()).child("address").setValue(currentAddress);
        }
        if(!currentEmail.equals(dbEmail)){
            reference.child(user.getUid()).child("email").setValue(currentEmail);
            user.updateEmail(currentEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
        if(!currentPassword.equals(dbPassword)){
            reference.child(user.getUid()).child("password").setValue(currentPassword);
            user.updatePassword(currentPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
        }


        Toast.makeText(profile.this,"Profile Updated..!",Toast.LENGTH_SHORT).show();


    }

    public void setData(){
        if(user != null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = reference.orderByChild("email").equalTo(user.getEmail());
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        dbAddress = snapshot.child(user.getUid()).child("address").getValue(String.class);
                        dbEmail = snapshot.child(user.getUid()).child("email").getValue(String.class);
                        dbMobile = snapshot.child(user.getUid()).child("mobile").getValue(String.class);
                        dbName = snapshot.child(user.getUid()).child("name").getValue(String.class);
                        dbPassword = snapshot.child(user.getUid()).child("password").getValue(String.class);

                        email.setText(dbEmail);
                        address.setText(dbAddress);
                        mobile.setText(dbMobile);
                        password.setText(dbPassword);
                        name.setText(dbName);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private boolean isAddressChanged() {
        if(!dbAddress.equals(address.getText().toString().trim())){
            reference.child(dbMobile).child("address").setValue(address.getText().toString().trim());
            return true;
        }else{
            return false;
        }
    }
    private boolean isPasswordChanged(){
        if(!dbPassword.equals(password.getText().toString().trim())){
            reference.child(dbMobile).child("password").setValue(password.getText().toString().trim());
            return true;
        }else{
            return false;
        }
    }

    void hello(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999){
            Bundle bundle=data.getExtras();
            Bitmap bitmap=(Bitmap) bundle.get("data");
            photo.setImageBitmap(bitmap);
        }
    }
}