package com.example.mayank.libraries.retrofit.network


import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST



interface IQuestion {


    @FormUrlEncoded
    @POST("quiz/get_question.php")
    fun getQuestion(
            @Field("ques_id") questionID: String): Call<Void>

    @FormUrlEncoded
    @POST("quiz/get_question.php")
    fun getData(
            @Field("ques_id") questionID: String): Call<Question>


    @POST("quiz/get_question.php")
    fun getQues(@Body pojo: String): Call<Question>


}