package com.example.vocalist;

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
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class SearchWordFragment extends Fragment {
    private View root;
    private SearchView searchView;
    private Retrofit retrofit;
    private String URL = "https://stdict.korean.go.kr/api/";
    private String TAG = "SearchWordFragment";
    private RecyclerView recyclerView;
    private ArrayList<WordPojoClass.Item> wordList = new ArrayList<>();;
    private SearchWordAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_word, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        startShowData();
    }
    public void initView(){
        searchView = root.findViewById(R.id.searchView_SearchWordFragment);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit =  new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        recyclerView = root.findViewById(R.id.recyclerview_SearchWordFragment);
        adapter = new SearchWordAdapter();
        adapter.setList(wordList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        recyclerView.setAdapter(adapter);
    }
    public void initListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: "+ query);
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<WordPojoClass> call = apiInterface.searchWord(getResources().getString(R.string.wordApiKey),query,"json");
                call.enqueue(new Callback<WordPojoClass>() {
                    @Override
                    public void onResponse(Call<WordPojoClass> call, Response<WordPojoClass> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+response.body().getChannel().getItems().get(0).getSense().getDefinition());
                            adapter.setList((ArrayList<WordPojoClass.Item>) response.body().getChannel().getItems());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<WordPojoClass> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    public void startShowData(){
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<WordPojoClass> call = apiInterface.searchWord(getResources().getString(R.string.wordApiKey),"벌","json");
        call.enqueue(new Callback<WordPojoClass>() {
            @Override
            public void onResponse(Call<WordPojoClass> call, Response<WordPojoClass> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.body().getChannel().getItems().get(0).getSense().getDefinition());
                    adapter.setList((ArrayList<WordPojoClass.Item>) response.body().getChannel().getItems());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<WordPojoClass> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }
}