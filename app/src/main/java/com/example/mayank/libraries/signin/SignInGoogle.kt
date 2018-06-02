package com.example.mayank.libraries.signin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.R
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import net.rmitsolutions.cameralibrary.Constants.logD
import org.jetbrains.anko.toast
import android.content.Intent
import android.R.attr.data
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.util.Base64
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignInGoogle : AppCompatActivity(), View.OnClickListener {


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val TAG = SignInAccount::class.java.simpleName
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_google)

        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)

    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            toast("${account.displayName} Sign in successfully")
            logD(TAG, "Display Name - ${account.displayName}")
            logD(TAG, "Email - ${account.email}")
            logD(TAG, "Given Name - ${account.givenName}")
            logD(TAG, "Family Name - ${account.familyName}")
        }else {
            toast("Sign in required !")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sign_in_button -> {
                signIn()
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }

    }

    fun signOut(view: View) {
        signOut()
    }

    private fun signOut() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            toast("Already Sign Out !")
            printHashKey(this)
        } else {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this) {
                        // ...
//                    revokeAccess()
                        toast("Sign Out Successfully !")

                    }
        }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this) {
                    // ...
                }
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }

    }
}
