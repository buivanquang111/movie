package com.example.youtube.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.youtube.Adapter.AdapterDeXuat;
import com.example.youtube.Adapter.AdapterPhimLe;
import com.example.youtube.Contact.DeXuat;
import com.example.youtube.Contact.PhimLe;
import com.example.youtube.Interface.IOnClickPlayPhimLe;
import com.example.youtube.R;
import com.example.youtube.SQL.SQLHelperSave;
import com.example.youtube.databinding.ActivityPlayVideoBinding;
import com.example.youtube.define.Define;
import com.example.youtube.define.Define_Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PlayVideo extends AppCompatActivity {

    ActivityPlayVideoBinding binding;
    Handler myHandler = new Handler();
    AdapterPhimLe adapterPhimLe;
    ArrayList<PhimLe> phimLeArrayList;

    double currentposition,totalduration;
    ArrayList<DeXuat> arrayList;
    AdapterDeXuat adapterDeXuat;
    DeXuat deXuatSave;

    AudioManager audioManager;
    int maxVol, stepVol, currentVol;
    int x1,y1;
    int time;
    boolean reChangeVol = true;
    boolean reChangePosition = true;

    SQLHelperSave sqlHelperSave;
    Define_Methods define_methods = new Define_Methods();
    ArrayList<DeXuat> arrayListDeXuatSave;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_video);

        String fileMp4 = getIntent().getStringExtra("link_mp4");
        String title = getIntent().getStringExtra("title");
        String img = getIntent().getStringExtra("image");

        binding.tvplayvideoview.setText(title);
        Uri uri = Uri.parse(fileMp4);

        deXuatSave = new DeXuat(img,title,fileMp4);
        binding.playvideoview.setVideoURI(uri);
        binding.playvideoview.requestFocus();
        binding.playvideoview.start();

        //list goi y
        phimLeArrayList = new ArrayList<>();
        new DoGetData(Define.STR_CATEGORY2).execute();

        //seekbar
        binding.playvideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
            }
        });

        //diplay controll
        Display display = new Display();
        myHandler.postDelayed(display,5000);

        //dừng video
        binding.imgpausevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playvideoview.pause();
                binding.imgplayvideo.setVisibility(View.VISIBLE);
                binding.imgpausevideo.setVisibility(View.GONE);
            }
        });

        //chạy video
        binding.imgplayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playvideoview.start();
                binding.imgplayvideo.setVisibility(View.GONE);
                binding.imgpausevideo.setVisibility(View.VISIBLE);

            }
        });

        //tua video nhanh 5s
        binding.imgtuanhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = binding.playvideoview.getCurrentPosition();
                int duration = binding.playvideoview.getDuration();
                int time = 5000;
                if(current+time<duration){
                    binding.playvideoview.seekTo(current+time);
                    Toast.makeText(getBaseContext(),"jump forward 5 seconds",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getBaseContext(),"cannot jump forward 5 seconds",Toast.LENGTH_LONG).show();
                }

            }
        });

        //lùi video 5s
        binding.imgtuacham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = binding.playvideoview.getCurrentPosition();
                int time = 5000;
                if(current-time>=0){
                    binding.playvideoview.seekTo(current-time);
                    Toast.makeText(getBaseContext(),"jump backward 5 seconds",Toast.LENGTH_LONG).show();
                }
                else{
                    binding.playvideoview.seekTo(0);
                    Toast.makeText(getBaseContext(),"cannot jump backward 5 seconds",Toast.LENGTH_LONG).show();
                }
            }
        });

        //save
        binding.savevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"save video",Toast.LENGTH_LONG).show();
                addSave(deXuatSave);
            }
        });

        //back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // setUp Volume and Position
        audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        stepVol = 100 / maxVol;
        currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVol = currentVol * stepVol;
        binding.tvvolume.setText(String.valueOf(currentVol));

        // Control Volume and Position
        binding.playvideoview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) motionEvent.getX();
                        y1 = (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        binding.currentimeTC.setText(timeConversion(binding.playvideoview.getDuration()));
                        if (Math.abs(motionEvent.getX() - x1) > 50) {
                            binding.rvtimetua.setVisibility(View.VISIBLE);
                            int timeCurent = (binding.playvideoview.getCurrentPosition() + (int) motionEvent.getX() - x1);
                            binding.playvideoview.seekTo(timeCurent);
                            binding.currenpositionTC.setText(timeConversion(timeCurent));
                        }

                        if (Math.abs(motionEvent.getY() - y1) > 50) {
                            binding.rvvolume.setVisibility(View.VISIBLE);
                            if (motionEvent.getY() - y1 < 0 && currentVol < 100) {
                                currentVol++;
                                binding.tvvolume.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);

                            } else if (motionEvent.getY() - y1 > 0 && currentVol > 0) {
                                currentVol--;
                                binding.tvvolume.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);

                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.rvtimetua.setVisibility(View.INVISIBLE);
                        binding.rvvolume.setVisibility(View.INVISIBLE);
                        reChangeVol = true;
                        reChangePosition = true;
                        break;
                }
                binding.control.setVisibility(View.VISIBLE);
                return true;
            }
        });

        //fullscreen
        binding.fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvplayvideoview.setVisibility(View.GONE);
                binding.displaylist.setVisibility(View.GONE);
                binding.exitfullscreen.setVisibility(View.VISIBLE);
                binding.fullscreen.setVisibility(View.GONE);
                time = binding.playvideoview.getCurrentPosition();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) binding.rvplayvideoview.getLayoutParams();

                params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params1.height = ViewGroup.LayoutParams.MATCH_PARENT;

                binding.rvplayvideoview.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                binding.playvideoview.seekTo(time);

            }
        });

        //exit fullscreen
        binding.exitfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvplayvideoview.setVisibility(View.VISIBLE);
                binding.displaylist.setVisibility(View.VISIBLE);
                binding.fullscreen.setVisibility(View.VISIBLE);
                binding.exitfullscreen.setVisibility(View.GONE);
                time = binding.playvideoview.getCurrentPosition();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) binding.rvplayvideoview.getLayoutParams();

                params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params1.height = 600;

                binding.rvplayvideoview.setLayoutParams(params1);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                binding.playvideoview.seekTo(time);

            }
        });




    }


    //time
    public String timeConversion(long value){
        String time;
        int dur = (int) value;
        int hrs = (dur/3600000);
        int mns = (dur/60000)%60000;
        int scs = dur % 60000/1000;
        if(hrs > 0){
            time = String.format("%02d:%02d:%02d",hrs,mns,scs);
        }else{
            time = String.format("%02d:%02d",mns,scs);
        }
        return time;
    }

    public void setVideoProgress(){
        currentposition = binding.playvideoview.getCurrentPosition();
        totalduration = binding.playvideoview.getDuration();

        binding.totalduration.setText(timeConversion((int) totalduration));
        binding.currentposition.setText(timeConversion((int) currentposition));
        binding.seekbar.setMax((int) totalduration);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                currentposition = binding.playvideoview.getCurrentPosition();
                binding.currentposition.setText(timeConversion((long)currentposition));
                binding.seekbar.setProgress((int) currentposition);
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);

        //seekbar change listener
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentposition = binding.seekbar.getProgress();
                binding.playvideoview.seekTo((int) currentposition);
            }
        });

    }

    class Display implements Runnable{

        @Override
        public void run() {
            myHandler.postDelayed(this,5000);
            binding.control.setVisibility(View.GONE);
        }
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
            binding.progressbarPlayVideo.setVisibility(View.VISIBLE);
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

                    phimLeArrayList.add(new PhimLe(avatar,title,mp4));

                }
                //adapterVideo.notifyDataSetChanged();
                adapterPhimLe=new AdapterPhimLe(phimLeArrayList);

                adapterPhimLe.setiOnClickPlayPhimLe(new IOnClickPlayPhimLe() {
                    @Override
                    public void onClickPlayPhimLe(PhimLe phimLe) {
                        Toast.makeText(getBaseContext(),"click video phim le",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getBaseContext(), PlayVideo.class);
                        intent.putExtra("link_mp4",phimLe.getMp4());
                        intent.putExtra("title",phimLe.getTitle());
                        intent.putExtra("image",phimLe.getImg());
                        startActivity(intent);
                    }
                });

                binding.rvListDeXuat.setAdapter(adapterPhimLe);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
                binding.rvListDeXuat.setLayoutManager(layoutManager);
                binding.progressbarPlayVideo.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void addSave(DeXuat dexuatAddSave){
        sqlHelperSave = new SQLHelperSave(getBaseContext());
        arrayListDeXuatSave = sqlHelperSave.getALLItem();
        if(arrayListDeXuatSave.isEmpty()==false && define_methods.CHECK(dexuatAddSave.getText(),arrayListDeXuatSave)){
            sqlHelperSave.deleteItem(dexuatAddSave.getText());
        }
        sqlHelperSave.insertItem(dexuatAddSave);

    }


}
