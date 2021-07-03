package com.example.dclv_v2126;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListShop extends AppCompatActivity{
    private Button btn;
    //private TextView textView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static String URL_READ_NEARBY = "http://192.168.1.4/DCLV/get_nearby.php";
    private RecyclerView rcvShop;
    private ListShopAdapter listShopAdapter;
    private static final String TAG = "ListShop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_list_shop);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        rcvShop = findViewById(R.id.rcv_shop);
        listShopAdapter = new ListShopAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvShop.setLayoutManager(linearLayoutManager);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {

                                    Double lat1 = location.getLongitude();
                                    Double lon1 = location.getLatitude();
                                    getUserDetail(lat1.toString(),lon1.toString() );

                                } else {
                                    Toast.makeText(ListShop.this, "FAIL", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            } else {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }

        ;

    }

    private double rad2deg(double a) {
        return (a * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    private void getUserDetail(String lat, String lng) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_NEARBY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");
                            if (success.equals("1")) {
                                List<Shop> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strName = object.getString("name").trim();
                                    String latget = object.getString("lat").trim();
                                    String longet = object.getString("lng").trim();

                                    double lat1 = Double.parseDouble(lat);
                                    double lon1 = Double.parseDouble(lng);

                                    double lat2 = Double.parseDouble(latget);
                                    double lon2 = Double.parseDouble(longet);
                                    LatLng point1 = new LatLng(lon1,lat1);
                                    LatLng point2 = new LatLng(lon2,lat2);

                                    Double d = SphericalUtil.computeDistanceBetween(point1,point2);
//                                    int R = 6371; // Radius of the earth in km

//                                    double dLat = deg2rad(lat2 - lat1);  // deg2rad below
//                                    double dLon = deg2rad(lon2 - lon1);
//                                    double a =
//                                            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                                                    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
//                                                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
//                                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//                                    double d = R * c *1000; // Distance in km
                                    int f = (int) Math.round(d);;

                                    list.add(new Shop(strName,String.valueOf(f) + " m"));
//                                    Toast.makeText(ListShop.this, strName, Toast.LENGTH_SHORT).show();
                                }
                                listShopAdapter.setData(list);
                                rcvShop.setAdapter(listShopAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ListShop.this, "Error Reading Detail 1 " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ListShop.this, "Error Reading Detail 2" + error.toString(), Toast.LENGTH_SHORT).show();

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", lat);
                params.put("lng", lng);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}