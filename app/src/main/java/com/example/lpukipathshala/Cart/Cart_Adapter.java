package com.example.lpukipathshala.Cart;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lpukipathshala.DataModels.Chat_Data;
import com.example.lpukipathshala.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartViewHolder> {

    public static final int LEFT_VIEW=0;
    public static final int RIGHT_VIEW=1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Chat_Data> details;
    public Cart_Adapter(List<Chat_Data> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       if(i==RIGHT_VIEW)
       {
           View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_message,viewGroup,false);
           return new CartViewHolder(view);
       }
       else
       {
           View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_message,viewGroup,false);
           return new CartViewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        final Chat_Data recycleItemAdd=details.get(i);
      cartViewHolder.showmessage.setText(recycleItemAdd.getShow_message());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
       TextView showmessage;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
           showmessage = itemView.findViewById(R.id.left);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mAuth.getUid().toString().equalsIgnoreCase(details.get(position).getSender_id()))
        {
            return RIGHT_VIEW;
        }
        else
            return LEFT_VIEW;
    }
}
