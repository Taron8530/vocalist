package com.example.vocalist;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslationFragment extends Fragment {

    String TAG = "TranslationFragment";
//    String apiKey = "AIzaSyADnu2zgfpVtjHvcBROhswCsAEkqKS4fxk";

    EditText inputEditText;
    TextView outputEditText;
    Button translationButton;

    Spinner spinner;


    String postParams;
    InputMethodManager imm;

    String targetLanguage = "en";
    Translate translate;

    String [] items = {"영어", "일본어", "중국어 간체", "중국어 번체", "스페인어", "프랑스어", "독일어", "러시아어", "이탈리아어", "베트남어", "태국어", "인도네시아어"};
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_translation, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = root.findViewById(R.id.spinner);
        inputEditText = root.findViewById(R.id.inputEditText);
        outputEditText = root.findViewById(R.id.outputEditText);
        translationButton = root.findViewById(R.id.translationButton);
        imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        new Thread(() -> {
            translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
        }).start();



        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String wordGetText = items[position];
                if(wordGetText.equals("영어")){
                    targetLanguage = "en";
                }else if(wordGetText.equals("일본어")){
                    targetLanguage = "ja";
                }else if(wordGetText.equals("중국어 간체")){
                    targetLanguage = "zh";
                }else if(wordGetText.equals("중국어 번체")){
                    targetLanguage = "zh-TW";
                }else if(wordGetText.equals("스페인어")){
                    targetLanguage = "es";
                }else if(wordGetText.equals("프랑스어")){
                    targetLanguage = "fr";
                }else if(wordGetText.equals("독일어")){
                    targetLanguage = "de";
                }else if(wordGetText.equals("러시아어")){
                    targetLanguage = "ru";;
                }else if(wordGetText.equals("이탈리아어")){
                    targetLanguage = "it";
                }else if(wordGetText.equals("베트남어")){
                    targetLanguage = "vi";
                }else if(wordGetText.equals("태국어")){
                    targetLanguage = "th";
                }else if(wordGetText.equals("인도네시아어")){
                    targetLanguage = "id";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 클릭됨"+inputEditText.getText().toString().equals(""));
                if(!inputEditText.getText().toString().equals("")){
                    Log.d(TAG, "onClick: 클11릭됨"+inputEditText.getText().toString().equals(""));
                    String tmp = inputEditText.getText().toString();

                    new Thread(() -> {
                        Translation translation = translate.translate(tmp, Translate.TranslateOption.targetLanguage(targetLanguage), Translate.TranslateOption.sourceLanguage("ko"));
                        String answer = translation.getTranslatedText();
                        Log.d(TAG, "onClick: 번역 결과"+answer);
                        outputEditText.setText(answer);
                    }).start();

                }
            }
        });
    }
}