package com.example.lpukipathshala.quoraa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lpukipathshala.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class answermoreup extends AppCompatActivity {
    Button muploadimage,muploadqanswer;
    EditText manswer;
    FirebaseAuth mAuth;
    CollectionReference crefernce;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answerofquestionlayout);

        muploadimage = findViewById(R.id.ansimgupload);
        muploadqanswer = findViewById(R.id.ansuploadbutton);
        manswer = findViewById(R.id.ansuserque);

        mAuth = FirebaseAuth.getInstance();
        muploadqanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crefernce = FirebaseFirestore.getInstance().collection("Answers");

            }
        });


    }
}
