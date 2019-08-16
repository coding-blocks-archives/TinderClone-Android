package com.codingblocks.tinder.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.codingblocks.tinder.R

fun FragmentManager?.commitWithAnimation(fragment: Fragment, string: String) {
    this?.commit {
        addToBackStack(string)
        setCustomAnimations(
            R.animator.slide_in_right,
            R.animator.slide_out_left,
            R.animator.slide_in_left,
            R.animator.slide_out_right
        )
        replace(R.id.container, fragment, string)
    }
}