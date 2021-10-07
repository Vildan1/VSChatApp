package com.example.vschatapp.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vschatapp.Models.MessageModel;
import com.example.vschatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;
    List<MessageModel> messageModelList;
    Boolean state;
    int view_type_sent=1, view_type_recived=2;

    public MessageAdapter(List<String> userKeysList, Activity activity, Context context,List<MessageModel> messageModelList) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat1-937a8-default-rtdb.firebaseio.com/");
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
        this.messageModelList= messageModelList;
        state=false;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

                if(viewType==view_type_sent)
                {
                    view = LayoutInflater.from(context).inflate(R.layout.message_send_layout, parent, false);
                    return new ViewHolder(view);
                }
                else
                {
                     view = LayoutInflater.from(context).inflate(R.layout.message_received_layout, parent, false);
                    return new ViewHolder(view);
                }

    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {

        holder.messageText.setText(messageModelList.get(position).getText());



    }


    //adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {

        return messageModelList.size();
    }

    //viewleri tanımlama işlemiyapılacak
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        CircleImageView friend_image;
        CircleImageView friend_state_img;

        ViewHolder(View itemView) {
            super(itemView);

           if(state==true)
           {
               messageText=messageText = (TextView) itemView.findViewById(R.id.message_send_text);
           }
           else
           {
               messageText=messageText = (TextView) itemView.findViewById(R.id.message_received_text);
           }

        }


    }

    @Override
    public int getItemViewType(int position) {
        if(userId.equals(messageModelList.get(position).getFrom())) {


            state=true;
            return view_type_sent;

        }
        else
        {
            state=false;
            return view_type_recived;

        }
    }
}