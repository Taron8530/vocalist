package com.example.vocalist;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @GET("MyWordLoad.php")
    Call<CoinedWordPojoClass> searchMyWord(
            @Query("nickName") String nickname
    );
    @GET("WordLoad10.php")
    Call<CoinedWordPojoClass> searchCoinedShowWord(
    );
    @GET("WordAdd.php")
    Call<String> wordAdd(
      @Query("word") String word,
      @Query("wordDetail") String detail,
      @Query("writer") String writer
    );
    @FormUrlEncoded
    @POST("Login.php")
    Call<LoginPojoClass> login(
            @Field("email") String email,
            @Field("pw") String password
    );
    @GET("QuizWordRequest.php")
    Call<RandomQuizPojoClass> randomQuiz();
}
