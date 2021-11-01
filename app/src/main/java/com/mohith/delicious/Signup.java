package com.mohith.delicious;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Signup extends Fragment {

    Button signup;
    Intent intent;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = (View) inflater.inflate(R.layout.fragment_signup, container, false);



        intent = new Intent(getActivity(), Home.class);

        final Button button = (Button) root.findViewById(R.id.signup);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return root;
    }
}