package com.example.lpukipathshala.Myaccount.OurProduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.lpukipathshala.DataModels.OurProductDetails;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.Equipments.Add_Equipment_Model;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.Myaccount.OurProduct.Sell_Adapter;
import com.example.lpukipathshala.R;
import com.facebook.shimmer.ShimmerFrameLayout;
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
    Sell_Adapter adapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference,collectionReference1;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Product_Sell.this);
    List<OurProductDetails> list ;
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
        shimmerFrameLayout = findViewById(R.id.shimmer);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        adapter = new Sell_Adapter (Product_Sell.this,list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        collectionReference = firebaseFirestore.collection("BOOKS");
        collectionReference.whereEqualTo("userId",mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    Add_Book_Model add_book_model = queryDocumentSnapshot.toObject(Add_Book_Model.class);
                    list.add(new OurProductDetails(add_book_model.getBookName(),add_book_model.getPrice(),add_book_model.getPicUrl()));

                }
                setLayout();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        collectionReference1= firebaseFirestore.collection("Equipments");
        collectionReference1.whereEqualTo("userId",mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Toast.makeText(Product_Sell.this, "hy", Toast.LENGTH_SHORT).show();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Add_Equipment_Model add_book_model1 = queryDocumentSnapshot.toObject(Add_Equipment_Model.class);
                    list.add(new OurProductDetails(add_book_model1.getEquipmentName(), add_book_model1.getPrice(), add_book_model1.getPicUrl()));
                    Log.i("-------------.>", "onSuccess: " + add_book_model1.getEquipmentName() + " " + add_book_model1.getPicUrl() +  " " + add_book_model1.getPrice());
                }
                setLayout();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Product_Sell.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


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
//
                startActivity(intent);
                break;
        }
        return true;
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


    }

    private void setLayout() {
        if(list.isEmpty() )
        {
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
            textView.setText("No Product");
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Product_Sell.this, AccountDetails.class);
        startActivity(intent);
        finish();
    }
}

