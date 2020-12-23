package com.example.sendes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LohsWithTags extends AppCompatActivity {
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String Uid = fAuth.getUid();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(this); ;
    private RecyclerView mRecyclerView;
    private ArrayList<ExampleItem> routetage = new ArrayList<>();
    private String docRef;
    private String logRef;
    private List<String> routeTags;
    private static final String TAG = "logswithtags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lohs_with_tags);

        Intent intent = getIntent();
        logRef =intent.getStringExtra(LogsWithDates.EXTRA_TEXT);
        docRef = intent.getStringExtra(LogsWithDates.EXTRA_TEXT2);
        Log.d("Logswdates",logRef);
        Log.d("Logswdates",docRef);
        DocumentReference documentReference = fStore.collection("users").document(Uid).collection("routes").document(docRef).collection("Logs").document(logRef);

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



                }
            }
        });
    }



    public void gohome(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }




    private void buildRecycler(){
        mRecyclerView = findViewById(R.id.tagswithstamps);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ExampleAdapter(routetage);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }
}