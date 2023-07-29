package com.example.vocalist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WordQuizFragment extends Fragment {
    private View root;
    private Button goRandomQuizButton;
    private Button goSpellingQuizButton;
    private TextView maxScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_word_quiz, container, false);
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int score = sharedPreferences.getInt("score",0);
        maxScore.setText("최고 점수 : "+score);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListener();
    }
    public void init(){
        goRandomQuizButton = root.findViewById(R.id.goRandomQuiz);
        goSpellingQuizButton = root.findViewById(R.id.goSpellingQuiz);
        maxScore = root.findViewById(R.id.randomQuizMaxScore);
    }
    public void setListener(){
        goRandomQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),RandomQuizAcitivity.class);
                startActivity(i);
            }
        });
    }
}