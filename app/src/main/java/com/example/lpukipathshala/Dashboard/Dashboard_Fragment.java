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
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.NavigationIconClickListener;
import com.example.lpukipathshala.product.ProductCardRecyclerViewAdapter;
import com.example.lpukipathshala.quoraa.MainActivity;

import java.util.ArrayList;

public class Dashboard_Fragment extends Fragment {
    MaterialButton myaccount,category,Doubts;
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            view.findViewById(R.id.product_grid).setBackground(getContext().getDrawable(R.drawable.shr_product_grid_background_shape));
//        }
        addRecyclerView(view);
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
       // arrayList.add(new Dashboard_Details("More", R.drawable.more));

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

        //toolbar.setNavigationOnClickListener(new NavigationIconClickListener(getContext(), view.findViewById(R.id.product_grid)));
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
                // FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), AccountDetails.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        category = view.findViewById(R.id.categories);
        category.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Category_Main.class);
            startActivity(intent);
            getActivity().finish();
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


    }

}
