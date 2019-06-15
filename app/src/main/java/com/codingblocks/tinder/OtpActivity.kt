package com.codingblocks.tinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_otp.otpToolbar
import kotlinx.android.synthetic.main.activity_otp.otpView

class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        setActionBar(otpToolbar)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        with(intent.getStringExtra(PHONE_NO)) {
            if (isNotEmpty()) {
                Snackbar.make(otpView, "OTP Sent to +$this", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
