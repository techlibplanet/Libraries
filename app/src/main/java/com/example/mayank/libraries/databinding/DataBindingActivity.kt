package com.example.mayank.libraries.databinding

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.R

class DataBindingActivity : AppCompatActivity() {


    lateinit var dataBinding: ActivityDataBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_data_binding)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding)
        val user = User("Mayank", "Sharma")
        dataBinding.users = user

    }
}
