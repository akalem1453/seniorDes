package com.example.sendes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coductor extends AppCompatActivity {

    private TextView tagNo;
    private static final String TAG = "conductor";
    private String docRef;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String Uid = fAuth.getUid();
    private List<String> tags;
    private ArrayList<ExampleItem> routetage = new ArrayList<>();
    private ArrayList<String> logTags = new ArrayList<>();
    private String LogTags[];
    private List<String> routeTags;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(this); ;
    private RecyclerView mRecyclerView;
    private Button process;
    String x[];
    private ArrayList<String> timeTags= new ArrayList<>();
    private List<String>  logs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coductor);
        Intent intent = getIntent();
        docRef = intent.getStringExtra(selectRoute.EXTRA_TEXT);
        tagNo = findViewById(R.id.tagcode);
        Log.d(TAG, docRef);
        process = findViewById(R.id.remove);
        DocumentReference documentReference = fStore.collection("users").document(Uid).collection("routes").document(docRef);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                buildRecycler();
                DocumentSnapshot document = task.getResult();
                Log.d(TAG, String.valueOf(document));
                if (document.exists()){
                    routeTags = (List<String>) document.get("route");
                    if (routeTags == null) {

                        Log.d(TAG, String.valueOf(Calendar.getInstance()));

                        Log.d(TAG, "yok");
                    }else{
                        Log.d(TAG, "var");
                        Log.d(TAG, String.valueOf(routeTags));
                        Log.d(TAG, String.valueOf(Calendar.getInstance().getTime()));
                    }

                    for(int i=0; i<routeTags.size();i++){
                        routetage.add(new ExampleItem( String.valueOf(routetage.size()+1),routeTags.get(i)));

                    }
                    LogTags = new String[routetage.size()];
                    x = new String[routetage.size()];


                }
            }
        });






        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = tagNo.getText().toString();


                Log.d("writtentext in field",code);

                if (routetage.size()>1) {
                    logTags.add(routetage.get(0).getmText2());
                    timeTags.add(String.valueOf(Calendar.getInstance().getTime()));





                    if (routetage.get(0).getmText2().equals(code)) {
                        routetage.remove(0);
                        mAdapter.notifyItemRemoved(0);
                        Log.d("contents of log", logTags.get(0));

                    } else {
                        Toast.makeText(Coductor.this, "wrong code", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    timeTags.add(String.valueOf(Calendar.getInstance().getTime()));
                    logTags.add(routetage.get(0).getmText2());

                    if (routetage.get(0).getmText2().equals(code)) {
                        routetage.remove(0);
                        mAdapter.notifyItemRemoved(0);


                    } else {
                        Toast.makeText(Coductor.this, "wrong code", Toast.LENGTH_SHORT).show();
                    }






                    for (int i=0;i<logTags.size();i++){
                        x[i]=
                                logTags.get(i) +" "+ timeTags.get(i);
                    }
                    logs =  Arrays.asList(x);
                    Log.d("contents", String.valueOf(logTags));
                    Map<String, Object> new_log = new HashMap<>();
                    new_log.put("route", logs);
                    Log.d("Conductor", timeTags.get(0).substring(0,11));
                    fStore.collection("users").document(Uid).collection("routes").document(docRef)
                            .collection("Logs").document("Log of "+timeTags.get(0).substring(0,16)).set(new_log);

                    Log.d("Conductor", timeTags.get(0).substring(0,11));

                    new AlertDialog.Builder(Coductor.this)
                            .setTitle("Patrol Done")
                            .setMessage("Going Back to Home")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // The user wants to leave - so dismiss the dialog and exit
                                    gotoMain();
                                }
                            }).show();




                }


            }
        });















    }
    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        new AlertDialog.Builder(this)
                .setTitle("Exiting the Patrol")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // The user wants to leave - so dismiss the dialog and exit
                        gotoMain();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // The user is not sure, so you can exit or just stay
                dialog.dismiss();
            }
        }).show();

    }


    private void buildRecycler(){
        mRecyclerView = findViewById(R.id.tags);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ExampleAdapter(routetage);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }
    public void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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