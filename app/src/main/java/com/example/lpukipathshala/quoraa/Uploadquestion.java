package com.example.lpukipathshala.quoraa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;

public class Uploadquestion extends AppCompatActivity {

    Button muploadque;
    Uri uri;
    CircleImageView muploadimage;
    EditText mqtype,mqquestion;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    String urlimage;
    CollectionReference crefernce;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formofque);

        muploadimage = findViewById(R.id.qimgupload);
        muploadque = findViewById(R.id.queuploadbutton);
        mqtype = findViewById(R.id.quetypeform);
        mqquestion = findViewById(R.id.queuserque1);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        muploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
        muploadque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mqtype.getText().toString().equals("") && !mqquestion.getText().toString().equals("")) {
                    progressDialog.setMessage("Please Wait a while...");
                    progressDialog.show();



                            crefernce = firebaseFirestore.collection("Questions");
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                    mQuestionGetSet questionGetSet = new mQuestionGetSet(mAuth.getUid(), mqquestion.getText().toString(), mqtype.getText().toString(), "", df.format(c));

                    crefernce.add(questionGetSet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Toast.makeText(Uploadquestion.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            StorageReference store = storageReference.child("images/questions"+documentReference.getId()+".jpg");
                            store.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    urlimage = taskSnapshot.getStorage().getDownloadUrl().toString();
                                    mQuestionGetSet questionGetSet = new mQuestionGetSet(mAuth.getUid(), mqquestion.getText().toString(), mqtype.getText().toString(), urlimage, df.format(c));
                                    firebaseFirestore.collection("Questions").document(documentReference.getId()).set(questionGetSet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Uploadquestion.this, "", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Uploadquestion.this, "CanNot Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(Uploadquestion.this, "Please fill the question type and question", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK){
            uri = result.getUri();
            muploadimage.setImageURI(uri);
        }
    }

    private void showImageChooser(){

        CropImage.activity().start(Uploadquestion.this);

    }
}
