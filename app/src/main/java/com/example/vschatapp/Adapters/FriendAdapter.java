package com.example.vschatapp.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vschatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;

    public FriendAdapter(List<String> userKeysList, Activity activity, Context context) {
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
            public void onDataChange( DataSnapshot dataSnapshot) {

                String  userName = dataSnapshot.child("isim").getValue().toString();
                String userImage= dataSnapshot.child("resim").getValue().toString();
                Boolean stateUser=Boolean.parseBoolean(dataSnapshot.child("state").getValue().toString());
                if(stateUser==true) {

                    holder.friend_state_img.setImageResource(R.drawable.online_icon);
                }
                else {
                    holder.friend_state_img.setImageResource(R.drawable.offline_icon);
                }
                Picasso.get().load(userImage).into(holder.friend_image);

                holder.friend_text.setText(userName);
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
        TextView friend_text;
        CircleImageView friend_image;
        CircleImageView friend_state_img;

        ViewHolder(View itemView) {
            super(itemView);
            friend_text = (TextView) itemView.findViewById(R.id.friend_text);
            friend_image = (CircleImageView) itemView.findViewById(R.id.friend_image);
            friend_state_img= (CircleImageView) itemView.findViewById(R.id.friend_state_img);

        }

    }
}