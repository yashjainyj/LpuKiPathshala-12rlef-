package com.example.lpukipathshala.Login_Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.MainActivity;
import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;

public class Login extends Fragment implements View.OnClickListener {

    FirebaseAuth mAuth;
    TextInputEditText email,password;
    MaterialButton login;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login,null);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        FirebaseApp.initializeApp(view.getContext());
        mAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(view.getContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait a while.....");
                progressDialog.show();
                loginUser(v);
            }
        });
        return view;
    }

    private void loginUser(final View view) {
        String email1 = email.getText().toString();
        String pass = password.getText().toString();
        if(email1.equalsIgnoreCase("") && pass.equalsIgnoreCase(""))
        {
            Toast.makeText(view.getContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email1, pass)
                    .addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(view.getContext(),HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(view.getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
    @Override
    public void onClick(View v) {

    }
}
