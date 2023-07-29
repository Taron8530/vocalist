package com.example.vocalist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RandomQuizPojoClass {
    @Expose
    @SerializedName("word")
    String word;
    @Expose
    @SerializedName("wordDetail")
    String detail;
    @Expose
    @SerializedName("writer")
    String writer;

}
