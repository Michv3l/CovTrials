package com.example.sars_cov_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminLogin extends AppCompatActivity {


    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    public int numOfVolunteers, numOfPositive, numOfPosVacc, numOfPosUnVac, halfVacDose, fullVacDose, halfUnvacDose, fullUnvacDose;
    Button lgOut;
    TextView vols,poscase, vacGrp, vacPos, unvacPos, stVacEff, fdose, hdose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        lgOut = findViewById(R.id.lgOut);
        vols = findViewById(R.id.volunteers);
        poscase = findViewById(R.id.positiveCases);
        vacGrp = findViewById(R.id.vaccine);
        vacPos = findViewById(R.id.vacGroup);
        unvacPos = findViewById(R.id.unvacGroup);
        stVacEff = findViewById(R.id.vacEff);
        hdose = findViewById(R.id.hdoseEff);
        fdose = findViewById(R.id.fdoseEff);



        lgOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });




        fstore.collection("volunteers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(userId, document.getId() + " => " + document.getData());
                        numOfVolunteers += 1;
                    }
                } else {
                    Log.d(userId, "Error getting documents: ", task.getException());
                }
                Log.d(userId, "numOfVolunteers"+String.valueOf(numOfVolunteers), task.getException());
                vols.setText("Total number of volunteers: "+numOfVolunteers);
            }
        });

        fstore.collection("volunteers").whereEqualTo("result","positive")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(userId, document.getId() + " => " + document.getData());
                        numOfPositive += 1;
                    }
                } else {
                    Log.d(userId, "Error getting documents: ", task.getException());
                }
                Log.d(userId, "numOfPositive"+String.valueOf(numOfPositive), task.getException());
                poscase.setText("Total number of positive cases: "+numOfPositive);

                if(numOfPositive>1){
                    vacGrp.setText("Group A: SLCV2020 -Vaccine; Group B: Unknown -Placebo");

                    fstore.collection("volunteers").whereEqualTo("result","positive").whereEqualTo("vaccine","A")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(userId, document.getId() + " => " + document.getData());
                                    numOfPosVacc += 1;
                                }
                            } else {
                                Log.d(userId, "Error getting documents: ", task.getException());
                            }
                            vacPos.setText("Number of positive cases Group A (Vaccine): "+numOfPosVacc);
                            Log.d(userId, "numOfPosVacc"+String.valueOf(numOfPosVacc), task.getException());

                            fstore.collection("volunteers").whereEqualTo("result","positive").whereEqualTo("vaccine","B")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(userId, document.getId() + " => " + document.getData());
                                            numOfPosUnVac += 1;
                                        }
                                    } else {
                                        Log.d(userId, "Error getting documents: ", task.getException());
                                    }
                                    unvacPos.setText("Number of positive cases Group B (Placebo): "+numOfPosUnVac);
                                    Log.d(userId, "numOfPosUnvacc"+String.valueOf(numOfPosUnVac), task.getException());
                                    int vacEfficiency = ((numOfPosUnVac - numOfPosVacc)/numOfPosUnVac)*100;
                                    stVacEff.setText("The overall vaccine efficieny rate: "+vacEfficiency+"%");
                                }
                            });

                        }
                    });

                    fstore.collection("volunteers").whereEqualTo("result","positive").whereEqualTo("vaccine","B")
                            .whereEqualTo("dose","0.5").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(userId, document.getId() + " => " + document.getData());
                                    halfUnvacDose += 1;
                                }
                            } else {
                                Log.d(userId, "Error getting documents: ", task.getException());
                            }
                            Log.d(userId, "halfUnvacDose"+String.valueOf(halfUnvacDose), task.getException());
                            fstore.collection("volunteers").whereEqualTo("result","positive").whereEqualTo("vaccine","A")
                                    .whereEqualTo("dose","0.5").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(userId, document.getId() + " => " + document.getData());
                                            halfVacDose += 1;
                                        }
                                    } else {
                                        Log.d(userId, "Error getting documents: ", task.getException());
                                    }
                                    Log.d(userId, "halfVacDose"+String.valueOf(halfVacDose), task.getException());
                                    int halfDoseEff = ((halfUnvacDose-halfVacDose)/halfUnvacDose)*100;
                                    hdose.setText("The vaccine efficiency rate for half dose: "+halfDoseEff+"%");
                                }
                            });
                        }
                    });


                    fstore.collection("volunteers").whereEqualTo("result","positive").whereEqualTo("vaccine","B")
                            .whereEqualTo("dose","1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(userId, document.getId() + " => " + document.getData());
                                    fullUnvacDose += 1;
                                }
                            } else {
                                Log.d(userId, "Error getting documents: ", task.getException());
                            }
                            Log.d(userId, "fullUnvacDose"+String.valueOf(fullUnvacDose), task.getException());
                            fstore.collection("volunteers").whereEqualTo("result","positive").whereEqualTo("vaccine","A")
                                    .whereEqualTo("dose","1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(userId, document.getId() + " => " + document.getData());
                                            fullVacDose += 1;
                                        }
                                    } else {
                                        Log.d(userId, "Error getting documents: ", task.getException());
                                    }
                                    Log.d(userId, "fullVacDose"+String.valueOf(fullVacDose), task.getException());
                                    int fullDoseEff = ((fullUnvacDose-fullVacDose)/fullUnvacDose)*100;
                                    fdose.setText("The vaccine efficiency rate for Single dose: "+fullDoseEff+"%");
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}