package com.example.vschatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vschatapp.Fragments.AnaSayfaFragment;
import com.example.vschatapp.Fragments.BildirimFragment;
import com.example.vschatapp.Fragments.KullaniciProfilFragment;
import com.example.vschatapp.Utils.ChangeFragment;
import com.example.vschatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AnaActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ChangeFragment changeFragment;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private  BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.navigation_home:
                    changeFragment.change(new AnaSayfaFragment());
                    return true;
                case    R.id.navigation_dashboard:
                    changeFragment.change(new BildirimFragment());
                    return true;
                case  R.id.navigation_profil:
                    changeFragment.change(new KullaniciProfilFragment());
                    return true;
                case R.id.navigation_exit:
                    cik();
                    return true;
            }
            return false;
        }

    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tanimla();
        kontrol();

        changeFragment = new ChangeFragment(AnaActivity.this);
        changeFragment.change(new AnaSayfaFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void cik()
    {
        auth.signOut();
        Intent intent = new Intent(AnaActivity.this, GirisActivity.class);
        startActivity(intent);
        finish();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanicilar");
        reference.child(user.getUid()).child("state").setValue(false);

    }

    public void tanimla()
    {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanicilar");
        reference.child(user.getUid()).child("state").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanicilar");
        reference.child(user.getUid()).child("state").setValue(true);
    }

    public void kontrol()
    {
        if(user==null)
        {
            Intent intent=new Intent(AnaActivity.this,GirisActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
            DatabaseReference reference=firebaseDatabase.getReference().child("Kullanicilar");
            reference.child(user.getUid()).child("state").setValue(true);


        }
    }



}