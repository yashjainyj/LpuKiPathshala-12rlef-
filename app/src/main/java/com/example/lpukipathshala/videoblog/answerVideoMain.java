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
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.lpukipathshala.R;
import com.example.lpukipathshala.quoraa.answeradapter;
import com.example.lpukipathshala.quoraa.mAnswerGetSet;
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

public class answerVideoMain extends AppCompatActivity {


    RecyclerView recyclerViewans;
    TextView answertext,ans;
    Uri uri;
    String urlimage;
    ProgressDialog progressDialog;
    ImageButton buttontouploadvideo;
    FloatingActionButton answertouploaddata;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<mAnswerVideoGetSet> list1;
    private static final int PICK_VIDEO_REQUEST = 2;

    String q_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answervideomainclass);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        q_id= intent.getStringExtra("q_id");
        storageReference = FirebaseStorage.getInstance().getReference();
        //   Toast.makeText(this, q_id, Toast.LENGTH_SHORT).show();
//
//        list1 = new ArrayList<>();
//        list1.add(new com.example.lpukipathshala.quoraa.answercontent("R.drawable.imageq",""));
//        list1.add(new com.example.lpukipathshala.quoraa.answercontent("R.drawable.imageq","Answer"));
//        list1.add(new com.example.lpukipathshala.quoraa.answercontent("R.drawable.imageq","Answer"));
//        list1.add(new com.example.lpukipathshala.quoraa.answercontent("R.drawable.imageq","Answer"));
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewans = findViewById(R.id.answerdetails_video);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        answertouploaddata = findViewById(R.id.uploadanswerofvideo);
        answertext = findViewById(R.id.answervideo);
        //  ans = findViewById(R.id.ansshowtext1);
        buttontouploadvideo = findViewById(R.id.uploadvideoofans);
        //answermoreupload = findViewById(R.id.answermore);

        answertext.setScroller(new Scroller(getApplicationContext()));
        answertext.setVerticalScrollBarEnabled(true);
        // ansshowtext1.setScroller(new Scroller(getApplicationContext()));
        //ansshowtext1.setVerticalScrollBarEnabled(true);
        /*answermoreupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(answercmainclass.this, com.example.lpukipathshala.quoraa.answermoreup.class);
                startActivity(intent);
            }
        });*/
        buttontouploadvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("video/");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select a video"),PICK_VIDEO_REQUEST);
            }
        });
        answertouploaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answertext.getText().toString();
                if(!answertext.getText().toString().equals("")  ) {
                    progressDialog.show();
                    progressDialog.setMessage("Please wait a while.....");
                    collectionReference = firebaseFirestore.collection("VideoAnswers");
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    mAnswerVideoGetSet answerGetSet = new mAnswerVideoGetSet(firebaseAuth.getUid(), q_id, answer, "",df.format(c));
                    collectionReference.add(answerGetSet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Toast.makeText(answercmainclass.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            getData();
                            StorageReference store = storageReference.child("images/videoanswers/" + documentReference.getId() + ".jpg");
                            if (uri != null) {
                                store.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> uriq = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriq.isComplete()) ;
                                        urlimage = uriq.getResult().toString();
                                        mAnswerVideoGetSet answerGetSet = new mAnswerVideoGetSet(firebaseAuth.getUid(), q_id, answer, urlimage,df.format(c));
                                        firebaseFirestore.collection("VideoAnswers").document(documentReference.getId()).set(answerGetSet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Toast.makeText(com.example.lpukipathshala.videoblog.answerVideoMain.this,"uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            } else {
                                mAnswerVideoGetSet answerGetSet1 = new mAnswerVideoGetSet(firebaseAuth.getUid(), q_id, answer, urlimage,df.format(c));
                                firebaseFirestore.collection("VideoAnswers").document(documentReference.getId()).set(answerGetSet1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        // Toast.makeText(answercmainclass.this, urlimage, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(com.example.lpukipathshala.videoblog.answerVideoMain.this,"uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            progressDialog.dismiss();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.lpukipathshala.videoblog.answerVideoMain.this, "CanNot Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(com.example.lpukipathshala.videoblog.answerVideoMain.this, "please write the answer or select the image", Toast.LENGTH_SHORT).show();
                }

                answertext.setText("");
                buttontouploadvideo.setImageResource(R.drawable.ic_video_box);
            }
        });

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
        collectionReference = firebaseFirestore.collection("VideoAnswers");
        collectionReference.whereEqualTo("quid",q_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list1 = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    mAnswerVideoGetSet mAnswerGetSet  = queryDocumentSnapshot.toObject(com.example.lpukipathshala.videoblog.mAnswerVideoGetSet.class);
                    //String q_id = queryDocumentSnapshot.getId();
                    list1.add(new mAnswerVideoGetSet(mAnswerGetSet.getUid(),mAnswerGetSet.getQuid(),mAnswerGetSet.getaAnswer(),mAnswerGetSet.getAvideourl(),mAnswerGetSet.getAnsdate()));
                }
                VideoAnswerAdapter answeradapter= new VideoAnswerAdapter(answerVideoMain.this,list1);
                recyclerViewans.setLayoutManager(new GridLayoutManager(com.example.lpukipathshala.videoblog.answerVideoMain.this,1));
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK && requestCode == PICK_VIDEO_REQUEST && data!=null){
            uri = data.getData();

        }
    }

    private void showImageChooser(){

        CropImage.activity().start(com.example.lpukipathshala.videoblog.answerVideoMain.this);

    }
}
