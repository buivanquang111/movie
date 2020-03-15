package com.example.youtube.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.Contact.DeXuat;
import com.example.youtube.Interface.IOnClickPlayDeXuat;
import com.example.youtube.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDeXuat extends RecyclerView.Adapter<AdapterDeXuat.Viewhoder> {
    Context context;
    ArrayList<DeXuat> deXuats;
    IOnClickPlayDeXuat iOnClickPlayDeXuat;

    public AdapterDeXuat(ArrayList<DeXuat> deXuats) {
        this.deXuats = deXuats;
    }

    public void setiOnClickPlayDeXuat(IOnClickPlayDeXuat iOnClickPlayDeXuat) {
        this.iOnClickPlayDeXuat = iOnClickPlayDeXuat;
    }

    @NonNull
    @Override
    public AdapterDeXuat.Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemdexuat,parent,false);
        context = parent.getContext();
        Viewhoder viewhoder = new Viewhoder(view);

        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDeXuat.Viewhoder holder, int position) {
        final DeXuat deXuat=deXuats.get(position);

        String imgurl = deXuat.getImg();
        String tvurl = deXuat.getText();

        holder.tvItemDeXuat.setText(tvurl);
        Picasso.with(context).load(imgurl).into(holder.imgItemDeXuat);
        holder.imgItemDeXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickPlayDeXuat.onClickPlayDeXuat(deXuat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deXuats.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder {
        public ImageView imgItemDeXuat;
        public TextView tvItemDeXuat;

        public Viewhoder(@NonNull View itemView) {
            super(itemView);
            imgItemDeXuat = itemView.findViewById(R.id.imgItemDeXuat);
            tvItemDeXuat = itemView.findViewById(R.id.tvItemDeXuat);
        }
    }
}
