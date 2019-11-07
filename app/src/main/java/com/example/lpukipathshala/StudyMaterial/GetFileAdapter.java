package com.example.lpukipathshala.StudyMaterial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lpukipathshala.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetFileAdapter extends RecyclerView.Adapter<GetFileAdapter.FileViewHolder> {

    private  Context context ;
    private ArrayList<String> arrayList;

    public GetFileAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.getfileview,viewGroup,false);
        FileViewHolder fileViewHolder = new FileViewHolder(view);
        return fileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder fileViewHolder, int i) {
        fileViewHolder.name.setTextColor(Color.BLACK);
        fileViewHolder.name.setBackground(null);
        fileViewHolder.name.setText(arrayList.get(i));
        fileViewHolder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context,Show_Files.class);
            intent.putExtra("name",arrayList.get(i));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FileViewHolder  extends RecyclerView.ViewHolder {
        TextView name;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
//            itemView.setOnClickListener(v -> {
//                Toast.makeText(context, "Goto -> /mnt/sdcard/LpuKiPathshala  folder", Toast.LENGTH_SHORT).show();
//            });
        }
    }
}
