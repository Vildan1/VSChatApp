package com.example.vschatapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vschatapp.Models.Kullanicilar;
import com.example.vschatapp.R;
import com.example.vschatapp.Utils.ChangeFragment;
import com.example.vschatapp.Utils.RandomName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class KullaniciProfilFragment extends Fragment {
    String imageUrl;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    View view;
    EditText kullaniciIsmi,input_egitim,input_dogumtarihi,input_hakkimda;
    CircleImageView profile_image;
    Button bilgiGüncelleButon,bilgiArkadasButon,bilgiIstekButon;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_kullanici_profil, container, false);
        tanimla();
        bilgileriGetir();
        return view;
    }

    public  void tanimla()
    {

        bilgiArkadasButon = (Button) view.findViewById(R.id.bilgiArkadasButon);
        bilgiIstekButon = (Button) view.findViewById(R.id.bilgiIstekButon);
        kullaniciIsmi = (EditText)view.findViewById(R.id.kullaniciIsmi);
        input_egitim = (EditText)view.findViewById(R.id.input_egitim);
        input_dogumtarihi = (EditText)view.findViewById(R.id.input_dogumtarihi);
        input_hakkimda = (EditText)view.findViewById(R.id.input_hakkimda);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        bilgiGüncelleButon = (Button) view.findViewById(R.id.bilgiGuncelleButon);
        firebaseStorage = FirebaseStorage.getInstance("gs://chat1-937a8.appspot.com");
        storageReference = firebaseStorage.getReference();
        bilgiGüncelleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                güncelle();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galareAc();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("Kullanicilar").child(user.getUid());
        bilgiArkadasButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment= new ChangeFragment(getContext());
                changeFragment.change(new ArkadaslarFragment());
            }
        });
        bilgiIstekButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment= new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());
            }
        });
    }

    private  void galareAc(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,5);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data )
    {
        if (requestCode==5 && resultCode== Activity.RESULT_OK)
        {
            Uri filePath = data.getData();
            StorageReference ref= storageReference.child("kullaniciresimleri").child(RandomName.getSaltString()+".jpg");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getContext(),"Resim Güncellendi...", Toast.LENGTH_LONG).show();

                        String isim = kullaniciIsmi.getText().toString();
                        String egitim = input_egitim.getText().toString();
                        String dogum = input_dogumtarihi.getText().toString();
                        String hakkimda = input_hakkimda.getText().toString();

                        reference = database.getReference().child("Kullanicilar").child(auth.getUid());

                        Map map = new HashMap();

                        map.put("isim",isim);
                        map.put("egitim",egitim);
                        map.put("dogumtarihi",dogum);
                        map.put("hakkimda",hakkimda);
                        map.put("resim", task.getResult().getDownloadUrl().toString());

                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    ChangeFragment fragment = new ChangeFragment(getContext());
                                    fragment.change(new KullaniciProfilFragment());
                                    Toast.makeText(getContext(),"Bilgiler başarıyla güncellendi...", Toast.LENGTH_LONG).show();
                                }else
                                {
                                    Toast.makeText(getContext(),"Bilgiler güncellenemedi...", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }else
                        Toast.makeText(getContext(),"Resim güncellenemedi...", Toast.LENGTH_LONG).show();

                }
            });

        }
    }

    public  void bilgileriGetir()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);
                kullaniciIsmi.setText(kl.getIsim());
                input_egitim.setText(kl.getEgitim());
                input_dogumtarihi.setText(kl.getDogumtarihi());
                input_hakkimda.setText(kl.getHakkimda());
                imageUrl = kl.getResim();

                if(!kl.getResim().equals("null")){
                    Picasso.get().load(kl.getResim()).into(profile_image);
                }else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public  void  güncelle()
    {
        String isim = kullaniciIsmi.getText().toString();
        String egitim = input_egitim.getText().toString();
        String dogum = input_dogumtarihi.getText().toString();
        String hakkimda = input_hakkimda.getText().toString();

        reference=database.getReference().child("Kullanicilar").child(auth.getUid());

        Map map = new HashMap();

        map.put("isim",isim);
        map.put("egitim",egitim);
        map.put("dogum",dogum);
        map.put("hakkimda",hakkimda);
        if (imageUrl.equals("null"))
        {
            map.put("resim","null");
        }else
        {
            map.put("resim",imageUrl);
        }
        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    ChangeFragment fragment = new ChangeFragment(getContext());
                    fragment.change(new KullaniciProfilFragment());
                    Toast.makeText(getContext(),"Bilgiler başarıyla güncellendi...", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getContext(),"Bilgiler güncellenemedi...", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}