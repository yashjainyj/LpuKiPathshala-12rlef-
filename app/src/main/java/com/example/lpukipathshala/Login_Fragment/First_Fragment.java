package com.example.lpukipathshala.Login_Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.Myaccount.EditProfile;
import com.example.lpukipathshala.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueSdkScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class First_Fragment extends Fragment {
    TrueSdkScope trueScope;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_first__fragment, null);
        Button signup = view.findViewById(R.id.signup);
        Button signin = view.findViewById(R.id.signin);
        Button mobile =  view.findViewById(R.id.phone);
        trueScope = new TrueSdkScope.Builder(getContext(), sdkCallback)
                .consentMode(TrueSdkScope.CONSENT_MODE_FULLSCREEN)
                .consentTitleOption(TrueSdkScope.SDK_CONSENT_TITLE_VERIFY)
                .footerType(TrueSdkScope.FOOTER_TYPE_SKIP)
                .build();
        TrueSDK.init(trueScope);
        if(!TrueSDK.getInstance().isUsable())
        {
            mobile.setVisibility(View.INVISIBLE);
        }
        mobile.setOnClickListener(v -> {
            if(TrueSDK.getInstance().isUsable())
            {
                Locale locale = new Locale("en");
                TrueSDK.getInstance().setLocale(locale);
                TrueSDK.getInstance().getUserProfile(First_Fragment.this);
            }
            else {
                Toast.makeText(getContext(), "TrueCaller is not installed in your Phone", Toast.LENGTH_SHORT).show();
            }

        });
        signin.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Login login = new Login();
            fragmentTransaction.replace(R.id.framelayout, login);
            fragmentTransaction.commit();
        });
        signup.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Register register = new Register();
            fragmentTransaction.replace(R.id.framelayout, register);
            fragmentTransaction.commit();
        });
        return view;
    }

    private final ITrueCallback sdkCallback = new ITrueCallback() {

        @Override
        public void onSuccessProfileShared(@NonNull final TrueProfile trueProfile) {
            //Toast.makeText(getContext(), "Hello " + trueProfile.firstName, Toast.LENGTH_SHORT).show();


            List<UserDetails> list = new ArrayList<>();
            list.add(new UserDetails(trueProfile.firstName,trueProfile.lastName,trueProfile.phoneNumber,trueProfile.email,trueProfile.city,"", ""));
            //MyUtility.userDetails = list;
            FirebaseAuth mAuth;
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getContext());
            FirebaseApp.initializeApp(getContext());
            progressDialog.setMessage("please wait a while.....");
            progressDialog.show();

            mAuth =FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(trueProfile.email,trueProfile.phoneNumber).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(getContext(), "Successfull", Toast.LENGTH_SHORT).show();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String phone = trueProfile.phoneNumber.substring(3,13);
                        UserDetails userDetails  =  new UserDetails(trueProfile.firstName,trueProfile.lastName,phone,trueProfile.email,"","", "");
                        firebaseFirestore.collection("Users").document(mAuth.getUid()).set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(getContext(), EditProfile.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        mAuth.signInWithEmailAndPassword(trueProfile.email, trueProfile.phoneNumber)
                                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the si   gned-in user's information
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(getContext(),HomeActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
        }

        @Override
        public void onFailureProfileShared(@NonNull final TrueError trueError) {
            Log.d(TAG, "---------------------------------onFailureProfileShared: " + trueError.getErrorType());
        }

        @Override
        public void onVerificationRequired() {

        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TrueSDK.getInstance().onActivityResultObtained( getActivity(),resultCode, data);
    }
}
