package com.example.dclv_v2126.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dclv_v2126.ListShop;
import com.example.dclv_v2126.LoginActivity;
import com.example.dclv_v2126.R;
import com.example.dclv_v2126.SessionManager;
import com.example.dclv_v2126.databinding.FragmentHomeBinding;
import com.example.dclv_v2126.register;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private static String URL_READ = "http://192.168.1.4/DCLV/read_detail.php";
    private TextView textView,textViewxemthem;
    SessionManager sessionManager;
    String getId;
    CircleImageView profile_image;
    LinearLayout linearLayout;
    private static final String TAG = "HomeActivity";
    RequestQueue requestQueue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestQueue = Volley.newRequestQueue(getContext());
        textView = binding.textHome;
        profile_image = binding.imageView2;
        textViewxemthem = binding.textView6;
        linearLayout = binding.linearLayoutHome;





        textViewxemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListShop.class));
            }
        });
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        getUserDetail();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getUserDetail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");
                            if(success.equals("1")){
                                for (int i= 0;i <jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
                                    String strImage = object.getString("image").trim();
                                    textView.setText(strName);
                                    Picasso.get().load(strImage).into(profile_image);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Error Reading Detail 1 " + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"Error Reading Detail 2"  + error.toString(),Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",getId);
                return params;

            }
        };

        requestQueue.add(stringRequest);
    }
}