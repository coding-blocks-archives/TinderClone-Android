package com.codingblocks.tinder.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import com.codingblocks.tinder.extensions.commitWithAnimation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up_intersted_in.*

const val INTERSTEDIN = "interstedIn"


class SignUpInterestedIn : Fragment() {

    private val sharedPrefs by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }
    var checkedChipId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_up_intersted_in, container, false)

    @SuppressLint("ResourceType", "CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                when (document.get("interested")) {
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
            }
        }
        genderGroup.setOnCheckedChangeListener { chipGroup, i ->
            button.isEnabled = chipGroup.checkedChipId != -1
            checkedChipId = when (chipGroup.checkedChipId) {
                4 -> 1
                8 -> 2
                12 -> 3
                else -> 1
            }
        }
        button.setOnClickListener {
            button.isEnabled = false
            FirebaseAuth.getInstance().uid?.let { uid ->
                val user = hashMapOf(
                    "interested" to checkedChipId
                )
                sharedPrefs.edit(commit = true) {
                    putInt(INTERSTEDIN, checkedChipId)
                }
                db?.set(user, SetOptions.merge())
            }.also {
                it?.apply {
                    addOnSuccessListener {
                        button.isEnabled = true
                        fragmentManager?.commitWithAnimation(SignUpCollege(), "Interested")
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
