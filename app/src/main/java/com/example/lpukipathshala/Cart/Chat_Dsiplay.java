package com.example.lpukipathshala.Cart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.ChatList;
import com.example.lpukipathshala.DataModels.Chat_Data;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.Myaccount.OurProduct.Product_Sell;
import com.example.lpukipathshala.Myaccount.OurProduct.Sell_Adapter;
import com.example.lpukipathshala.Notification.Client;
import com.example.lpukipathshala.Notification.Token;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.Product_Details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Chat_Dsiplay extends AppCompatActivity {
    RecyclerView recyclerView;
    String b_id;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Set<String> set = new HashSet();
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    TextView textView;
    CollectionReference collectionReference;
    String u_id;
    //ArrayList<Chat_Data> list = new ArrayList();
    ArrayList<ChatList> list;
    ArrayList<UserDetails> userlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_display);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat_Dsiplay.this, Product_Sell.class);
                startActivity(intent);
                finish();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = FirebaseFirestore.getInstance().collection("User");
        textView = findViewById(R.id.no);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_view);

        Intent intent = getIntent();
        b_id = intent.getStringExtra("b_id");
        //intent.putStringArrayListExtra("u_ida",list);
//        list.add("Yash");
//        list.add("Sagar");
//        list.add("Ishan");
//        list.add("Vaibhav");

//        getChats();


        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(mAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ChatList chatList = dataSnapshot1.getValue(ChatList.class);
                    list.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());

}

    private void chatList() {
        userlist = new ArrayList<>();
       collectionReference=  firebaseFirestore.collection("Users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userlist.clear();
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    String s = queryDocumentSnapshot.getId();
                    UserDetails userDetails = queryDocumentSnapshot.toObject(UserDetails.class);
                    for(ChatList chatList : list)
                    {
                        if(chatList.getId().equalsIgnoreCase(s))
                        {
                            userlist.add(userDetails);
                        }
                    }
                }
                if(userlist.isEmpty())
                {
                    progressDialog.dismiss();
                    textView.setVisibility(View.VISIBLE);
                }
                else {
                    textView.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Chat_Dsiplay.this);
                    Chat_Display_Adapter adapter = new Chat_Display_Adapter (Chat_Dsiplay.this,userlist);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    progressDialog.dismiss();

                }
            }
        });
    }


    private  void updateToken(String token)
{
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
    Token token1  = new Token(token);
    reference.child(mAuth.getUid()).setValue(token1);
}

//
//    public void getChats()
//    {
//        progressDialog.setMessage("Please wait a while...");
//        progressDialog.show();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                set.clear();
//                list.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    Chat_Data chat_data = dataSnapshot1.getValue(Chat_Data.class);
//                    u_id = chat_data.getSender_id();
//                    if( !chat_data.getSender_id().equalsIgnoreCase(mAuth.getUid()) )
//                    {
//
//                        set.add(chat_data.getSender_id());
//                        // u_id[0] = chat_data.getSender_id();
//
//                    }
//                }
//                for(String s1:set)
//                {
//                    list.add(new Chat_Data(s1));
//                }
//                if(list.isEmpty())
//                {
//                    progressDialog.dismiss();
//                    textView.setVisibility(View.VISIBLE);
//                }
//                else {
//                    textView.setVisibility(View.GONE);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Chat_Dsiplay.this);
//                    Chat_Display_Adapter adapter = new Chat_Display_Adapter (Chat_Dsiplay.this,list);
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setAdapter(adapter);
//                    recyclerView.setHasFixedSize(true);
//                    progressDialog.dismiss();
//                    updateToken(FirebaseInstanceId.getInstance().getToken());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
