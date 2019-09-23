package com.example.lpukipathshala.Category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lpukipathshala.Cart.Cart_Adapter;
import com.example.lpukipathshala.Cart.Cart_Details;
import com.example.lpukipathshala.R;

import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.Category_ViewHolder>  {
    List<Category_Details> details;

    public Category_Adapter(List<Category_Details> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public Category_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_layout,viewGroup,false);
        Category_ViewHolder category_viewHolder=new Category_ViewHolder(view);
        return category_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Category_ViewHolder category_viewHolder, int i) {
        final Category_Details recycleItemAdd=details.get(i);
        category_viewHolder.cname.setText(recycleItemAdd.getCname());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class Category_ViewHolder extends RecyclerView.ViewHolder{
        public TextView cname;

        public Category_ViewHolder(@NonNull View itemView) {
            super(itemView);
           cname = itemView.findViewById(R.id.cname);
        }
    }
}
