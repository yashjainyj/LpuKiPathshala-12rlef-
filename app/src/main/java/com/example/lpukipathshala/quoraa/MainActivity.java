package com.example.lpukipathshala.quoraa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   RecyclerView recyclerViewque;
    SearchView searchViewque;
    BottomNavigationView bottomNavigationView;
    ArrayList<mQuestionGetSet> list;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
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

       /* Queansweradpter queansweradpter = new Queansweradpter(this,list);
        recyclerViewque.setLayoutManager(new GridLayoutManager(this,1));
        recyclerViewque.setAdapter(queansweradpter);*/

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_songs:
                        Intent intent = new Intent(MainActivity.this,Uploadquestion.class);
                        startActivity(intent);

                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference = firebaseFirestore.collection("Questions");
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
                Queansweradpter queansweradpter = new Queansweradpter(MainActivity.this,list);
                recyclerViewque.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                recyclerViewque.setAdapter(queansweradpter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
