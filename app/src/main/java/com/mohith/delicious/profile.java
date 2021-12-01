package com.mohith.delicious;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class profile extends AppCompatActivity {
    ImageView photo;
    Button update;
    EditText email,password,mobile,address,name;
    TextInputLayout email_out,password_out,mobile_out;
    DatabaseReference reference;
    String dbEmail,dbPassword,dbMobile,dbAddress,dbName;

    FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseStorage storage;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(profile.this);
        reference = FirebaseDatabase.getInstance().getReference("users");

        email = findViewById(R.id.update_email);
        password = findViewById(R.id.update_password);
        mobile = findViewById(R.id.update_mobile);
        address = findViewById(R.id.update_address);
        name = findViewById(R.id.name);

        email_out = findViewById(R.id.email);
        password_out = findViewById(R.id.password);
        mobile_out = findViewById(R.id.mobile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        getImageFirebase();
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
                ImageClick();
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

        if(TextUtils.isEmpty(currentEmail)){
            Toast.makeText(profile.this,"Email Required",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(currentMobile)){
            Toast.makeText(profile.this,"Mobile Number Required",Toast.LENGTH_SHORT).show();

        }else if(currentMobile.length() != 10){
            Toast.makeText(profile.this,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(currentPassword)){
            Toast.makeText(profile.this,"Password Required",Toast.LENGTH_SHORT).show();
        }else if(!isValidPassword(currentPassword)){
            Toast.makeText(profile.this,"Weak Password",Toast.LENGTH_SHORT).show();
        }else {

            if (!currentName.equals(dbName)) {
                reference.child(user.getUid()).child("name").setValue(currentName);
            }

            if (!currentMobile.equals(dbMobile)) {
                reference.child(user.getUid()).child("mobile").setValue(currentMobile);
            }

            if (!currentAddress.equals(dbAddress)) {
                reference.child(user.getUid()).child("address").setValue(currentAddress);
            }
            if (!currentEmail.equals(dbEmail)) {
                changeemail(dbEmail,currentEmail,currentPassword);
                dbEmail = currentEmail;
            }
            if (!currentPassword.equals(dbPassword)) {
                reference.child(user.getUid()).child("password").setValue(currentPassword);
                user.updatePassword(currentPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
            }


            Toast.makeText(profile.this, "Profile Updated..!", Toast.LENGTH_SHORT).show();
        }

    }

    private void changeemail(String mail,String newMail, String pwd) {
        AuthCredential credential = EmailAuthProvider.getCredential(mail, pwd); // Current Login Credentials

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.updateEmail(newMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(profile.this, "Invalid Email", Toast.LENGTH_LONG).show();
                        }else{
                            reference.child(user.getUid()).child("email").setValue(newMail);
                        }
                    }
                });
            }
        });
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

    public void firebaseUploadImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        if(user != null) {

            progressDialog.show();
            progressDialog.setContentView(R.layout.progress);

            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );


            storageReference = FirebaseStorage.getInstance().getReference("images").child(user.getUid());
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(profile.this,"Couldn't Change",Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getImageFirebase();
                    Toast.makeText(profile.this,"Image Updated",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }
    }


    void ImageClick(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999 && data != null){
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            if(bitmap != null){
                firebaseUploadImage(bitmap);

            }
        }
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