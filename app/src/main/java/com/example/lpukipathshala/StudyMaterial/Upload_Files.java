package com.example.lpukipathshala.StudyMaterial;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lpukipathshala.Dashboard.Dashboard;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.regex.Pattern;

public class Upload_Files extends AppCompatActivity {
    TextInputEditText branch,code;
    TextView filename,label,percentage;
    String selected_branch,Selected_Degree;
    boolean selectebranch[] = new boolean[9];
    private StorageReference storageReference;
    DatabaseReference databaseReference;
    Button upload;
    MaterialButton cancel;
    StorageTask storageTask;
    RelativeLayout relativeLayout;
    ProgressBar progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_m_upload_files);
        setUpToolbar();
        upload=findViewById(R.id.download);
        upload.setText("Upload File");
        branch = findViewById(R.id.branch);
        code = findViewById(R.id.code);
        filename = findViewById(R.id.fileuploadname);
        label = findViewById(R.id.label);
        relativeLayout = findViewById(R.id.relative);
        percentage = findViewById(R.id.percentage);
        cancel = findViewById(R.id.cancel);
        label.setText("");
        percentage.setText("");
        filename.setText("");

        storageReference = FirebaseStorage.getInstance().getReference();
        cancel.setOnClickListener(v->{
            storageTask.cancel();
            progressDialog.setProgress(0);
            label.setText("");
            percentage.setText("");
            filename.setText("");
            relativeLayout.setVisibility(View.GONE);
        });
        progressDialog = findViewById(R.id.progress_horizontal);
        progressDialog.setProgress(0);
        branch.setOnClickListener(v -> setBranch());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        upload.setOnClickListener(v -> {
            new MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.zip$"))
                    .start();
        });
    }
    public  void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_Files.this, StudyMaterial_Main.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Uri uri = Uri.fromFile(new File(filePath));
            if(selected_branch!=null && !code.getText().toString().equalsIgnoreCase(""))
            {
                relativeLayout.setVisibility(View.VISIBLE);
                StorageReference d = storageReference.child("Study Material/"+selected_branch+"/" +code.getText().toString()+".zip");
                d.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uri1 = taskSnapshot.getStorage().getDownloadUrl();
                   while (!uri1.isComplete()){ };
                    databaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial").child(selected_branch).child(code.getText().toString());
                    FileDownload_URL fileDownload_url = new FileDownload_URL(uri1.getResult().toString());
                    databaseReference.setValue(fileDownload_url);
                    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(Upload_Files.this, "uploaded succeessfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.i("Fail", "onActivityResult: " + e);
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double percentage1 = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress((int) percentage1);
                        filename.setText(code.getText().toString());
                        String progrss = taskSnapshot.getBytesTransferred()/1024+"KB / "+taskSnapshot.getTotalByteCount()/1024+"KB";
                        label.setText(progrss);
                        percentage.setText((int)percentage1+"%");
                        storageTask = taskSnapshot.getTask();
                    }
                });

            }
            else
                Toast.makeText(this, "Field is empty", Toast.LENGTH_SHORT).show();
        }
    }


    void setBranch()
    {

        for(int i=0;i<8;i++)
            selectebranch[i]=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Files.this).setTitle("Choose Branch");
        View view1 = getLayoutInflater().inflate(R.layout.select_branch,null);

        EditText edittext[] = {view1.findViewById(R.id.engineering) ,view1.findViewById(R.id.management),view1.findViewById(R.id.agriculture),view1.findViewById(R.id.hotel),view1.findViewById(R.id.biotechnology),view1.findViewById(R.id.law),view1.findViewById(R.id.medicalscience),view1.findViewById(R.id.commerce),view1.findViewById(R.id.other)};
        builder.setView(view1);
        edittext[8].setVisibility(View.GONE);
        builder.setPositiveButton("Select", (dialog, which) -> {
            branch.setText(selected_branch);
            dialog.dismiss();
        });
        builder.setNegativeButton("cancel",null);
        builder.show();
        edittext[0].setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[0]);
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
        });
        edittext[1].setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[1]);
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
        });

        edittext[2].setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[2]);
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
        });
        edittext[3].setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[3]);
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
        });
        edittext[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[4]);
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
                PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[6]);
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
                PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[5]);
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
                PopupMenu popup = new PopupMenu(Upload_Files.this,edittext[7]);
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

}
