package com.example.lpukipathshala.quoraa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Queansweradpter extends RecyclerView.Adapter<Queansweradpter.MyViewHolder> {

    private Context mContext;
    private List<mQuestionGetSet> mData;


    public Queansweradpter(Context mContext,ArrayList<mQuestionGetSet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardformainpage,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {





        mQuestionGetSet dailyAlbum = mData.get(i);
        myViewHolder.documentReference=  myViewHolder.firebaseFirestore.collection("Users").document(dailyAlbum.getUid());
        myViewHolder.documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                myViewHolder.qusername.setText(userDetails.getFname()+" "+userDetails.getLname());
                myViewHolder.qusername.setBackground(null);
                myViewHolder.queimg.setBackground(null);
                myViewHolder.quedate.setBackground(null);
                myViewHolder.queque.setBackground(null);
                myViewHolder.userimg.setBackground(null);
                myViewHolder.qtype.setBackground(null);
                Glide.with(mContext).load(userDetails.getPic_url()).into(myViewHolder.userimg);
               // Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(back);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        myViewHolder.qtype.setText(dailyAlbum.getQtype());
        myViewHolder.queque.setText(dailyAlbum.getQtitle());
        if(dailyAlbum.getQimgurl()==null)
        {
            myViewHolder.queimg.setVisibility(View.GONE);
        }
        else{
            Glide.with(mContext).load(dailyAlbum.getQimgurl()).into(myViewHolder.queimg);

        }
        //myViewHolder.quename.setText(dailyAlbum.getQueusername());
        myViewHolder.quedate.setText(dailyAlbum.getQdate());
        //myViewHolder.queans.setText(dailyAlbum.getQueanswer());
        //myViewHolder.userimg.setImageResource(dailyAlbum.getUserimg());
        //myViewHolder.queimg.setImageResource(dailyAlbum.getPostimg());
       myViewHolder.buttonofrecycle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mContext, answercmainclass.class);
              // Toast.makeText(mContext, dailyAlbum.getQ_id(), Toast.LENGTH_SHORT).show();
               intent.putExtra("q_id",dailyAlbum.getQ_id());
               mContext.startActivity(intent);
           }
       });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DocumentReference documentReference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        TextView qtype,queque,qusername,quedate,queans;
        ImageView userimg;
        ImageView queimg;
        ImageView buttonofrecycle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            qtype = itemView.findViewById(R.id.typeofque);
            queque = itemView.findViewById(R.id.question);
            qusername = itemView.findViewById(R.id.username);
            quedate = itemView.findViewById(R.id.dateofque);
            queans = itemView.findViewById(R.id.Answers);
          //  queimg = itemView.findViewById(R.id.Imagetopost);
            userimg = itemView.findViewById(R.id.profile_image);
            buttonofrecycle = itemView.findViewById(R.id.moresnswers);

             queimg = itemView.findViewById(R.id.Imagetopost);
        }
    }
}
