package com.example.vschatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.vschatapp.Activity.KayitOlActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
 FirebaseAuth auth;
 FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        kontrol();
    }
    public void tanimla()
    {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

    }
    public void  kontrol()
    {
        if(user==null)
        {
            Intent intent=new Intent(MainActivity.this , KayitOlActivity.class);
            startActivity(intent);
            finish();
        }
    }

}