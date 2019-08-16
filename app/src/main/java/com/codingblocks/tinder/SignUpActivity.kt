package com.codingblocks.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codingblocks.tinder.fragments.SignUp1
import com.codingblocks.tinder.fragments.SignUpGender
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportFragmentManager.beginTransaction()
            .add(R.id.container,SignUp1())
            .commit()
    }
}
