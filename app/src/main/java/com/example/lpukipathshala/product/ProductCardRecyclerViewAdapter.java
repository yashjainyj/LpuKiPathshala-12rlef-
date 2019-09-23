package com.example.lpukipathshala.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.R;

import java.util.ArrayList;

/**
* Adapter used to show a simple grid of products.
*/
public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

   private ArrayList<Add_Book_Model> productList;
Context context;

   ProductCardRecyclerViewAdapter(Context context,ArrayList<Add_Book_Model> productList) {
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
            Add_Book_Model product = productList.get(position);
            holder.productTitle.setText(product.getBookName());
            holder.productPrice.setText(product.getPrice());
            holder.b_id = product.getBookId();
            holder.u_id = product.getUserId();
            Glide.with(context).load(product.getPicUrl()).into(holder.productImage);
        }
    }

   @Override
   public int getItemCount() {
       return productList.size();
   }
}