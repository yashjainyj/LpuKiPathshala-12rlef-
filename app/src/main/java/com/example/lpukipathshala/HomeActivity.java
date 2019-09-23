package com.example.lpukipathshala;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;


import com.example.lpukipathshala.Login_Fragment.Login;
import com.example.lpukipathshala.product.ProductGridFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity implements NavigationHost{
    Button logout;
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        if (savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.Frame,new ProductGridFragment()).commit();
        }
    }
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.Frame, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
