package com.example.vocalist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPojoClass {
    @Expose
    @SerializedName("email")
    String email;
    @Expose
    @SerializedName("nickName")
    String nickname;
    @Expose
    @SerializedName("success")
    String response;
}
