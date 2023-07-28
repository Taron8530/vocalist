package com.example.vocalist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordPojoClass {
    @Expose
    @SerializedName("channel")
    private Channel channel;

    public Channel getChannel(){
        return channel;
    }
    // Getters, setters, and other methods...

    public class Channel {

        @Expose
        @SerializedName("total")
        private int total;

        @Expose
        @SerializedName("num")
        private int num;

        @Expose
        @SerializedName("title")
        private String title;

        @Expose
        @SerializedName("start")
        private int start;

        @Expose
        @SerializedName("description")
        private String description;

        @Expose
        @SerializedName("item")
        private List<Item> items;

        @Expose
        @SerializedName("link")
        private String link;

        @Expose
        @SerializedName("lastBuildDate")
        private String lastBuildDate;

        public List<Item> getItems(){
            return items;
        }
        // Getters, setters, and other methods...
    }

    public static class Item {

        @Expose
        @SerializedName("sup_no")
        private String supNo;

        @Expose
        @SerializedName("word")
        private String word;

        @Expose
        @SerializedName("target_code")
        private String targetCode;

        @Expose
        @SerializedName("sense")
        private Sense sense;

        @Expose
        @SerializedName("pos")
        private String pos;
        public String getWord(){
            return word;
        }
        public Sense getSense(){
            return sense;
        }

        // Getters, setters, and other methods...
    }

    public static class Sense {

        @Expose
        @SerializedName("definition")
        private String definition;

        @Expose
        @SerializedName("link")
        private String link;

        @Expose
        @SerializedName("type")
        private String type;

        public String getDefinition(){
            return definition;
        }

        // Getters, setters, and other methods...
    }
}
