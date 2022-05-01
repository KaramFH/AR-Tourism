package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;
    String token;

    List<String> bodies = new ArrayList<>();
    List<String> users = new ArrayList<>();
    List<String> dates = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    protected void get_posts(){

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url("http://10.0.2.2:5000/get_posts")
                .header("x-access-token",token).get()
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(
                    @NotNull Call call,
                    @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show();
                    }
                });
                countDownLatch.countDown();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String jsonData = response.body().string();
                try {
                    JSONArray data = new JSONArray(jsonData);

                    for (int i =0; i<data.length();i++){
                        JSONObject obj = data.getJSONObject(i);
                        Log.v("bod",obj.getString("post_body"));
                        bodies.add(obj.getString("post_body"));
                        users.add(obj.getString("username"));
                        dates.add(obj.getString("date"));

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                countDownLatch.countDown();

            }

        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void login(View v){

        String username = (((TextView) findViewById(R.id.username)).getText()).toString();
        String password = (((TextView) findViewById(R.id.password)).getText()).toString();

        if ((username.length() == 0) || (password.length() == 0)){
            Toast.makeText(this, "please enter your username and password",Toast.LENGTH_LONG).show();

        }

        else{
            OkHttpClient okHttpClient = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build();

            Request request = new Request.Builder().url("http://10.0.2.2:5000/login")
                    .post(requestBody)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(
                        @NotNull Call call,
                        @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String jsonData = response.body().string();
                    try {
                        JSONObject data = new JSONObject(jsonData);

                        if (data.getString("message").equals("User does not exist!")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else if (data.getString("message").equals("Wrong password!")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try{
                        JSONObject data = new JSONObject(jsonData);
                        token = data.getString("token");
                        Log.v("token",token);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            });

            try {
                if (token.length() != 0) {

                    setContentView(R.layout.activity_homepage);

                    MapsFragment mp = new MapsFragment();
                    get_posts();

                    Fragment rf = new RecycleFragment().newInstance(bodies,users,dates);
                    bnv = findViewById(R.id.bottom_nav);
                    bnv.getMenu().findItem(R.id.nav_home).setChecked(true);
                    bnv.setOnItemSelectedListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.nav_home:
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, mp).commit();
                                    return true;

                                case R.id.nav_feed:
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,rf).commit();

                                    return true;

                            }
                        return true;
                    });

                }
            }
            catch(Exception e){

            }
        }
    }

    public void register(View v){
        Intent i =  new Intent(this, Register.class);
        startActivity(i);

    }


}