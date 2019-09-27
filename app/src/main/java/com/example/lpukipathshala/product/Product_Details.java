package com.example.lpukipathshala.product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.Cart.Cart;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.Equipments.Add_Equipment_Model;
import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.Notification.Token;
import com.example.lpukipathshala.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

public class Product_Details extends AppCompatActivity {
    TextView description,product_name,author_name,price,details;
    FloatingActionButton favourite;
    boolean a = true;
    String b_id,s="",u_id,TYPE;
    ImageView imageView;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    ShimmerFrameLayout shimmerFrameLayout;
    ScrollView scrollView;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    DocumentReference documentReference ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            if(TYPE==null)
            {
                Intent intent = new Intent(Product_Details.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(Product_Details.this, HomeActivity.class);
                intent.putExtra("type",TYPE);
                startActivity(intent);
                finish();
            }

        });
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        shimmerFrameLayout = findViewById(R.id.shimmer);
        scrollView = findViewById(R.id.s);
        Intent getintent = getIntent();
        imageView=findViewById(R.id.product_image);
        b_id = getintent.getStringExtra("b_id");
        TYPE = getintent.getStringExtra("type");
        favourite = findViewById(R.id.favourite);

        favourite.setOnClickListener(v -> {
            if(a)
            {
                favourite.setImageResource(R.drawable.like);
                a=false;
            }
            else
            {
                favourite.setImageResource(R.drawable.like1);
                a=true;
            }
        });
        description = findViewById(R.id.description);
        product_name = findViewById(R.id.productname);
        author_name = findViewById(R.id.authorname);
        price = findViewById(R.id.product_price);
        details = findViewById(R.id.details);
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
        if(TYPE==null)
        {
            collectionReference = firebaseFirestore.collection("BOOKS");
            collectionReference.whereEqualTo("bookId",b_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                    {
                        Add_Book_Model add_book_model= queryDocumentSnapshot.toObject(Add_Book_Model.class);
                        product_name.setText(add_book_model.getBookName());
                        author_name.setText(add_book_model.getAuthorName());
                        price.setText("Rs."+add_book_model.getPrice());
                        u_id = add_book_model.getUserId();
                        Glide.with(Product_Details.this).load(add_book_model.getPicUrl()).into(imageView);
                        s = add_book_model.getDescription()+"\n\n";
                        description.setText(s);
                        //  Toast.makeText(Product_Details.this, add_book_model.getUserId(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(Product_Details.this, u_id, Toast.LENGTH_SHORT).show();
                    documentReference=  firebaseFirestore.collection("Users").document(u_id);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                            s="Seller Contact Details\n\n";
                            s=s+"Name      : "+userDetails.getFname() + " " + userDetails.getLname()+"\n";
                            s=s+"Email       : "+userDetails.getEmail()+"\n";
                            s=s+"Phone      : "+userDetails.getPhone()+"\n";
                            s=s+"Location  : "+userDetails.getLocation()+"\n";
                            details.setText(s);

                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                            // Toast.makeText(Product_Details.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Product_Details.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.i("sadad", "onFailure: ---------------------------------------------------------------");
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
        else if (TYPE.equalsIgnoreCase(MyUtility.TYPE))
        {
            //Toast.makeText(this, b_id, Toast.LENGTH_SHORT).show();
            collectionReference = firebaseFirestore.collection("Equipments");
            collectionReference.whereEqualTo("equipmentId",b_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                    {
                        Add_Equipment_Model add_book_model= queryDocumentSnapshot.toObject(Add_Equipment_Model.class);
                        product_name.setText(add_book_model.getEquipmentName());
                        author_name.setVisibility(View.GONE);
                        price.setText("Rs."+add_book_model.getPrice());
                        u_id = add_book_model.getUserId();
                        Glide.with(Product_Details.this).load(add_book_model.getPicUrl()).into(imageView);
                        s = add_book_model.getDescription()+"\n\n";
                        description.setText(s);
                        //  Toast.makeText(Product_Details.this, add_book_model.getUserId(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(Product_Details.this, u_id, Toast.LENGTH_SHORT).show();
                    documentReference=  firebaseFirestore.collection("Users").document(u_id);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                            s="Seller Contact Details\n\n";
                            s=s+"Name      : "+userDetails.getFname() + " " + userDetails.getLname()+"\n";
                            s=s+"Email       : "+userDetails.getEmail()+"\n";
                            s=s+"Phone      : "+userDetails.getPhone()+"\n";
                            s=s+"Location  : "+userDetails.getLocation()+"\n";
                            details.setText(s);
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                            // Toast.makeText(Product_Details.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Product_Details.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.i("sadad", "onFailure: ---------------------------------------------------------------");
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }


    private  void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1  = new Token(token);
        reference.child(mAuth.getUid()).setValue(token1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.chat :
                Intent intent = new Intent(Product_Details.this,Cart.class);
                intent.putExtra("u_id",u_id);
                intent.putExtra("b_id",b_id);
                startActivity(intent);
                break;
        }
        return true;
    }
}
