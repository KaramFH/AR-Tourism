package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

            RequestBody body
                    = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build();

            Request request = new Request.Builder().url("http://10.0.2.2:5000/login")
                    .post(body)
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
                        String token = data.getString("token");

                        Log.v("fefe",token);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    System.out.println(response.body().string());

//                    if (response.body().string().equals("token")) {
//                        Log.d("fefe","wlak token ya hbb");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(), "data received", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
                }
            });





        }

    }
}