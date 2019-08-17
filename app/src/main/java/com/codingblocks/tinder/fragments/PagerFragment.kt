package com.codingblocks.tinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import kotlinx.android.synthetic.main.item_page.*

class PagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            container.setBackgroundResource(it.getInt("color"))
            tvTitle.text = "Item ${it.getInt("position")}"
        }
    }
}