package com.example.vocalist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageFragment extends Fragment {

    private TextView emailTextView;
    private TextView nickNameTextView;
    private String email;
    private String nickName;
    private View root;

    private String TAG = "MyPageFragment";
    private String URL = "http://13.209.140.171/";

    private String duplication;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private CoinedWordAdapter adapter;
    private ArrayList<CoinedWordPojoClass.Data> list;


    Button logOutButton, withdrawalButton;


    MyPageFragment(String email,String nickname){
        nickName = nickname;
        this.email = email;
    }
    MyPageFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_my_page, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();
    }
    public void init(){
        emailTextView = root.findViewById(R.id.myEmailTextView);
        nickNameTextView = root.findViewById(R.id.myNickNameView);
        emailTextView.setText(email);
        nickNameTextView.setText(nickName);
        recyclerView = root.findViewById(R.id.recyclerView);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit =  new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        adapter = new CoinedWordAdapter();
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
        requestMyWord();

    }
    public void requestMyWord(){
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        retrofit2.Call<CoinedWordPojoClass> call = apiInterface.searchMyWord(nickName);
        call.enqueue(new retrofit2.Callback<CoinedWordPojoClass>() {
            @Override
            public void onResponse(retrofit2.Call<CoinedWordPojoClass> call, retrofit2.Response<CoinedWordPojoClass> response) {
                if(response.isSuccessful()){
                    if(response.body() != null) {
                        Log.d(TAG, "onResponsess: "+response);
                        list = (ArrayList<CoinedWordPojoClass.Data>) response.body().getData();
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                    if(response.body().getData().size() <= 0){
                        TextView textView = root.findViewById(R.id.showComment_Mypage);
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<CoinedWordPojoClass> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }

    public void initListener(){

        logOutButton = root.findViewById(R.id.logoutButton);
        withdrawalButton = root.findViewById(R.id.withdrawalButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "로그아웃 됐습니다." , Toast.LENGTH_SHORT).show();
                deleteSharedPreference();
            }
        });

        withdrawalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                withdrawalSendServer();
                Toast.makeText(getContext(), "회원탈퇴 됐습니다." , Toast.LENGTH_SHORT).show();
                deleteSharedPreference();
            }
        });

    }

    public void withdrawalSendServer(){
        HttpUrl.Builder urlBuilder_withdrawal = HttpUrl.parse("http://13.209.140.171/GoodBye.php").newBuilder();
        String url = urlBuilder_withdrawal.build().toString();

        RequestBody formBody_withdrawal = new FormBody.Builder()
                .add("nickName", nickName)
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody_withdrawal)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String ResponseDate = response.body().string().trim();
                if (ResponseDate.equals("1")) {
                    duplication = "1";
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Log.e(TAG, "회원 탈퇴 성공");
                }else {
                    duplication= "0";
                    Log.e(TAG , "회원 탈퇴 실패" + ResponseDate);
                }
            }
        });
    }

    public void deleteSharedPreference(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}