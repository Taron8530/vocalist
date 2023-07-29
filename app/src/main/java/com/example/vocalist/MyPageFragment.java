package com.example.vocalist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPageFragment extends Fragment {

    private TextView emailTextView;
    private TextView nickNameTextView;
    private String email;
    private String nickName;
    private View root;
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
    }
    public void init(){
        emailTextView = root.findViewById(R.id.email_MyPageFragment);
        nickNameTextView = root.findViewById(R.id.nickname_MyPageFragment);
        emailTextView.setText(email);
        nickNameTextView.setText(nickName);
    }
}