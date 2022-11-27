package com.example.sars_cov_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email,password;
    private Button lgbutton;
    private FirebaseAuth fAuth;
    private TextView regLinkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        lgbutton = findViewById(R.id.btn_login);
        fAuth = FirebaseAuth.getInstance();
        regLinkBtn = findViewById(R.id.regLink);

        regLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        lgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lemail = email.getText().toString().trim();
                String lpassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(lemail)){
                    email.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(lpassword)){
                    password.setError("Password is required");
                    return;
                }

                fAuth.signInWithEmailAndPassword(lemail,lpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(Login.this,"You logged in successfully",Toast.LENGTH_SHORT);
                            toast.show();
                            if(lemail.contentEquals("admin1@admin.com")){
                                startActivity(new Intent(getApplicationContext(),AdminLogin.class));
                            }else{
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        } else{
                            Toast toast = Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        });

    }
}