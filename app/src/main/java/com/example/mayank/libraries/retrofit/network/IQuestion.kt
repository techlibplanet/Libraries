package com.example.mayank.libraries.retrofit.network

import android.telecom.Call
import retrofit2.http.*

interface IQuestion {


    @FormUrlEncoded
    @POST("quiz/get_question.php")
    fun getQuestion(
            @Field("ques_id") questionID: String): retrofit2.Call<Void>

    @FormUrlEncoded
    @POST("quiz/get_question.php")
    fun getData(
            @Field("ques_id") questionID: String): retrofit2.Call<Question>


}