package com.example.lpukipathshala.StudyMaterial;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.lpukipathshala.R;
import java.io.File;
import java.util.ArrayList;
public class ShowFile_Adapter extends RecyclerView.Adapter<ShowFile_Adapter.FileViewHolder> {
        private Context context ;
        private ArrayList<String> arrayList;
        private String code;
        public ShowFile_Adapter(Context context, ArrayList<String> arrayList, String code) {
            this.context = context;
            this.arrayList = arrayList;
            this.code = code;
        }
        @NonNull
        @Override
        public ShowFile_Adapter.FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
                File file = new File("/mnt/sdcard/LpuKiPathshala/"+code+"/"+arrayList.get(i));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(Intent.createChooser(intent, "Select"));
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