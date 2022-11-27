package com.example.sars_cov_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText email, password, cpassword, fname, gender, age, address, health;
    Button regbtn;
    FirebaseAuth fAuth;
    String userID;
    TextView loginLinkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        fname = findViewById(R.id.fullName);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        address = findViewById(R.id.address);
        health = findViewById(R.id.health);
        regbtn = findViewById(R.id.rbutton);
        loginLinkBtn = findViewById(R.id.logLink);
        fAuth = FirebaseAuth.getInstance();

        loginLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        if (fAuth.getInstance().getCurrentUser() != null) {
            if(fAuth.getInstance().getCurrentUser().getUid().equals("DZ9bjrlqHQU6ojIo39PMgLZmtzj2")){
                startActivity(new Intent(getApplicationContext(),AdminLogin.class));
            }else{
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String remail = email.getText().toString().trim();
                final String rpass = password.getText().toString().trim();
                final String ccheckpass = cpassword.getText().toString().trim();
                final String rgender = gender.getText().toString().trim().toLowerCase();
                final String raddress = address.getText().toString().trim();
                final String rhealth = health.getText().toString().trim();
                final String rfname = fname.getText().toString().trim();
                final String checkAge = age.getText().toString().trim();
                if(TextUtils.isEmpty(checkAge)){
                    age.setError("Your age is required");
                    return;
                }
                try{
                    final int rAge = Integer.parseInt(checkAge);

                    if(TextUtils.isEmpty(remail)){
                        email.setError("Your Email is required");
                        return;
                    }

                    if(TextUtils.isEmpty(rpass)){
                        password.setError("Please enter a password");
                        return;
                    }

                    if (!ccheckpass.contentEquals(rpass)){
                        cpassword.setError("Your password does not match");
                        return;
                    }

                    if(TextUtils.isEmpty(rgender)||TextUtils.isEmpty(raddress)||TextUtils.isEmpty(rhealth)
                            ||TextUtils.isEmpty(rfname)){
                        Toast.makeText(getApplicationContext(),"Please fill in all fields",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!(rgender.contentEquals("male")||rgender.contentEquals("female")||rgender.contentEquals("other"))){
                        gender.setError("Please enter Male/Female/Other");
                        return;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    age.setError("Please enter a valid age");
                    return;
                }


                fAuth.createUserWithEmailAndPassword(remail,rpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_SHORT);
                            toast.show();
                            userID=fAuth.getCurrentUser().getUid();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast toast = Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        });



    }
}