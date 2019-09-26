package com.example.lpukipathshala.Cart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.DataModels.ChatList;
import com.example.lpukipathshala.DataModels.Chat_Data;
import com.example.lpukipathshala.DataModels.UserDetails;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.ProductCardRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.LayoutInflater.from;

public class Chat_Display_Adapter extends RecyclerView.Adapter<Chat_Display_Adapter.chatViewHolder> {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Context context;
    List<UserDetails> details;
    public Chat_Display_Adapter(Context context,List<UserDetails> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public chatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= from(viewGroup.getContext()).inflate(R.layout.chat_display_layout,viewGroup,false);
            return new Chat_Display_Adapter.chatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Display_Adapter.chatViewHolder cartViewHolder, int i) {
        UserDetails chat_data = details.get(i);
        cartViewHolder.name.setText(chat_data.getFname()+" "+chat_data.getLname());
               Glide.with(context).load(chat_data.getPic_url()).into(cartViewHolder.imageView);
//        cartViewHolder.documentReference=  cartViewHolder.firebaseFirestore.collection("Users").document(chat_data.getSender_id());
//        cartViewHolder.documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
//                cartViewHolder.name.setText(userDetails.getFname()+" "+userDetails.getLname());
//                Glide.with(context).load(userDetails.getPic_url()).into(cartViewHolder.imageView);
//                cartViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage("Contach Info");
//                        View view = LayoutInflater.from(context).inflate(R.layout.reciver_profile_details,null);
//                        builder.setView(view);
//                        ImageView imageView = view.findViewById(R.id.image_reciver);
//                        TextInputEditText name = view.findViewById(R.id.name);
//                        TextInputEditText number = view.findViewById(R.id.phonenumber);
//                        if (userDetails.getPic_url()!=null)
//                        Glide.with(context).load(userDetails.getPic_url()).into(imageView);
//                        name.setText(userDetails.getFname()+" "+userDetails.getLname());
//                        number.setText(userDetails.getPhone());
//                        builder.show();
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
       // cartViewHolder.name.setText(chat_data.getSender_id());
       cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               cartViewHolder.collectionReference = cartViewHolder.firebaseFirestore.collection("Users");
               cartViewHolder.collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       String u_id ="";
                       for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                       {
                           UserDetails productEntry = queryDocumentSnapshots1.toObject(UserDetails.class);
                           if(productEntry.getEmail().equalsIgnoreCase(chat_data.getEmail()))
                           {
                              u_id = queryDocumentSnapshots1.getId();
                           }
                       }
                       Intent intent = new Intent(context,Cart.class);
                       intent.putExtra("u_id",u_id);
                       context.startActivity(intent);
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
//                       Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                       Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
                   }
               });

           }
       });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class chatViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView imageView;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        CollectionReference collectionReference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.profile);

        }
    }


}

