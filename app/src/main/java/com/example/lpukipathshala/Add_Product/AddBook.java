package com.example.lpukipathshala.Add_Product;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.Myaccount.EditProfile;
import com.example.lpukipathshala.Myaccount.OurProduct.Product_Sell;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBook extends AppCompatActivity {
    private TextInputEditText bname,bauthor,bEdition,branch,desc,price;
    private String selected_edition,selected_branch,Selected_Degree;
    Button add;
    boolean selectedition[]= new boolean[8];
    boolean selectebranch[]= new boolean[9];
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference ;
    ProgressDialog progressDialog;
    private Uri uriProfileImage;
    Uri  profileUrl;
    private StorageReference storageReference;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        FirebaseApp.initializeApp(this);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBook.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bname = findViewById(R.id.bname);
        bauthor = findViewById(R.id.bauthor);
        bEdition = findViewById(R.id.select_edition);
        branch = findViewById(R.id.branch);
        desc = findViewById(R.id.desc);
        price = findViewById(R.id.price);
        mAuth = FirebaseAuth.getInstance();
        circleImageView = findViewById(R.id.circleImageView);
        add= findViewById(R.id.btn_add_book);
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
        desc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() ==R.id.desc) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(bname.getText()) && !TextUtils.isEmpty(bauthor.getText()) && !TextUtils.isEmpty(desc.getText()) && !TextUtils.isEmpty(bEdition.getText()) && !TextUtils.isEmpty(branch.getText())&& !TextUtils.isEmpty(price.getText()))
                {
                    progressDialog.setMessage("please wait a while.....");
                    progressDialog.show();
                    if(uriProfileImage!=null) {
                        StorageReference d = storageReference.child("images/book/"+mAuth.getUid()+bname.getText().toString()+".jpg");
                        d.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                profileUrl = uri.getResult();
//                                profileUrl = taskSnapshot.getStorage().getDownloadUrl().getResult().toString();
                                //Toast.makeText(AddBook.this, "Upload Success" + profileUrl.toString(), Toast.LENGTH_SHORT).show();
                                collectionReference = firebaseFirestore.collection("BOOKS");
                                String b_id = mAuth.getUid()+bname.getText().toString();
                                Add_Book_Model add_book_model = new Add_Book_Model(mAuth.getUid(),b_id,bname.getText().toString(),bauthor.getText().toString(),bEdition.getText().toString(),branch.getText().toString(),desc.getText().toString(),price.getText().toString(),profileUrl.toString());
                                collectionReference.add(add_book_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddBook.this, "Book Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddBook.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddBook.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(AddBook.this, "Please Select Image", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(AddBook.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<8;i++)
                    selectedition[i]=false;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBook.this).setTitle("Choose Edition");
                View view = getLayoutInflater().inflate(R.layout.select_edition,null);

                EditText edittext[] = {view.findViewById(R.id.edition1) ,view.findViewById(R.id.edition2),view.findViewById(R.id.edition3),view.findViewById(R.id.edition4),view.findViewById(R.id.edition5),view.findViewById(R.id.edition6),view.findViewById(R.id.edition7),view.findViewById(R.id.edition8)};
                builder.setView(view);
                builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bEdition.setText(selected_edition);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel",null);


                builder.show();
                edittext[0].setOnClickListener(v1 -> {
                    if(selectedition[0]==false)
                    {
                        selectedition[0] = true;
                        edittext[0].setCursorVisible(false);
                        selected_edition = "Edition 1";
                        edittext[0].setBackground(getResources().getDrawable(R.drawable.asscent));
                        edittext[0].setHintTextColor(Color.BLACK);
                    }
                    else
                    {
                        selectedition[0] = false;
                        selected_edition = "";
                        edittext[0].setCursorVisible(false);
                        edittext[0].setBackground(getResources().getDrawable(R.drawable.profile));
                        edittext[0].setHintTextColor(Color.GRAY);
                    }
                    for(int i=1;i<8;i++)
                    {
                        selectedition[i]=false;
                        edittext[i].setCursorVisible(false);
                        edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                        edittext[i].setHintTextColor(Color.GRAY);
                    }
                });
                edittext[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[1]==false)
                        {
                            selectedition[1] = true;
                            edittext[1].setCursorVisible(false);
                            selected_edition = "Edition 2";
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[1].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[1] = false;
                            selected_edition = "";
                            edittext[1].setCursorVisible(false);
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[1].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==1)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[2]==false)
                        {
                            selectedition[2] = true;
                            edittext[2].setCursorVisible(false);
                            selected_edition = "Edition 3";
                            edittext[2].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[2].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[2] = false;
                            selected_edition = "";
                            edittext[2].setCursorVisible(false);
                            edittext[2].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[2].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==2)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[3]==false)
                        {
                            selectedition[3] = true;
                            edittext[3].setCursorVisible(false);
                            selected_edition = "Edition 4";
                            edittext[3].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[3].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[3] = false;
                            selected_edition = "";
                            edittext[3].setCursorVisible(false);
                            edittext[3].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[3].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==3)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[4]==false)
                        {
                            selectedition[4] = true;
                            edittext[4].setCursorVisible(false);
                            selected_edition = "Edition 5";
                            edittext[4].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[4].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[4] = false;
                            selected_edition = "";
                            edittext[4].setCursorVisible(false);
                            edittext[4].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[4].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==4)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[1]==false)
                        {
                            selectedition[1] = true;
                            edittext[1].setCursorVisible(false);
                            selected_edition = "Edition 2";
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[1].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[1] = false;
                            selected_edition = "";
                            edittext[1].setCursorVisible(false);
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[1].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==1)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[6].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[6]==false)
                        {
                            selectedition[6] = true;
                            edittext[6].setCursorVisible(false);
                            selected_edition = "Edition 7";
                            edittext[6].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[6].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[6] = false;
                            selected_edition = "";
                            edittext[6].setCursorVisible(false);
                            edittext[6].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[6].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==6)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[5].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[5]==false)
                        {
                            selectedition[5] = true;
                            edittext[5].setCursorVisible(false);
                            selected_edition = "Edition 6";
                            edittext[5].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[5].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[5] = false;
                            selected_edition = "";
                            edittext[5].setCursorVisible(false);
                            edittext[5].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[5].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==5)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[7].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedition[7]==false)
                        {
                            selectedition[7] = true;
                            edittext[7].setCursorVisible(false);
                            selected_edition = "Edition 8";
                            edittext[7].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[7].setHintTextColor(Color.BLACK);
                        }
                        else
                        {
                            selectedition[7] = false;
                            selected_edition = "";
                            edittext[7].setCursorVisible(false);
                            edittext[7].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[7].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<8;i++)
                        {
                            if(i==7)
                                continue;
                            selectedition[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

            }
        });


        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<8;i++)
                    selectebranch[i]=false;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBook.this).setTitle("Choose Branch");
                View view = getLayoutInflater().inflate(R.layout.select_branch,null);

                EditText edittext[] = {view.findViewById(R.id.engineering) ,view.findViewById(R.id.management),view.findViewById(R.id.agriculture),view.findViewById(R.id.hotel),view.findViewById(R.id.biotechnology),view.findViewById(R.id.law),view.findViewById(R.id.medicalscience),view.findViewById(R.id.commerce),view.findViewById(R.id.other)};
                builder.setView(view);
                builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!edittext[8].getText().toString().equalsIgnoreCase("")) {
                            selected_branch = edittext[8].getText().toString();
                            //Selected_Degree = "" + selected_branch;
                            edittext[8].setText(selected_branch);


                        }
                        branch.setText(selected_branch);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
                edittext[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[0]);
                        popup.getMenuInflater()
                                .inflate(R.menu.enginner, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[0].getHint().toString();
                                edittext[0].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[0]==false)
                        {
                            selectebranch[0] = true;
                            edittext[0].setCursorVisible(false);
                            edittext[0].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[0].setHintTextColor(Color.BLACK);

                        }
                        for(int i=1;i<9;i++)
                        {
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[1]);
                        popup.getMenuInflater()
                                .inflate(R.menu.management, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[1].getHint().toString();
                                edittext[1].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[1]==false)
                        {
                            selectebranch[1] = true;
                            edittext[1].setCursorVisible(false);
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[1].setHintTextColor(Color.BLACK);
                        }
                        for(int i=0;i<9;i++)
                        {
                            if(i==1)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setText("");
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[2]);
                        popup.getMenuInflater()
                                .inflate(R.menu.agriculture, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                edittext[2].setText(selected_branch);
                                Selected_Degree = edittext[2].getHint().toString();
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[2]==false)
                        {
                            selectebranch[2] = true;
                            edittext[2].setCursorVisible(false);
                            edittext[2].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[2].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==2)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[3]);
                        popup.getMenuInflater()
                                .inflate(R.menu.hotel, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[3].getHint().toString();
                                edittext[3].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[3]==false)
                        {
                            selectebranch[3] = true;
                            edittext[3].setCursorVisible(false);
                            edittext[3].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[3].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==3)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[4]);
                        popup.getMenuInflater()
                                .inflate(R.menu.biotech, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                edittext[4].setText(selected_branch);
                                Selected_Degree = edittext[4].getHint().toString();
                                return true;
                            }
                        });
                        popup.show();

                        if(selectebranch[4]==false)
                        {
                            selectebranch[4] = true;
                            edittext[4].setCursorVisible(false);
                            edittext[4].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[4].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==4)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[6].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[6]);
                        popup.getMenuInflater()
                                .inflate(R.menu.medicalscience, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[6].getHint().toString();
                                edittext[6].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[6]==false)
                        {
                            selectebranch[6] = true;
                            edittext[6].setCursorVisible(false);
                            edittext[6].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[6].setHintTextColor(Color.BLACK);
                        }
                        for(int i=0;i<9;i++)
                        {
                            if(i==6)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[5].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[5]);
                        popup.getMenuInflater()
                                .inflate(R.menu.law, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[5].getHint().toString();
                                edittext[5].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[5]==false)
                        {
                            selectebranch[5] = true;
                            edittext[5].setCursorVisible(false);
                            edittext[5].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[5].setHintTextColor(Color.BLACK);
                        }
                        for(int i=0;i<9;i++)
                        {
                            if(i==5)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[7].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(AddBook.this,edittext[7]);
                        popup.getMenuInflater()
                                .inflate(R.menu.commerce, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[7].getHint().toString();
                                edittext[7].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[7]==false)
                        {
                            selectebranch[7] = true;
                            edittext[7].setCursorVisible(false);
                            edittext[7].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[7].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==7)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[8].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectebranch[8] == false) {
                            selectebranch[8] = true;
                            edittext[8].setCursorVisible(true);
                            edittext[8].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[8].setHintTextColor(Color.GRAY);
                        }
                        else {
                            selectebranch[8] = false;
                            edittext[8].setCursorVisible(true);
                            edittext[8].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[8].setHintTextColor(Color.GRAY);
                        }
                        for (int i = 0; i < 9; i++) {
                            if (i == 8)
                                continue;
                            selectebranch[i] = false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            uriProfileImage = result.getUri();
            circleImageView.setImageURI(resultUri);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }
    private void showImageChooser() {
        CropImage.activity().start(AddBook.this);
    }



}
