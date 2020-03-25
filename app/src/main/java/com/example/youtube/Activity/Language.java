package com.example.youtube.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.youtube.R;
import com.example.youtube.databinding.ActivityLanguageBinding;

import java.util.Locale;

public class Language extends AppCompatActivity {

    ActivityLanguageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);

        binding.rvVietNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenNgonNgu("vi");
            }
        });

        binding.rvAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenNgonNgu("en");
            }
        });

        binding.backLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void chuyenNgonNgu(String language){
        Locale locale=new Locale(language);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(
                config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(getBaseContext(),Language.class);
        startActivity(intent);

    }
}
