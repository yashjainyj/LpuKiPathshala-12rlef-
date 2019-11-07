package com.example.lpukipathshala.StudyMaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.lpukipathshala.R;

import java.io.File;
import java.util.ArrayList;

public  class Show_Files extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_files);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        String code = intent.getStringExtra("name");
        File file2 = new File("/mnt/sdcard/LpuKiPathshala/" + code);
        File[] files = file2.listFiles();
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
                arrayList.add(files[i].getName());
            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            ShowFile_Adapter getFileAdapter = new ShowFile_Adapter(this,arrayList,code);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(getFileAdapter);
    }
}
