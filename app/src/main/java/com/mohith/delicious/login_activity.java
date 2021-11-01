package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class login_activity extends AppCompatActivity {

    Button login, signup;
    FragmentAdapter fragmentAdapter;
    ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        viewPager2 = findViewById(R.id.view_pager);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(0);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(1);
            }
        });

        FragmentAdapter fragmentAdapter = new FragmentAdapter(this,2);
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


    void manageAnimation(int position){

        switch (position){
            case 0:
                login.setBackgroundResource(R.drawable.btn_selected);
                login.setTextColor(Color.parseColor("#FFFFFFFF"));

                signup.setBackgroundResource(R.drawable.btn_notselected);
                signup.setTextColor(Color.parseColor("#FF5E00"));

                break;

            case 1:
                signup.setBackgroundResource(R.drawable.btn_selected);
                signup.setTextColor(Color.parseColor("#FFFFFFFF"));

                login.setBackgroundResource(R.drawable.btn_notselected);
                login.setTextColor(Color.parseColor("#FF5E00"));
                break;

        }

    }


    static class FragmentAdapter extends FragmentStateAdapter{
        int countTabs;
        public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, int tabs) {
            super(fragmentActivity);
            countTabs = tabs;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new Login();
                    break;
                case 1:
                    fragment = new Signup();
                    break;
            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return countTabs;
        }
    }
}