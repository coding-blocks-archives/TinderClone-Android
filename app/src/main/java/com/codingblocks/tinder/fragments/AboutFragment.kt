package com.codingblocks.tinder.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import com.codingblocks.tinder.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
        db?.get()?.addOnSuccessListener {
            documentSnapshot ->
            val user = documentSnapshot.toObject<User>()
            Picasso.get().load(user?.photos?.get(0)).into(profileImgView)
            nameTv.text = user?.name
        }

    }

}
