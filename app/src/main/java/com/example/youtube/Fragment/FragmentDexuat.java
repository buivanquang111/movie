package com.example.youtube.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youtube.Activity.PlayVideo;
import com.example.youtube.Adapter.AdapterDeXuat;
import com.example.youtube.Contact.DeXuat;
import com.example.youtube.Interface.IOnClickPlayDeXuat;
import com.example.youtube.R;
import com.example.youtube.databinding.FragmentFragmentDexuatBinding;
import com.example.youtube.define.Define;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class FragmentDexuat extends Fragment {

   FragmentFragmentDexuatBinding binding;
    AdapterDeXuat adapterDeXuat;
    ArrayList<DeXuat> deXuatArrayList;
    ArrayList<DeXuat> deXuatArrayList2;
    ArrayList<DeXuat> deXuatArrayList3;


    public static FragmentDexuat newInstance(){
        Bundle args=new Bundle();
        FragmentDexuat fragment=new FragmentDexuat();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_dexuat,container,false);

        ActionViewFlipper();
        deXuatArrayList = new ArrayList<>();
        new DoGetData(Define.STR_VIDEOHOT).execute();
        deXuatArrayList2 = new ArrayList<>();
        new DoGetData2(Define.STR_CATEGORY2).execute();
        deXuatArrayList3 = new ArrayList<>();
        new DoGetData3(Define.STR_CATEGORY1).execute();
        return binding.getRoot();
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();

        mangquangcao.add(Define.STR_VIEWFLIPPER1);
        mangquangcao.add(Define.STR_VIEWFLIPPER2);
        mangquangcao.add(Define.STR_VIEWFLIPPER3);
        mangquangcao.add(Define.STR_VIEWFLIPPER4);
        mangquangcao.add(Define.STR_VIEWFLIPPER5);

        for (int i=0 ;i<mangquangcao.size(); i++){
            ImageView imageView =new ImageView(getContext());
            Picasso.with(getContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.viewFlipper.addView(imageView);
        }
        binding.viewFlipper.setFlipInterval(3000);
        binding.viewFlipper.setAutoStart(true);
        Animation animation_slie_in = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_right);
        Animation animation_slie_out = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_right);
        binding.viewFlipper.setInAnimation(animation_slie_in);
        binding.viewFlipper.setOutAnimation(animation_slie_out);

    }

    public class DoGetData extends AsyncTask<Void, Void, Void> {
        String urlApi;
        String result="";

        public DoGetData(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url=new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is= connection.getInputStream();
                int bytecharacter;
                while (( bytecharacter = is.read()) != -1){
                    result +=(char) bytecharacter;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray=new JSONArray(result);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String avatar=jsonObject.getString("avatar");
                    String title=jsonObject.getString("title");
                    String mp4=jsonObject.getString("file_mp4");

                    deXuatArrayList.add(new DeXuat(avatar,title,mp4));

                }
                //adapterVideo.notifyDataSetChanged();
                adapterDeXuat=new AdapterDeXuat(deXuatArrayList);

                adapterDeXuat.setiOnClickPlayDeXuat(new IOnClickPlayDeXuat() {
                    @Override
                    public void onClickPlayDeXuat(DeXuat deXuat) {
                        Toast.makeText(getContext(),"click video",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), PlayVideo.class);
                        intent.putExtra("link_mp4",deXuat.getMp4());
                        intent.putExtra("title",deXuat.getText());
                        intent.putExtra("image",deXuat.getImg());
                        startActivity(intent);

                    }


                });

                binding.listDeXuat.setAdapter(adapterDeXuat);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
                binding.listDeXuat.setLayoutManager(layoutManager);
                binding.progressbar.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public class DoGetData2 extends AsyncTask<Void, Void, Void> {
        String urlApi;
        String result="";

        public DoGetData2(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url=new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is= connection.getInputStream();
                int bytecharacter;
                while (( bytecharacter = is.read()) != -1){
                    result +=(char) bytecharacter;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray=new JSONArray(result);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String avatar=jsonObject.getString("avatar");
                    String title=jsonObject.getString("title");
                    String mp4=jsonObject.getString("file_mp4");

                    deXuatArrayList2.add(new DeXuat(avatar,title,mp4));

                }
                //adapterVideo.notifyDataSetChanged();
                adapterDeXuat=new AdapterDeXuat(deXuatArrayList2);

                adapterDeXuat.setiOnClickPlayDeXuat(new IOnClickPlayDeXuat() {
                    @Override
                    public void onClickPlayDeXuat(DeXuat deXuat) {
                        Toast.makeText(getContext(),"click video",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), PlayVideo.class);
                        intent.putExtra("link_mp4",deXuat.getMp4());
                        intent.putExtra("title",deXuat.getText());
                        intent.putExtra("image",deXuat.getImg());
                        startActivity(intent);
                    }


                });

                binding.listDeXuat2.setAdapter(adapterDeXuat);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
                binding.listDeXuat2.setLayoutManager(layoutManager);
                binding.progressbar.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public class DoGetData3 extends AsyncTask<Void, Void, Void> {
        String urlApi;
        String result="";

        public DoGetData3(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url=new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is= connection.getInputStream();
                int bytecharacter;
                while (( bytecharacter = is.read()) != -1){
                    result +=(char) bytecharacter;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray=new JSONArray(result);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String avatar=jsonObject.getString("avatar");
                    String title=jsonObject.getString("title");
                    String mp4=jsonObject.getString("file_mp4");

                    deXuatArrayList3.add(new DeXuat(avatar,title,mp4));

                }
                //adapterVideo.notifyDataSetChanged();
                adapterDeXuat=new AdapterDeXuat(deXuatArrayList3);

                adapterDeXuat.setiOnClickPlayDeXuat(new IOnClickPlayDeXuat() {
                    @Override
                    public void onClickPlayDeXuat(DeXuat deXuat) {
                        Toast.makeText(getContext(),"click video",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), PlayVideo.class);
                        intent.putExtra("link_mp4",deXuat.getMp4());
                        intent.putExtra("title",deXuat.getText());
                        intent.putExtra("image",deXuat.getImg());
                        startActivity(intent);
                    }


                });

                binding.listDeXuat3.setAdapter(adapterDeXuat);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
                binding.listDeXuat3.setLayoutManager(layoutManager);
                binding.progressbar.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
