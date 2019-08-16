package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import com.codingblocks.tinder.extensions.commitWithAnimation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up1.*
import java.util.regex.Pattern


class SignUp1 : Fragment() {

    val db = FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  =inflater.inflate(R.layout.fragment_sign_up1, container, false)

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText.addTextChangedListener {
            button.isEnabled = EMAIL_ADDRESS_PATTERN.matcher(it.toString()).matches() && it.toString().isNotEmpty()
        }
        button.setOnClickListener {
            FirebaseAuth.getInstance().uid?.let { uid ->
                val user = hashMapOf("Email" to editText.text.toString())
                db?.set(user, SetOptions.merge())
            }.also {
                it?.addOnSuccessListener {
                    fragmentManager?.commitWithAnimation(SignUp2(), "Email")
                }
                it?.addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
        }
    }


}
