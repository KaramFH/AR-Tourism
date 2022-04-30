package com.example.fyp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecycleFragment extends Fragment {

    private List<String> bodies = new ArrayList<>(Arrays.asList(new String[]{"Server Down"}));
    private List<String> users = new ArrayList<>(Arrays.asList(new String[]{"None"}));
    private List<String> dates = new ArrayList<>(Arrays.asList(new String[]{"None"}));


    public RecycleFragment(){

    }
    public RecycleFragment(List<String> bod, List<String> user, List<String> date){

        this.bodies = bod;
        this.users = user;
        this.dates = date;
    }

    public static Fragment newInstance(List<String> bod,List<String> user,List<String> date) {
        return new RecycleFragment(bod,user,date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view_fragment,container,false);


        RecyclerView recyclerView = view.findViewById(R.id.recycle_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecycleViewAdapter(bodies,users,dates));

        return view;
    }

    private class RecycleViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private TextView mUserText;
        private TextView mDateText;


        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecycleViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.card_view, container, false));

            mCardView = itemView.findViewById(R.id.card_container);
            mTextView = itemView.findViewById(R.id.text_holder);
            mUserText = itemView.findViewById(R.id.user_holder);
            mDateText = itemView.findViewById(R.id.time_holder);


        }
    }

    private class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>{

        List<String> mList;
        List<String> uList;
        List<String> dList;


        public RecycleViewAdapter(List<String> list,List<String> users,List<String> dates){
            this.mList = list;
            this.uList = users;
            this.dList = dates;

        }

        @NonNull
        @Override
        public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new RecycleViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

            holder.mTextView.setText(mList.get(position));
            holder.mUserText.setText(uList.get(position));
            holder.mDateText.setText(dList.get(position));

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }





}
