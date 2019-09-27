package com.example.lpukipathshala.Myaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.Cart.Cart;
import com.example.lpukipathshala.Cart.Chat_Dsiplay;
import com.example.lpukipathshala.Dashboard.Dashboard;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.MainActivity;
import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.Myaccount.OurProduct.Product_Sell;
import com.example.lpukipathshala.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountDetails extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    TextView name,location,email,phone,about,cart,sell;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;
    StorageReference storageReference;
    ImageView favourite1,sell1,chatdisplay1;
    ProgressDialog progressDialog;
    private CircleImageView circleImageView;
    ScrollView scrollView;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_details);
        setUpToolbar();
        floatingActionButton = findViewById(R.id.edit);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        scrollView = findViewById(R.id.scrollview);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phonenumber);
        about = findViewById(R.id.about);
        cart = findViewById(R.id.cart);
        sell = findViewById(R.id.sell);
        favourite1=findViewById(R.id.favouriteimage);
        sell1 = findViewById(R.id.sellimage);
        chatdisplay1 = findViewById(R.id.chatimage);
        circleImageView = findViewById(R.id.circleImageView);
        back = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        cart.setText("Chats");
        storageReference= FirebaseStorage.getInstance().getReference();
        chatdisplay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, Chat_Dsiplay.class);
                startActivity(intent);
                finish();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, Chat_Dsiplay.class);
                startActivity(intent);
                finish();
            }
        });
        sell1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, Product_Sell.class);
                startActivity(intent);
                finish();
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, Product_Sell.class);
                startActivity(intent);
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this,EditProfile.class);
                startActivity(intent);
            }
        });






    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, Dashboard.class);
                startActivity(intent);
                finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.account_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout :  FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AccountDetails.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
            break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getUid()!=null)
        {
            documentReference=  firebaseFirestore.collection("Users").document(mAuth.getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                    name.setText(userDetails.getFname()+" "+userDetails.getLname());
                    location.setText(userDetails.getLocation());
                    email.setText(userDetails.getEmail());
                    phone.setText(userDetails.getPhone());
                    about.setText(userDetails.getAbout());
                    Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(circleImageView);
                    Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(back);

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AccountDetails.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Intent intent = new Intent(AccountDetails.this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }



    }
}
