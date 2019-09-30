package com.example.lpukipathshala.product;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.R;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {

    public TextView productTitle;
    public TextView productPrice;
    public ImageView productImage;
    public String b_id;
    public String u_id;
    public String TYPE;

    public ProductCardViewHolder(@NonNull View itemView) {
        super(itemView);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.product_price);
        productImage = itemView.findViewById(R.id.product_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TYPE==null)
                {
                    Intent intent = new Intent(itemView.getContext(),Product_Details.class);
                    intent.putExtra("b_id",b_id);
                    itemView.getContext().startActivity(intent);
                }
                else if(TYPE.equalsIgnoreCase(MyUtility.TYPE))
                {
                    Intent intent = new Intent(itemView.getContext(),Product_Details.class);
                    intent.putExtra("b_id",b_id);
                    intent.putExtra("type",TYPE);
                    itemView.getContext().startActivity(intent);
                }

            }
        });
    }
}
