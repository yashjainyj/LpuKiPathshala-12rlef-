package com.example.lpukipathshala.Equipments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.ProductCardViewHolder;

import java.util.ArrayList;

public class Equipment_Grid_Adapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private ArrayList<Add_Equipment_Model> productList;
    Context context;

    Equipment_Grid_Adapter(Context context, ArrayList<Add_Equipment_Model> productList) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent, false);

        return new ProductCardViewHolder(layoutView);

    }

    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            Add_Equipment_Model product = productList.get(position);
            holder.productTitle.setText(product.getEquipmentName());
            holder.productPrice.setText("Rs." + product.getPrice());
            holder.b_id = product.getEquipmentId();
            holder.u_id = product.getUserId();
            holder.TYPE = product.getType();
            Glide.with(context).load(product.getPicUrl()).into(holder.productImage);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}