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

public class Logs extends AppCompatActivity {

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private ArrayList<ExampleItem> mroutelist;
    private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    Button goToLog;
    private String Uid = fAuth.getUid();
    private String documentTitle;
    private List<String> list = new ArrayList<>();
    private int p ;



    public static final String EXTRA_TEXT = "Logs.EXTRA_TEXT";

    private String x [];

    private void setDocumentTitle(String docTitle){
        documentTitle = docTitle;
    }

    private String getDocumentTitle(){
        return documentTitle;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        goToLog = findViewById(R.id.gotologs);
        goToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConduct();
            }
        });


        CollectionReference routes = fStore.collection("users").document(Uid).collection("routes");
        routes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    mroutelist = new ArrayList<>();
                    buildRecycler();
                    mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            mroutelist.get(position).changeText1("Selected");
                            setDocumentTitle( mroutelist.get(position).getmText2());
                            mAdapter.notifyItemChanged(position);
                            p=position;
                            Log.d("logs", String.valueOf(p));

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
                        mroutelist.add(new ExampleItem( "  ",x[i]));
                    }


                }else{
                    Log.d("Logs","Error getting documents: ", task.getException());
                }

            }

        });

    }






    private void buildRecycler(){
        mRecyclerView = findViewById(R.id.usedroutes);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ExampleAdapter(mroutelist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }

    public void startConduct(){

        String s = getDocumentTitle();
        mroutelist.get(p).changeText1("  ");
        mAdapter.notifyItemChanged(p);


        Intent intent = new Intent(this, LogsWithDates.class);

        intent.putExtra(EXTRA_TEXT,s);
        startActivity(intent);

    }
}