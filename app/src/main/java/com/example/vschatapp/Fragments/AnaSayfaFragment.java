package com.example.vschatapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vschatapp.Adapters.UserAdapter;
import com.example.vschatapp.Models.Kullanicilar;
import com.example.vschatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnaSayfaFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeysList;
    RecyclerView userListRecylerView;
    View view;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        tanimla();
        kullaniciGetir();
        return view;
    }

    public void tanimla()
    {
        userKeysList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        reference = firebaseDatabase.getReference();
        userListRecylerView = view.findViewById(R.id.userListRecylerView);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(),2);
        userListRecylerView.setLayoutManager(mng);
        userAdapter = new UserAdapter(userKeysList,getActivity(),getContext());
        userListRecylerView.setAdapter(userAdapter);
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
    }
    public void kullaniciGetir()
    {
        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                reference.child("Kullanicilar").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final @NonNull DataSnapshot dataSnapshot) {
                        Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);

                        if (!kl.getIsim().equals("null")&& !dataSnapshot.getKey().equals(user.getUid()))
                        {
                            if(userKeysList.indexOf(dataSnapshot.getKey())==-1)
                            {
                                userAdapter.notifyDataSetChanged();
                            }
                            userKeysList.add(dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}