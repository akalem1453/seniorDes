package com.example.sendes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView code;
    private Button scan;
    private Button add_point;
    private Button add_route;
    private ArrayList<ExampleItem> mExampleList;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String Uid;
    private String  codeArray[];
    private List<String>  tags;
    private String routeName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addroute);

        code = findViewById(R.id.code);
        scan = findViewById(R.id.scan_btn);
        mExampleList = new ArrayList<>();




        mRecyclerView = findViewById(R.id.newroute_recyler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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




        add_route = findViewById(R.id.upload_route);
        add_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                Uid = fAuth.getCurrentUser().getUid();
                codeArray = new String[mExampleList.size()];
                AlertDialog.Builder nameDialog = new AlertDialog.Builder(addroute.this);
                nameDialog.setTitle("Enter Route Name");

                EditText nameinput = new EditText(addroute.this);
                nameDialog.setView(nameinput);
                nameinput.setInputType(InputType.TYPE_CLASS_TEXT);
                nameDialog.setPositiveButton("Add route", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        routeName = nameinput.getText().toString();
                        for(int i = 0; i <mExampleList.size();i++){
                            codeArray[i] = mExampleList.get(i).getmText2();
                        }
                        tags = Arrays.asList(codeArray);
                        Map<String, Object> new_route = new HashMap<>();
                        new_route.put("route", tags);

                        fStore.collection("users").document(Uid).collection("routes").document(routeName).set(new_route);
                        gotoMain();




                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                nameDialog.show();








            }
        });




    }


    public void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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