package com.codingblocks.tinder.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import com.codingblocks.tinder.extensions.commitWithAnimation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up_gender.*

class SignUpGender : Fragment() {

    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }
    var checkedChipId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_gender, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                when (document.get("gender")) {
                    1 -> {
                        genderGroup.check(1)
                    }
                    2 -> {
                        genderGroup.check(2)

                    }
                    3 -> {
                        genderGroup.check(3)

                    }
                }
                gender_checkbox.isChecked = document.getBoolean("show_gender") ?: false
            }
        }
        genderGroup.setOnCheckedChangeListener { chipGroup, i ->
            button.isEnabled = chipGroup.checkedChipId != -1
            checkedChipId = chipGroup.checkedChipId

        }
        button.setOnClickListener {
            button.isEnabled = false
            FirebaseAuth.getInstance().uid?.let { uid ->
                val user = hashMapOf(
                    "show_gender" to gender_checkbox.isChecked,
                    "gender" to checkedChipId
                )

                db?.set(user, SetOptions.merge())
            }.also {
                it?.apply {
                    addOnSuccessListener {
                        button.isEnabled = true
                        fragmentManager?.commitWithAnimation(SignUpOrientation(), "Gender")
                    }
                    addOnFailureListener {
                        button.isEnabled = true
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }


}
