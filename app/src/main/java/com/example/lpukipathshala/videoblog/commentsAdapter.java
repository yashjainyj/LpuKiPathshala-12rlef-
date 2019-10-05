package com.example.lpukipathshala.videoblog;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.quoraa.mAnswerGetSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class commentsAdapter extends RecyclerView.Adapter<com.example.lpukipathshala.videoblog.commentsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<mComments> mData;


    public commentsAdapter(Context mContext, ArrayList<mComments> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public com.example.lpukipathshala.videoblog.commentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardofvideocomments,viewGroup,false);
        return new com.example.lpukipathshala.videoblog.commentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.lpukipathshala.videoblog.commentsAdapter.MyViewHolder myViewHolder, int i) {
        mComments dailyAlbum = mData.get(i);
        myViewHolder.documentReference=  myViewHolder.firebaseFirestore.collection("Users").document(dailyAlbum.getUid());
        myViewHolder.documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                myViewHolder.ansusername.setText(userDetails.getFname()+" "+userDetails.getLname());
                myViewHolder.ansusername.setBackground(null);
                myViewHolder.answeruserdate.setBackground(null);
                myViewHolder.ansuserimage.setBackground(null);
                Glide.with(mContext).load(userDetails.getPic_url()).into(myViewHolder.ansuserimage);
                // Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(back);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
       // myViewHolder.ansusername.setText(dailyAlbum.getCusername());
        myViewHolder.answertext.setText(dailyAlbum.getNuseranswer());
        myViewHolder.answeruserdate.setText(dailyAlbum.getCuserdate());

        }
        // myViewHolder.answerimg.setImageResource(dailyAlbum.getImgurl());


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DocumentReference documentReference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        TextView answertext;
        ScrollView scrollofans;
        ImageView ansuserimage;
        TextView ansusername,answeruserdate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            answertext = itemView.findViewById(R.id.ansshowtext);
            scrollofans = itemView.findViewById(R.id.scrollofans);
            ansusername = itemView.findViewById(R.id.answer_user_name);
            answeruserdate = itemView.findViewById(R.id.answer_dateofque);
            ansuserimage = itemView.findViewById(R.id.answer_profile_image);
            //            scrollofans.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//
//            });

        }
    }
}