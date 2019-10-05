package com.example.lpukipathshala.videoblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lpukipathshala.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class videocommentrecycler extends AppCompatActivity {


    RecyclerView recyclerViewans;
    EditText answertext,ans;
    ProgressDialog progressDialog;
    FloatingActionButton answertouploaddata;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<mComments> list1;
    private static final int PICK_VIDEO_REQUEST = 2;

    String a_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentsrecyclerview);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        a_id = intent.getStringExtra("a_id");
        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewans = findViewById(R.id.answerdetails);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        answertouploaddata = findViewById(R.id.uploadanswer);
        answertext = findViewById(R.id.answer);


        answertext.setScroller(new Scroller(getApplicationContext()));
        answertext.setVerticalScrollBarEnabled(true);

        answertouploaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!answertext.getText().toString().equals("")) {
                    progressDialog.show();
                    progressDialog.setMessage("Please wait a while.....");
                    collectionReference = firebaseFirestore.collection("comments");
                    Date c = Calendar.getInstance().getTime();
                    String answer = answertext.getText().toString();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    mComments answerGetSet = new mComments(firebaseAuth.getUid(),answer,df.format(c),a_id);
                    collectionReference.add(answerGetSet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            getData();
                            mComments answerGetSet = new mComments(firebaseAuth.getUid(),answer,df.format(c),a_id);
                            firebaseFirestore.collection("comments").document(documentReference.getId()).set(answerGetSet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(com.example.lpukipathshala.videoblog.videocommentrecycler.this,"uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
                else {
                    Toast.makeText(com.example.lpukipathshala.videoblog.videocommentrecycler.this, "please write the comments before upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
       /* answertouploaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answertext.getText().toString();
                if (!answertext.getText().toString().equals("")) {
                    progressDialog.show();
                    progressDialog.setMessage("Please wait a while.....");
                    collectionReference = firebaseFirestore.collection("comments");
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    *//*mComments answerGetSet = new mComments(firebaseAuth.getUid(), a_id, answer, "",df.format(c));
                    collectionReference.add(answerGetSet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Toast.makeText(answercmainclass.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            getData();
                            mComments answerGetSet = new mAnswerVideoGetSet(firebaseAuth.getUid(),answer, urlimage,df.format(c));
                            firebaseFirestore.collection("VideoAnswers").document(documentReference.getId()).set(answerGetSet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(com.example.lpukipathshala.videoblog.videocommentrecycler.this,"uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.dismiss();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.lpukipathshala.videoblog.videocommentrecycler.this, "CanNot Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(com.example.lpukipathshala.videoblog.videocommentrecycler.this, "please write the answer or select the image", Toast.LENGTH_SHORT).show();
                }

                answertext.setText("");
                *//*
                }
            }
        }
    }*/
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    public void getData(){
        collectionReference = firebaseFirestore.collection("comments");
        collectionReference.whereEqualTo("cqid",a_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list1 = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    mComments mAnswerGetSet  = queryDocumentSnapshot.toObject(com.example.lpukipathshala.videoblog.mComments.class);
                    //String q_id = queryDocumentSnapshot.getId();
                    list1.add(new mComments(mAnswerGetSet.getUid(),mAnswerGetSet.getNuseranswer(),mAnswerGetSet.getCuserdate(),mAnswerGetSet.getCqid()));
                }
                commentsAdapter answeradapter= new commentsAdapter(videocommentrecycler.this,list1);
                recyclerViewans.setLayoutManager(new GridLayoutManager(com.example.lpukipathshala.videoblog.videocommentrecycler.this,1));
                recyclerViewans.setAdapter(answeradapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerViewans.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}

