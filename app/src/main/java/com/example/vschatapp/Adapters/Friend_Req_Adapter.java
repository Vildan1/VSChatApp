package com.example.vschatapp.Adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vschatapp.Fragments.OtherProfileFragment;
import com.example.vschatapp.Models.Kullanicilar;
import com.example.vschatapp.R;
import com.example.vschatapp.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_Req_Adapter extends RecyclerView.Adapter<Friend_Req_Adapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;

    public Friend_Req_Adapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();

    }

    //layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_req_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {

        //holder.usernameTextView.setText(userKeysList.get(position).toString());

        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final @NonNull DataSnapshot dataSnapshot) {

                Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);
                Log.i("istek kullanici", kl.toString());
                Picasso.get().load(kl.getResim()).into(holder.friend_req_image);

                holder.friend_req_text.setText(kl.getIsim());
                holder.friend_req_ekle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kabulEt(userId, userKeysList.get(position));

                    }
                });
                holder.friend_req_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redEt(userId, userKeysList.get(position));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {

        return userKeysList.size();
    }

    //viewleri tanımlama işlemiyapılacak
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friend_req_text;
        CircleImageView friend_req_image;
        Button friend_req_ekle;
        Button friend_req_red;

        ViewHolder(View itemView) {
            super(itemView);
            friend_req_text = (TextView) itemView.findViewById(R.id.friend_req_text);
            friend_req_image = (CircleImageView) itemView.findViewById(R.id.friend_req_image);
            friend_req_ekle = (Button) itemView.findViewById(R.id.friend_req_ekle);
            friend_req_red = (Button) itemView.findViewById(R.id.friend_req_red);

        }

    }

    public void kabulEt(final String userId, final String otherId) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        final String reportDate = df.format(today);

        reference.child("Arkadaslar").child(userId).child(otherId).child("tarih").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                reference.child("Arkadaslar").child(otherId).child(userId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Arkadaşlık İsteğini Kabul Ettiniz.", Toast.LENGTH_LONG).show();
                            reference.child("Arkadaşlık İstek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reference.child("Arkadaşlık İstek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });


    }

    public void redEt(final String userId, final String otherId) {

        reference.child("Arkadaşlık İstek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaşlık İstek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Arkadaşlık isteğini reddettiniz", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }
    }
