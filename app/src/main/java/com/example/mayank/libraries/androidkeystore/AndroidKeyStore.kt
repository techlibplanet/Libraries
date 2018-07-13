package com.example.mayank.libraries.androidkeystore

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.mayank.libraries.R
import java.io.IOException
import java.security.cert.CertificateException
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentActivity
import android.util.Base64
import android.util.Log
import net.rmitsolutions.cameralibrary.Constants.logD
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.EditText
import android.widget.TextView
import com.example.mayank.libraries.getPref
import com.example.mayank.libraries.putPref
import org.jetbrains.anko.find


class AndroidKeyStore : AppCompatActivity(), View.OnClickListener {


    private val TAG = AndroidKeyStore::class.java.simpleName
    private val SAMPLE_ALIAS = "MYALIAS"

    private var CLICKABLES = intArrayOf(R.id.buttonSaveKeyStore, R.id.buttonRetriveKeyStore)

    private var encryptor: EnCryptor? = null
    private var decryptor: DeCryptor? = null
    private lateinit var inputAlias : EditText

    private lateinit var alias : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_key_store)

        inputAlias = find(R.id.editTextAlias)

        encryptor = EnCryptor(this)

        try {
            decryptor = DeCryptor()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        for (id in CLICKABLES){
            findViewById<Button>(id).setOnClickListener(this)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonSaveKeyStore ->{
                alias = inputAlias.text.toString().trim()
                val encryptText = find<EditText>(R.id.editTextToEncrypt).text.toString().trim()
                encryptText(alias,encryptText)
            }

            R.id.buttonRetriveKeyStore ->{
                decryptText()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun encryptText(alias : String,encryptText : String) {
        try {
            val encryptedText = encryptor?.encryptText(alias,encryptText)

            logD(TAG, "${Base64.encodeToString(encryptedText, Base64.DEFAULT)}")
            //tvEncryptedText.setText(Base64.encodeToString(encryptedText, Base64.DEFAULT))
        } catch (e: UnrecoverableEntryException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: NoSuchProviderException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: KeyStoreException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: IOException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: NoSuchPaddingException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: InvalidKeyException) {
            Log.e(TAG, "onClick() called with: " + e.message, e)
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: SignatureException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

    }

    private var decryptedText : String ? =null

    private fun decryptText() {
        try {
            alias = inputAlias.text.toString().trim()
            val iv = getPref("encryptionIv","")
            val data = getPref("encryptedKey", "")
            val encryptedData = Base64.decode(data, Base64.DEFAULT)
            val encryptedIv = Base64.decode(iv, Base64.DEFAULT)
            logD(TAG, "Alias - $alias")
            if (decryptor!=null && encryptor!=null){
                decryptedText = decryptor?.decryptData(alias, encryptedData, encryptedIv)
                logD(TAG, "$decryptedText")
            }else {
                decryptedText = "Null"
            }
            find<TextView>(R.id.textViewDecryptKey).text = decryptedText
        } catch (e: UnrecoverableEntryException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: KeyStoreException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: NoSuchPaddingException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: NoSuchProviderException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: IOException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: InvalidKeyException) {
            Log.e(TAG, "decryptData() called with: " + e.message, e)
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }

    }


}
