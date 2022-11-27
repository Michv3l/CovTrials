package com.example.sars_cov_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    Button lgOut, posRes;
    TextView lkQr;
    FirebaseFirestore fstores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lkQr=findViewById(R.id.linkQr);
        lgOut=findViewById(R.id.lgOut);
        posRes=findViewById(R.id.covPos);
        fstores=FirebaseFirestore.getInstance();



        lgOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        lkQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Qrcode.class));
            }
        });

        posRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    String userId = fAuth.getCurrentUser().getUid();
                    String result = "positive";

                    fstores.collection("volunteers").document(userId).update("result",result).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Message", "QR code saved");
                        }
                    });
                    Toast.makeText(MainActivity.this,"Details Saved",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}