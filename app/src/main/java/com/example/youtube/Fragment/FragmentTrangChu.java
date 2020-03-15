package com.example.youtube.Fragment;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youtube.R;
import com.example.youtube.Adapter.ViewPagerAdapter;
import com.example.youtube.databinding.FragmentFragmentTrangChuBinding;


public class FragmentTrangChu extends Fragment {
    FragmentFragmentTrangChuBinding binding;

    public FragmentTrangChu() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_trang_chu,container,false);
        addTabs(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        return binding.getRoot();
    }

    public void addTabs(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.add(new FragmentDexuat(),"Đề Xuất");
        adapter.add(new FragmentPhimBo(),"Phim Bộ");
        adapter.add(new FragmentPhimLe(),"Phim Lẻ");
        viewPager.setAdapter(adapter);
    }
}
