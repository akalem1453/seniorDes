package com.example.sendes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LogsWithDates extends AppCompatActivity {
    private String docRef;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private ArrayList<ExampleItem> logList;
    private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    private String uid = fAuth.getUid();
    private String documentTitle;
    private List<String> list = new ArrayList<>();
    private String x [];
    Button nextPage;
    private int p ;

    public static final String EXTRA_TEXT = "LogsWithDates.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "LogsWithDates.EXTRA_TEXT2";


    private void setDocumentTitle(String docTitle){
        documentTitle = docTitle;
    }

    public String getDocumentTitle(){
        return documentTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_with_dates);
        Intent intent = getIntent();
        docRef = intent.getStringExtra(Logs.EXTRA_TEXT);

        nextPage = findViewById(R.id.gotologs);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConduct();
            }
        });





        CollectionReference logs = fStore.collection("users").document(uid).collection("routes").document(docRef).collection("Logs");
        logs.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    logList = new ArrayList<>();
                    buildRecycler();
                    mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            logList.get(position).changeText1("Selected");
                            setDocumentTitle( logList.get(position).getmText2());
                            mAdapter.notifyItemChanged(position);
                            p=position;



                            Log.d("logswithdates",documentTitle);


                        }
                    });







                    for (QueryDocumentSnapshot document : task.getResult()){
                        list.add(document.getId());
                    }
                    x = new String[list.size()];
                    for (int i=0; i<x.length;i++){
                        x[i] = list.get(i);
                    }

                    for (int i=0;i<x.length;i++){
                        logList.add(new ExampleItem( String.valueOf(logList.size()+1),x[i]));
                    }





                }else{
                    Log.d("logswithdates","Error getting documents: ", task.getException());
                }

            }

        });

    }



    private void buildRecycler(){
        mRecyclerView = findViewById(R.id.logs);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ExampleAdapter(logList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }

    public void startConduct(){
        String s = getDocumentTitle();
        String routename= docRef;
        logList.get(p).changeText1(String.valueOf(p+1));
        mAdapter.notifyItemChanged(p);

        Intent intent = new Intent(this, LohsWithTags.class);

        intent.putExtra(EXTRA_TEXT,s);
        intent.putExtra(EXTRA_TEXT2,routename);
        startActivity(intent);

    }
}