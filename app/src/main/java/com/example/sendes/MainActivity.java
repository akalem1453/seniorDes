package com.example.sendes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.barcode.BarcodeScanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }
    public void gotoroute(View view){
        startActivity(new Intent(getApplicationContext(),addroute.class));
    }
    public void selectRoute(View view){
        startActivity(new Intent(getApplicationContext(),selectRoute.class));
    }


}