package com.codingblocks.tinder

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.codingblocks.tinder.extensions.changeState
import com.codingblocks.tinder.extensions.hideSoftKeyboard
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_otp.*
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

    val db by lazy {
        Firebase.firestore.collection("users")
    }

    private val SMS_CONSENT_REQUEST = 2  // Set to an unused request code
    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get consent intent
                        val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                        } catch (e: ActivityNotFoundException) {
                            // Handle the exception ...
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // Time out occurred, handle the error.
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        setActionBar(otpToolbar)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
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

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
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

        val task = SmsRetriever.getClient(this).startSmsUserConsent(null)
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsVerificationReceiver, intentFilter)

        otpVerify.setOnClickListener {
            val credential = mVerificationId?.let { it1 -> PhoneAuthProvider.getCredential(it1, getOtp()) }
            credential?.let { it1 -> signInWithPhoneAuthCredential(it1) }
        }


    }

    private fun getOtp(): String {
        var code = ""
        otp.forEach {
            code += it?.text ?: ""
        }
        return code
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // ...
            SMS_CONSENT_REQUEST ->
                // Obtain the phone number from the result
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Get SMS message content
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    // Extract one-time code from the message and complete verification
                    // `message` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                   // val oneTimeCode = parseOneTimeCode(message) // define this function

                    // send one time code to the server
                } else {
                    // Consent denied. User can type OTC manually.
                }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val uid = task.result?.user?.uid
                    uid?.let {
                        db.document(it).get().addOnSuccessListener {
                            if (it != null && it["photos"] != null) {
                                with(Intent(this, HomeActivity::class.java)) {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(this)
                                }
                                finish()
                            } else {
                                with(Intent(this, SignUpActivity::class.java)) {
                                    startActivity(this)
                                }
                            }
                        }
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
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
                    otpVerify.changeState(true)
                    hideSoftKeyboard()
                }
            } else if (s.length > 1) {
                if (allOtherFilled) {
                    //if all next edit texts are filled , enable the pay button
                    otpVerify.changeState(true)
                    hideSoftKeyboard()
                } else if (nextEdit != null) {
                    if (nextEdit.text.isEmpty()) {
                        //if next edit is not filled, move to next edit and set the second digit
                        moveToNextEdit(nextEdit, view as EditText)
                    } else {
                        //if any other edit is not filled, stay in current edit
                        otpVerify.changeState(false)
                        stayOnCurrentEdit(view as EditText)
                    }
                }
            } else if (s.isEmpty()) {
                if (null != previousEdit)
                    moveToPreviousEdit(previousEdit)
                otpVerify.changeState(false)
            }
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
