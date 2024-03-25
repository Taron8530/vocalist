package com.example.vocalist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordAddFragment extends Fragment {
    private OnAddClickListener onAddClickListener;
    private View root;
    private Retrofit retrofit;
    private EditText wordEditText;
    private EditText wordDetailEditText;
    private Button submitButton;
    private Button backButton;
    private String writer;
    private String URL = "http://43.200.172.115/";
    private String TAG = "WordAddFragment";
    String email;
    WordAddFragment(String email,String nickName){
        this.email = email;
        writer = nickName;
    }
    WordAddFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_word_add, container, false);
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();
    }
    public void init(){
        wordEditText = root.findViewById(R.id.word_Edittext_WordAddFragment);
        wordDetailEditText = root.findViewById(R.id.wordDetailEditText_WordAddFragment);
        submitButton = root.findViewById(R.id.submit_WordAddFragment);
        backButton = root.findViewById(R.id.back_Button_WordAddFragment);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit =  new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public void initListener(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClickListener.backButtonOnclick();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = wordEditText.getText().toString();
                String detail = wordDetailEditText.getText().toString();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<String> call = apiInterface.wordAdd(word,detail,writer);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+response.body());
                            if(response.body().equals("1")){
                                Toast.makeText(getContext(),"등록 했습니다.",Toast.LENGTH_SHORT).show();
                                onAddClickListener.backButtonOnclick();
                            }else{
                                Toast.makeText(getContext(),"잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t);
                    }
                });
            }
        });
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
}