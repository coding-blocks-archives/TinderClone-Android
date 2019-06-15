package com.codingblocks.tinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.ccp
import kotlinx.android.synthetic.main.activity_login.loginBtn
import kotlinx.android.synthetic.main.activity_login.loginToolbar
import kotlinx.android.synthetic.main.activity_login.loginView
import kotlinx.android.synthetic.main.activity_login.phoneedtv

const val PHONE_NO = "phoneNo"


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setActionBar(loginToolbar)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

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

    private fun isPhoneNoValid(number: String): Boolean {
        return number.length >= 10
    }
}
