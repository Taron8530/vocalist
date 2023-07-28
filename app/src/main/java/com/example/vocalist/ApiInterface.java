package com.example.vocalist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("search.do")
    Call<WordPojoClass> searchWord(
            @Query("key") String key,
            @Query("q") String searchWord,
            @Query("req_type") String type
    );
    @GET("WordLoad.php")
    Call<CoinedWordPojoClass> searchCoinedWord(
            @Query("search") String word
    );
    @GET("WordLoad.php")
    Call<CoinedWordPojoClass> searchCoinedShowWord(
    );
    @GET("WordAdd.php")
    Call<String> wordAdd(
      @Query("word") String word,
      @Query("wordDetail") String detail,
      @Query("writer") String writer
    );
}
