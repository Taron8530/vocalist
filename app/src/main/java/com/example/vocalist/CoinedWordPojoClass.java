package com.example.vocalist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinedWordPojoClass {
    @Expose
    @SerializedName("data")
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    class Data{
        @Expose
        @SerializedName("word")
        private String word;
        @Expose
        @SerializedName("wordDetail")
        private String detail;
        @Expose
        @SerializedName("writer")
        private String writer;

        public String getDetail() {
            return detail;
        }

        public String getWord() {
            return word;
        }

        public String getWriter() {
            return writer;
        }
    }
}
