package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.codingblocks.tinder.R
import kotlinx.android.synthetic.main.fragment_sign_up_intersted_in.*

class SignUpInterstedIn : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_intersted_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genderGroup.setOnCheckedChangeListener { chipGroup, i ->
            button.isEnabled = chipGroup.checkedChipId != -1
        }
        button.setOnClickListener {
            fragmentManager?.commit {
                addToBackStack("Orientation")
                setCustomAnimations(
                    R.animator.slide_in_right,
                    R.animator.slide_out_left,
                    R.animator.slide_in_left,
                    R.animator.slide_out_right
                )
                replace(R.id.container, SignUpCollege(), "Orientation")
            }
        }
    }


}
