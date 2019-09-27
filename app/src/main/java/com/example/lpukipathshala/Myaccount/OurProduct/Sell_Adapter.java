package com.example.lpukipathshala.Myaccount.OurProduct;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.DataModels.OurProductDetails;
import com.example.lpukipathshala.Equipments.Add_Equipment_Model;
import com.example.lpukipathshala.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class Sell_Adapter extends RecyclerView.Adapter<Sell_Adapter.CartViewHolder> {

    List<OurProductDetails> details;
    Context context;
    public Sell_Adapter(Context context, List<OurProductDetails> details) {

        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_layout,viewGroup,false);
        CartViewHolder cartViewHolder=new CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
            final OurProductDetails recycleItemAdd=details.get(i);
            cartViewHolder.product_name.setBackground(null);
            cartViewHolder.product_price.setBackground(null);
            cartViewHolder.product_name.setText(recycleItemAdd.getName());
            cartViewHolder.product_price.setText(recycleItemAdd.getPrice());
            cartViewHolder.delete.setBackground(null);
            cartViewHolder.delete.setImageResource(R.drawable.delete);
            Glide.with(context).load(recycleItemAdd.getPicUrl()).into(cartViewHolder.product_image);
    }

    @Override
    public int getItemCount() {

        return details.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        public ImageView product_image;
        public TextView product_name,product_price;
        public ImageButton delete;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_price = itemView.findViewById(R.id.product_price);
            product_name = itemView.findViewById(R.id.productname);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
