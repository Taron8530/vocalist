package com.example.vocalist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnAddClickListener{
    private  BottomNavigationView bottomNavigationView;
    private SearchWordFragment searchWordFragment;
    private WordQuizFragment wordQuizFragment;
    private CoinedWordFragment coinedWordFragment;
    private WordAddFragment wordAddFragment;
    private TranslationFragment translationFragment;
    private MyPageFragment myPageFragment;
    private String email;
    private String nickName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_menu);
        getSharedPreference();
        init();
        searchWordFragment = new SearchWordFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, searchWordFragment).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.tab_search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, searchWordFragment).commit();
                    return true;
                } else if (itemId == R.id.tab_coined_word) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, coinedWordFragment).commit();
                    return true;
                } else if (itemId == R.id.tab_quiz) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, wordQuizFragment).commit();
                    Log.e("프래그먼트", "여긴 프로필");
                    setTitle("프로필");
                    return true;
                } else if (itemId == R.id.tab_translation) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, translationFragment).commit();
                    return true;
                } else if (itemId == R.id.tab_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, myPageFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
    public void init(){
        searchWordFragment = new SearchWordFragment();
        wordQuizFragment = new WordQuizFragment();
        coinedWordFragment = new CoinedWordFragment();
        wordAddFragment = new WordAddFragment(email,nickName);
        translationFragment = new TranslationFragment();
        myPageFragment = new MyPageFragment(email,nickName);
    }

    @Override
    public void addOnclick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, wordAddFragment).commit();
    }

    @Override
    public void backButtonOnclick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_MainActivity, coinedWordFragment).commit();
    }
    public void getSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        nickName = sharedPreferences.getString("nickname","");
    }
}