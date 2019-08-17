package com.codingblocks.tinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.mobileBtn

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this,HomeActivity::class.java))
        }
        mobileBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}
