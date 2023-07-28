package com.example.vocalist;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslationFragment extends Fragment {

    EditText inputEditText;
    TextView outputEditText;
    Button translationButton;

    Spinner spinner;

    String wordGetText;

    String postParams;

    String english = "source=ko&target=en&text=";
    String japanese = "source=ko&target=ja&text=";
    String simplifiedChinese = "source=ko&target=zh-CN&text=";
    String traditionalChinese = "source=ko&target=zh-TW&text=";
    String spanish = "source=ko&target=es&text=";
    String french = "source=ko&target=fr&text=";
    String german = "source=ko&target=de&text=";
    String russian = "source=ko&target=ru&text=";
    String italian = "source=ko&target=it&text=";
    String vietnamese = "source=ko&target=vi&text=";
    String thai = "source=ko&target=th&text=";
    String indonesian = "source=ko&target=id&text=";

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                wordGetText = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask task = new BackgroundTask();
                String tmp = inputEditText.getText().toString();
                task.execute(tmp);
            }
        });
    }
    class BackgroundTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... str) {
            String inputTest = str[0];
            String clientId = "dkqVOngV0scCK5jkEnM4";
            String clientSecret = "26kqKNfHmn";
            String result = "";
            try {
                String text = URLEncoder.encode(inputTest, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                if(wordGetText.equals("영어")){
                    postParams = english + text;
                }else if(wordGetText.equals("일본어")){
                    postParams = japanese + text;
                }else if(wordGetText.equals("중국어 간체")){
                    postParams = simplifiedChinese + text;
                }else if(wordGetText.equals("중국어 번체")){
                    postParams = traditionalChinese + text;
                }else if(wordGetText.equals("스페인어")){
                    postParams = spanish + text;
                }else if(wordGetText.equals("프랑스어")){
                    postParams = french + text;
                }else if(wordGetText.equals("독일어")){
                    postParams = german + text;
                }else if(wordGetText.equals("러시아어")){
                    postParams = russian + text;
                }else if(wordGetText.equals("이탈리아어")){
                    postParams = italian + text;
                }else if(wordGetText.equals("베트남어")){
                    postParams = vietnamese + text;
                }else if(wordGetText.equals("태국어")){
                    postParams = thai + text;
                }else if(wordGetText.equals("인도네시아어")){
                    postParams = indonesian + text;
                }
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                //번역 결과 받아온다.
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                result = response.toString();
            } catch (Exception e) {
                result = "번역 실패";
                System.out.println(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(s);
            String tmp = element.getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString();
            outputEditText.setText(tmp);
        }
    }
}