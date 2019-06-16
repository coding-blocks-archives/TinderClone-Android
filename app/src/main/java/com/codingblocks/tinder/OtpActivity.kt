package com.codingblocks.tinder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_otp.numberTv
import kotlinx.android.synthetic.main.activity_otp.otp1
import kotlinx.android.synthetic.main.activity_otp.otp2
import kotlinx.android.synthetic.main.activity_otp.otp3
import kotlinx.android.synthetic.main.activity_otp.otp4
import kotlinx.android.synthetic.main.activity_otp.otp5
import kotlinx.android.synthetic.main.activity_otp.otp6
import kotlinx.android.synthetic.main.activity_otp.otpToolbar
import kotlinx.android.synthetic.main.activity_otp.otpVerify
import kotlinx.android.synthetic.main.activity_otp.otpView
import java.util.concurrent.TimeUnit


class OtpActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    lateinit var phoneNumber: String
    private var mVerificationId: String? = null
    private var mVerificationInProgress = false

    lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var otp = arrayOfNulls<EditText>(6)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        setActionBar(otpToolbar)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        otp = arrayOf(otp1, otp2, otp3, otp4, otp5, otp6)

        otp.forEach {
            it?.addTextChangedListener(GenericTextWatcher(it))
        }

        //Initializing Variables
        database = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                otpVerify.isEnabled = false
//            signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // [START_EXCLUDE silent]
                // [END_EXCLUDE]
                if (e is FirebaseTooManyRequestsException) {
                    Snackbar.make(
                        otpView, "Quota exceeded.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    mVerificationInProgress = false

                }
            }

            override fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                mVerificationId = verificationId
                mResendToken = token
                mVerificationInProgress = true

            }
        }

        with(intent.getStringExtra(PHONE_NO))
        {
            if (isNotEmpty()) {
                phoneNumber = this
                numberTv.text = this
                phoneAuth(this)
                Snackbar.make(otpView, "OTP Sent to +$this", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private fun phoneAuth(phoneNumber: String) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+$phoneNumber", // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            mCallbacks
        )        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            mCallbacks, // OnVerificationStateChangedCallbacks
            token
        )             // ForceResendingToken from callbacks
    }

    inner class GenericTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun afterTextChanged(s: Editable) {
            var allOtherFilled = false
            var nextEdit: EditText? = null
            var previousEdit: EditText? = null
            when (view) {
                otp1 -> {
                    otp.forEachIndexed { index, editText ->
                        if (index != 0) {
                            allOtherFilled = editText?.text?.length == 1
                        }
                    }

                    nextEdit = otp[1]
                }
                otp2 -> {
                    otp.forEachIndexed { index, editText ->
                        if (index != 1) {
                            allOtherFilled = editText?.text?.length == 1
                        }
                    }
                    previousEdit = otp[0]
                    nextEdit = otp[2]
                }
                otp3 -> {
                    otp.forEachIndexed { index, editText ->
                        if (index != 2) {
                            allOtherFilled = editText?.text?.length == 1
                        }
                    }
                    previousEdit = otp[1]
                    nextEdit = otp[3]
                }
                otp4 -> {
                    otp.forEachIndexed { index, editText ->
                        if (index != 3) {
                            allOtherFilled = editText?.text?.length == 1
                        }
                    }
                    previousEdit = otp[2]
                    nextEdit = otp[4]
                }
                otp5 -> {
                    otp.forEachIndexed { index, editText ->
                        if (index != 4) {
                            allOtherFilled = editText?.text?.length == 1
                        }
                    }
                    previousEdit = otp[3]
                    nextEdit = otp[5]
                }
                otp6 -> {
                    otp.forEachIndexed { index, editText ->
                        if (index != 5) {
                            allOtherFilled = editText?.text?.length == 1
                        }
                    }

                    previousEdit = otp[4]
                }
            }
            if (s.length == 1) {
                if (allOtherFilled) {
                    //if next 2 edit texts are filled , enable the pay button
                    enableDisableButton(true)
                    hideSoftKeyboard()
                }
            } else if (s.length > 1) {
                if (allOtherFilled) {
                    //if all next edit texts are filled , enable the pay button
                    enableDisableButton(true)
                    hideSoftKeyboard()
                } else if (nextEdit != null) {
                    if (nextEdit.text.isEmpty()) {
                        //if next edit is not filled, move to next edit and set the second digit
                        moveToNextEdit(nextEdit, view as EditText)
                    } else {
                        //if any other edit is not filled, stay in current edit
                        enableDisableButton(false)
                        stayOnCurrentEdit(view as EditText)
                    }
                }
            } else if (s.isEmpty()) {
                if (null != previousEdit)
                    moveToPreviousEdit(previousEdit)
                enableDisableButton(false)
            }
        }

        private fun enableDisableButton(state: Boolean) {
            otpVerify.isEnabled = state
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

        private fun stayOnCurrentEdit(editText: EditText) {
            editText.setText(editText.text.toString().substring(0, 1))
            editText.setSelection(editText.text.length)
        }

        private fun moveToPreviousEdit(editText: EditText) {
            editText.setSelection(editText.text.length)
            editText.requestFocus()
        }

        private fun moveToNextEdit(editText2: EditText, editText1: EditText) {
            editText2.setText(editText1.text.toString().substring(1, 2))
            editText2.requestFocus()
            editText2.setSelection(editText2.text.length)
            editText1.setText(editText1.text.toString().substring(0, 1))
        }
    }
}
