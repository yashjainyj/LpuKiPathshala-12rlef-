package com.example.lpukipathshala.quoraa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.lpukipathshala.R;

import java.util.ArrayList;
import java.util.List;

public class answeradapter extends RecyclerView.Adapter<answeradapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<mAnswerGetSet> mData;


    public answeradapter(Context mContext, ArrayList<mAnswerGetSet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardofanswer,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        mAnswerGetSet dailyAlbum = mData.get(i);
        myViewHolder.answertext.setText(dailyAlbum.getaAnswer());
       // myViewHolder.answerimg.setImageResource(dailyAlbum.getImgurl());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView answertext;
        ScrollView scrollofans;
        ImageView answerimg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            answerimg = itemView.findViewById(R.id.Answershowimage);
            answertext = itemView.findViewById(R.id.ansshowtext1);
            scrollofans = itemView.findViewById(R.id.scrollofans);

//            scrollofans.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//
//            });
            answertext.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}