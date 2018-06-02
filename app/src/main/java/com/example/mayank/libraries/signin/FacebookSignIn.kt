package com.example.mayank.libraries.signin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.R
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import java.util.Arrays.asList
import com.facebook.login.widget.LoginButton
import java.util.*


class FacebookSignIn : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_sign_in)

        callbackManager = CallbackManager.Factory.create();


    }
}
