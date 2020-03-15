package com.example.youtube.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.youtube.Fragment.FragmentCaNhan;
import com.example.youtube.Fragment.FragmentTrangChu;
import com.example.youtube.R;
import com.example.youtube.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getFragment(new FragmentTrangChu());

       binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               if(menuItem.getItemId() == R.id.navtrangchu){
                   Toast.makeText(getBaseContext(),"trang chủ",Toast.LENGTH_LONG).show();
                   getFragment(new FragmentTrangChu());
                   return true;
               }
               else if(menuItem.getItemId() == R.id.navcanhan){
                   Toast.makeText(getBaseContext(),"cá nhân",Toast.LENGTH_LONG).show();
                   getFragment(new FragmentCaNhan());
                   return true;
               }
               return false;
           }
       });

    }

//    public void addTabs(ViewPager viewPager){
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.add(new FragmentDexuat(),"Đề Xuất");
//        adapter.add(new FragmentPhimBo(),"Phim Bộ");
//        adapter.add(new FragmentPhimLe(),"Phim Lẻ");
//        viewPager.setAdapter(adapter);
//    }

    public void getFragment(Fragment fragment){
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout,fragment)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
