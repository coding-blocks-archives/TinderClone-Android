package com.codingblocks.tinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.auth.api.credentials.Credential.EXTRA_KEY
import android.R.attr.data
import com.google.android.gms.auth.api.credentials.Credential


const val PHONE_NO = "phoneNo"
val RESOLVE_HINT = 1001
val TAG = "Phone Selector"


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setActionBar(loginToolbar)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        requestHint()

        loginBtn.setOnClickListener {
            hideSoftKeyboard()
            if (phoneedtv.text.isNullOrEmpty() || !isPhoneNoValid(phoneedtv.text.toString())) {
                Snackbar.make(loginView, "Incorrect Number !!!!", Snackbar.LENGTH_SHORT).show()
            } else {
                with(
                    Intent(this, OtpActivity::class.java).putExtra(
                        PHONE_NO,
                        ccp.selectedCountryCode + phoneedtv.text
                    )
                ) {
                    startActivity(this)
                }

            }
        }

    }

    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val apiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.CREDENTIALS_API)
            .enableAutoManage(this) {
                Log.i(TAG, "Mobile Number: ${it.errorMessage}")
            }.build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(apiClient, hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            RESOLVE_HINT, null, 0, 0, 0
        )
    }

    private fun isPhoneNoValid(number: String): Boolean {
        return number.length >= 10
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                val cred = data?.getParcelableExtra(EXTRA_KEY) as Credential
                if (cred != null) {
                    // cred.od; <-- E.164 format phone number on 10.2.+ devices
                    val unformattedPhone = cred.id
                    Log.e(TAG, "Client connection failed: $unformattedPhone")
                    phoneedtv.setText(unformattedPhone.substring(startIndex = 3))

                }
            }
        }
    }
}
