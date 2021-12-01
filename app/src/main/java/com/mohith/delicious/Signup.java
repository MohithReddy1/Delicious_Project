package com.mohith.delicious;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
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

import java.util.regex.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends Fragment {

    TextInputEditText email,mobile,password;
    TextInputLayout email_out,mobile_out,password_out;
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

        email_out = root.findViewById(R.id.email);
        mobile_out = root.findViewById(R.id.phonenumber);
        password_out = root.findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        email_out.setHelperText("*Required");
        mobile_out.setHelperText("*Required");
        password_out.setHelperText("*Required");

        email.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged (CharSequence s,int start, int count,int after){
             }
             @Override
             public void onTextChanged ( final CharSequence s, int start, int before,int count){
                 email_out.setHelperText("");
             }
             @Override
             public void afterTextChanged ( final Editable s){
             }
         }
        );

        mobile.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged (CharSequence s,int start, int count,int after){
              }
              @Override
              public void onTextChanged ( final CharSequence s, int start, int before,int count){
                  mobile_out.setHelperText("");
              }
              @Override
              public void afterTextChanged ( final Editable s){
              }
          }
        );


        password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged (CharSequence s,int start, int count,int after){
                }
                @Override
                public void onTextChanged ( final CharSequence s, int start, int before,int count){
                    password_out.setHelperText("");
                }
                @Override
                public void afterTextChanged ( final Editable s){
                }
            }
        );


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

        if(TextUtils.isEmpty(userEmail)){
            email_out.setHelperText("*Required");
            email.requestFocus();
        }else if(TextUtils.isEmpty(userMobile)){
            mobile_out.setHelperText("*Required");
            mobile.requestFocus();
        }else if(userMobile.length() != 10){
            mobile_out.setHelperText("*Invalid Mobile Number");
            mobile.requestFocus();
        }else if(TextUtils.isEmpty(userPassword)){
            password_out.setHelperText("*Required");
            password.requestFocus();
        }else if(!isValidPassword(userPassword)){
            password_out.setHelperText("Password must contain 1 Caps,1 Small,1 Symbol,8 to 12 Chars");
            password.requestFocus();
        }else{
            email.setError(null);
            mobile.setError(null);
            password.setError(null);

            mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        Toast.makeText(getActivity(),"Registered..!",Toast.LENGTH_SHORT).show();
                        createAccount(userEmail,userMobile,userPassword,task.getResult().getUser().getUid());
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
                                email.setError("Invalid Email");
                                email.requestFocus();
                                break;
                        }

                    }
                }
            });
        }
    }
    public void createAccount(String userEmail,String userMobile,String userPassword,String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(userEmail);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    UserHelperClass helperClass = new UserHelperClass(userEmail,userMobile,userPassword,userId);
                    reference.child(userId).setValue(helperClass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,12}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

}