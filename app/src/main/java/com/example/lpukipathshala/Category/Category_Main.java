package com.example.lpukipathshala.Category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.NavigationIconClickListener;

import java.util.ArrayList;
import java.util.List;

public class Category_Main extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        recyclerView = findViewById(R.id.recycler_view);
       // setUpToolbar();
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_Main.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        List<Category_Details> details = new ArrayList<>();
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        details.add(new Category_Details("Science"));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        Category_Adapter adapter = new Category_Adapter(details);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }


    }
