package com.example.dclv_v2126.ui.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.dclv_v2126.R;
import com.example.dclv_v2126.SessionManager;
import com.example.dclv_v2126.databinding.FragmentDashboardBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    String getId;
    private static String URL_READ = "http://192.168.1.4/DCLV/read_detail.php";
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private ImageView ivOutput1,ivOutput2;
    private Button button;
    SessionManager sessionManager;
    private TextView textView;
    RequestQueue requestQueue;

    private static final String TAG = "HomeActivity";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ivOutput1 = binding.ivOutput1;
        ivOutput2 = binding.ivOutput2;
        textView = binding.textview3;
        requestQueue = Volley.newRequestQueue(getContext());

        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);



        getUserDetail();








//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
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
                                    String strPhonenumber = object.getString("phonenumber").trim();

                                    String sText = strPhonenumber;

                                    textView.setText(strPhonenumber);
                                    MultiFormatWriter writer = new MultiFormatWriter();

                                    try {
                                        BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE,200, 200);
                                        BarcodeEncoder encoder = new BarcodeEncoder();
                                        Bitmap bitmap = encoder.createBitmap(matrix);

                                        ivOutput1.setImageBitmap(bitmap);

                                        BitMatrix matrix2 = writer.encode(sText, BarcodeFormat.CODABAR,300, 70);
                                        BarcodeEncoder encoder2 = new BarcodeEncoder();
                                        Bitmap bitmap2 = encoder.createBitmap(matrix2);
                                        ivOutput2.setImageBitmap(bitmap2);
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }


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