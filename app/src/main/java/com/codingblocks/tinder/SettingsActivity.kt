package com.codingblocks.tinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSlideListener()
    }

    private fun setSlideListener() {
        age_slider.setOnThumbValueChangeListener { multiSlider, thumb, thumbIndex, value ->
            if (thumbIndex == 0) {
                age_text.text = (18 + thumb.value).toString() + age_text.text.substring(2)
            } else if (thumbIndex == 1) {
                age_text.text = age_text.text.substring(0, 5) + (18 + thumb.value).toString()
            }
        }

        distance_slider.setOnThumbValueChangeListener { multiSlider, thumb, thumbIndex, value ->
            if (thumbIndex == 0) {
                age_text.text = "${2 + thumb.value}km"
            }
        }

        visibleSwitch.setOnCheckedChangeListener { compoundButton, b ->

        }

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().apply {
                signOut()
                addAuthStateListener {
                    if (it.currentUser == null) {
                        val intent = Intent(this@SettingsActivity, SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
