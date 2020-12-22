package com.example.sendes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class selectRoute extends AppCompatActivity implements Serializable {
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private ArrayList<ExampleItem> routelist;
    private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    private String Uid = fAuth.getUid();
    public List<String> list = new ArrayList<>();

    public static final String EXTRA_TEXT = "selectRoute.EXTRA_TEXT";

    public String documentTitle;

    private static final String TAG = "selectRoute";

    String x [];

    private void setDocumentTitle(String docTitle){
        documentTitle = docTitle;
    }

    public String getDocumentTitle(){
        return documentTitle;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        Button button = findViewById(R.id.startcon);
        button.setOnClickListener(new View.OnClickListener() {
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
                    routelist = new ArrayList<>();
                    buildRecycler();
                    mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            routelist.get(position).changeText1("Selected");
                            setDocumentTitle( routelist.get(position).getmText2());
                            mAdapter.notifyItemChanged(position);



                            Log.d(TAG,documentTitle);


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
                        routelist.add(new ExampleItem( String.valueOf(routelist.size()+1),x[i]));
                    }




                    Log.d(TAG, list.toString());
                    Log.d(TAG, String.valueOf(x.length));
                    Log.d(TAG, x[0]);
                    Log.d(TAG, x[6]);
                }else{
                    Log.d(TAG,"Error getting documents: ", task.getException());
                }

            }

        });




    }
    public void startConduct(){
        String s = getDocumentTitle();

        Intent intent = new Intent(this, Coductor.class);
        intent.putExtra(EXTRA_TEXT,s);
        startActivity(intent);

    }



    public void buildRecycler(){
        mRecyclerView = findViewById(R.id.currentroutes);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ExampleAdapter(routelist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }



}