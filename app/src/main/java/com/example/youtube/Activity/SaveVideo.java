package com.example.youtube.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.youtube.Adapter.AdapterDeXuat;
import com.example.youtube.Contact.DeXuat;
import com.example.youtube.Interface.IOnClickPlayDeXuat;
import com.example.youtube.R;
import com.example.youtube.SQL.SQLHelper;
import com.example.youtube.SQL.SQLHelperSave;
import com.example.youtube.databinding.ActivitySaveVideoBinding;
import com.example.youtube.define.Define_Methods;

import java.util.ArrayList;

public class SaveVideo extends AppCompatActivity {

    ActivitySaveVideoBinding binding;
    SQLHelperSave sqlHelperSave;
    ArrayList<DeXuat> arrayList;
    ArrayList<DeXuat> deXuatArrayList;
    AdapterDeXuat adapterDeXuat;
    Define_Methods define_methods = new Define_Methods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_save_video);

        sqlHelperSave = new SQLHelperSave(getBaseContext());
        deXuatArrayList = sqlHelperSave.getALLItem();
        arrayList = new ArrayList<>();

        binding.deleteSaveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelperSave.deleteAll();
                deXuatArrayList = sqlHelperSave.getALLItem();
                adapterDeXuat = new AdapterDeXuat(deXuatArrayList);
                binding.listSaveVideo.setAdapter(adapterDeXuat);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
                binding.listSaveVideo.setLayoutManager(layoutManager);
            }
        });

        binding.backSaveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        int size = deXuatArrayList.size();
        for (int i=size-1;i>=0;i--){
            arrayList.add(deXuatArrayList.get(i));
        }

        adapterDeXuat = new AdapterDeXuat(arrayList);
        adapterDeXuat.setiOnClickPlayDeXuat(new IOnClickPlayDeXuat() {
            @Override
            public void onClickPlayDeXuat(DeXuat deXuat) {

                if(arrayList.isEmpty()==false && define_methods.CHECK(deXuat.getText(),arrayList)){
                    sqlHelperSave.deleteItem(deXuat.getText());
                }
                sqlHelperSave.insertItem(deXuat);

                Intent intent = new Intent(getBaseContext(), PlayVideo.class);
                intent.putExtra("link_mp4",deXuat.getMp4());
                intent.putExtra("title",deXuat.getText());
                intent.putExtra("image",deXuat.getImg());
                startActivity(intent);
            }
        });
        binding.listSaveVideo.setAdapter(adapterDeXuat);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 2, RecyclerView.VERTICAL, false);
        binding.listSaveVideo.setLayoutManager(layoutManager);
    }

}
