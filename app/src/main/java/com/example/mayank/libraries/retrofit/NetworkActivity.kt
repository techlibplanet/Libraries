package com.example.mayank.libraries.retrofit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.R
import com.example.mayank.libraries.retrofit.network.ApiClient
import com.example.mayank.libraries.retrofit.network.IQuestion
import com.example.mayank.libraries.retrofit.network.Question
import kotlinx.android.synthetic.main.activity_network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkActivity : AppCompatActivity() {

    private val TAG = NetworkActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)


    }

    fun getQuestion(view:View){
        val id =editTextQuesId.text.toString().trim()
        getQuestion(id)
        getAnother(id)
    }

    private fun getAnother(number: String){
        showLogDebug(TAG, "Inside get another")
        val service = ApiClient()
        val iQuestion : IQuestion = service.getService()
        val call = iQuestion.getData(number).enqueue(object : Callback<Question>{
            override fun onFailure(call: Call<Question>?, t: Throwable?) {
                showLogDebug(TAG, "Error - $t")
            }

            override fun onResponse(call: Call<Question>?, response: Response<Question>?) {
                showLogDebug(TAG, "On Success - $response")

                if (response?.isSuccessful!!){
                    val result = response.body().toString()
                    showLogDebug(TAG, "Question : "+result)
                    textViewQuestion.text = result
                }
            }

        })

    }

    private fun getQuestion(number : String){
        showLogDebug(TAG, "Inside Get Question")
        val service = ApiClient()
        val iQuestion : IQuestion = service.getService()
        val call = iQuestion.getQuestion(number).enqueue(object : Callback<Void>{
//            override fun onFailure(call: Call<Void>?, t: Throwable?) {
//                showLogDebug(TAG, "Error - $t")
//            }
//
//            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
//                showLogDebug(TAG, "On Success - $response")
//
//                if (response?.isSuccessful!!){
//                    val result = response.body()
//                    showLogDebug(TAG, "Question : "+result?.question!!)
//                    textViewQuestion.text = result.question
//                }
//            }
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                showLogDebug(TAG, "Error - $t")
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                showLogDebug(TAG, "On Success - $response")

                if (response?.isSuccessful!!){
                    val result = response.body()
                    showLogDebug(TAG, "Question : $result")
//                    textViewQuestion.text = result.question
                }
            }

        })
    }
}
