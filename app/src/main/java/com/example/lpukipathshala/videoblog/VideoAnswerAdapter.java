package com.example.lpukipathshala.videoblog;

import android.content.Context;
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

public class VideoAnswerAdapter extends RecyclerView.Adapter<com.example.lpukipathshala.videoblog.VideoAnswerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<mAnswerVideoGetSet> mData;


    public VideoAnswerAdapter(Context mContext, ArrayList<mAnswerVideoGetSet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public com.example.lpukipathshala.videoblog.VideoAnswerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.card_of_video_answer,viewGroup,false);
        return new com.example.lpukipathshala.videoblog.VideoAnswerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.lpukipathshala.videoblog.VideoAnswerAdapter.MyViewHolder myViewHolder, int i) {
        mAnswerVideoGetSet dailyAlbum = mData.get(i);
        myViewHolder.documentReference=  myViewHolder.firebaseFirestore.collection("Users").document(dailyAlbum.getUid());
        myViewHolder.documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                myViewHolder.ansusername.setText(userDetails.getFname()+" "+userDetails.getLname());
                myViewHolder.ansusername.setBackground(null);
                myViewHolder.answeruserdate.setBackground(null);
                myViewHolder.ansuserimage.setBackground(null);
                myViewHolder.answervideoview.setBackground(null);
                Glide.with(mContext).load(userDetails.getPic_url()).into(myViewHolder.ansuserimage);
                // Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(back);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        myViewHolder.answertext.setText(dailyAlbum.getaAnswer());
        myViewHolder.answeruserdate.setText(dailyAlbum.getAnsdate());
        if(dailyAlbum.getAvideourl()==null)
        {
            myViewHolder.answervideoview.setVisibility(View.GONE);
        }
        else{
            myViewHolder.answervideoview.setVideoURI(Uri.parse(dailyAlbum.getAvideourl()));
            myViewHolder.answervideoview.seekTo(1);
            myViewHolder.answervideoview.requestFocus();

            myViewHolder.answervideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            myViewHolder.answervideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    myViewHolder.answervideoview.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            MediaController mediaController = new MediaController(mContext);
                            myViewHolder.answervideoview.setMediaController(mediaController);
                            mediaController.setAnchorView(myViewHolder.answervideoview);
                        }
                    });
                }
            });
            myViewHolder.answervideoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
         //   Glide.with(mContext).load(dailyAlbum.getAvideourl());
       /*     myViewHolder.answervideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            myViewHolder.mc = new MediaController(mContext);
                            myViewHolder.answervideoview.setMediaController(myViewHolder.mc);
                            myViewHolder.answervideoview.setVideoURI(Uri.parse(dailyAlbum.getAvideourl()));
                            myViewHolder.answervideoview.requestFocus();
                            myViewHolder.mc.setAnchorView(myViewHolder.answervideoview);
                        }
                    });
                }
            });
            myViewHolder.answervideoview.start();*/

        }
        // myViewHolder.answerimg.setImageResource(dailyAlbum.getImgurl());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DocumentReference documentReference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        TextView answertext;
        FullScreenVideoView answervideoview;
        ScrollView scrollofans;
        MediaController mc;
        ImageView ansuserimage;
        TextView ansusername,answeruserdate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            answervideoview = new FullScreenVideoView(mContext);
            answertext = itemView.findViewById(R.id.ansshowtextoanswer);
            scrollofans = itemView.findViewById(R.id.scrollofans);
            answervideoview = itemView.findViewById(R.id.Answershowvideo);
            ansuserimage = itemView.findViewById(R.id.answer_profile_image);
            ansusername = itemView.findViewById(R.id.answer_user_name);
            answeruserdate = itemView.findViewById(R.id.answer_dateofque);

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