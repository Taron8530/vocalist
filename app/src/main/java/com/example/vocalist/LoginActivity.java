package com.example.vocalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText passwordEditText;
    private TextView signUp;
    private Button submit;
    private Retrofit retrofit;

    private String URL = "http://13.209.140.171/";
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idCheck();
        init();
        initListener();

    }
    public void init(){
        idEditText = findViewById(R.id.idEdit_loginActivity);
        passwordEditText = findViewById(R.id.pwEdit_loginActivity);
        submit = findViewById(R.id.loginOk_loginActivity);
        signUp = findViewById(R.id.signUp_loginActivity);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit =  new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public void initListener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"아이디 또는 패스워드를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    String email = idEditText.getText().toString().trim();
                    String pw = passwordEditText.getText().toString().trim();
                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                    Call<LoginPojoClass> call = apiInterface.login(email,pw);
                    call.enqueue(new Callback<LoginPojoClass>() {
                        @Override
                        public void onResponse(Call<LoginPojoClass> call, Response<LoginPojoClass> response) {
                            if(response.isSuccessful()){
                                Log.d(TAG, "onResponse: "+response);
                                if(response.body().response.equals("1")){
                                    Toast.makeText(LoginActivity.this,"로그인 성공!",Toast.LENGTH_SHORT);
                                    putAccountInfo(response.body().email,response.body().nickname);
                                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(LoginActivity.this,"아이디 또는 비밀번호 확인해주세요.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginPojoClass> call, Throwable t) {
                            Log.d(TAG, "onFailure: "+t);
                        }
                    });
                }

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });
    }
    public void putAccountInfo(String email,String nickname){
        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email);
        editor.putString("nickname",nickname);
        editor.apply();
    }
    public void idCheck(){
        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        if(!preferences.getString("email","").equals("")){
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }
    }
}