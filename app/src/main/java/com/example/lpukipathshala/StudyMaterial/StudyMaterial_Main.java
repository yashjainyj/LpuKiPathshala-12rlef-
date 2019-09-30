package com.example.lpukipathshala.StudyMaterial;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lpukipathshala.Dashboard.Dashboard;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.Permission;
import java.util.zip.ZipInputStream;

import ir.mahdi.mzip.zip.ZipArchive;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class StudyMaterial_Main extends AppCompatActivity {

    public static int STORAGE_PERMISSION_CODE = 1;
   DatabaseReference databaseReference;
    TextInputEditText branch,code;
    FloatingActionButton upload;
    String selected_branch,Selected_Degree;
    boolean selectebranch[] = new boolean[9];
    Button download;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_material_main);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        setUpToolbar();
        branch=findViewById(R.id.branch);
        branch.setOnClickListener(v -> {
            setBranch();
        });
        code = findViewById(R.id.code);
        download = findViewById(R.id.download);
        upload = findViewById(R.id.uploadfile);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_branch!=null && !code.getText().toString().equalsIgnoreCase(""))
                {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("StudyMaterial/"+selected_branch+"/"+code.getText().toString());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                FileDownload_URL fileDownload_url = dataSnapshot.getValue(FileDownload_URL.class);
                                downloadfile(StudyMaterial_Main.this,code.getText().toString(),".zip", DIRECTORY_DOWNLOADS,fileDownload_url.getUrl());
                                ZipArchive zipArchive = new ZipArchive();
                                zipArchive.unzip(DIRECTORY_DOWNLOADS+"/CSE 101","/sdcard/LpuKiPathshala/","");
                            }
                            else
                                Toast.makeText(StudyMaterial_Main.this, "Sorry We don't have file", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(StudyMaterial_Main.this, "Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyMaterial_Main.this,Upload_Files.class);
                startActivity(intent);

            }
        });
    }


    private void downloadfile(StudyMaterial_Main studyMaterial_main, String code, String extension,String directoryDownloads, String url) {
        DownloadManager downloadManager = (DownloadManager) studyMaterial_main.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request((uri));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(studyMaterial_main,directoryDownloads,code+extension);
        downloadManager.enqueue(request);
    }


    public  void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyMaterial_Main.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void setBranch()
    {
        for(int i=0;i<8;i++)
            selectebranch[i]=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(StudyMaterial_Main.this).setTitle("Choose Branch");
        View view1 = getLayoutInflater().inflate(R.layout.select_branch,null);

        EditText edittext[] = {view1.findViewById(R.id.engineering) ,view1.findViewById(R.id.management),view1.findViewById(R.id.agriculture),view1.findViewById(R.id.hotel),view1.findViewById(R.id.biotechnology),view1.findViewById(R.id.law),view1.findViewById(R.id.medicalscience),view1.findViewById(R.id.commerce),view1.findViewById(R.id.other)};
        builder.setView(view1);
        edittext[8].setVisibility(View.GONE);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                branch.setText(selected_branch);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel",null);
        builder.show();
        edittext[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[0]);
                popup.getMenuInflater()
                        .inflate(R.menu.enginner, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[1]);
                popup.getMenuInflater()
                        .inflate(R.menu.management, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[2]);
                popup.getMenuInflater()
                        .inflate(R.menu.agriculture, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[3]);
                popup.getMenuInflater()
                        .inflate(R.menu.hotel, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[4]);
                popup.getMenuInflater()
                        .inflate(R.menu.biotech, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[6]);
                popup.getMenuInflater()
                        .inflate(R.menu.medicalscience, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[5]);
                popup.getMenuInflater()
                        .inflate(R.menu.law, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                PopupMenu popup = new PopupMenu(StudyMaterial_Main.this,edittext[7]);
                popup.getMenuInflater()
                        .inflate(R.menu.commerce, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(StudyMaterial_Main.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            //Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestForPermission();
        }
    }

    private void requestForPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("Storage Permission needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(StudyMaterial_Main.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
