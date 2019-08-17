package com.codingblocks.tinder.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.codingblocks.tinder.fragments.AboutFragment
import com.codingblocks.tinder.fragments.PagerFragment

class ViewPagerFragmentStateAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AboutFragment()
            1, 2 -> PagerFragment().apply {
                arguments = bundleOf(
                    "color" to colors[position],
                    "position" to position
                )

            }
            else -> PagerFragment().apply {
                arguments = bundleOf(
                    "color" to colors[position],
                    "position" to position
                )

            }
        }
    }

    private val colors = intArrayOf(
        android.R.color.holo_red_light,
        android.R.color.holo_blue_dark,
        android.R.color.holo_purple
    )


    override fun getItemCount(): Int = colors.size
}
