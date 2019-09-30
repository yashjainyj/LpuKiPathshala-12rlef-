package com.example.lpukipathshala.videoblog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.lpukipathshala.R;
import com.example.lpukipathshala.quoraa.Queansweradpter;
import com.example.lpukipathshala.quoraa.Uploadquestion;
import com.example.lpukipathshala.quoraa.mQuestionGetSet;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Video_main_class extends AppCompatActivity {

    RecyclerView recyclerViewque;
    SearchView searchViewque;
    FloatingActionButton bottomNavigationView;
    ArrayList<mQuestionGetSet> list;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    ShimmerFrameLayout shimmerFrameLayout;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        /*list.add(new Demoquecontent("Maths","What is Addition in maths?",R.drawable.ic_local_post_office_black_24dp,
                R.drawable.queuser,"Prem Narayan","8sep2019",
                "It is used to add the two number and get the sum"));
        list.add(new Demoquecontent("Maths","What is Addition in maths?",R.drawable.ic_local_post_office_black_24dp,
                R.drawable.queuser,"Prem Narayan","8sep2019",
                "It is used to add the two number and get the sum"));
        list.add(new Demoquecontent("Maths","What is Addition in maths?",R.drawable.ic_local_post_office_black_24dp,
                R.drawable.queuser,"Prem Narayan","8sep2019",
                "It is used to add the two number and get the sum"));
        list.add(new Demoquecontent("Maths","What is Addition in maths?",R.drawable.ic_local_post_office_black_24dp,
                R.drawable.queuser,"Prem Narayan","8sep2019",
                "It is used to add the two number and get the sum"));*/


        recyclerViewque = findViewById(R.id.recycler_bloquery);
        bottomNavigationView = findViewById(R.id.bottomnavque);
        shimmerFrameLayout = findViewById(R.id.shimmer);
       /* Queansweradpter queansweradpter = new Queansweradpter(this,list);
        recyclerViewque.setLayoutManager(new GridLayoutManager(this,1));
        recyclerViewque.setAdapter(queansweradpter);*/

        /*bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_songs:
                        Intent intent = new Intent(MainActivity.this,Uploadquestion.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_artists:{
                        Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    }
                }
                return true;
            }
        });*/
        bottomNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video_main_class.this, Uploadvideoquestion.class);
                startActivity(intent);
            }
        });

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
    @Override
    protected void onStart() {
        super.onStart();
        collectionReference = firebaseFirestore.collection("VideoQuestions");
        list = new ArrayList<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    mQuestionGetSet mQuestionGetSet  = queryDocumentSnapshot.toObject(com.example.lpukipathshala.quoraa.mQuestionGetSet.class);
                    String q_id = queryDocumentSnapshot.getId();
                    list.add(new mQuestionGetSet(mQuestionGetSet.getUid(),mQuestionGetSet.getQtitle(),mQuestionGetSet.getQtype(),mQuestionGetSet.getQimgurl(),mQuestionGetSet.getQdate(),q_id));
                }
                Queansweradpter queansweradpter = new Queansweradpter(Video_main_class.this,list);
                recyclerViewque.setLayoutManager(new GridLayoutManager(Video_main_class.this,1));
                recyclerViewque.setAdapter(queansweradpter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerViewque.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}
