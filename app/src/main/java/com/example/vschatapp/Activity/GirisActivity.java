package com.example.vschatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class GirisActivity extends AppCompatActivity {

    private EditText input_email_login,input_password_login;
    private Button loginButton;
    private FirebaseAuth auth;
    private TextView hesapYok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        tanimla();
    }

    public void tanimla()
    {
        input_email_login = findViewById(R.id.input_email_login);
        input_password_login = findViewById(R.id.input_password_login);
        loginButton = findViewById(R.id.loginButton);
        hesapYok = findViewById(R.id.hesapYok);
        auth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email_login.getText().toString();
                String pass = input_password_login.getText().toString();
                if(!email.equals("")&&!pass.equals(""))
                {
                    sistemeGiris(email,pass);
                }else
                {
                    Toast.makeText(getApplicationContext(), "Boş girilemez...", Toast.LENGTH_LONG).show();
                }
            }
        });
        hesapYok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this, KayitOlActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void sistemeGiris(String email, String pass)
    {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(GirisActivity.this, AnaActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Hatalı bilgi girdiniz...", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}