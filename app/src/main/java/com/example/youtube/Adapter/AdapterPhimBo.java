package com.example.youtube.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.Contact.PhimBo;
import com.example.youtube.Interface.IOnClickPlayPhimBo;
import com.example.youtube.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPhimBo extends RecyclerView.Adapter<AdapterPhimBo.Viewhoder> {

    Context context;
    ArrayList<PhimBo> phimBos;
    IOnClickPlayPhimBo iOnClickPlayPhimBo;

    public AdapterPhimBo(ArrayList<PhimBo> phimBos) {
        this.phimBos = phimBos;
    }

    public void setiOnClickPlayPhimBo(IOnClickPlayPhimBo iOnClickPlayPhimBo) {
        this.iOnClickPlayPhimBo = iOnClickPlayPhimBo;
    }

    @NonNull
    @Override
    public AdapterPhimBo.Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemphimbo,parent,false);
        context = parent.getContext();
        Viewhoder viewhoder = new Viewhoder(view);

        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhimBo.Viewhoder holder, int position) {
        final PhimBo phimBo = phimBos.get(position);

        String avatar = phimBo.getAvatar();
        String title = phimBo.getTitle();

        holder.tvPhimBo.setText(title);
        Picasso.with(context).load(avatar).into(holder.imgPhimBo);
        holder.imgPhimBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickPlayPhimBo.onClickPlayPhimBo(phimBo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phimBos.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder{

        ImageView imgPhimBo;
        TextView tvPhimBo;

        public Viewhoder(@NonNull View itemView) {
            super(itemView);

            imgPhimBo = itemView.findViewById(R.id.imgPhimBo);
            tvPhimBo = itemView.findViewById(R.id.tvPhimBo);
        }
    }
}
