package com.example.youtube.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youtube.R;
import com.example.youtube.databinding.FragmentFragmentCaNhanBinding;


public class FragmentCaNhan extends Fragment {
    FragmentFragmentCaNhanBinding binding;
    public FragmentCaNhan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_ca_nhan,container,false);

        return binding.getRoot();
    }

}
