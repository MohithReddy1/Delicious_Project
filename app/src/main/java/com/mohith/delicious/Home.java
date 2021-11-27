package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class Home extends AppCompatActivity {

    Button pizza, burger, desert;
    FragmentAdapter fragmentAdapter;
    ViewPager2 viewPager2;
    TextView name;
    ImageView photo;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager2 = findViewById(R.id.view_pager);
        pizza = findViewById(R.id.pizza);
        burger = findViewById(R.id.burger);
        desert = findViewById(R.id.desert);
        name = findViewById(R.id.name);
        photo = findViewById(R.id.photo);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getImageFirebase();

        setName();

        BottomNavigationView bottomNavigationView = findViewById(R.id.footer);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
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

        pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager2.setCurrentItem(0);
            }
        });

        burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager2.setCurrentItem(1);

            }
        });

        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager2.setCurrentItem(2);

            }
        });

        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, 3);
        viewPager2.setAdapter(fragmentAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                super.onPageSelected(position);
                manageAnimation(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    private void setName(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(user.getEmail());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.child(user.getUid()).child("name").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getImageFirebase(){
        storageReference = FirebaseStorage.getInstance().getReference("images/"+user.getUid());
        try{

            File localFile = File.createTempFile(user.getUid(),".png");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("path",localFile.getAbsolutePath());
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    photo.setImageBitmap(bitmap);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });
        }catch (IOException e){

        }


    }

    void manageAnimation(int position){

        switch (position){
            case 0:
                pizza.setBackgroundResource(R.drawable.highlight_btn);
                pizza.setTextColor(Color.parseColor("#FFFFFFFF"));

                burger.setBackgroundResource(R.drawable.btn_bg);
                burger.setTextColor(Color.parseColor("#FF5E00"));

                desert.setBackgroundResource(R.drawable.btn_bg);
                desert.setTextColor(Color.parseColor("#FF5E00"));
                break;

            case 1:
                burger.setBackgroundResource(R.drawable.highlight_btn);
                burger.setTextColor(Color.parseColor("#FFFFFFFF"));

                pizza.setBackgroundResource(R.drawable.btn_bg);
                pizza.setTextColor(Color.parseColor("#FF5E00"));

                desert.setBackgroundResource(R.drawable.btn_bg);
                desert.setTextColor(Color.parseColor("#FF5E00"));
                break;

            case 2:
                desert.setBackgroundResource(R.drawable.highlight_btn);
                desert.setTextColor(Color.parseColor("#FFFFFFFF"));

                burger.setBackgroundResource(R.drawable.btn_bg);
                burger.setTextColor(Color.parseColor("#FF5E00"));

                pizza.setBackgroundResource(R.drawable.btn_bg);
                pizza.setTextColor(Color.parseColor("#FF5E00"));
                break;
        }

    }

    static class FragmentAdapter extends FragmentStateAdapter {

        int numtabs;

        public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, int tabs) {
            super(fragmentActivity);
            numtabs = tabs;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            Fragment fragment = null;

            switch (position){
                case 0:
                    fragment = new pizza_fragment();
                    break;

                case 1:
                    fragment = new burger_fragment();
                    break;

                case 2:
                    fragment = new desert_fragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getItemCount() {
            return numtabs;
        }
    }

}