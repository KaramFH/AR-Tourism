package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signup(View v){

        String username = (((TextView) findViewById(R.id.r_username)).getText()).toString();
        String email = (((TextView) findViewById(R.id.r_email)).getText()).toString();
        String firstname = (((TextView) findViewById(R.id.r_firstname)).getText()).toString();
        String lastname = (((TextView) findViewById(R.id.r_lastname)).getText()).toString();
        String password = (((TextView) findViewById(R.id.r_password)).getText()).toString();

        if ((username.length() == 0) || (password.length() == 0) || (email.length()==0) || (firstname.length()==0) || (lastname.length() == 0)){
            Toast.makeText(this, "please fill all the information",Toast.LENGTH_LONG).show();
        }

        else{
            OkHttpClient okHttpClient = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("email", username)
                    .addFormDataPart("first_name", username)
                    .addFormDataPart("last_name", username)
                    .addFormDataPart("password", password)
                    .build();

            Request request = new Request.Builder().url("http://10.0.2.2:5000/signup")
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

                        if (data.getString("message").equals("Username already exists!")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else if (data.getString("message").equals("Email already exists!")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else if (data.getString("message").equals("New User Created!")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User Created!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }


    }



}