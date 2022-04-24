package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
                    bnv = findViewById(R.id.bottom_nav);

                    bnv.setOnItemSelectedListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.nav_home:
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, mp).commit();
                                    return true;

                                case R.id.nav_feed:
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