package com.mohith.delicious;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Login extends Fragment {

    TextInputEditText email,password;
    Button login;
    Intent intent;
    FirebaseAuth mAuth;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = (View) inflater.inflate(R.layout.fragment_login, container, false);

        intent = new Intent(getActivity(), Home.class);

        email = root.findViewById(R.id.email_input);
        password = root.findViewById(R.id.password_input);

        mAuth = FirebaseAuth.getInstance();


        login = root.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginUser();
            }
        });

        return root;
    }

    public void loginUser(){
        String userMobile = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        if(TextUtils.isEmpty(userMobile)){
            email.setError("please enter phone number");
            email.requestFocus();
        }if(TextUtils.isEmpty(userPassword)){
            password.setError("please enter password");
            password.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(userMobile,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        Toast.makeText(getActivity(),"Logged in",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(),Home.class));
                    }else{

                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        switch (errorCode) {

                            case "ERROR_INVALID_EMAIL":

                            case "ERROR_USER_NOT_FOUND":
                                email.setError("Account doesn't exist");
                                email.requestFocus();
                                break;

                            case "ERROR_WRONG_PASSWORD":
                                password.setError("Incorrect password");
                                password.requestFocus();
                                break;
                        }

                    }
                }
            });
        }
    }
}
