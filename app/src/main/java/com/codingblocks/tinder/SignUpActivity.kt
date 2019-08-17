package com.codingblocks.tinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.codingblocks.tinder.fragments.SignUp1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {
    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportFragmentManager.commit {
            add(R.id.container, SignUp1())
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
