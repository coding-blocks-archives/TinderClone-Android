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
import org.jetbrains.anko.indeterminateProgressDialog
import java.util.regex.Pattern


class SignUp1 : Fragment() {

    private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  =inflater.inflate(R.layout.fragment_sign_up1, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText.addTextChangedListener {
            button.isEnabled = EMAIL_ADDRESS_PATTERN.matcher(it.toString()).matches() && it.toString().isNotEmpty()
        }
        val dialog = requireContext().indeterminateProgressDialog("")
        button.setOnClickListener {
            FirebaseAuth.getInstance().uid?.let { uid ->
                val user = hashMapOf("Email" to editText.text.toString())
                db?.set(user, SetOptions.merge())
            }.also {
                it?.apply {
                    addOnCompleteListener {
                        button.isEnabled = false
                        dialog.show()
                    }
                    addOnSuccessListener {
                        button.isEnabled = true
                        dialog.hide()
                        fragmentManager?.commitWithAnimation(SignUp2(), "Email")
                    }
                    addOnFailureListener {
                        button.isEnabled = true
                        dialog.hide()
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }


}
