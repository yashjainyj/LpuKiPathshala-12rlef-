package com.example.lpukipathshala.Dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.example.lpukipathshala.Dashboard.NavigationHost;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.ProductGridFragment;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements NavigationHost {
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
            getSupportFragmentManager().beginTransaction().add(R.id.Frame,new Dashboard_Fragment()).commit();
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
