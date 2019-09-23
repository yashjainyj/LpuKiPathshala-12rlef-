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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.Cart.Cart;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.MainActivity;
import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.Myaccount.OurProduct.Product_Sell;
import com.example.lpukipathshala.R;
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
    ProgressDialog progressDialog;
    private CircleImageView circleImageView;
    ImageView back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_details);
        setUpToolbar();
        floatingActionButton = findViewById(R.id.edit);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phonenumber);
        about = findViewById(R.id.about);
        cart = findViewById(R.id.cart);
        sell = findViewById(R.id.sell);
        circleImageView = findViewById(R.id.circleImageView);
        back = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, Cart.class);
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


//


//        if(MyUtility.userDetails!=null)
//        {
//
//            name.setText(MyUtility.userDetails.get(0).getFname() + " " + MyUtility.userDetails.get(0).getLname());
//            location.setText(MyUtility.userDetails.get(0).getLocation());
//            email.setText(MyUtility.userDetails.get(0).getEmail());
//            phone.setText(MyUtility.userDetails.get(0).getPhone());
//            about.setText(MyUtility.userDetails.get(0).getAbout());
//        }




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
                Intent intent = new Intent(AccountDetails.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        myaccount = findViewById(R.id.myaccount);
//        myaccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // FirebaseAuth.getInstance().signOut();
////                Intent intent = new Intent(AccountDetails.this, AccountDetails.class);
////                startActivity(intent);
////               finish();
//            }
//        });
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
            break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mAuth.getUid().equalsIgnoreCase(""))
        {
            progressDialog.setMessage("please wait a while.....");
             progressDialog.show();
//            try {
//
//                File file = File.createTempFile("image","jpg");
//
//                storageReference =storageReference.child("images/user/"+mAuth.getUid()+"/"+mAuth.getUid() +".jpg");
//                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        circleImageView.setImageBitmap(bitmap);
//                        back.setImageBitmap(bitmap);
//                        progressDialog.dismiss();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

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
                    progressDialog.dismiss();
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
