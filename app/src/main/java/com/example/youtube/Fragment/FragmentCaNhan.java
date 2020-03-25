package com.example.youtube.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtube.Activity.History;
import com.example.youtube.Activity.Language;
import com.example.youtube.Activity.SaveVideo;
import com.example.youtube.Activity.ScanQR;
import com.example.youtube.R;
import com.example.youtube.databinding.FragmentFragmentCaNhanBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FragmentCaNhan extends Fragment {
    FragmentFragmentCaNhanBinding binding;
    CallbackManager callbackManager;
    String name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_ca_nhan,container,false);

        binding.tvDangXuat.setVisibility(View.INVISIBLE);
        binding.tvName.setVisibility(View.INVISIBLE);
        binding.loginButton.setReadPermissions(Arrays.asList("public_profile","name"));
        setLogin_Button();
        setLogout_Button();


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

        binding.rvScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanQR.class);
                startActivity(intent);
            }
        });

        binding.rvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"click Language",Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(getContext(),Language.class);
                startActivity(intent1);

            }
        });


        return binding.getRoot();
    }

//    private void printKeyHash(){
//        try {
//            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.example.youtube", PackageManager.GET_SIGNATURES);
//            for (Signature signature:info.signatures){
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }

    private void setLogout_Button() {
        binding.tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                binding.tvDangXuat.setVisibility(View.INVISIBLE);
                binding.tvName.setVisibility(View.INVISIBLE);
                binding.tvName.setText("");
                binding.imageprofilepictureview.setProfileId(null);
                binding.loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setLogin_Button() {
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                binding.loginButton.setVisibility(View.INVISIBLE);
                binding.tvName.setVisibility(View.VISIBLE);
                binding.tvDangXuat.setVisibility(View.VISIBLE);
                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest =GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON",response.getJSONObject().toString());
                try {
                    name = object.getString("name");
                    binding.imageprofilepictureview.setProfileId(Profile.getCurrentProfile().getId());
                    binding.tvName.setText(name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }
}
