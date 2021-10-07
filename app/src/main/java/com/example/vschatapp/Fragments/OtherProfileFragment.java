package com.example.vschatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vschatapp.Activity.ChatActivity;
import com.example.vschatapp.Models.Kullanicilar;
import com.example.vschatapp.R;
import com.example.vschatapp.Utils.ShowToastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {


    View view;
    String otherId,userId;
    TextView userProfileNameText,userProfileEgitimText,userProfileDogumText,
            userProfileHakkimdaText,userProfileTakipText,userProfilArkadasText,userProfileNameText2;

    ImageView userProfileArkadasImage,userProfileMesajImage,userProfileTakipImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference_Arkadaslik;
    CircleImageView userProfileProfileImage;
    FirebaseAuth auth;
    FirebaseUser user;
    String kontrol="",begeniKontrol="";
    ShowToastMessage showToastMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tanimla();
        action();
        getBegeniText();
        getArkadasText();
        return view;
    }

    public void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        reference=firebaseDatabase.getReference();
        reference_Arkadaslik=firebaseDatabase.getReference().child("Arkadaslik_Istek");
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        userId= user.getUid();

        otherId = getArguments().getString("userid");
        userProfileNameText =(TextView)view.findViewById(R.id.userProfileNameText);
        userProfileEgitimText =(TextView)view.findViewById(R.id.userProfileEgitimText);
        userProfileDogumText =(TextView)view.findViewById(R.id.userProfileDogumText);
        userProfileHakkimdaText =(TextView)view.findViewById(R.id.userProfileHakkimdaText);
        userProfileTakipText = (TextView)view.findViewById(R.id.userProfilTakipText);
        userProfilArkadasText = (TextView)view.findViewById(R.id.userProfilArkadasText);
        userProfileNameText2 = (TextView)view.findViewById(R.id.userProfileNameText2);

        userProfileArkadasImage = (ImageView)view.findViewById(R.id.userProfileArkadasImage);
        userProfileMesajImage = (ImageView)view.findViewById(R.id.userProfileMesajImage);
        userProfileTakipImage = (ImageView)view.findViewById(R.id.userProfileTakipImage);
        userProfileProfileImage =(CircleImageView)view.findViewById(R.id.userProfileProfilImage);

        reference_Arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId))
                {
                    kontrol = "istek";
                    userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle);
                }else
                {

                    userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherId))
                {
                    kontrol = "arkadas";
                    userProfileArkadasImage.setImageResource(R.drawable.deleting);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId))
                {
                    begeniKontrol = "begendi";
                    userProfileTakipImage.setImageResource(R.drawable.takip_on);

                }else
                {
                    userProfileTakipImage.setImageResource(R.drawable.takip_off);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showToastMessage = new ShowToastMessage(getContext());

    }
    public  void action()
    {
        reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);
                userProfileNameText.setText("İsim: " +kl.getIsim());
                userProfileEgitimText.setText("Eğitim: " +kl.getEgitim());
                userProfileDogumText.setText("Doğum Tarihi: " +kl.getDogumtarihi());
                userProfileHakkimdaText.setText("Hakkımda: " +kl.getHakkimda());
                userProfileNameText2.setText(kl.getIsim());
                if(!kl.getResim().equals("null")){
                    Picasso.get().load(kl.getResim()).into(userProfileProfileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userProfileArkadasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol.equals("istek"))
                {
                    arkadasIptalEt(otherId,userId);
                }else if (kontrol.equals("arkadas"))
                {
                    arkadasTablosundanCikar(otherId,userId);
                }
                else
                {
                    arkadaşEkle(otherId,userId);
                }
            }
        });
        userProfileTakipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begeniKontrol.equals("begendi"))
                {
                    begeniIptal(userId,otherId);
                }else
                {
                    begen(userId,otherId);
                }
            }
        });
        userProfileMesajImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userName",userProfileNameText2.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
            }
        });
    }

    private void begeniIptal(String userId, String otherId) {
        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userProfileTakipImage.setImageResource(R.drawable.takip_off);
                begeniKontrol ="";
                showToastMessage.showToast("Beğenme iptal edildi.");
                getBegeniText();
            }
        });
    }

    private void arkadasTablosundanCikar(final String otherId,final String userId) {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle);
                        showToastMessage.showToast("Arkadaslıktan çıkarıldı");
                        getArkadasText();
                    }
                });
            }
        });
    }

    public void arkadaşEkle(final String otherId, final String userId)
    {
        reference_Arkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    reference_Arkadaslik.child(otherId).child(userId).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                kontrol="istek";
                                showToastMessage.showToast("Arkadaşlık İsteği Başarıyla gönderildi.");
                                userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle);
                            }else
                            {
                                showToastMessage.showToast("Bir problem meydana geldi");

                            }
                        }
                    });
                }else
                {
                    showToastMessage.showToast("Bir problem meydana geldi");
                }
            }
        });
    }
    public void arkadasIptalEt(final String otherId, final String userId)
    {
        reference_Arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_Arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle);
                        showToastMessage.showToast("Arkadaşlık isteği iptal edildi");

                    }
                });
            }
        });
    }

    public  void begen(String userId, String otherId)
    {
        reference.child("Begeniler").child(otherId).child(userId).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    showToastMessage.showToast("Profiili beğendiniz.");
                    userProfileTakipImage.setImageResource(R.drawable.takip_on);
                    begeniKontrol= "begendi";
                    getBegeniText();
                }
            }
        });


    }
    public void getBegeniText()
    {

     //   final List<String> begeniList = new ArrayList<>();
     // userProfileTakiptext.setText(0 Begeni");

       reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userProfileTakipText.setText(dataSnapshot.getChildrenCount()+ "Beğeni");
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }
    public void getArkadasText() {
        //  final List<String> arkList = new ArrayList<>();
        userProfileTakipText.setText("0 Arkadaş");

        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfilArkadasText.setText(dataSnapshot.getChildrenCount() + "Arkadaş");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}