package com.example.youtube.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.Contact.PhimLe;
import com.example.youtube.Interface.IOnClickPlayPhimLe;
import com.example.youtube.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPhimLe extends RecyclerView.Adapter<AdapterPhimLe.Viewhoder> {
    Context context;
    ArrayList<PhimLe> phimLes;
    IOnClickPlayPhimLe iOnClickPlayPhimLe;

    public AdapterPhimLe(ArrayList<PhimLe> phimLes) {
        this.phimLes = phimLes;
    }

    public void setiOnClickPlayPhimLe(IOnClickPlayPhimLe iOnClickPlayPhimLe) {
        this.iOnClickPlayPhimLe = iOnClickPlayPhimLe;
    }

    @NonNull
    @Override
    public AdapterPhimLe.Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemphimle,parent,false);
        context = parent.getContext();
        Viewhoder viewhoder = new Viewhoder(view);

        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhimLe.Viewhoder holder, int position) {
        final PhimLe phimLe = phimLes.get(position);

         String img = phimLe.getImg();
         String title = phimLe.getTitle();

         holder.tvPhimLe.setText(title);
         Picasso.with(context).load(img).into(holder.imgPhimLe);
         holder.imgPhimLe.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 iOnClickPlayPhimLe.onClickPlayPhimLe(phimLe);
             }
         });

    }

    @Override
    public int getItemCount() {
        return phimLes.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder{
        ImageView imgPhimLe;
        TextView tvPhimLe;

        public Viewhoder(@NonNull View itemView) {
            super(itemView);
            imgPhimLe = itemView.findViewById(R.id.imgPhimLe);
            tvPhimLe = itemView.findViewById(R.id.tvPhimLe);
        }
    }
}
