package com.example.vocalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomQuizAcitivity extends AppCompatActivity {
    private Retrofit retrofit;
    private EditText inputAnswer;
    private Button submit;
    private TextView detail;
    private TextView timerTextView;
    private TextView word;
    private String answer;
    private String TAG = "RandomQuizActivity";
    private String URL = "http://13.209.140.171/";
    private Handler handler;
    private int secondsPassed = 30;
    private int score = 0;
    private TextView scoreTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_quiz_acitivity);
        init();
    }
    public void init(){
        detail = findViewById(R.id.hint_random_quiz_Activity);
        word = findViewById(R.id.answer_random_quiz_Activity);
        submit = findViewById(R.id.submit_random_quiz_Activity);
        timerTextView = findViewById(R.id.timer);
        inputAnswer = findViewById(R.id.inputAnswer_randomQuizActivity);
        scoreTextView = findViewById(R.id.score_randomQuiz);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit =  new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        scoreTextView.setText("스코어 : "+ score);
        newQuiz();
        startTimer();
        initListener();
    }
    public void initListener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputAnswer.getText().toString().equals("")){
                    Toast.makeText(RandomQuizAcitivity.this,"답을 입력해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    if(inputAnswer.getText().toString().equals(answer)){
                        secondsPassed = 30;
                        Toast.makeText(RandomQuizAcitivity.this,"정답입니다.",Toast.LENGTH_SHORT).show();
                        newQuiz();
                        score += 1;
                        scoreTextView.setText("스코어 : "+ score);
                    }else{
                        Toast.makeText(RandomQuizAcitivity.this,"오답입니다..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void startUpdating(View view) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
            handler.post(updateTextView);
        }
    }
    private final Runnable updateTextView = new Runnable() {
        @Override
        public void run() {
            if (secondsPassed > 0) {
                timerTextView.setText(String.format("남은 시간: %d초", secondsPassed));
                secondsPassed--;
                handler.postDelayed(this, 1000);
            } else {
                handler.removeCallbacks(this);
                secondsPassed = 0;
                Toast.makeText(RandomQuizAcitivity.this,"게임 오버 3초 뒤에 나가집니다.",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this,3000);
                setSharedPreference();
                handler = null;
            }
        }
    };
    public void startTimer(){
        startUpdating(null);
    }
    public void newQuiz(){
        inputAnswer.setText("");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<RandomQuizPojoClass> call = apiInterface.randomQuiz();
        call.enqueue(new Callback<RandomQuizPojoClass>() {
            @Override
            public void onResponse(Call<RandomQuizPojoClass> call, Response<RandomQuizPojoClass> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.body().word);
                    Log.d(TAG, "onResponse: "+response.body().detail);
                    detail.setText(response.body().detail);
                    answer = response.body().word;
                    String answerHint= "";
                    for(int i = 0;i < answer.length();i++){
                        answerHint += "*  ";
                    }
                    word.setText(answerHint);
                }
            }

            @Override
            public void onFailure(Call<RandomQuizPojoClass> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }
    public void setSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int score = sharedPreferences.getInt("score",0);
        if(score < this.score){
            editor.putInt("score",this.score);
            editor.commit();
        }
        finish();
    }
}