package com.example.vschatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vschatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {

    EditText input_email;
    EditText input_password;
    Button registerButton;
    FirebaseAuth auth;
    TextView hesapvarText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        tanimla();


    }

    public void tanimla() {
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        registerButton = (Button) findViewById(R.id.registerButton);
        hesapvarText = (TextView) findViewById(R.id.hesapvarText);
        auth = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String pass = input_password.getText().toString();

                if (!email.equals("") && !pass.equals("")) {
                    input_email.setText("");
                    input_password.setText("");
                    kayitOl(email, pass);
                } else {
                    Toast.makeText(getApplicationContext(), "Bilgileri boş giremezsiniz....", Toast.LENGTH_LONG).show();
                }
            }
        });

        hesapvarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitOlActivity.this, GirisActivity.class);
                fileList();
            }
        });


    }


    public void kayitOl(String email, String pass)
    {
        Log.i("testttttttttttttt",""+ auth);
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {

                    firebaseDatabase = FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
                    reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());

                    Map map = new HashMap();
                    map.put("resim","null");
                    map.put("isim", "null");
                    map.put("egitim", "null");
                    map.put("dogum", "null");
                    map.put("hakkimda", "null");

                    reference.setValue(map);

                    Intent intent = new Intent(KayitOlActivity.this, AnaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
            {
                Toast.makeText(getApplicationContext(), "Kayit olma esnasında bir problem yaşandı." , Toast.LENGTH_LONG).show();
            }
        }
    });
}
}
