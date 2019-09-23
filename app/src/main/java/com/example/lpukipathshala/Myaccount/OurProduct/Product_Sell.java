package com.example.lpukipathshala.Myaccount.OurProduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lpukipathshala.Cart.Cart;
import com.example.lpukipathshala.Cart.Cart_Adapter;
import com.example.lpukipathshala.Cart.Cart_Details;
import com.example.lpukipathshala.Cart.Chat_Dsiplay;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.Myaccount.OurProduct.Sell_Adapter;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Product_Sell extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);
        recyclerView = findViewById(R.id.recycler_view);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.no);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_Sell.this, AccountDetails.class);
                startActivity(intent);
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.chat :
                Intent intent = new Intent(Product_Sell.this,Chat_Dsiplay.class);
//                intent.putExtra("u_id",u_id);
//                intent.putExtra("b_id",b_id);
                startActivity(intent);
                break;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        collectionReference = firebaseFirestore.collection("BOOKS");
        List<Add_Book_Model> list = new ArrayList<>();
        progressDialog.setMessage("please wait a while.....");
        progressDialog.show();
        collectionReference.whereEqualTo("userId",mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    Add_Book_Model add_book_model = queryDocumentSnapshot.toObject(Add_Book_Model.class);
                    list.add(new Add_Book_Model(add_book_model.getBookName(),add_book_model.getPrice(),add_book_model.getPicUrl(),add_book_model.getBookId()));

                }
                if(list.isEmpty())
                {
                    progressDialog.dismiss();
                    textView.setVisibility(View.VISIBLE);
                }
                else
                {
                    textView.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Product_Sell.this);
                    Sell_Adapter adapter = new Sell_Adapter (Product_Sell.this,list);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
