package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.codingblocks.tinder.R
import com.codingblocks.tinder.extensions.commitWithAnimation
import kotlinx.android.synthetic.main.fragment_sign_up2.*

class SignUp2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText.addTextChangedListener {
            button.isEnabled = !it.isNullOrEmpty()
        }
        button.setOnClickListener {
            fragmentManager?.commitWithAnimation(SignUp3(),"Name")

        }
    }


}
