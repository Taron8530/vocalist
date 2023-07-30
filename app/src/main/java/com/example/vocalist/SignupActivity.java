package com.example.vocalist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {

    private  String emailValidation = "^[a-zA-Z0-9._%+-]+@(naver|gmail|daum|hanmail)+(.com|.net)$";

    private String pwValidation = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\\\(\\\\)\\\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\\\(\\\\)\\\\-_=+]).{8,12}$";
    private String nickNameValidation = "^(?!(?:[0-9]+)$)([a-z]|[가-힣]|[0-9가-힣a-z] |[A-Z]){2,10}$";

    String TAG = "signUpActivity";

    EditText editEmail, editFirstPw, editSecondPw, editNickname;

    TextView emailChk , nicknameChk, firstPwChk, secondPwChk;

    Button emailChkButton, nicknameChkButton, signUpButton;

    String email, nickName, firstPw;

    ImageView firstPwImage, secondPwImage,backButton;

    String emailDuplication, nickNameDuplication;

    int buttonChk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstPwImage = findViewById(R.id.passwordCheckImage);
        secondPwImage = findViewById(R.id.secondPasswordCheckImage);

        emailChkButton = findViewById(R.id.emailCheck_signupActivity);
        nicknameChkButton = findViewById(R.id.nicknameCheck_signupActivity);
        signUpButton = findViewById(R.id.signUpBtn_signupActivity);
        backButton = findViewById(R.id.cancel_signupActivity);

        emailChk = findViewById(R.id.emailCheckText_signupActivity);
        nicknameChk = findViewById(R.id.nicknameCheckText_signupActivity);
        firstPwChk = findViewById(R.id.firstPasswordCheckText_signupActivity);
        secondPwChk = findViewById(R.id.pwCheckText_signupActivity);

        editEmail = findViewById(R.id.emailWrite_signupActivity);
        editNickname = findViewById(R.id.nicknameWrite_signupActivity);
        editFirstPw = findViewById(R.id.passwordWrite_signupActivity);
        editSecondPw = findViewById(R.id.pwDubleCheck_signupActivity);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nicknameChkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChk = 1;
                nicknameChk.setVisibility(View.VISIBLE);
                nickName = editNickname.getText().toString().trim();
                if (nickName.length() > 10) {
                    nicknameChk.setText("닉네임이 10글자 이상입니다.");
                    nicknameChk.setVisibility(View.VISIBLE);
                    nicknameChk.setTextColor(Color.RED);
                }else if(nickName.length() < 2){
                    nicknameChk.setText("닉네임이 2글자 이하입니다.");
                    nicknameChk.setVisibility(View.VISIBLE);
                    nicknameChk.setTextColor(Color.RED);
                }else if (nickNameDuplication == "1") {
                    nicknameChk.setText("중복된 닉네임 입니다.");
                    nicknameChk.setVisibility(View.VISIBLE);
                    nicknameChk.setTextColor(Color.RED);
                } else if (nickNameDuplication == "0") {
                    nicknameChk.setText("사용가능한 닉네임 입니다.");
                    nicknameChk.setVisibility(View.VISIBLE);
                    nicknameChk.setTextColor(Color.GREEN);
                }
            }
        });

        emailChkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editEmail.getText().toString().trim();
                buttonChk = 1;
                if(emailDuplication == "0"){
                    emailChk.setText("사용 가능한 이메일 입니다.");
                    emailChk.setTextColor(Color.GREEN);
                }else if (emailDuplication == "1"){
                    emailChk.setText("중복된 이메일 입니다.");
                    emailChk.setTextColor(Color.RED);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, String.valueOf(buttonChk));
                if(buttonChk == 1){
                    signUpSendServer();
                } else if(buttonChk == 0){
                    Toast.makeText(getApplicationContext(), "중복 확인 버튼을 다시 눌러주세요." , Toast.LENGTH_SHORT).show();
                }

            }
        });

        editNickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean gainFocus) {
                if(gainFocus){
                    nicknameChk.setVisibility(View.VISIBLE);
                }else{
                    nicknameChk.setVisibility(View.INVISIBLE);
                }
            }
        });

        editNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nickNameChkSendServer();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                buttonChk = 0;
                nickName = editNickname.getText().toString().trim();
                if(nickName.matches(nickNameValidation) && nickName.length() > 2){
                    nicknameChk.setText("조건에 맞는 닉네임 입니다.");
                    nicknameChk.setTextColor(Color.GREEN);
                }else if (nickName.length() > 10){
                    nicknameChk.setText("닉네임이 10자 이상입니다.");
                    nicknameChk.setTextColor(Color.RED);
                } else {
                    nicknameChk.setText("조건에 맞지 않는 닉네임 입니다.");
                    nicknameChk.setTextColor(Color.RED);
                }
            }
        });

        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean gainFocus) {
                if(gainFocus){
                    emailChk.setVisibility(View.VISIBLE);
                }else{
                    emailChk.setVisibility(View.INVISIBLE);
                }
            }
        });


        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                emailChkSendServer();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                buttonChk = 0;
                email = editEmail.getText().toString().trim();
                if(email.matches(emailValidation) && email.length() > 0){
                    emailChk.setText("조건에 맞는 이메일 형식입니다.");
                    emailChk.setTextColor(Color.GREEN);
                }else {
                    emailChk.setText("조건에 맞지 않는 이메일 형식입니다.");
                    emailChk.setTextColor(Color.RED);
                }
            }
        });


        editFirstPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean gainFocus) {
                if(gainFocus){
                    firstPwChk.setVisibility(View.VISIBLE);
                }else{
                    firstPwChk.setVisibility(View.INVISIBLE);
                }
            }
        });

        editFirstPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                firstPw = editFirstPw.getText().toString().trim();
                if(firstPw.matches(pwValidation) && firstPw.length() > 0){
                    firstPwChk.setText("사용 가능한 비밀번호 입니다.");
                    firstPwChk.setTextColor(Color.GREEN);
                }else if (firstPw.length() > 12){
                    firstPwChk.setText("비밀번호가 12자 이상입니다.");
                    firstPwChk.setTextColor(Color.RED);
                }else {
                    firstPwChk.setText("영어 + 숫자 + 특수문자를 포함한 비밀번호를 만들어주세요.");
                    firstPwChk.setTextColor(Color.RED);
                }
            }
        });


        editSecondPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean gainFocus) {
                if(gainFocus){
                    secondPwChk.setVisibility(View.VISIBLE);
                }else{
                    secondPwChk.setVisibility(View.INVISIBLE);
                }
            }
        });

        editSecondPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editFirstPw.getText().toString().trim().equals(editSecondPw.getText().toString().trim())) {
                    secondPwChk.setText("비밀번호가 일치 합니다.");
                    secondPwChk.setTextColor(Color.GREEN);
                } else {
                    secondPwChk.setText("비밀번호가 일치하지 않습니다.");
                    secondPwChk.setTextColor(Color.RED);
                }

            }
        });

        firstPwImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstPwImage.getTag().equals("0")){
                    firstPwImage.setTag("1");
                    firstPwImage.setImageResource(R.drawable.open);
                    editFirstPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    firstPwImage.setTag("0");
                    firstPwImage.setImageResource(R.drawable.close);
                    editFirstPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        secondPwImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(secondPwImage.getTag().equals("0")){
                    secondPwImage.setTag("1");
                    secondPwImage.setImageResource(R.drawable.open);
                    editSecondPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    secondPwImage.setTag("0");
                    secondPwImage.setImageResource(R.drawable.close);
                    editSecondPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

    }

    public void emailChkSendServer(){
        HttpUrl.Builder urlBuilder_email = HttpUrl.parse("http://13.209.140.171/emailCheck.php").newBuilder();
        String url = urlBuilder_email.build().toString();

        RequestBody formBody_email = new FormBody.Builder()
                .add("email", editEmail.getText().toString().trim())
                .build();

        OkHttpClient client_id = new OkHttpClient();
        Request request_id = new Request.Builder()
                .url(url)
                .post(formBody_email)
                .build();

        client_id.newCall(request_id).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String emailResponseDate = response.body().string().trim();
                if (emailResponseDate.equals("1")) {
                    emailDuplication = "1";
                    Log.e(TAG, "중복 이메일 있음");
                }else {
                    emailDuplication= "0";
                    Log.e(TAG , "중복 이메일 없음" + emailResponseDate);
                }
            }
        });
    }

    public void nickNameChkSendServer(){
        HttpUrl.Builder urlBuilder_nickName = HttpUrl.parse("http://13.209.140.171/nickNameCheck.php").newBuilder();
        String url = urlBuilder_nickName.build().toString();

        RequestBody formBody_nickName = new FormBody.Builder()
                .add("nickName", editNickname.getText().toString().trim())
                .build();

        OkHttpClient client_id = new OkHttpClient();
        Request request_id = new Request.Builder()
                .url(url)
                .post(formBody_nickName)
                .build();

        client_id.newCall(request_id).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String nickNameResponseDate = response.body().string().trim();
                if (nickNameResponseDate.equals("1")) {
                    nickNameDuplication = "1";
                    Log.e(TAG, "중복 닉네임 있음");
                }else {
                    nickNameDuplication= "0";
                    Log.e(TAG , "중복 닉네임 없음" + nickNameResponseDate);
                }
            }
        });
    }



    public void signUpSendServer(){
        HttpUrl.Builder urlBuilder_signUp = HttpUrl.parse("http://13.209.140.171/SignUp.php").newBuilder();
        String url = urlBuilder_signUp.build().toString();

        RequestBody formBody_signUp = new FormBody.Builder()
                .add("email", editEmail.getText().toString().trim())
                .add("nickName", editNickname.getText().toString().trim())
                .add("pw", editFirstPw.getText().toString().trim())
                .build();

        OkHttpClient client_signUp = new OkHttpClient();
        Request request_signUp = new Request.Builder()
                .url(url)
                .post(formBody_signUp)
                .build();

        client_signUp.newCall(request_signUp).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseDate = response.body().string().trim();
                if (responseDate.equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "회원가입 성공!" , Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(), "회원가입 실패..", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });
    }

}