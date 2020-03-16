package com.example.youtube.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.youtube.Activity.PlayVideo;
import com.example.youtube.Adapter.AdapterDeXuat;
import com.example.youtube.Adapter.AdapterPhimBo;
import com.example.youtube.Contact.DeXuat;
import com.example.youtube.Contact.PhimBo;
import com.example.youtube.Interface.IOnClickPlayDeXuat;
import com.example.youtube.Interface.IOnClickPlayPhimBo;
import com.example.youtube.R;
import com.example.youtube.SQL.SQLHelper;
import com.example.youtube.databinding.FragmentFragmentPhimBoBinding;
import com.example.youtube.define.Define;
import com.example.youtube.define.Define_Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class FragmentPhimBo extends Fragment {

    FragmentFragmentPhimBoBinding binding;
    AdapterPhimBo adapterPhimBo;
    ArrayList<PhimBo> phimBoArrayList;

    SQLHelper sqlHelper;
    ArrayList<DeXuat> arrayListSQL;
    Define_Methods define_methods = new Define_Methods();


    public static FragmentPhimBo newInstance(){
        Bundle args=new Bundle();
        FragmentPhimBo fragment=new FragmentPhimBo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_phim_bo,container,false);

        phimBoArrayList = new ArrayList<>();
        new DoGetData(Define.STR_CATEGORY1).execute();

        return binding.getRoot();
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
            binding.progressPhimBo.setVisibility(View.VISIBLE);
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

                    phimBoArrayList.add(new PhimBo(avatar,title,mp4));

                }
                //adapterVideo.notifyDataSetChanged();
                adapterPhimBo=new AdapterPhimBo(phimBoArrayList);

                adapterPhimBo.setiOnClickPlayPhimBo(new IOnClickPlayPhimBo() {
                    @Override
                    public void onClickPlayPhimBo(PhimBo phimBo) {
                        Toast.makeText(getContext(),"click video phim bo",Toast.LENGTH_LONG).show();

                        DeXuat deXuat = new DeXuat(phimBo.getAvatar(),phimBo.getTitle(),phimBo.getFilemp4());
                        sqlHelper = new SQLHelper(getContext());
                        arrayListSQL = sqlHelper.getAllItem();
                        if(arrayListSQL.isEmpty()==false && define_methods.CHECK(deXuat.getText(),arrayListSQL)){
                            sqlHelper.deleteItem(deXuat.getText());
                        }
                        sqlHelper.insertItem(deXuat);

                        Intent intent = new Intent(getContext(), PlayVideo.class);
                        intent.putExtra("link_mp4",phimBo.getFilemp4());
                        intent.putExtra("title",phimBo.getTitle());
                        intent.putExtra("image",phimBo.getAvatar());
                        startActivity(intent);
                    }
                });

                binding.listPhimBo.setAdapter(adapterPhimBo);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                binding.listPhimBo.setLayoutManager(layoutManager);
                binding.progressPhimBo.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
