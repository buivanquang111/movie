package com.example.youtube.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.youtube.Activity.History;
import com.example.youtube.Activity.SaveVideo;
import com.example.youtube.R;
import com.example.youtube.databinding.FragmentFragmentCaNhanBinding;

public class FragmentCaNhan extends Fragment {
    FragmentFragmentCaNhanBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_ca_nhan,container,false);

        binding.rvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), History.class);
                startActivity(intent);
            }
        });
        binding.rvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SaveVideo.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

}
