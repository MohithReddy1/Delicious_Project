package com.mohith.delicious;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signup extends Fragment {

    TextInputEditText email,mobile,password;
    Button signup;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = (View) inflater.inflate(R.layout.fragment_signup, container, false);


        email = root.findViewById(R.id.email_input);
        mobile = root.findViewById(R.id.mobile_input);
        password = root.findViewById(R.id.password_input);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        signup = (Button) root.findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        return root;
    }

    public void createUser(){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        String userEmail = email.getText().toString().trim();
        String userMobile = mobile.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        int i = R.drawable.propic_dummy;
        String image = Integer.toString(i);

        if(TextUtils.isEmpty(userEmail)){
            email.setError("please enter email");
            email.requestFocus();
        }else if(TextUtils.isEmpty(userMobile)){
            mobile.setError("please enter phone number");
            mobile.requestFocus();
        }else if(TextUtils.isEmpty(userPassword)){
            password.setError("please enter password");
            password.requestFocus();
        }else{
            email.setError(null);
            mobile.setError(null);
            password.setError(null);

            mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Registered..!",Toast.LENGTH_SHORT).show();
                        createAccount(userEmail,userMobile,userPassword,task.getResult().getUser().getUid(),image);
                        startActivity(new Intent(getActivity(),Home.class));
                    }else{

                        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();

                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        switch (errorCode) {
                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                email.setError("Account already existed");
                                email.requestFocus();
                                break;

                            case "ERROR_INVALID_EMAIL":
                                email.setError("Invalid email");
                                email.requestFocus();
                                break;
                        }

                    }
                }
            });
        }
    }
    public void createAccount(String userEmail,String userMobile,String userPassword,String userId, String image){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(userEmail);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    UserHelperClass helperClass = new UserHelperClass(userEmail,userMobile,userPassword,userId, image);
                    reference.child(userId).setValue(helperClass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}