package com.example.vocalist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinedWordFragment extends Fragment {
    private OnAddClickListener onAddClickListener;
    private Button addButton;
    private View root;
    private SearchView searchView;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private CoinedWordAdapter adapter;
    private ArrayList<CoinedWordPojoClass.Data> list;
    private String URL = "http://13.209.140.171/";
    private String TAG = "CoinedWordFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_coined_word, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();
        startShowData();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAddClickListener) {
            onAddClickListener = (OnAddClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }
    public void init(){
        addButton = root.findViewById(R.id.coined_word_add_button_CoinedWordFragment);
        searchView = root.findViewById(R.id.searchView_CoinedWordFragment);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit =  new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        recyclerView = root.findViewById(R.id.recyclerview_CoinedFragment);
        adapter = new CoinedWordAdapter();
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }
    public void initListener(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClickListener.addOnclick();
                Log.d(TAG, "onClick: 추가 버튼 클릭");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: "+ query);
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<CoinedWordPojoClass> call = apiInterface.searchCoinedWord(query);
                call.enqueue(new Callback<CoinedWordPojoClass>() {
                    @Override
                    public void onResponse(Call<CoinedWordPojoClass> call, Response<CoinedWordPojoClass> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+response);
                            adapter.setList((ArrayList<CoinedWordPojoClass.Data>) response.body().getData());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinedWordPojoClass> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextSubmit: "+ newText);
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<CoinedWordPojoClass> call = apiInterface.searchCoinedWord(newText);
                call.enqueue(new Callback<CoinedWordPojoClass>() {
                    @Override
                    public void onResponse(Call<CoinedWordPojoClass> call, Response<CoinedWordPojoClass> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+response);
                            adapter.setList((ArrayList<CoinedWordPojoClass.Data>) response.body().getData());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinedWordPojoClass> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t);
                    }
                });
                return true;
            }
        });
    }
    public void startShowData(){
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CoinedWordPojoClass> call = apiInterface.searchCoinedShowWord();
        call.enqueue(new Callback<CoinedWordPojoClass>() {
            @Override
            public void onResponse(Call<CoinedWordPojoClass> call, Response<CoinedWordPojoClass> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response);
                    adapter.setList((ArrayList<CoinedWordPojoClass.Data>) response.body().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CoinedWordPojoClass> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }
}