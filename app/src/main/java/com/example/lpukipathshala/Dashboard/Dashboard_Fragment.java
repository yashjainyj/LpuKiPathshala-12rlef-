package com.example.lpukipathshala.Dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.lpukipathshala.Category.Category_Details;
import com.example.lpukipathshala.Category.Category_Main;
import com.example.lpukipathshala.Equipments.EquipmentGrid;
import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.StudyMaterial.StudyMaterial_Main;
import com.example.lpukipathshala.product.NavigationIconClickListener;
import com.example.lpukipathshala.product.ProductCardRecyclerViewAdapter;
import com.example.lpukipathshala.product.ProductGridFragment;
import com.example.lpukipathshala.quoraa.MainActivity;
import com.example.lpukipathshala.videoblog.Video_main_class;

import java.io.File;
import java.util.ArrayList;

public class Dashboard_Fragment extends Fragment {
    MaterialButton myaccount,category,Doubts,Dashboard,Books,Equipments,video,study_material;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.dashboard_fragment,null);
        setUpToolbar(view);
        recyclerView = view.findViewById(R.id.recycler_view);
        addRecyclerView(view);
        File file2 = new File("/mnt/sdcard/LpuKiPathshala");
        try{
            if(!file2.exists()) {
                file2.mkdir();
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private void addRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        ArrayList<Dashboard_Details> arrayList = new ArrayList<>();
        arrayList.add(new Dashboard_Details("Old Books", R.drawable.oldbook));
        arrayList.add(new Dashboard_Details("Equipments", R.drawable.project));
        arrayList.add(new Dashboard_Details("Q&A", R.drawable.qanda));
        arrayList.add(new Dashboard_Details("Video Blog", R.drawable.vblog));
        arrayList.add(new Dashboard_Details("Study Material", R.drawable.study));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position==2)
                    return 2;
                else
                    return 1;

            }
        });
        DashBoard_Adapter dashBoard_adapter = new DashBoard_Adapter(arrayList,getContext());
        recyclerView.setAdapter(dashBoard_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setUpToolbar(final View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.menu),
                getContext().getResources().getDrawable(R.drawable.close)));



        myaccount = view.findViewById(R.id.myaccount);
        myaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountDetails.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        Doubts = view.findViewById(R.id.doubts);
        Doubts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Dashboard = view.findViewById(R.id.dashboard);
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Dashboard.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        Books = view.findViewById(R.id.Books);
        Books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        Equipments = view.findViewById(R.id.equipment);
        Equipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.putExtra("type","Equipment");
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        video = view.findViewById(R.id.videoblog);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Video_main_class.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        study_material = view.findViewById(R.id.study_material);
        study_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StudyMaterial_Main.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });



    }


}
