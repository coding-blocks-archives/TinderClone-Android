package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import com.codingblocks.tinder.extensions.commitWithAnimation
import kotlinx.android.synthetic.main.fragment_sign_up1.*

class SignUp1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  =inflater.inflate(R.layout.fragment_sign_up1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText.addTextChangedListener {
            button.isEnabled = !it.isNullOrEmpty()
        }
        button.setOnClickListener {
            fragmentManager?.commitWithAnimation(SignUp2(), "Email")
        }
    }


}
