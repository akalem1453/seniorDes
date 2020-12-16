package com.example.sendes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addroute extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView code;
    Button scan;
    Button add_point;
    Button add_route;
    public ArrayList<ExampleItem> mExampleList;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String Uid;
    String  codeArray[];
    List<String>  tags;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addroute);

        code = findViewById(R.id.code);
        scan = findViewById(R.id.scan_btn);
        mExampleList = new ArrayList<>();



        add_point = findViewById(R.id.add_btn);
        add_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointCode;
                pointCode = code.getText().toString();

                String pointPosition = String.valueOf(mExampleList.size()+1);
                mExampleList.add(new ExampleItem(pointPosition,pointCode));
                mAdapter.notifyItemInserted(mExampleList.size()+1);



            }
        });



        mRecyclerView = findViewById(R.id.newroute_recyler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




        add_route = findViewById(R.id.upload_route);
        add_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                Uid = fAuth.getCurrentUser().getUid();
                codeArray = new String[mExampleList.size()];

                for(int i = 0; i <mExampleList.size();i++){
                    codeArray[i] = mExampleList.get(i).getmText2();
                }
                tags = Arrays.asList(codeArray);
                Map<String, Object> new_route = new HashMap<>();
                new_route.put("route", tags);
                 fStore.collection("users").document(Uid).collection("routes").document("route1").set(new_route);








            }
        });









    }


    public void start(View view){

        initScan();
    }


    private void initScan(){
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);


        if(scanResult !=null){

            code.setText(scanResult.getContents());


        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}