package com.example.mayank.libraries.retrofit.network

import com.google.gson.annotations.SerializedName

class Question {

    @SerializedName("ques_id")
    var quesCode: String? = null

    @SerializedName("question")
    var question: String? = null

    @SerializedName("option_a")
    var optionA: String? = null

    @SerializedName("option_b")
    var optionB: String? = null

    @SerializedName("option_c")
    var optionC: String? = null

    @SerializedName("option_d")
    var optionD: String? = null

    @SerializedName("option_e")
    var optionE: String? = null

    @SerializedName("answer")
    var answer: String? = null
}