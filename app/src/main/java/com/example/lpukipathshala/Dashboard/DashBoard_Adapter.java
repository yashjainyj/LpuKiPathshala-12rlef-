package com.example.lpukipathshala.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lpukipathshala.HomeActivity;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.StudyMaterial.StudyMaterial_Main;
import com.example.lpukipathshala.quoraa.MainActivity;
import com.example.lpukipathshala.videoblog.Video_main_class;

import java.util.List;

public class DashBoard_Adapter extends RecyclerView.Adapter<DashBoard_Adapter.Dashboard_ViewHolder>  {
    List<Dashboard_Details> details;
    private Context context;
    public DashBoard_Adapter(List<Dashboard_Details> details, Context context) {
        this.details = details;
        this.context = context;
    }

    @NonNull
    @Override
    public Dashboard_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_recycler_view_layput,viewGroup,false);
        Dashboard_ViewHolder category_viewHolder=new Dashboard_ViewHolder(view);
        return category_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Dashboard_ViewHolder category_viewHolder, int i) {
        final Dashboard_Details recycleItemAdd=details.get(i);
        category_viewHolder.cname.setText(recycleItemAdd.getName());
        category_viewHolder.imageView.setImageResource(recycleItemAdd.getImage());
        switch (recycleItemAdd.getName())
        {
            case "Old Books" :
                category_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "Equipments" :
                category_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("type","Equipment");
                        context.startActivity(intent);
                    }
                });
                break;
            case "Q&A" :
                category_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "Study Material" :
                category_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, StudyMaterial_Main.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "Video Blog":
                category_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Video_main_class.class);
                        context.startActivity(intent);
                    }
                });
        }

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class Dashboard_ViewHolder extends RecyclerView.ViewHolder{
        public TextView cname;
        public ImageView imageView;

        public Dashboard_ViewHolder(@NonNull View itemView) {
            super(itemView);
            cname = itemView.findViewById(R.id.product_title);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }
}
