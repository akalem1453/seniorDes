package com.example.sendes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Coductor extends AppCompatActivity {

    private TextView tagNo;
    private static final String TAG = "conductor";
    public String docRef;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String Uid = fAuth.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coductor);
        Intent intent = getIntent();
        docRef = intent.getStringExtra(selectRoute.EXTRA_TEXT);
        tagNo = findViewById(R.id.tagcode);







    }
    public void openCam(View view){

        initScan();
    }


    private void initScan(){
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);


        if(scanResult !=null){

            tagNo.setText(scanResult.getContents());


        }

        super.onActivityResult(requestCode, resultCode, intent);
    }



}