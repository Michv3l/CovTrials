package com.example.sars_cov_3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Qrcode extends AppCompatActivity implements View.OnClickListener{

    private IntentIntegrator qrScan;
    private Button scanBtn, subBtn;
    private EditText qrDose, qrVgroup;
    private FloatingActionButton backBtn;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        scanBtn = (Button) findViewById(R.id.scanBtn);
        qrDose = findViewById(R.id.textDose);
        qrVgroup = findViewById(R.id.textVGroup);
        fstore = FirebaseFirestore.getInstance();
        subBtn = findViewById(R.id.qrSub);
        backBtn = findViewById(R.id.fabBack);
        qrScan = new IntentIntegrator(this);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vGroup = qrVgroup.getText().toString().toUpperCase();
                String dose = qrDose.getText().toString();
                String result = "negative";

                if (TextUtils.isEmpty(vGroup) || (!vGroup.contentEquals("A") && !vGroup.contentEquals("B")) ) {
                    qrVgroup.setError("Please choose A or B");
                    return;
                }
                if (TextUtils.isEmpty(dose) || (!dose.contentEquals("1") && !dose.contentEquals("0.5")) ) {
                    qrDose.setError("Please choose 1 or 0.5");
                    return;
                }
             else {
                try {
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    String userId = fAuth.getCurrentUser().getUid();

                    DocumentReference docRef = fstore.collection("volunteers").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("vaccine", vGroup);
                    user.put("dose", dose);
                    user.put("result", result);

                    docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Message", "QR code saved");
                        }
                    });
                    Toast.makeText(Qrcode.this,"Details Saved",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        scanBtn.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"Result was not Found",Toast.LENGTH_SHORT).show();
                qrDose.setText("Nothing Found");
                qrVgroup.setText("Nothing Found");
            } else{
                try{
                    qrDose.setText(result.getContents());
                    FirebaseAuth fAuth= FirebaseAuth.getInstance();
                    String userId=fAuth.getCurrentUser().getUid();

                    DocumentReference docRef = fstore.collection("volunteers").document(userId);
                    Map<String, Object> user=new HashMap<>();
                    String qrArray = result.getContents();
                    Gson g = new Gson();
                    Volunteer v = g.fromJson(qrArray,Volunteer.class);
                    String nVaccine = v.getVaccine();
                    String nDose = v.getDose();
                    String nRes = "negative";

                    user.put("vaccine", nVaccine);
                    user.put("dose", nDose);
                    user.put("result", nRes);

                    docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Message","QR code saved");
                        }
                    });
                    Toast.makeText(this,"Details Saved",Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }
}